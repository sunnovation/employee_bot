package com.employee.bot.util;

import java.util.Random;

public class BotUtility {


    public static String generateUId() {

        int[] result=generate(3);
        return String.valueOf(result);
    }


    static int[] generate(int n) {
        // Numbers 100 and 101 contain duplicates, so lower limit is 102.
        // Upper limit is 987 (inclusive), since 988, 989, and 99x all contain duplicates.
        return new Random().ints(1, 999)
                .filter(BotUtility::isThreeUniqueDigits)
                .limit(n)
                .toArray();
    }
    private static boolean isThreeUniqueDigits(int i) {
        int d1 = i / 100;
        int d2 = i / 10 % 10;
        int d3 = i % 10;
        return (d1 != d2 && d1 != d3 && d2 != d3);
    }

    public static String generateConversationID() {

        int id = 100 + (int) (Math.random()*899);
        return String.valueOf(id);
    }
}
