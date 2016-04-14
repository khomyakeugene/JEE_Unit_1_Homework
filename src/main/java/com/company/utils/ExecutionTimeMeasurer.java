package com.company.utils;

import java.lang.reflect.Method;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class ExecutionTimeMeasurer {
    public static long getNanoTime(Object object, Method method, Object ... obj) {
        long start = System.nanoTime();
        SelfDescribingObjectService.invokeMethod(object,method, obj);
        long finish = System.nanoTime();

        return finish - start;
    }

    public static long getNanoTime(Object object, Method method) {
        return getNanoTime(object,method, new Object[] {});
    }

    public static long getNanoTime(Object object, Method method, Object methodArgument) {
        return getNanoTime(object,method, new Object[] {methodArgument});
    }

    public static long getNanoTime(Object object, Method method, int methodArgument1, Object methodArgument2) {
        return getNanoTime(object,method, new Object[] {methodArgument1, methodArgument2});
    }
}
