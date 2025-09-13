# Apollo.io - LeetCode Discuss

## Fetching posts for tag: `apollo.io`

### 1. [Interview Experience – Apollo.io (System Design Round)](https://leetcode.com/discuss/post/7097930/interview-experience-apolloio-system-des-qap9)


# Interview Experience – Apollo.io (System Design Round)

- **Author:** None
- **Created at:** Aug 19, 2025 at 05:19 AM
- **Updated at:** Aug 19, 2025 at 05:19 AM
- **Tags:** Apollo.io, Feedback, Interview

- **Role:** Senior Engineer
- **Round:** System Design
- **Duration:** 1 hour
- **Right from the start, the interviewer didn’t clarify what he was expecting. We ended up spending most of the time on non:** functional aspects like scaling, replication, and failure handling. By the time we got to the actual HLD/architecture, there was barely any time left to cover it properly.
- **What frustrated me even more was that the interviewer himself didn’t seem confident in the areas he was probing. The discussion went in circles, felt shallow, and lacked the structure you’d expect in a senior:** level system design round.
- **Advice to others:** Unless you want to practice patience, don’t waste your time here.

Additional info:
- I had a an system design interview with Apollo.io, and honestly, it turned out to be a waste of time.
- I’ve had tough design rounds at other companies, but even then, I walked away feeling it was a fair challenge. Here, it just felt like the whole session was mismanaged and not a true reflection of my experience.

---

### 2. [Apollo.io | Frontend Engineer | Wastage of time, apply at your own risk!!](https://leetcode.com/discuss/post/7003517/apolloio-frontend-engineer-wastage-of-ti-yz6d)


# Apollo.io | Frontend Engineer | Wastage of time, apply at your own risk!!

- **Author:** None
- **Created at:** Jul 25, 2025 at 12:38 PM
- **Updated at:** Jul 25, 2025 at 12:39 PM
- **Tags:** Apollo.io, Frontend, Interview

Interviewed at Apollo.io in July 2025. I have applied via referral. The whole process took 3 weeks. And it was a waste of time (read to know more).

**Round 1: Live Coding Round on CODERBYTE**

2 Simple JS based problem solving questions on string manipulation, pretty easy and doable with basic problem solving. Cleared the next round.

**Round 2: App Creation Round**

Need to set up an interview repo locally before the interview. Was asked to build a TodoMVC application in React,Javascript and CSS. You are free to use any external help except AI tools. Focus is on functionality first with a viewable styling. Was able to build it with all the functionalities required and decent styling. The interviewer was satisfied.

**Round 3: Debugging Round**

Was given a podcast player and list of 12 bugs which were there in code and needed to be fixed in order. The interviewer was helpful, I was able to get 10/12 in due time and the interviewer seemed satisfied with my approach. The application was in Redux which I wasn't familiar with, but managed to understand it, having knowledge of similar state management libraries.

**Round 3: Behavioural Round**

Was with a engineering manager who asked me about

Most challenging project?
How do you manage a conflict with a team member?
Why Apollo? What do you look for in a company before joining?
Any time when you suggested some change and there was a resistance to it?
How do you use AI in your current workflow? How much productivity improvement have you seen using AI?
What are your thoughts on the future of AI in development?

I thought it went well. I touched upon all the aspects of what I have worked upon so far.

**Verdict - REJECTED**

Got a rejection mail after 1 week with the reason for rejection being "Lack of AI mindset" (yeah I am not making this up).
I honestly don't know what they mean by that, because I clearly told in the Behavioral round how I am using to improve my productivity right now and willingness to adopt the AI ways of working at Apollo.io. If AI experience was such a must have, they should have told this beforehand, seems to me a made up excuse.
So beware of this company and interview at your own risk.

YOE - 5, TC - 48LPA

---

### 3. [Apollo.io | Senior Frontend Engineer (L5) | Remote](https://leetcode.com/discuss/post/6537254/apolloio-senior-frontend-engineer-l5-rem-mufg)


# Apollo.io | Senior Frontend Engineer (L5) | Remote

- **Author:** None
- **Created at:** Mar 14, 2025 at 09:07 PM
- **Updated at:** Apr 01, 2025 at 06:53 PM
- **Tags:** Frontend, Compensation

### Current
YOE: 6+
Current Company : Service Based Org
Current Title - Frontend Lead
Current TC - 42 LPA (36 Base + 6 ESOPS)
College : Tier 3

### Offer
Date of Offer - March 2025
Title - Senior Frontend Engineer (L5)
Base - 57 LPA
Stocks - 62k USD - 4 Years (25% - 1st year, remaining vested monthly)
Annual Bonus - 10% Base
Joining Bonus - 3 Lakh - one time
WFH Setup Assistance - 300 USD - one time
Total CTC - ~80LPA


##### Benefits:
- Fully remote. No other benefits compensation wise.
- PF + Health Insurance
- Monthly Education Budget (~8000 INR)

Minor increase in base after 1 round of negotiation.

---

### 4. [Apollo.io Interview | Senior Backend | Reject](https://leetcode.com/discuss/post/6463243/apolloio-interview-senior-backend-reject-cnmf)


# Apollo.io Interview | Senior Backend | Reject

- **Author:** None
- **Created at:** Feb 24, 2025 at 04:32 PM
- **Updated at:** Feb 24, 2025 at 04:32 PM
- **Tags:** Interview

Round 1 Coding
Very abstract question statement on https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/description/
Interviewer was really helpful.
Overall, he was happy with the approach and solution.

Round 2 Practical coding
Implement a dynamic price sharing module which takes in calculates factors such as distance, sugre pricing, night ride etc.
Was aware of the question and answered.


Round 3 HLD :
Given Entities as below:

Client => A tenant on the system
User => A user of a particular client
Segment => A subset of users from all users of a client

where total Clients = C
where each Client has upto U Users
where each Client has upto S UserSegments

Each User can belong to more than 1 UserSegment


Design an API that satisfies below requirements:

- Given multiple (ClientID, UserSegmentID, UserID) tuples per API request, return true/false based on if the UserId belongs to that UserSegmentId or not, for each tuple.
  \t- Assume that queries are all valid (ClientId exists, UserSegmentId exists and belongs to the respective ClientId only)

Tip: Pick something that you are comfortable diving deep on, when it comes to storage.



No of clients - 10,000
No of users per client  5 mill
Number of segments per client - 100

Came up with precomputing everything and storing inside redis with tuple as a key. DIscussed about sharding based on client id, problem arising when more shards being added. Went with consistent hashing approach. Interviewier pointed on storage cache being used, went with seggregating clientId:userSegementId and userSegmentId:userId being a second key, and have two lookups.
Interviewer was not giving any hints.

Recieved rejection mail within next 2 hours.

---

### 5. [Apollo.io | Senior Frontend Engineer | L5](https://leetcode.com/discuss/post/6364682/apolloio-senior-frontend-engineer-l5-by-9npxx)


# Apollo.io | Senior Frontend Engineer | L5

- **Author:** None
- **Created at:** Feb 02, 2025 at 11:48 PM
- **Updated at:** Feb 02, 2025 at 11:51 PM
- **Tags:** interview experience, Interview, Frontend

Hi Folks,

I recently had the opportunity to interview for the Senior Frontend Engineer role at Apollo.io in India. I\'m sharing this experience to give back to the community and hopefully help others who are preparing for similar interviews.

Total YOE: 5.5 years

**Round 1: Live coding**
Round time: 60 mins
There were 3 question(2 JavaScript and 1 React) on Codility platform. I do not remember the questions but they were pretty easy afaik.

**Round 2: App Creation**
Round time: 60 mins
I was asked to build a Todo app in React.
![image](https://assets.leetcode.com/users/images/2b2cbc0a-a3dd-4568-9e0f-72bd6f228892_1738407573.6314495.png)

Focus on how to manage the state for this app as you have to support filters like show all, show completed and show pending.

**Round 3: Debugging Interview**
Round time: 60 mins
I was given a music player application on React which had bugs in it. The agenda of the round was to fix the list of bugs which interviewer will share with you within the round time.
I was able to fix 17 out of 18 as I ran out of time.

**Round 4: System Design**
Round time: 60 mins
Interviewer shared a figma design with me and asked how we can build the application.
I talked about the architecture, how we can make the UX better, rendering choice CSR vs SSR vs ISG, network optimisation, code optimisation.

**Round 5: Hiring Manager**
It was a normal discussion where manager asked a few questions about projects I worked upon, how do I plan my day ahead and told about the expectations about the role. They wanted to know whether I\'ll be able to fit in their culture and work in a remote setup.

At last I was offered the position, compensation details: https://leetcode.com/discuss/compensation/6353841/apolloio-senior-frontend-engineer-l5/2835437

---

### 6. [Apollo.io | Senior Frontend Engineer | L5](https://leetcode.com/discuss/post/6347625/apolloio-senior-frontend-engineer-l5-by-olss1)


# Apollo.io | Senior Frontend Engineer | L5

- **Author:** None
- **Created at:** Jan 30, 2025 at 09:35 AM
- **Updated at:** Jan 30, 2025 at 09:35 AM
- **Tags:** Compensation, Frontend

Years of experience: 5.5 years
Current title: Senior Frontend Engineer (L4)
Prior Experience: Product based(Unicorn)
Current CTC:
* Fixed: 43 LPA
* ESOPs at Joining time: 22 lakhs vested over 4 years (Current valuation is 1.2 Cr)

Company: Apollo.io
Title: Senior Frontend Engineer (L5)
Base: 60 LPA
Variable/Bonus: 10% of base(6 lakhs)
Joining Bonus: 4 lakhs
ESOPs: 62,000 USD vested over 4 years

Pls share your thoughts on the offer

PS : I will share the interview experience pretty soon

---

### 7. [Apollo.io | SSE | Bad experience](https://leetcode.com/discuss/post/6326326/apolloio-sse-bad-experience-by-anonymous-me33)


# Apollo.io | SSE | Bad experience

- **Author:** None
- **Created at:** Jan 25, 2025 at 05:27 AM
- **Updated at:** Jan 26, 2025 at 06:50 AM
- **Tags:** Interview

**Round 1 Coding**
Very abstract question statement on binary search
Make your assumptions and code the solution
Got very little hints from the interviewer. I stated all my assumptions and coded the solution.
Overall, he was convinced with solution.


**Round 2 HLD :  Strong Hire - L5**
Design a system for live comment section on youtube.
Had a in depth discussion on all aspects in the design.
Pretty intense round.
They asked me to mail the final HLD, back of the envelope calculations and any assumptions I took.


**Round 3 Deep dive - Lean Hire - L5**
Detailed discussion of your project
Focus technical and non-technical aspects
Why mircro-service over monolith
Fault tolerance
Observability


**Round 4 Practical coding - Hire - L6, Strong Hire - L5**
Implement a stock market order book
user can sell/buy at a price
match these users

**Round 5 Behaviourial - Strong Hire - L5**
Why did you leave last 2 orgs?
Most successful project
constructive feedback from your manager
any purview outside your domain
quickly resolved any item
conflict with manager

Waited 1 week for the final answer and got a reject because I performed for a L5 but they expected L6 based on my experience (9 years) where I clearly communicated to my recruiter that I am open for both L5/L6 because the compensation and the role was a match for L5 as well.

If YOE was an issue, this could have been communicated at the start itself that I would be interviewed for L6 only.
Wasted 3 weeks and all the anxiety for nothing.
I had read these kind of reviews about Apollo.io but thought it would not happen with me.

Please clarify any of these things with the recruiter initally itself without wasting any further time.

---

### 8. [Frontend interview question - Apollo.io](https://leetcode.com/discuss/post/6284903/frontend-interview-question-apolloio-by-9f5ce)


# Frontend interview question - Apollo.io

- **Author:** None
- **Created at:** Jan 15, 2025 at 04:12 PM
- **Updated at:** Jan 16, 2025 at 06:18 AM
- **Tags:** Array, JavaScript, Interview

- **Given an array of numbers, insert a hyphen (\':** \') between any two adjacent numbers where both numbers are odd.
- **Input:** [5, 7, 4, 9]
- **Output:** [5, \'-\', 7, 4, 9]
- **// Explanation:** 5 and 7 are both odd, so add hyphen between them
- **Input:** [3, 5, 1, 2, 7]
- **Output:** [3, \'-\', 5, \'-\', 1, 2, 7]
- **// Explanation:** (3,5) and (5,1) are pairs of odd numbers
- **Can use PrepareFrontend:** https://preparefrontend.com for practising more frontend problems.

Additional info:
- Was asked this question in Apollo.io frontend coding round.
- Return the resulting array which will contain both numbers and hyphens.
- Example
- ```
- ```

---

### 9. [Don't Waste Time with Apollo.io](https://leetcode.com/discuss/post/5794516/dont-waste-time-with-apolloio-by-anonymo-m1x6)


# Don't Waste Time with Apollo.io

- **Author:** None
- **Created at:** Sep 16, 2024 at 11:57 AM
- **Updated at:** Sep 16, 2024 at 11:57 AM
- **Tags:** Interview

Pathetic company with slave culture

---

### 10. [Help | Apollo.io Senior Frontend Interview | Thank you in Advance](https://leetcode.com/discuss/post/5713169/help-apolloio-senior-frontend-interview-xji98)


# Help | Apollo.io Senior Frontend Interview | Thank you in Advance

- **Author:** raz_rocks
- **Created at:** Aug 31, 2024 at 02:49 AM
- **Updated at:** Aug 31, 2024 at 02:49 AM
- **Tags:** interview experience, Interview

Hello Folks,
Is there anyone who recently appeared for the interview at Apollo.io for the Senior Frontend role? Can anyone please share your interview experience and questions for each round?

---

### 11. [Adobe | CS2 | Offer](https://leetcode.com/discuss/post/5636032/adobe-cs2-offer-by-anonymous_user-ss7h)


# Adobe | CS2 | Offer

- **Author:** None
- **Created at:** Aug 14, 2024 at 03:29 PM
- **Updated at:** Oct 24, 2024 at 11:03 AM
- **Tags:** Compensation

Education & Experience:
- Tier 3, CS BTech
- 8 years total experience as Frontend Developer (React.js)
- Currently earning \u20B938 LPA Base + \u20B98 Lakhs in shares
- Location: Bangalore

Recent Offer Status:
1. Atlassian:
    - Position: Senior Frontend Engineer, Jira Issue Domain
    - Base: \u20B965 LPA
    - Bonus Target: 15%
    - RSUs: $150,000 USD for 4 years
    - Sign-on Bonus: \u20B97 Lakhs
    - Proposed Start Date: September 23, 2024
    - Location: Remote (Karnataka)

2. Thoughtspot:
    - Position: Senior Member of Technical Staff (SMTS)

3. Arista Networks:
    - Base: \u20B940 LPA
    - Performance Bonus: \u20B96 LPA
    - Shares: \u20B96 LPA
    - Total: \u20B952 LPA

4. Adobe (ACCEPTED):
    - Position: Computer Scientist-2
    - Base: \u20B952 LPA (Fixed salary components):
    - Performance Bonus (AIP): 15% (\u20B97.8 LPA)
    - RSUs: $143,800 USD for 4 years
        * 25% vests after 1st year
        * Remaining 75% vests quarterly over 3 years (6.25% per quarter)
    - Joining Bonus: \u20B93 Lakhs (one month after joining)
    - Anniversary Bonus: \u20B92 Lakhs (after 12 months)
    - Retirals:
        * PF: \u20B92.99 LPA
        * Gratuity: \u20B91.20 LPA
    - Total Annual Earning Potential: \u20B963.98 LPA (excluding RSUs)
    - Location: Bangalore
    - Total CTC: 93.98 LPA

5. Apollo.io:
    - Cleared all technical rounds
    - Cancelled further interviews after receiving Adobe offer

Final Decision:
- Accepted Adobe\'s offer
- Decision factors: Comprehensive compensation package, work culture considerations, and assessment of layoff risks
- Significant upgrade from current package, with strong RSU component and bonus structure

---

### 12. [Apollo.io](https://leetcode.com/discuss/post/5340178/apolloio-by-anonymous_user-131o)


# Apollo.io

- **Author:** None
- **Created at:** Jun 20, 2024 at 06:13 AM
- **Updated at:** Jun 20, 2024 at 06:13 AM
- **Tags:** System Design, High Level Design, Interview

Design a web crawler for given 1000 list of urls.
followup quesitons
what is the connection between the component. which protocol to use.
how to store the data url, and multimedia.
what if any system goes down.

---

### 13. [Help | Apollo.io | Frontend](https://leetcode.com/discuss/post/5315694/help-apolloio-frontend-by-anonymous_user-qdmo)


# Help | Apollo.io | Frontend

- **Author:** None
- **Created at:** Jun 15, 2024 at 05:26 AM
- **Updated at:** Jun 15, 2024 at 05:26 AM
- **Tags:** Frontend, Interview

Has anyone appeared for Apollo.io for Senior Frontend role ? Can you tell me about the process and how long it takes ? How can I prepare for it the best and what kind of questions are asked ?

Thank you.

---

### 14. [Companies Paying >50 LPA (FOR SDE) in Gurgaon , Delhi, Noida, WFH | 2024](https://leetcode.com/discuss/post/5264530/companies-paying-50-lpa-for-sde-in-gurga-r2co)


# Companies Paying >50 LPA (FOR SDE) in Gurgaon , Delhi, Noida, WFH | 2024

- **Author:** shailender999
- **Created at:** Jun 05, 2024 at 03:35 PM
- **Updated at:** Jun 09, 2024 at 01:06 PM
- **Tags:** Google, Amazon, Adobe, Zomato, Compensation

Hey Leetcode Community,
let\'s list down  companies in Delhi NCR (Gurgaon, Delhi, Noida) and WFH companies which pay more than 50 LPA annually for experienced professionals (>4 YOE).

Mention company name along with location.
e.g. Adobe - Noida

I will be adding the companies in below list:-

1. Google - Gurgaon
2. Microsoft - Noida
3. Adobe - Noida
4. Zomato - Gurgaon
5. Blinkit - Gurgaon
6. Amazon - Gurgaon
7. Deel - WFH
8. Gemini (Crypto) - Gurgaon
9. Coinbase - WFH
10. Apollo.io - WFH
11. Airbnb - WFH
12. Rubrik
13. Github - WFH (Not hiring in India )
14. Salesforce (Approval for remote or Gurgaon location)
15. Atlassian - WFH
16. Twilio - WFH
17. Confluent - WFH
18. Quilbot - WFH
19. Oracle (Conditional WFH)
20. Cohesity - WFH
21. BCG
22. Booking.com - Gurgaon
23. Sprinklr - Gurgaon
24. Stripe - WFH
25.

UpVote this post, so that it remains on top
...

---

### 15. [[offer Evaluation] Intuit vs Microsoft vs Walmart](https://leetcode.com/discuss/post/5164129/offer-evaluation-intuit-vs-microsoft-vs-k4u5s)


# [offer Evaluation] Intuit vs Microsoft vs Walmart

- **Author:** None
- **Created at:** May 16, 2024 at 06:46 AM
- **Updated at:** May 28, 2024 at 07:29 AM
- **Tags:** Microsoft, Intuit, Walmart Labs, Coinbase, Rippling, Compensation

Hi folks,

Have recently been giving interviews and cracked couple of organisations, here\'s are some offer that i got, Need your help in eveluating these.

YOE - ~8
Current Org - well known indian startup (Flipkart, Razorpay types)
Current comp -  60LPA (base) + 6LPA (variable) + ~1.1cr ESOPs vested for 4 years
Reason for switching - Peanut appraisal since two years, toxic enviroment and current manager is piece of shit.


**Evalutaion Criteria**
future growth options > Learning = Pay > WLB = Remote > other benefits

--------- **Intuit**
**Position** - SSE
**Base** - 44LPA
**Bonus** - 15% of base
**Stocks** - 50K USD RSU for 4 years
**Joining Bonus** - 3L
**Retention Bonus** - Nil
**Other benefits** - Standard intuit benefits
**Location** - bangalore
**Round of Negotiation done** - 0


------- **Walmart**
**Position**- SSE
**Base** - 38LPA
**Bonus** - 20% of base = 7.6L
**Stocks** - 25% of base per year = 9,50,000/year
**Joining Bonus** - Nil
**Retention Bonus** - Nil
**Other benefits** - Standard walmart benefits
**location** - bangalore
**Round of Negotiation done** - 0


------- **Microsoft**
**Position**- SSE (L62)
**Base** - 46LPA
**Bonus** - 0-30% of base
**Stocks** - 80K USD for 4 years
**Joining Bonus** - Nil
**Retention Bonus** - Nil
**Other benefits** - Standard MS benefits
**location** - bangalore (potential WFH)
**Round of Negotiation done** - 0


**Other Companies in pipeline**
[Apollo.io(in 4th round), Rippling(3rd round), Thoughtspot(2 rounds)]

**Rejected From**
[Coinbase, Atlassian, quince]

**Please help me with following things along with your evalutation?**
- Would to prefer Intuit over MS?
- Why Walmart is so cheap?
- Does MS provides WFH easily, Recruiter said to connect with HM?
- Which org provides better future growth options?
- Any room for negotiation in these offers?


**Update 1**
Rippling - rejected
Apollo.io - waiting for feedback after last round (been 1.5 weeks :(

---

### 16. [Apollo.io- STAY AWAY, CLOWN COMPANY, WASTE OF TIME](https://leetcode.com/discuss/post/5080926/apolloio-stay-away-clown-company-waste-o-swb6)


# Apollo.io- STAY AWAY, CLOWN COMPANY, WASTE OF TIME

- **Author:** None
- **Created at:** Apr 27, 2024 at 06:40 PM
- **Updated at:** Apr 27, 2024 at 06:40 PM
- **Tags:** Staff, Interview

Hello LC,

I applied for Staff Engg at Apollo.io, through a referral by my own college junior, WHO LATER REJECTS ME IN BEHAVIOURAL ROUND too :D - look at the level of clownship each of their employee has attained.
I had applied in Jan\'24 but received a call in Mar\'24, I have had my new employer joined already by then.
Yet, thought of taking the interviews due to slightly more money $$ although Glassdoor reviews, seemed to be a BIG, BIG SIGN OF DANGER!
Anyways, here\'s my experience, which has been an utter waste of time, TBH. And I only wish, I had listened to all other LC Reviews about Apollo.io which are so true, and despite being aware of such reviews- CLOWN COMPANY / STAY AWAY FROM APOLLO.IO etc, I went ahead, ONLY BECAUSE IT SEEMED TO PAY MORE THAN I \'D DRAW AS A STAFF ENGG PRESENTLY.

1. CODERBYTE ROUND-
   Dont worry about it. 2 LC MEDIUM+ questions, 1 is HARD though, Make sure you search the question and port the answer from that Other screen, well :)

2. DSA/PROBLEM SOLVING-
   q-> find subsequence in an array equalling a Target Sum. Then, find a "subset"(not contiguous) equaling that target Sum.
   verdict- No Flags, +ve Feedback

3. System Design-
   Design a geo-based Event Notification System.
   verdit- No Flags, +ve Feedback, CONGRATULATIONS!!

4. Behavioural Round-
   Now is where things get interesting!
   This round is "behavioural". My other rounds have been Technical, and all OK REPORT.
   And this Behavioural Round is to be conducted by someone who\'s my College Junior. Someone who has referred me. And yet, the Interviewer rejects me :D
   "Yiyee kya hai?"(carryminati style)!
   q-> introduce yourself in a format
- name of org
- team you were part of
- projects you were doing
- teammates you were managing
  q-> other silly, very silly questions. "Has something like this> happened to you? how did you come out of it?",
  this>
- you wanted to do something else team wanted to do something else.
- you had hard deadlines and you were lagging yet you emerged like a dark horse.
- you motivated a junior so much ki machaa daali aage chalke usne life mein!

I just did my level best, faking things the STAR way.
SUGGESTION- These behavioural rounds should ideally be banned. All employees actually act totally different of what they are expected to answer in behavioural rounds.

4 Days of Wait - Finally A Rejection Mail!
Meaning, despite my 3 tech rounds having gone so well, I gassed so much in the behavioural round that they were too suffocated to give me a chance for the remaining LLD round.

From what info I have collated, they do this to almost everybody.

My suggestion-
LOOK AT GLASSDOOR REVIEWS ONCE. THEY\'RE HORRIBLE.
AND THEN, LOOK AT ALL THE APOLLO.IO REVIEWS ON LC.

You\'d never want to even apply for this company. Life is short. Don\'t ruin it for such a company full of such Clowns.

---

### 17. [Apollo.io Debugging round | Front end | What to Expect?](https://leetcode.com/discuss/post/5020599/apolloio-debugging-round-front-end-what-e78wi)


# Apollo.io Debugging round | Front end | What to Expect?

- **Author:** None
- **Created at:** Apr 14, 2024 at 06:46 AM
- **Updated at:** Apr 15, 2024 at 06:34 PM
- **Tags:** Frontend, Interview

As the title suggests, I have couple of interviews coming up with Apollo.io. while other rounds seems straight forward but they seems to have a round for debugging.

Has anyone gone through the round? what to expect from this? What is the evaluation criteria here? Tried asking HRs were she gave me a automated reply.

Update - A react/redux app is given, which has some bugs. You need to fix those bugs.

---

### 18. [Apollo.io Senior Software Engineer](https://leetcode.com/discuss/post/4981647/apolloio-senior-software-engineer-by-age-r9k8)


# Apollo.io Senior Software Engineer

- **Author:** AgentSmith33
- **Created at:** Apr 06, 2024 at 07:32 AM
- **Updated at:** Apr 06, 2024 at 07:39 AM
- **Tags:** Interview

- **R0:** Coderbyte, 2 LC  medium
- **R1:** DSA, given two arrays pushed and popped, we need to tell it is a valid pop sequence or not. https://leetcode.com/problems/validate-stack-sequences/description/
- **R2[HLD]:** design a geobased meeting scheduler for offiline meeting within a city and send notification to participants to leave for meeting based on time taken from there current location to meeting location.
- **R3:** Every possible scenario based question you can imagine
- **R4[LLD]:** design a stock exchange,serves two kind of traders, institutional traders and indivisual traders, institutional traders will get notification of any stock buy/sell orders they subsribed.

---

### 19. [apollo.io - senior frontend - clown company](https://leetcode.com/discuss/post/4557327/apolloio-senior-frontend-clown-company-b-78fv)


# apollo.io - senior frontend - clown company

- **Author:** None
- **Created at:** Jan 13, 2024 at 08:07 AM
- **Updated at:** Jan 13, 2024 at 08:07 AM
- **Tags:** Frontend, Interview

I interviewed with Apollo.io for senior frontend L5 role.
It was a stupid waste of time and effort.

I had 2 rounds before facing rejection.

R1: It will be a live F2F interview on Codility platform. There was one easy react question and 2 easy questions on strings.

R2: They\'ll ask you to code a TodoMVC app in react and apparently you have to code everything and do a decent styling in 50 mins. It doesn\'t matter if you use good coding practices and implement your solution in a modular, scalable, extensible way. You should just go scorched earth and use the most ugliest code possible to complete all requirements to pass this round.

Recruiters won\'t bother giving you a feedback of why you didn\'t make it to the next round.

---

### 20. [Apollo.io - Senior Backend Engineer - Remote - Need help](https://leetcode.com/discuss/post/4441365/apolloio-senior-backend-engineer-remote-fqfek)


# Apollo.io - Senior Backend Engineer - Remote - Need help

- **Author:** None
- **Created at:** Dec 22, 2023 at 01:00 PM
- **Updated at:** Dec 22, 2023 at 01:00 PM
- **Tags:** interview experience, System Design, Interview

Hi all,

I have second round of interview scheduled for Apollo.io. This will be a system design round. Can you guys please share your interview experience for Apollo.io. What kind of questions to expect?

---

### 21. [Apollo.io | Senior Software Engineer | L6 | Remote](https://leetcode.com/discuss/post/4140782/apolloio-senior-software-engineer-l6-rem-6jnb)


# Apollo.io | Senior Software Engineer | L6 | Remote

- **Author:** None
- **Created at:** Oct 07, 2023 at 07:26 AM
- **Updated at:** Oct 07, 2023 at 07:33 AM
- **Tags:** Arcesium, Compensation

- **Education:** BTech and MTech, Tier-I college, India
- **Years of Experience:** 8+
- **Prior Experience:** Arcesium, Silent layoff - PIP
- **Date of the Offer:** September 2023
- **Company:** Apollo.io - also called Zenleads
- **Title/Level:** Senior Software Engineer, L6
- **Location:** Remote for ever
- **Base Salary:** 66LPA INR
- **Signing Bonus:** 5LPA INR
- **Stock bonus:** 13.7 Lacs INR per year ESOPs (adjusted for strike price, actual was 16 Lacs per year)
- **Performance:** based bonus - 7.2 LPA INR
- **Total comp (Salary + Bonus + Stock):** 92 Lacs first year, 87 Lacs from second year
- **Benefits:** Fully remote. No other benefits compensation wise.
- **Other details:** Lots of negotiation discussions but recruiter did not budge.
- **Previous salary:** 77 LPA (63LPA Base, 6LPA Long term incentives, 8LPA Bonus)

Additional info:
- Offer declined because Stocks would be granted only after completing 1st year and signing bonus needs to be returned if contract ends before a year. Going by layoffs around, it was highly risky to have compensation dependent on so many conditions.

---

### 22. [Stay away from Apollo.io](https://leetcode.com/discuss/post/3914431/stay-away-from-apolloio-by-anonymous_use-a90l)


# Stay away from Apollo.io

- **Author:** None
- **Created at:** Aug 15, 2023 at 04:37 PM
- **Updated at:** Aug 15, 2023 at 04:37 PM
- **Tags:** Interview

There is a company called Apollo.io, it is a US based complete remote startup and provides very high compensation. I recently interviewed with them and had a horrible experience. I am posting this just to make sure that other talented candidates will not have this kind of experience as the company ethics are really bad and they think that they are dealing in some market where all the candidates are just like chickens to them where they can only buy one.

I applied on their carrers site and received a test link around one month after applying. Below is the interview experience.

**Round 1 (Algo DS and Problem Solving)** -

Was asked a fairly simple question, coded the solution then came the follow up question which was a bit tricky one, suggested the solution and it was accepted by the interviewer, could not code it due to time constraints but the interviewer said it is fine.

Cleared the round and R2 was scheduled in 2 days.

**Round 2 (High Level System Design)** -

Design the live comment section for a streaming platform

Provided a high level design with deep diving for the connection handler for all the connected devices. Interviewer was satisfied with the approach.

Cleared the round and recieved the invite for R3 within half an hour after interview.

**Round 3 (Behavioural Round)** -

This round was mostly focused on the past experience. I was asked to explain one recent project in deep, apart from that few situation based questions were there. Interviewer seemed satisfied.

Cleared the round and Final round was scheduled in 3 days.

**Round 4 (Low Level System Design)** -

Design the Pricing module for a ride sharing application, pricing will be calculated on the basis of some features such as distance, time of the day, demand of rides etc. System should be designed in such a way where we can add and remove features from the system.

Provided the solution using factory pattern, Coded the complete solution in the coderbyte within the given time. Interviewer seemed satisifed.

Till this point I was receving updates very quickly from the company, but after the final round they ghosted me, after some time I mailed them asking for the updates as I was preety confident about the last round and all other rounds were preety good too, they said that they are waiting for the debrief. I waited few more days and asked for an update after a week, just after my email I recevied an email where they said.

```
Even though you completed the interview process and . We felt that you have just missed the mark
```

They did not had any reason to reject so they literally said you have just missed the mark, after 5 elimination rounds of interview they provided this kind of feedback for the candidate and when I asked further as I was not satisified they said I was good but not good enough comparing to one other candidate which they were interviewing parallely.

If I was not good enough they should have told me earlier, If I was clearing all the rounds it means I was meeting the expecations set by them to be picked for the role. They wasted my one month.

Why this is more concerning because I had a similar experience in December 2021 when I was interviewed for Backend Engineer role, they took 7 rounds and then came to me saying feedback of two rounds was not positive but at the time of the process the company was saying that each round is an elimination round.

Please stay away from such companies who are taking benefit of the difficult time in the industry and treating candidates like this.

---

