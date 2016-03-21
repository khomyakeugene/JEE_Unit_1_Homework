package com.company.collections;

import com.company.utils.ExecutionTimeMeasurer;
import com.company.utils.RandomDataGenerator;
import com.company.utils.MethodDescriptor;
import com.company.utils.Utils;

import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class CollectionPerformanceMeter {
    public static final String MEASURE_PERFORMANCE_PATTERN = "Element quantity: %d, Measure performance of <%s> method ...";
    public static final String ATTEMPT_PATTERN = "Attempt %d ...";
    public static final String METHOD_DESCRIPTOR_NOT_FOUND_PATTERN = "Descriptor of method <%s> not found!";

    private Integer[] sourceData;

    private int collectionSize;
    private int measuringQuantity;
    private AbstractCollection<Integer> collection;

    private HashMap<String, MethodDescriptor> methodDescriptor = new HashMap<>();

    public CollectionPerformanceMeter(AbstractCollection<Integer> collection, int collectionSize, int measuringQuantity) {
        setCollection(collection);
        setCollectionSize(collectionSize);
        setMeasuringQuantity(measuringQuantity);
    }

    public CollectionPerformanceMeter() {
        this (null, 0, 0);
    }

    public void setCollectionSize(int collectionSize) {
        this.collectionSize = collectionSize;

        // Re-generate source data
        sourceData = new RandomDataGenerator().generateIntegerArray(collectionSize);
    }

    public void setCollection(AbstractCollection<Integer> collection) {
        this.collection = collection;
    }

    public void setMeasuringQuantity(int measuringQuantity) {
        this.measuringQuantity = measuringQuantity;
    }

    private void reInitMeasureParameters(int collectionSize, int measuringQuantity) {
        if (collectionSize != this.collectionSize) {
            setCollectionSize(collectionSize);
        }

        if (measuringQuantity != this.measuringQuantity) {
            setMeasuringQuantity(measuringQuantity);
        }
    }

    public MethodDescriptor getMethodDescriptorByName(String methodName) throws Exception {
        MethodDescriptor result = methodDescriptor.get(methodName);
        if (result == null) {
            Utils.throwTextException(this, String.format(METHOD_DESCRIPTOR_NOT_FOUND_PATTERN, methodName));
        }

        return result;
    }

    public void populateCollection() {
        collection.clear();
        collection.addAll(Arrays.asList(sourceData));
    }

    private int indexUpperLimit() {
        return collection.size();
    }

    private int contentUpperLimit() {
        return collection.size() * RandomDataGenerator.DEFAULT_UPPER_INT_LIMIT_MULTIPLIER;
    }

    private int getMethodUpperLimit(MethodDescriptor methodDescriptor) {
        int result = 0;

        switch (methodDescriptor.getMethodArgumentType()) {
            case ONE_INT:
                result = indexUpperLimit();
                break;

            case ONE_OBJECT:
            case ONE_INT_AND_ONE_OBJECT:
                result = contentUpperLimit();
                break;
        }

        return result;
    }

    public long measurePerformance(AbstractCollection<Integer> collection,
                                   MethodDescriptor methodDescriptor,
                                   int collectionSize,
                                   int measuringQuantity) {
        // Mandatory fix collection - important for using further
        setCollection(collection);
        // Re-new measure parameters
        reInitMeasureParameters(collectionSize, measuringQuantity);

        Object object = methodDescriptor.isCollectionAsObjectMethod() ? collection : this;
        Method method = methodDescriptor.getMethod(object);

        // Pre-populate data if necessary
        if (methodDescriptor.isDataPrePopulate()) {
            if (collection.size() != collectionSize) {
                populateCollection();
            }
        } else {
            collection.clear();
        }

        long[] results = new long[measuringQuantity];
        boolean tryToCallGarbageCollector = false;
        Utils.printMessage(collection, String.format(MEASURE_PERFORMANCE_PATTERN, collectionSize, method.getName()));
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        for (int i = 0; i < measuringQuantity; i++) {
            Utils.rePrintLine(String.format(ATTEMPT_PATTERN, i+1));
            Integer randomInputData = randomDataGenerator.generateRandomInt(getMethodUpperLimit(methodDescriptor));
            try {
                switch (methodDescriptor.getMethodArgumentType()) {
                    case NO_ARGUMENTS:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method);
                        break;

                    case ONE_OBJECT:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method, randomInputData);
                        break;

                    case ONE_INT:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method, randomInputData.intValue());
                        break;

                    case ONE_INT_AND_ONE_OBJECT:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method,
                                randomDataGenerator.generateRandomInt(indexUpperLimit()), randomInputData);
                        break;
                }

                if (tryToCallGarbageCollector) {
                    // Try to free unused resources if it necessary
                    System.gc();
                }
            } catch (OutOfMemoryError e) {
                if (tryToCallGarbageCollector) {
                    throw new OutOfMemoryError();
                } else {
                    tryToCallGarbageCollector = true;
                    // Try to free unused resources
                    System.gc();
                    // Repeat current attempt
                    i--;
                }
            }
        }
        Utils.printDoneMessage();

        return (long)(Arrays.stream(results).average().getAsDouble());
    }
}