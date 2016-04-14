package com.company.utils;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Yevgen on 10.01.2016.
 */
public class SelfDescribingObjectService {
    static Method searchMethod(String className, String methodName, Class<?>[] parameterTypes, boolean onlyPublic) {
        Method method = null;
        if (methodName != null) {
            try {
                Class<?> cls = Class.forName(className);

                try {
                    method = onlyPublic ? cls.getMethod(methodName, parameterTypes) :
                            cls.getDeclaredMethod(methodName, parameterTypes);
                    if (method != null) {
                        method.setAccessible(true);
                    }
                } catch (NullPointerException | SecurityException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return method;
    }

    public static Method searchMethod(Object object, String methodName, Class[] parameterTypes, boolean onlyPublic) {
        return searchMethod(object.getClass().getName(), methodName, parameterTypes, onlyPublic);
    }

    public static Method searchPublicMethod(Object object, String methodName, Class[] parameterTypes) {
        return searchMethod(object, methodName, parameterTypes, true);
    }
    
    public static Object invokeMethod(Object object, Method method, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
