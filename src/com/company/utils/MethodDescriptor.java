package com.company.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class MethodDescriptor {
    public static final String METHOD_NAME_REGEX_DELIMITER = "\\.";

    public enum MethodArgumentType {
        NO_ARGUMENTS {
            @Override
            Class[] buildParameterTypes() {
                return new Class[]{};
            }
        },

        ONE_OBJECT {
            @Override
            Class[] buildParameterTypes() {
                return new Class[]{Object.class};
            }
        },

        ONE_INT {
            @Override
            Class[] buildParameterTypes() {
                return new Class[]{int.class};
            }
        },

        ONE_INT_AND_ONE_OBJECT {
            @Override
            Class[] buildParameterTypes() {
                return new Class[]{int.class, Object.class};
            }
        };

        abstract Class[] buildParameterTypes();

        public Method getMethod(Object object, String methodName) {
            return SelfDescribingObjectService.searchPublicMethod(object, methodName, buildParameterTypes());
        }
    }

    private String methodName;
    private String fullMethodName;
    private String[] subsidiaryMethodNames;
    private MethodArgumentType[] methodArgumentType;
    private boolean collectionAsObjectMethod;
    private boolean dataPrePopulate;

    public MethodDescriptor(String fullMethodName, MethodArgumentType[] methodArgumentType, boolean collectionAsObjectMethod, boolean dataPrePopulate) {
        setFullMethodName(fullMethodName);
        
        this.methodArgumentType = methodArgumentType;
        this.collectionAsObjectMethod = collectionAsObjectMethod;
        this.dataPrePopulate = dataPrePopulate;
    }

    public boolean isCollectionAsObjectMethod() {
        return collectionAsObjectMethod;
    }

    public boolean isDataPrePopulate() {
        return dataPrePopulate;
    }

    public String getFullMethodName() {
        return this.fullMethodName;
    }

    public void setFullMethodName(String fullMethodName) {
        String[] mn = methodName.split(fullMethodName);
        subsidiaryMethodNames = Arrays.copyOf(mn, mn.length-1);
        
        this.fullMethodName = fullMethodName;
        this.methodName = mn[mn.length-1];
    }

    public String getMethodName() {
        return this.methodName;
    }

    public MethodArgumentType getMethodArgumentType() {
        return methodArgumentType[methodArgumentType.length-1];
    }

    public Object invokeMethod(Object object, Method method) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Method getMethod(Object object) {
            /*
            String[] methodNames = methodName.split(METHOD_NAME_REGEX_DELIMITER);

            // All the names before the last one interpret as methods which are returning the next-in-chain <object>
            for (int i = 0; i < methodNames.length-1; i++) {
                Method method = methodArgumentType[methodArgumentType.length-1].getMethod(object, methodNames[i]);
                object = method.invoke(object);

                methodArgumentType[i].getMethod(object, methodNames[i]).
            }
    */
        return getMethodArgumentType().getMethod(object, getMethodName());
    }
}
