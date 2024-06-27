package dev.pluginz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import dev.pluginz.abschreiben.BinarySearchTree;
public class BinarySearchTreeTest {

    private BinarySearchTree<ComparableContentImpl> bst;

    @BeforeEach
    public void setup() {
        bst = new BinarySearchTree<>();
    }

    @Test
    public void insertAndSearchForValue() {
        ComparableContentImpl value = new ComparableContentImpl(10);
        bst.insert(value);
        ComparableContentImpl result = bst.search(value);
        assertEquals(value, result);
    }

    @Test
    public void searchForNonExistentValue() {
        ComparableContentImpl value = new ComparableContentImpl(10);
        ComparableContentImpl result = bst.search(value);
        assertNull(result);
    }

    @Test
    public void insertDuplicateValue() {
        ComparableContentImpl value = new ComparableContentImpl(10);
        bst.insert(value);
        bst.insert(value);
        ComparableContentImpl result = bst.search(value);
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
            bst.insert(value);
        }

        // Capture the output of the traverse method
        StringBuilder outContent = new StringBuilder();
        traverse(bst, outContent);

        // Check if the output is sorted
        String[] output = outContent.toString().trim().split(" "); // Trim trailing space
        int[] outputNumbers = Arrays.stream(output).mapToInt(Integer::parseInt).toArray();
        assertTrue(IntStream.range(0, outputNumbers.length - 1)
                .allMatch(i -> outputNumbers[i] <= outputNumbers[i + 1]));
    }
    private void traverse(BinarySearchTree<ComparableContentImpl> pBST, StringBuilder output) {
        if (!pBST.isEmpty()) {
            traverse(pBST.getLeftTree(), output);
            output.append(pBST.getContent().toString()).append(" "); // Append with space
            traverse(pBST.getRightTree(), output);
        }
    }
}