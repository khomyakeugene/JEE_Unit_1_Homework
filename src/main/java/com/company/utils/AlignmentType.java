package com.company.utils;

import java.util.Arrays;

/**
 * Created by Yevgen on 25.03.2016 as a part of the project "JEE_Unit_1_Homework".
 */
public enum AlignmentType {
    LEFT {
        @Override
        String alignString(String data, int width) {
            return data + getCharPiece(data, width);
        }
    },

    RIGHT {
        @Override
        String alignString(String data, int width) {
            return getCharPiece(data, width) + data;
        }
    },

    CENTRE {
        @Override
        String alignString(String data, int width) {
            int resultWidth = getResultLineLength(data, width);

            int spaceCount = resultWidth - data.length();
            int leftSpacePieceLength = spaceCount>>>1;
            int rightSpacePieceLength = (spaceCount - leftSpacePieceLength);
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
}
