package de.tillmannrohlfing;

import de.tillmannrohlfing.interfaces.ComparableContent;

public class BTree<CT extends ComparableContent<CT>> {

    private int order; // Maximum number of children per node
    private Node<CT> root;

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
            } else {
                // 2. If the node is full, split it.
                CT pivot = content[content.length / 2];
                Node<CT> left = new Node<>(order - 1);
                Node<CT> right = new Node<>(order - 1);
                for (CT ct : content) {
                    if (ct.isLess(pivot)) {
                        left.insert(ct);
                    }
                    if (ct.isGreater(pivot)) {
                        right.insert(ct);
                    }
                }
                if (newContent.isLess(pivot)) {
                    left.insert(newContent);
                }
                if (newContent.isGreater(pivot)) {
                    right.insert(newContent);
                }

                // 3. Insert the new key into the appropriate node.
            }
        }
        // ... other B-Tree node operations (search, delete, etc.)
        public void betterInsert(CT key){

        }

        public CT search(CT key) {
            if (key != null){
                for (int i = 0; i < numKeys; i++){
                    if (content[i] != null){
                        CT localContent = content[i];
                        if (localContent.isEqual(key)){
                            return localContent;
                        } else if (localContent.isGreater(key) && children[i] != null) {
                            return children[i].search(key);
                        }
                    }
                }
            }
            return key;
        }

        public void traverse() {
            int i;
            for (i = 0; i < numKeys; i++) {
                if (children[i] != null) {
                    children[i].traverse();
                }
                System.out.println(content[i]);
            }
            if (children[i] != null) {
                children[i].traverse();
            }
        }
    }
    public void insert(CT key) {
        if (root == null) {
            root = new Node<>(order - 1);
        }
        root.insert(key);
    }

    // ... other B-Tree operations (search, traversal, etc.)
}


