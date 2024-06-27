package dev.pluginz;

import dev.pluginz.abschreiben.BinarySearchTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class BTreeTest {

    private BTree<ComparableContentImpl> bTree;

    @BeforeEach
    public void setup() {
        bTree = new BTree<>(ComparableContentImpl.class);
    }

    @Test
    public void insertAndSearchForValue() {
        ComparableContentImpl value = new ComparableContentImpl(10);
        bTree.insert(value);
        ComparableContentImpl result = bTree.search(value);
        assertEquals(value, result);
    }

    @Test
    public void searchForNonExistentValue() {
        ComparableContentImpl value = new ComparableContentImpl(10);
        ComparableContentImpl result = bTree.search(value);
        assertNull(result);
    }

    @Test
    public void insertDuplicateValue() {
        ComparableContentImpl value = new ComparableContentImpl(10);
        bTree.insert(value);
        bTree.insert(value);
        ComparableContentImpl result = bTree.search(value);
        assertEquals(value, result);
    }

    @Test
    public void insertRandomNumbersPerformanceTest() {
        BinarySearchTree bst = new BinarySearchTree<ComparableContentImpl>();
        BTreeButFast bTreeButFast = new BTreeButFast<>(ComparableContentImpl.class);
        BTreeButFasterAsFast bTreeButFasterAsFast = new BTreeButFasterAsFast<>(ComparableContentImpl.class);
        Random random = new Random();
        int numNumbers = 1000000; // Number of random numbers to insert

        // Generate random numbers first
        ComparableContentImpl[] randomNumbers = new ComparableContentImpl[numNumbers];
        for (int i = 0; i < numNumbers; i++) {
            randomNumbers[i] = new ComparableContentImpl(random.nextInt(201));
        }

        // Insert into BTree
        long startTime = System.nanoTime();
        for (ComparableContentImpl value : randomNumbers) {
            bTree.insert(value);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("BTree: Time taken to insert " + numNumbers + " random numbers: " + duration + " milliseconds");

        // Insert into BTreeButFast
        startTime = System.nanoTime();
        for (ComparableContentImpl value : randomNumbers) {
            bTreeButFast.insert(value);
        }
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("BTreeButFast: Time taken to insert " + numNumbers + " random numbers: " + duration + " milliseconds");

        // Insert into BTreeButFasterAsFast
        startTime = System.nanoTime();
        for (ComparableContentImpl value : randomNumbers) {
            bTreeButFasterAsFast.insert(value);
        }
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("BTreeButFastAsFast: Time taken to insert " + numNumbers + " random numbers: " + duration + " milliseconds");


        // Insert into BinarySearchTree
        startTime = System.nanoTime();
        for (ComparableContentImpl value : randomNumbers) {
            bst.insert(value);
        }
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("BST: Time taken to insert " + numNumbers + " random numbers: " + duration + " milliseconds");
    }


    @Test
    public void insertAndTraverseValues() {
        ComparableContentImpl[] values = {
                new ComparableContentImpl(10),
                new ComparableContentImpl(20),
                new ComparableContentImpl(5),
                new ComparableContentImpl(6),
                new ComparableContentImpl(12),
                new ComparableContentImpl(30),
                new ComparableContentImpl(7),
                new ComparableContentImpl(1),
                new ComparableContentImpl(742),
                new ComparableContentImpl(2),
                new ComparableContentImpl(3),
                new ComparableContentImpl(70),
                new ComparableContentImpl(82),
                new ComparableContentImpl(80),
                new ComparableContentImpl(21),
                new ComparableContentImpl(19),
                new ComparableContentImpl(20),
                new ComparableContentImpl(5),
                new ComparableContentImpl(11)
        };

        for (ComparableContentImpl value : values) {
            bTree.insert(value);
        }

        // Capture the output of the traverse method
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        bTree.traverse();

        // Check if the output is sorted
        String[] output = outContent.toString().split(" ");
        int[] outputNumbers = Arrays.stream(output).mapToInt(Integer::parseInt).toArray();
        assertTrue(IntStream.range(0, outputNumbers.length - 1)
                .allMatch(i -> outputNumbers[i] <= outputNumbers[i + 1]));
    }
}