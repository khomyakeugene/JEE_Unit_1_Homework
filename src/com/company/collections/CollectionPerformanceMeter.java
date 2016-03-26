package com.company.collections;

import com.company.utils.*;

import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.OptionalDouble;

/**
 * Created by Yevgen on 19.03.2016 as a part of the project "JEE_Homework_1".
 */
public class CollectionPerformanceMeter {
    public static final String MEASURE_PERFORMANCE_PATTERN = "Element quantity: %d, Measure performance of <%s> method ...";
    public static final String ATTEMPT_PATTERN = "Attempt %d ...";

    private int collectionSize;
    private int measuringQuantity;
    private AbstractCollection<Integer> collection;
    private Integer[] sourceData;

    private RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    public CollectionPerformanceMeter(AbstractCollection<Integer> collection, int collectionSize, int measuringQuantity) {
        setCollection(collection);
        setCollectionSize(collectionSize);
        setMeasuringQuantity(measuringQuantity);
    }

    public CollectionPerformanceMeter(int collectionSize, int measuringQuantity) {
        this(null, collectionSize, measuringQuantity);
    }

    public CollectionPerformanceMeter() {
        this(null, 0, 0);
    }

    public void setCollectionSize(int collectionSize) {
        this.collectionSize = collectionSize;

        // Re-generate source data
        sourceData = randomDataGenerator.generateIntegerArray(collectionSize);
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

    public void populateCollection() {
        collection.clear();
        collection.addAll(Arrays.asList(sourceData));
    }

    private int indexUpperLimit() {
        return collection.size();
    }

    private int contentUpperLimit() {
        return indexUpperLimit() * RandomDataGenerator.DEFAULT_UPPER_INT_LIMIT_MULTIPLIER;
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

    private Integer generateRandomInteger(int upperLimit) {
        return randomDataGenerator.generateRandomInt(upperLimit);
    }

    public long measurePerformance(AbstractCollection<Integer> collection,
                                   MethodDescriptor methodDescriptor,
                                   int collectionSize,
                                   int measuringQuantity) {
        // Mandatory fix collection - important for using further
        setCollection(collection);
        // Re-new measure parameters
        reInitMeasureParameters(collectionSize, measuringQuantity);

        // Pre-populate data if necessary
        if (methodDescriptor.isDataPrePopulate()) {
            if (collection.size() != collectionSize) {
                populateCollection();
            }
        } else {
            collection.clear();
        }

        // Also execute subsidiary methods, if there are some, getting as a result "main" object
        Object object = methodDescriptor.invokeSubsidiaryMethods(methodDescriptor.isCollectionAsObjectMethod() ? collection : this,
                randomDataGenerator.generateRandomInt(indexUpperLimit()));
        // Pre-"main" method - it is necessary, for example, for <Iterator.remove> or <ListIterator.remove> method
        Method preMethod = methodDescriptor.getWithoutParametersVoidPreMethod(object);
        // "Main" method
        Method method = methodDescriptor.getMethod(object);
        MethodArgumentType methodArgumentType = methodDescriptor.getMethodArgumentType();

        long[] results = new long[measuringQuantity];
        boolean tryToCallGarbageCollector = false;
        Utils.printMessage(collection, String.format(MEASURE_PERFORMANCE_PATTERN, collectionSize, methodDescriptor.getFullMethodName()));
        for (int i = 0; i < measuringQuantity; i++) {
            Utils.rePrintLine(String.format(ATTEMPT_PATTERN, i + 1));
            // Pre-"main" method - it is necessary, for example, for <Iterator.remove> or <ListIterator.remove> method
            if (preMethod != null) {
                SelfDescribingObjectService.invokeMethod(object, preMethod);
            }
            // "Main" method
            try {
                switch (methodArgumentType) {
                    case NO_ARGUMENTS:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method);
                        break;

                    case ONE_OBJECT:
                    case ONE_INT:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method,
                                generateRandomInteger(getMethodUpperLimit(methodDescriptor)));
                        break;

                    case ONE_INT_AND_ONE_OBJECT:
                        results[i] = ExecutionTimeMeasurer.getNanoTime(object, method,
                                randomDataGenerator.generateRandomInt(indexUpperLimit()),
                                generateRandomInteger(getMethodUpperLimit(methodDescriptor)));
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

        OptionalDouble optionalDoubleResult = Arrays.stream(results).average();
        return optionalDoubleResult.isPresent() ? (long)optionalDoubleResult.getAsDouble() : -1L;
    }

    public long measurePerformance(AbstractCollection<Integer> collection,
                                   MethodDescriptor methodDescriptor) {
        return measurePerformance(collection, methodDescriptor, this.collectionSize, this.measuringQuantity);
    }
}
