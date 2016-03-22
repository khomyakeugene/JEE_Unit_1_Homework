package com.company.utils;

import java.lang.reflect.Method;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class MethodDescriptor {
    public static final String METHOD_NAME_DELIMITER = "\\.";
    
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

    private String fullMethodName;
    private String[] subsidiaryMethodNames;
    private String methodName;
    private MethodArgumentType methodArgumentType;
    private boolean collectionAsObjectMethod;
    private boolean dataPrePopulate;

    public MethodDescriptor(String fullMethodName, MethodArgumentType methodArgumentType, boolean collectionAsObjectMethod, boolean dataPrePopulate) {
        setFullMethodName(methodName);
        
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

    public void setFullMethodName(String methodName) {
        this.fullMethodName = fullMethodName;
        
        Stirng[] mn = methodName.split(METHOD_NAME_DELIMITER);
        subsidiaryMethodNames = Arrays.copyOf(mn, mn.length - 1);
    
        this.methodName = mn[mn.length - 1];
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodArgumentType getMethodArgumentType() {
        return methodArgumentType;
    }

    public Method getMethod(Object object) {
        return methodArgumentType.getMethod(object, methodName);
    }
}
