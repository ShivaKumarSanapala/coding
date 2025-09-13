package interviews.com.deliveroo;

// Dependencies: Java 11+, Jackson (com.fasterxml.jackson.core / com.fasterxml.jackson.databind)

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BestHotelFetcher {
    private final HttpClient client;
    private final ObjectMapper mapper;

    private final Duration requestTimeout = Duration.ofSeconds(8);
    private final boolean serverSideSortedByRating; // if true, API returns highest-rated first

    public BestHotelFetcher(boolean serverSideSortedByRating) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.mapper = new ObjectMapper();
        this.serverSideSortedByRating = serverSideSortedByRating;
    }

    /**
     * High-level method: fetch pages until exhausted and return best hotel (or null)
     * Example: API pattern: GET <a href="https://api.example.com/hotels?page=1&pageSize=100">...</a>
     * Adjust buildPageUri() to suit your API (cursor vs page number).
     */
    public Hotel findBestHotel(String baseUrl, int pageSize) throws IOException, InterruptedException {
        int page = 1;
        Hotel best = null;

        while (true) {
            URI uri = buildPageUri(baseUrl, page, pageSize);
            PageResponse pageResp = fetchPageWithRetries(uri);

            if (pageResp == null) { // treat as a fatal error
                throw new IOException("Failed to fetch page " + page);
            }

            // Process page items streaming-style
            for (Hotel h : pageResp.items) {
                if (best == null || isBetter(h, best)) {
                    best = h;
                }
            }

            // If server returns sorted by rating descending we can stop after first page
            if (serverSideSortedByRating && page > 0 && pageResp.items != null && !pageResp.items.isEmpty()) {
                // first item on pageResp is best for entire dataset
                return pageResp.items.get(0);
            }

            // Decide whether to continue pagination
            if (pageResp.hasNext != null) {
                if (!pageResp.hasNext) break;
                page = pageResp.nextPage != null ? pageResp.nextPage : page + 1;
            } else if (pageResp.nextPageToken != null) {
                // token-based pagination (cursor / token)
                // In this simple loop we require pageResp.nextPageToken == null to stop.
                // If token exists, update baseUrl or parameters accordingly (omitted for brevity).
                // For demonstration, assume nextPageToken unused here -> break
                break;
            } else if (pageResp.items == null || pageResp.items.isEmpty()) {
                break;
            } else {
                page++; // best-effort increment for numeric page APIs
            }
        }
        return best;
    }

    // --- Helper: decide better hotel (rating desc, then id asc)
    private boolean isBetter(Hotel a, Hotel b) {
        if (Double.compare(a.rating, b.rating) != 0) {
            return a.rating > b.rating;
        }
        // tie-breaker by id lexicographically (stable/deterministic)
        return a.id.compareTo(b.id) < 0;
    }

    // --- Builds URI for a page-number API. Modify for cursor-based APIs.
    private URI buildPageUri(String baseUrl, int page, int pageSize) {
        String s = String.format("%s?page=%d&pageSize=%d", baseUrl, page, pageSize);
        return URI.create(s);
    }

    // --- Fetch a single page with retry & backoff
    private PageResponse fetchPageWithRetries(URI uri) throws InterruptedException {
        int attempt = 0;
        // Config
        int maxRetries = 4;
        while (attempt <= maxRetries) {
            attempt++;
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(uri)
                        .timeout(requestTimeout)
                        .GET()
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

                int code = resp.statusCode();
                if (code >= 200 && code < 300) {
                    // parse JSON
                    return mapper.readValue(resp.body(), PageResponse.class);
                } else if (code == 429) {
                    // Rate-limit: honor Retry-After if present
                    OptionalLong retryAfterMs = parseRetryAfterMillis(resp);
                    long backoff = retryAfterMs.orElse(calculateBackoffWithJitter(attempt));
                    Thread.sleep(backoff);
                    // continue retry
                } else if (code >= 500 && code < 600) {
                    // server error -> retry
                    long backoff = calculateBackoffWithJitter(attempt);
                    Thread.sleep(backoff);
                } else {
                    // client error -> treat as fatal (adjust if particular 4xx should be retried)
                    System.err.println("Fatal HTTP error " + code + " for " + uri);
                    return null;
                }
            } catch (IOException e) {
                // network I/O issue -> retry
                long backoff = calculateBackoffWithJitter(attempt);
                Thread.sleep(backoff);
            }
        }
        // exhausted retries
        System.err.println("Exhausted retries for " + uri);
        return null;
    }

    // parse Retry-After header (seconds or HTTP-date). Return ms if present.
    private OptionalLong parseRetryAfterMillis(HttpResponse<?> resp) {
        Optional<String> header = resp.headers().firstValue("Retry-After");
        if (header.isEmpty()) return OptionalLong.empty();
        String v = header.get().trim();
        try {
            // numeric seconds
            long secs = Long.parseLong(v);
            return OptionalLong.of(secs * 1000L);
        } catch (NumberFormatException e) {
            // fallback: ignore HTTP-date parsing for brevity
            return OptionalLong.empty();
        }
    }

    private long calculateBackoffWithJitter(int attempt) {
        // initial backoff
        long baseBackoffMillis = 300;
        long base = baseBackoffMillis * (1L << Math.max(0, attempt - 1));
        long jitter = ThreadLocalRandom.current().nextLong(0, base / 2 + 1);
        return base + jitter;
    }

    // --- Models (adapt to actual API JSON)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageResponse {
        public List<Hotel> items;
        public Boolean hasNext;        // optional, if API returns it
        public Integer nextPage;       // optional numeric next page
        public String nextPageToken;   // optional cursor token
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hotel {
        public String id;
        public String name;
        public double rating;
        @Override
        public String toString() {
            return name + " (" + rating + ")";
        }
    }

    // --- Example main (mock URL)
    public static void main(String[] args) throws Exception {
        BestHotelFetcher f = new BestHotelFetcher(false);
        String baseUrl = "https://api.example.com/hotels"; // replace with actual
        // Example usage (in real life, pageSize should be API-supported max)
        Hotel best = f.findBestHotel(baseUrl, 100);
        if (best != null) System.out.println("Best hotel: " + best);
        else System.out.println("No hotels found / error");
    }
}
