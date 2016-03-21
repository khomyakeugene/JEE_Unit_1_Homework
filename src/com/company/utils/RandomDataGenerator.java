package com.company.utils;

/**
 * Created by Yevgen on 18.03.2016 as a part of the project "JEE_Homework_1".
 */
public class RandomDataGenerator {
    public static final String GENERATE_RANDOM_INTEGER_ELEMENTS_PATTERN = "Generate random %d <Integer> elements ...";

    public static final int DEFAULT_UPPER_INT_LIMIT = 1000;
    public static final int DEFAULT_UPPER_INT_LIMIT_MULTIPLIER = 10;

    private int upperIntLimit;

    public RandomDataGenerator(int upperLimit) {
        this.upperIntLimit = upperLimit;
    }

    public RandomDataGenerator() {
        this (DEFAULT_UPPER_INT_LIMIT);
    }

    public void setUpperIntLimit(int upperIntLimit) {
        this.upperIntLimit = upperIntLimit;
    }

    public int generateRandomInt() {
        return generateRandomInt(this.upperIntLimit);
    }

    public int generateRandomInt(int upperIntLimit) {
        return (int)(Math.random() * upperIntLimit);
    }

    public Integer[] generateIntegerArray(int size) {
        Integer[] result = new Integer[size];

        setUpperIntLimit(size * DEFAULT_UPPER_INT_LIMIT_MULTIPLIER);

        Utils.printLine(String.format(GENERATE_RANDOM_INTEGER_ELEMENTS_PATTERN, size));
        for (int i = 0; i < size; i++) {
            result[i] = generateRandomInt();
        }
        Utils.printDoneMessage();

        return result;
    }
}
