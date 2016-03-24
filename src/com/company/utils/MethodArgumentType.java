package com.company.utils;

import java.lang.reflect.Method;

/**
 * Created by Yevgen on 25.03.2016 as a part of the project "JEE_Unit_1_Homework".
 */
public enum MethodArgumentType {
    NO_ARGUMENTS {
        @Override
        Class[] buildParameterTypes() {
            return new Class[]{};
        }

        @Override
        public Object invokeMethod(Object object, Method method, Integer argument) {
            return SelfDescribingObjectService.invokeMethod(object, method);
        }
    },

    ONE_OBJECT {
        @Override
        Class[] buildParameterTypes() {
            return new Class[]{Object.class};
        }

        public Object invokeMethod(Object object, Method method, Integer argument) {
            return SelfDescribingObjectService.invokeMethod(object, method, argument);
        }
    },

    ONE_INT {
        @Override
        Class[] buildParameterTypes() {
            return new Class[]{int.class};
        }

        @Override
        public Object invokeMethod(Object object, Method method, Integer argument) {
            return SelfDescribingObjectService.invokeMethod(object, method, argument);
        }
    },

    ONE_INT_AND_ONE_OBJECT {
        @Override
        Class[] buildParameterTypes() {
            return new Class[]{int.class, Object.class};
        }

        @Override
        public Object invokeMethod(Object object, Method method, Integer argument) {
            return SelfDescribingObjectService.invokeMethod(object, method, argument, argument);
        }
    };

    abstract Class[] buildParameterTypes();

    public Method getMethod(Object object, String methodName) {
        return SelfDescribingObjectService.searchPublicMethod(object, methodName, buildParameterTypes());
    }

    public Object invokeMethod(Object object, String methodName, Integer argument) {
        return invokeMethod(object, getMethod(object, methodName), argument);
    }

    public abstract Object invokeMethod(Object object, Method method, Integer argument);
}

