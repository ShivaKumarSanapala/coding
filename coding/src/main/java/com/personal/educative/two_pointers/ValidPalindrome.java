package com.personal.educative.two_pointers;

public class ValidPalindrome {
    public boolean isPalindrome(String input) {
        if (input == null) return false;
        int i = 0, j = input.length() - 1;

        while (i < j) {
            while (i < j && !Character.isLetterOrDigit(input.charAt(i))) i++;
            while (i < j && !Character.isLetterOrDigit(input.charAt(j))) j--;

            if (Character.toLowerCase(input.charAt(i)) != Character.toLowerCase(input.charAt(j))) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}
