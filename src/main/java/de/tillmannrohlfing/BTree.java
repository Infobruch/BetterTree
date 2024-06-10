package de.tillmannrohlfing;

import de.tillmannrohlfing.interfaces.ComparableContent;

public class BTree<ContentType extends ComparableContent<ContentType>> {

    private int order; // Maximum number of children per node
    private Node<ContentType> root;

    public BTree(int order) {
        this.order = order;
        this.root = null;
    }

    private class Node<CT extends ComparableContent<CT>> {
        private CT[] content;
        private Node<CT>[] children;
        private int numKeys;  // Number of keys currently in the node

        @SuppressWarnings("unchecked") // Safe generic array creation
        public Node(int maxKeys) {
            content = (CT[]) new ComparableContent[maxKeys];
            children = (Node<CT>[]) new Node[maxKeys + 1];
            numKeys = 0;
        }

        public void insert(CT newContent) {
            // Implement B-Tree insertion logic here:
            // 1. Find the appropriate position for the new key.
            if (numKeys != content.length) {
                for (int i = 0; i < numKeys; i++) {
                    if (content[i] != null) {
                        CT localContent = content[i];
                        if (localContent.isGreater(newContent)) {
                            content[i] = newContent;
                            numKeys++;
                            insert(localContent);
                        }
                    } else {
                        content[i] = newContent;
                        numKeys++;
                    }
                }
            }else {

            }
            // 2. If the node is full, split it.
            // 3. Insert the new key into the appropriate node.
        }
        // ... other B-Tree node operations (search, delete, etc.)
    }

    public void insert(ContentType content) {
        if (root == null) {
            root = new Node<>(order - 1); // Root starts empty
        }
        root.insert(content);
    }

    // ... other B-Tree operations (search, traversal, etc.)
}
