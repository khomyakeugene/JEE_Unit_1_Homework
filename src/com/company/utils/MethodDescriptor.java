package com.company.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class MethodDescriptor {
    public static final String METHOD_NAME_REGEX_DELIMITER = "\\.";

    private String methodName;
    private String fullMethodName;
    private String withoutParametersVoidPreMethodName;
    private String[] subsidiaryMethodNames;
    private MethodArgumentType[] subsidiaryMethodArgumentTypes;
    private MethodArgumentType methodArgumentType;
    private boolean collectionAsObjectMethod;
    private boolean dataPrePopulate;

    public MethodDescriptor(String fullMethodName, String withoutParametersVoidPreMethodName,
                            MethodArgumentType[] methodArgumentType, boolean collectionAsObjectMethod,
                            boolean dataPrePopulate) {
        setFullMethodName(fullMethodName);
        setMethodArgumentType(methodArgumentType);

        this.withoutParametersVoidPreMethodName = withoutParametersVoidPreMethodName;
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
        String[] mn = fullMethodName.split(METHOD_NAME_REGEX_DELIMITER);
        subsidiaryMethodNames = Arrays.copyOf(mn, mn.length-1);

        this.fullMethodName = fullMethodName;
        this.methodName = mn[mn.length-1];
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getWithoutParametersVoidPreMethodName() {
        return withoutParametersVoidPreMethodName;
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

    public Method getWithoutParametersVoidPreMethod(Object object) {
        return MethodArgumentType.NO_ARGUMENTS.getMethod(object, getWithoutParametersVoidPreMethodName());
    }

    public Object invokeSubsidiaryMethods(Object object, Integer argument) {
        for (int i = 0; i < subsidiaryMethodNames.length; i++) {
            object = subsidiaryMethodArgumentTypes[i].invokeMethod(object, subsidiaryMethodNames[i], argument);
        }

        return object;
    }
}
