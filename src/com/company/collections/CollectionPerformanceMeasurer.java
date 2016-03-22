package com.company.collections;

import com.company.utils.MethodDescriptor;

import java.util.AbstractCollection;
import java.util.HashMap;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class CollectionPerformanceMeasurer {
    private AbstractCollection<Integer> abstractCollection;
    private HashMap<String, MethodDescriptor> methodDescriptorMap = new HashMap<>();
    private HashMap<String, Long> methodResultMap = new HashMap<>();

    public CollectionPerformanceMeasurer(AbstractCollection<Integer> abstractCollection) {
        this.abstractCollection = abstractCollection;
    }

    public AbstractCollection<Integer> getAbstractCollection() {
        return abstractCollection;
    }

    public Long getMethodResult(String methodName) {
        return methodResultMap.get(methodName);
    }

    public void setMethodResult(String methodName, long value) {
        methodResultMap.put(methodName, value);
    }

    public HashMap<String, MethodDescriptor> getMethodDescriptorMap() {
        return methodDescriptorMap;
    }

    public void addMethodDescriptor(String methodName,
                                    MethodDescriptor.MethodArgumentType[] methodArgumentType,
                                    boolean useCollectionAsObjectMethod,
                                    boolean needDataPrePopulate) {
        methodDescriptorMap.put(methodName,
                new MethodDescriptor(methodName, methodArgumentType, useCollectionAsObjectMethod, needDataPrePopulate));
        methodResultMap.put(methodName, new Long(0));
    }

    public void addMethodDescriptor(String methodName,
                                    MethodDescriptor.MethodArgumentType[] methodArgumentType,
                                    boolean needDataPrePopulate) {
        addMethodDescriptor(methodName, methodArgumentType, true, needDataPrePopulate);
    }

    public String getCollectionName() {
        return this.abstractCollection.getClass().getName();
    }
}
