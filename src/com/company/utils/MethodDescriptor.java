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
        
        public Object invokeMethod(Object object, Method method, Object... args) {
            return null;
        }
    }

    private String methodName;
    private String fullMethodName;
    private String[] subsidiaryMethodNames;
    private MethodArgumentType[] subsidiaryMethodArgumentTypes;
    private MethodArgumentType methodArgumentType;
    private boolean collectionAsObjectMethod;
    private boolean dataPrePopulate;

    public MethodDescriptor(String fullMethodName, MethodArgumentType[] methodArgumentType, boolean collectionAsObjectMethod, boolean dataPrePopulate) {
        setFullMethodName(fullMethodName);
        setMethodArgumentType(methodArgumentType);
        
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
        return this.methodArgumentType;
    }

    public void setMethodArgumentType(MethodArgumentType[] methodArgumentType) {
        subsidiaryMethodArgumentTypes = Arrays.copyOf(methodArgumentType, methodArgumentType.length-1);
        
        this.methodArgumentType = methodArgumentType[methodArgumentType.length-1];
    }
    
    public MethodArgumentType getSubsidiaryMethodArgumentTypes() {
        return subsidiaryMethodArgumentTypes;
    }

    public Method getMethod(Object object) {
        return getMethodArgumentType().getMethod(object, getMethodName());
    }
}
