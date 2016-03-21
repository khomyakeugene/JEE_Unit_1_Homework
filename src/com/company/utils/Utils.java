package com.company.utils;

import java.util.Arrays;

/**
 * Created by Yevgen on 06.01.2016.
 */

public class Utils {
    public static final String DONE_MESSAGE = "done";
    public final static String MESSAGE_WITH_PREFIX_PATTERN = "<%s>: %s";
    public final static String EXCEPTION_MESSAGE_PATTERN = "<%s>: %s";

    public enum AlignmentType {
        LEFT {
            String alignString(String data, int width) {
                return data + getCharPiece(data, width);
            }
        },

        RIGHT {
            String alignString(String data, int width) {
                return getCharPiece(data, width) + data;
            }
        },

        CENTRE {
            String alignString(String data, int width) {
                int resultWidth = getResultLineLength(data, width);

                int leftSpacePieceLength = (width - resultWidth) / 2;
                int rightSpacePieceLength = (width - resultWidth - leftSpacePieceLength);
                String withoutRightPiece = getCharPiece(data, width - rightSpacePieceLength) + data;

                return withoutRightPiece + getCharPiece(withoutRightPiece, width);
            }
        };

        abstract String alignString(String data, int width);

        int getResultLineLength(String data, int width) {
            int dataLength = data.length();
            return dataLength > width ? dataLength : width;
        }

        String getCharPiece(String data, int width) {
            char spacePiece[] = new char[getResultLineLength(data, width) - data.length()];
            Arrays.fill(spacePiece, ' ');

            return new String(spacePiece);
        }
    };

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


    public static void throwTextException(Object object, String text) throws Exception {
        throw new Exception(String.format(EXCEPTION_MESSAGE_PATTERN, object.getClass().getName(), text));
    }

    public static void printDoneMessage() {
        printMessage(DONE_MESSAGE);
    }

    public static String getLongestString(String[] data) {
        return Arrays.stream(data).max((f1, f2) -> new Integer(f1.length()).compareTo(new Integer(f2.length()))).get();
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