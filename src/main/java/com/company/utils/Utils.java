package com.company.utils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Yevgen on 06.01.2016.
 */

public class Utils {
    private static final String DONE_MESSAGE = "done";
    private final static String MESSAGE_WITH_PREFIX_PATTERN = "<%s>: %s";

    public static void printLine(String message) {
        System.out.print(message);
    }

    public static void rePrintLine(String message) {
        printLine("\r" + message);
    }

    public static void printMessage(String message) {
        printLine(message + "\n");
    }

    public static void printMessage(Object object, String message) {
        printMessage(String.format(MESSAGE_WITH_PREFIX_PATTERN, object.getClass().getName(), message));
    }

    public static void printDoneMessage() {
        printMessage(DONE_MESSAGE);
    }

    private static String getLongestString(String[] data) {
        Optional<String> longest = Arrays.stream(data).max((f1, f2) -> new Integer(f1.length()).compareTo(f2.length()));

        return longest.isPresent() ? longest.get() : "";
    }

    public static int getLengthOfLongestString(String[] data) {
        String s = getLongestString(data);
        return s.length();
    }

    public static String[] convertColumnToStringArray (String[][] tableData, int columnNumber) {
        String[] result = new String[tableData.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = tableData[i][columnNumber];
        }

        return result;
    }
}