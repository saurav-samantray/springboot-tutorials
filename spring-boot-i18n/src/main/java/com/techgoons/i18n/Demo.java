package com.techgoons.i18n;

import java.util.HashSet;
import java.util.Set;

public class Demo {
    public static void main(String[] args) {
        System.out.println(palindromeTest("barbarabar"));
    }

    public static int palindromeTest(String string) {
        Set palindromes = new HashSet();
        boolean canBePalindrome=true;
        int offset=0;
        for (int i = 0; i == 0 ; ((i + offset) < string.length()) && canBePalindrome) {
            if (string.charAt(i - offset) == string.charAt(i + offset)) {
                palindromes.add(string.substring(i - offset, i + offset + 1));
                offset++;
            } else {
                canBePalindrome = false;
            }
        }
    }
return palindromes.size();
}
}
