package interviews;

public class RingCentralInterview {


    public static void main(String[] args) {
        // Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
        int [] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println(calculate(height));
        //Output: 6

        /*
         * Approach:
         * - Use two pointers, `i` and `j`, to identify boundaries that can trap water.
         * - Start with `i = 0`.
         * - For each position `i`, do the following:
         *   - If the height at `i` is greater than 0 (a valid left boundary):
         *     - Search for a valid right boundary `j`:
         *       - Start `j` from `i + 1`.
         *       - Move `j` forward until:
         *           - You find a bar where height[j] >= height[i] (ideal right boundary), OR
         *           - You reach the end and fallback to the highest bar found after `i` (suboptimal but best possible).
         *     - Once `j` is determined:
         *       - Calculate the trapped water between `i` and `j`:
         *         water += (min(height[i], height[j]) * (j - i - 1)) - sum of heights between i and j
         *   - Move `i` to `j` and repeat the process.
         *
         * Note:
         * - Width of each bar is 1.
         * - The algorithm ensures all trapped water is accounted for in a single pass.
         */


    }
//         int [] height = {0, 5, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    private static int calculate(int[] height) {
        int i = 0, result = 0;

        while (i < height.length - 1) {
            int j = i + 1;
            int maxIndex = j;

            // Find the next bar that is >= current, or the tallest if none
            while (j < height.length) {
                if (height[j] >= height[i]) break;
                if (height[j] > height[maxIndex]) maxIndex = j;
                j++;
            }

            // Use the right boundary
            if (j < height.length && height[j] >= height[i]) {
                result += calculateWater(i, j, height);
                i = j;
            } else {
                result += calculateWater(i, maxIndex, height);
                i = maxIndex;
            }
        }
        return result;
    }

    private static int calculateWater(int i, int j, int[] height) {
        if (i >= j) return 0;

        int minHeight = Math.min(height[i], height[j]);
        int water = 0;

        for (int k = i + 1; k < j; k++) {
            water += Math.max(0, minHeight - height[k]);
        }
        return water;
    }

}
