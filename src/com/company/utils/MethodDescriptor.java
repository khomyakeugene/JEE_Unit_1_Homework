package com.company.utils;

package utils;

import java.util.Arrays;
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

            @Override
            public Object invokeMethod(Object object, Method method, Integer argument) {
                return SelfDescribingObjectService.invokeMethod(object, method, new Object[] {});
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
                return SelfDescribingObjectService.invokeMethod(object, method, argument.intValue());
            }
        },

        ONE_INT_AND_ONE_OBJECT {
            @Override
            Class[] buildParameterTypes() {
                return new Class[]{int.class, Object.class};
            }
            
            @Override
            public Object invokeMethod(Object object, Method method, Integer argument) {
                return SelfDescribingObjectService.invokeMethod(object, method, argument.intValue(), argument);
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
    
    public Method getMethod(Object object) {
        return getMethodArgumentType().getMethod(object, getMethodName());
    }
    
    public Object invokeSubsidiaryMethods(Object object, Integer argument) {
        // Execute all subsidiary methods, getting as a result "main" object
        for (int i = 0; i < subsidiaryMethodNames.length; i++) {
            object = subsidiaryMethodArgumentTypes[i].invokeMethod(object, subsidiaryMethodNames[i], argument);
        }

        return object;
    }
}
