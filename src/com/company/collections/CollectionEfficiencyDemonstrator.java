package com.company.collections;

import com.company.utils.*;

import java.util.*;

/**
 * Created by Yevgen on 18.03.2016 as a part of the project "JEE_Homework_1".
 */
public class CollectionEfficiencyDemonstrator {
    public static final String HEADER_PATTERN = "Elements: %d, Time: ns";

    public static final String NEXT_METHOD_NAME = "next";
    public static final String ADD_METHOD_NAME = "add";
    public static final String GET_METHOD_NAME = "get";
    public static final String REMOVE_METHOD_NAME = "remove";
    public static final String CONTAINS_METHOD_NAME = "contains";
    public static final String POPULATE_COLLECTION_METHOD_NAME = "populateCollection";
    public static final String LIST_ITERATOR_ADD_METHOD_NAME = "listIterator.add";
    public static final String ITERATOR_REMOVE_METHOD_NAME = "iterator.remove";

    public static final String FILENAME_PATTERN = "collection_efficiency_%d.txt";

    public static final int ATTEMPT_COUNT = 100;

    public static final int COLLECTION_SIZE_10000 = 10000;
    public static final int COLLECTION_SIZE_100000 = 100000;
    public static final int COLLECTION_SIZE_1000000 = 1000000;
    public static final int[] collectionSize = {COLLECTION_SIZE_10000, COLLECTION_SIZE_100000, COLLECTION_SIZE_1000000};

    private HashMap<String, CollectionPerformanceMeasurer> testData = new HashMap<>();

    private void buildTestData() {
        // ArrayList
        CollectionPerformanceMeasurer arrayListPerformanceMeasurer =
                new CollectionPerformanceMeasurer(new ArrayList<>());
        arrayListPerformanceMeasurer.addMethodDescriptor(ADD_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT_AND_ONE_OBJECT}, true);
        arrayListPerformanceMeasurer.addMethodDescriptor(GET_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT}, true);
        arrayListPerformanceMeasurer.addMethodDescriptor(REMOVE_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT}, true);
        arrayListPerformanceMeasurer.addMethodDescriptor(CONTAINS_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        arrayListPerformanceMeasurer.addMethodDescriptor(POPULATE_COLLECTION_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.NO_ARGUMENTS}, false, false);
        arrayListPerformanceMeasurer.addMethodDescriptor(LIST_ITERATOR_ADD_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT,
                                MethodArgumentType.ONE_OBJECT}, true);
        arrayListPerformanceMeasurer.addMethodDescriptor(ITERATOR_REMOVE_METHOD_NAME, NEXT_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.NO_ARGUMENTS,
                                MethodArgumentType.NO_ARGUMENTS}, true);
        testData.put(arrayListPerformanceMeasurer.getCollectionName(), arrayListPerformanceMeasurer);

        // LinkedList
        CollectionPerformanceMeasurer linkedListPerformanceMeasurer =
                new CollectionPerformanceMeasurer(new LinkedList<>());
        linkedListPerformanceMeasurer.addMethodDescriptor(ADD_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT_AND_ONE_OBJECT}, true);
        linkedListPerformanceMeasurer.addMethodDescriptor(GET_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT}, true);
        linkedListPerformanceMeasurer.addMethodDescriptor(REMOVE_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT}, true);
        linkedListPerformanceMeasurer.addMethodDescriptor(CONTAINS_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        linkedListPerformanceMeasurer.addMethodDescriptor(POPULATE_COLLECTION_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.NO_ARGUMENTS}, false, false);
        arrayListPerformanceMeasurer.addMethodDescriptor(LIST_ITERATOR_ADD_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_INT,
                                MethodArgumentType.ONE_OBJECT}, true);
        arrayListPerformanceMeasurer.addMethodDescriptor(ITERATOR_REMOVE_METHOD_NAME, NEXT_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.NO_ARGUMENTS,
                                MethodArgumentType.NO_ARGUMENTS}, true);
        testData.put(linkedListPerformanceMeasurer.getCollectionName(), linkedListPerformanceMeasurer);

        // HashSet
        CollectionPerformanceMeasurer hashSetPerformanceMeasurer =
                new CollectionPerformanceMeasurer(new HashSet<>());
        hashSetPerformanceMeasurer.addMethodDescriptor(ADD_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        hashSetPerformanceMeasurer.addMethodDescriptor(REMOVE_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        hashSetPerformanceMeasurer.addMethodDescriptor(CONTAINS_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        hashSetPerformanceMeasurer.addMethodDescriptor(POPULATE_COLLECTION_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.NO_ARGUMENTS}, false, false);
        testData.put(hashSetPerformanceMeasurer.getCollectionName(), hashSetPerformanceMeasurer);

        // TreeSet
        CollectionPerformanceMeasurer treeSetPerformanceMeasurer =
                new CollectionPerformanceMeasurer(new TreeSet<>());
        treeSetPerformanceMeasurer.addMethodDescriptor(ADD_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        treeSetPerformanceMeasurer.addMethodDescriptor(REMOVE_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        treeSetPerformanceMeasurer.addMethodDescriptor(CONTAINS_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.ONE_OBJECT}, true);
        treeSetPerformanceMeasurer.addMethodDescriptor(POPULATE_COLLECTION_METHOD_NAME,
                new MethodArgumentType[] {MethodArgumentType.NO_ARGUMENTS}, false, false);
        testData.put(treeSetPerformanceMeasurer.getCollectionName(), treeSetPerformanceMeasurer);
    }

   public CollectionEfficiencyDemonstrator() {
       buildTestData();
    }

    private void oneSizeCalculate(int collectionSize, int measuringQuantity) {
        CollectionPerformanceMeter collectionPerformanceMeter = new CollectionPerformanceMeter(collectionSize, measuringQuantity);

        testData.keySet().stream().
                forEach(c ->
                        testData.get(c).getMethodDescriptorMap().keySet().
                                forEach(m ->
                                {CollectionPerformanceMeasurer collectionPerformanceMeasurer = testData.get(c);
                                    MethodDescriptor methodDescriptor = collectionPerformanceMeasurer.getMethodDescriptorMap().get(m);
                                    String fullMethodName = methodDescriptor.getFullMethodName();
                                    collectionPerformanceMeasurer.setMethodResult(fullMethodName, collectionPerformanceMeter.
                                            measurePerformance(collectionPerformanceMeasurer.getAbstractCollection(),
                                            methodDescriptor));}));
    }

    private String[][] prepareTableData(int collectionSize) {
        // Method name as Columns
        HashSet<String> columnNames = new HashSet<>();
        testData.keySet().stream().forEach(c -> columnNames.addAll(testData.get(c).getMethodDescriptorMap().keySet()));

        // Table data
        String[][] result = new String[testData.size()+1][columnNames.size()+1];
        // Left upper corner is something like header
        result[0][0] = String.format(HEADER_PATTERN, collectionSize);
        // First line - column names
        Iterator<String> columnNameIterator = columnNames.iterator();
        // Maybe, "too strong" "data length" control but let it keep it here
        for (int i = 1; i < result[0].length && columnNameIterator.hasNext(); i++) {
            result[0][i] = columnNameIterator.next();
        }

        Iterator<String> collectionNameIterator = testData.keySet().iterator();
        // Maybe, "too strong" "data length" control but let it keep it here
        for (int i = 1; i < result.length && collectionNameIterator.hasNext(); i++) {
            // First column contains collection name
            result[i][0] = collectionNameIterator.next();
            // Collection descriptor
            CollectionPerformanceMeasurer collectionPerformanceMeasurer = testData.get(result[i][0]);
            for (int j = 1; j < result[i].length; j++) {
                Long methodResult = collectionPerformanceMeasurer.getMethodResult(result[0][j]);
                result[i][j] = (methodResult == null) ? "" : methodResult.toString();
            }
        }

        return result;
    }

    private void showToScreen(String[] table) {
        for (String row: table) {
            Utils.printMessage(row);
        }
    }

    private String getFileName(int collectionSize) {
        return String.format(FILENAME_PATTERN, collectionSize);
    }

    private void writeToFile(String fileName, String[] table) {
        TextFile.writeListToFile(fileName, Arrays.asList(table));
    }

    private void showAndSave(int collectionSize) {
        // Prepare result text table
        String[] table = TableBuilder.buildTable(prepareTableData(collectionSize), AlignmentType.RIGHT);

        // Show to screen
        showToScreen(table);
        // Save to file
        writeToFile(getFileName(collectionSize), table);
    }

    private void oneSizeDemonstrate(int collectionSize, int measuringQuantity) {
        oneSizeCalculate(collectionSize, measuringQuantity);
        showAndSave(collectionSize);
    }

    public void demonstrate() {
        for (int collSize: collectionSize) {
            oneSizeDemonstrate(collSize, ATTEMPT_COUNT);
        }
    }
}
