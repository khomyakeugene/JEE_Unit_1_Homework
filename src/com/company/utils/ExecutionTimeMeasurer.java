package com.company.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class ExecutionTimeMeasurer {
    public static long getNanoTime(Object object, Method method) {
        long start = System.nanoTime();
        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        long finish = System.nanoTime();

        return finish - start;
    }

    public static long getNanoTime(Object object, Method method, Object methodArgument) {
        long start = System.nanoTime();
        try {
            method.invoke(object, methodArgument);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        long finish = System.nanoTime();

        return finish - start;
    }

    public static long getNanoTime(Object object, Method method, int methodArgument) {
        long start = System.nanoTime();
        try {
            method.invoke(object, methodArgument);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        long finish = System.nanoTime();

        return finish - start;
    }

    public static long getNanoTime(Object object, Method method, int methodArgument1, Object methodArgument2) {
        long start = System.nanoTime();
        try {
            method.invoke(object, methodArgument1, methodArgument2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        long finish = System.nanoTime();

        return finish - start;
    }
}
