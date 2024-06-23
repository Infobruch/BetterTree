package de.tillmannrohlfing;

import de.tillmannrohlfing.interfaces.ComparableContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BTreeTest {

    private BTree<ComparableContentImpl> bTree;

    @BeforeEach
    void setUp() {
        bTree = new BTree<>(ComparableContentImpl.class);
    }

    @Test
    void testInsert() {
        ComparableContentImpl[] values = {
                new ComparableContentImpl(10),
                new ComparableContentImpl(20),
                new ComparableContentImpl(5),
                new ComparableContentImpl(6),
                new ComparableContentImpl(12),
                new ComparableContentImpl(30),
                new ComparableContentImpl(7),
                new ComparableContentImpl(17),
                new ComparableContentImpl(4)
        };

        for (ComparableContentImpl value : values) {
            bTree.insert(value);
        }

        // Check if values are inserted and can be searched
        for (ComparableContentImpl value : values) {
            assertNotNull(bTree.search(value));
        }
    }

    @Test
    void testSearch() {
        ComparableContentImpl value1 = new ComparableContentImpl(10);
        ComparableContentImpl value2 = new ComparableContentImpl(20);

        bTree.insert(value1);
        bTree.insert(value2);

        assertEquals(value1, bTree.search(value1));
        assertEquals(value2, bTree.search(value2));
        assertNull(bTree.search(new ComparableContentImpl(30)));
    }
}
