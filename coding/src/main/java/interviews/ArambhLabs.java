package interviews;/*
Given a String S, Find all possible Palindromic partitions of the given String.
Example 1:
Input:
S = "geeks"
Output:
g e e k s
g ee k s
Explanation:
All possible palindromic partitions
are printed.
Example 2:
Input:
S = "madammadam"
Output:
m a d a m

 */


import java.util.ArrayList;
import java.util.List;

public class ArambhLabs {
    public static void main(String[] args) {
        String input = "mmadam";
//        List<List<String>> output =  findAllPalindromePartitions(input);
        List<List<String>> output =  findAllPalindromePartitionsBackTracking(input);
        // test slideOverTheInput for size 2
        System.out.println(output);

    }

    private static List<List<String>> findAllPalindromePartitionsBackTracking(String input) {
        if(input == null || input.length() == 0) {
            return new ArrayList<>();
        }
        if(input.length() == 1) {
            List<String> list = new ArrayList<>();
            list.add(input);
            return list;
        }
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            List<String> temp = new ArrayList<>();

        }
        return null;
    }


    private static List<List<String>> findAllPalindromePartitions(String input) {
        List<List<String>> result = new ArrayList<>();
        for(int i = 0; i < input.length(); i++) {
            //  for each size of subarray check for each one if they are palindrome or not
            int size = i+1;
            result.add(slideOverTheInput(input, size));
        }
        return result;
    }

    private static List<String> slideOverTheInput(String input, int slideSize){
         int i = 0;
         int j = i + slideSize - 1;
         List<String> result = new ArrayList<>();
         while(j<input.length()){
             if(isPalindrome(input,i,j)){
                 result.add(input.substring(i,j+1));
             }
             j++;
             i++;
         }
         return result;
    }

    private static boolean isPalindrome(String s, int i, int j) {

        // iterate i from left to right until it meets j
        while(i<j){
            if(s.charAt(i)!=s.charAt(j)){
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}
