package de.tillmannrohlfing;

import de.tillmannrohlfing.BTree;
import de.tillmannrohlfing.ComparableContentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class BTreeTest {

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