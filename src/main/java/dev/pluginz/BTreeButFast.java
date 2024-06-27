package dev.pluginz;

import dev.pluginz.interfaces.ComparableContent;

import java.util.ArrayList;
import java.util.List;

/**
 * BTree is a generic class that represents a B-Tree data structure.
 * The B-Tree nodes store objects of a generic type CT that extends ComparableContent<CT>.
 */
public class BTreeButFast<CT extends ComparableContent<CT>> {

    /**
     * BTreeListener is an interface for classes that want to be notified when the BTree changes.
     */
    public interface BTreeListener<CT extends ComparableContent<CT>> {
        void onTreeChanged(BTreeButFast<CT> tree);
    }

    /**
     * Node is a nested class that represents a node in the B-Tree.
     */
    public class Node<CT extends ComparableContent<CT>> {
        private static final int T = 4; // Increased minimum degree for better fanout
        private Object[] keys; // Use Object[] for slight performance gain
        private Node<CT>[] children;
        private int numKeys;
        private boolean isLeaf; // Added missing semicolon

        /**
         * Node constructor.
         * @param isLeaf indicates whether the node is a leaf node.
         */
        @SuppressWarnings("unchecked")
        Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new Object[2 * T - 1];
            this.children = new Node[2 * T];
            this.numKeys = 0;
        }

        // Getters
        public Object[] getKeys() {
            return keys;
        }

        public Node<CT>[] getChildren() {
            return children;
        }

        public int getNumKeys() {
            return numKeys;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setNumKeys(int numKeys) {
            this.numKeys = numKeys;
        }
        @SuppressWarnings("unchecked")
        public CT getKey(int index) {
            return (CT) keys[index];
        }

        /**
         * Traverses the subtree rooted with this node and prints the keys in order.
         */
        public void traverse() {
            int i;
            for (i = 0; i < numKeys; i++) {
                if (!isLeaf) {
                    children[i].traverse();
                }
                System.out.print(keys[i] + " ");
            }
            if (!isLeaf) {
                children[i].traverse();
            }
        }
    }

    private Node<CT> root;
    private final Class<CT> clazz;
    private final List<BTreeListener<CT>> listeners = new ArrayList<>();


    /**
     * BTree constructor.
     * @param clazz the Class object representing CT.
     */
    public BTreeButFast(Class<CT> clazz) {
        this.root = new Node<>(true); // Removed unnecessary clazz argument
        this.clazz = clazz;
    }

    /**
     * Inserts a key into the B-Tree.
     * @param key the key to be inserted.
     */
    public void insert(CT key) {
        Node<CT> r = root;
        if (r.getNumKeys() == 2 * Node.T - 1) {
            Node<CT> s = new Node<>(false);
            s.getChildren()[0] = r;
            root = s;
            splitChild(s, 0);
            insertNonFull(s, key);
        } else {
            insertNonFull(r, key);
        }
        notifyListeners();
    }

    /**
     * Splits a full child node of the given node.
     * @param node the parent node.
     * @param i the index of the child to be split.
     */
    private void splitChild(Node<CT> node, int i) {
        Node<CT> y = node.getChildren()[i];
            Node<CT> z = new Node<>(y.isLeaf());
            z.setNumKeys(Node.T - 1);
            for (int j = 0; j < Node.T - 1; j++) {
                z.getKeys()[j] = y.getKeys()[j + Node.T];
            }
            if (!y.isLeaf()) {
                for (int j = 0; j < Node.T; j++) {
                    z.getChildren()[j] = y.getChildren()[j + Node.T];
                }
            }
            y.setNumKeys(Node.T - 1);
            for (int j = node.getNumKeys(); j >= i + 1; j--) {
                node.getChildren()[j + 1] = node.getChildren()[j];
            }
            node.getChildren()[i + 1] = z;
            for (int j = node.getNumKeys() - 1; j >= i; j--) {
                node.getKeys()[j + 1] = node.getKeys()[j];
            }
            node.getKeys()[i] = y.getKeys()[Node.T - 1];
            node.setNumKeys(node.getNumKeys() + 1);
            notifyListeners();
        }

    /**
     * Inserts a key into a non-full node.
     * @param node the node.
     * @param key the key to be inserted.
     */
    private void insertNonFull(Node<CT> node, CT key) {
        int i = node.getNumKeys() - 1;
        while (i >= 0 && key.isLess(node.getKey(i))) {
            i--;
        }
        i++;

        if (node.isLeaf()) {
            System.arraycopy(node.keys, i, node.keys, i + 1, node.getNumKeys() - i);
            node.keys[i] = key;
            node.numKeys++;
        } else {
            if (node.getChildren()[i].getNumKeys() == 2 * Node.T - 1) {
                splitChild(node, i);
                if (key.isGreater(node.getKey(i))) {
                    i++;
                }
            }
            insertNonFull(node.getChildren()[i], key);
        }
    }

    /**
     * Searches for a key in the B-Tree.
     * @param key the key to be searched for.
     * @return the key if it is found, null otherwise.
     */
    public CT search(CT key) {
        return search(root, key);
    }

    /**
     * Searches for a key in the subtree rooted with the given node.
     * @param node the root of the subtree.
     * @param key the key to be searched for.
     * @return the key if it is found, null otherwise.
     */
    private CT search(Node<CT> node, CT key) {
        int i = 0;
        while (i < node.getNumKeys() && key.isGreater(node.getKey(i))) {
            i++;
        }
        if (i < node.getNumKeys() && key.isEqual(node.getKey(i))) {
            return node.getKey(i);
        }
        if (node.isLeaf()) {
            return null;
        } else {
            return search(node.getChildren()[i], key);
        }
    }

    /**
     * Traverses the B-Tree in order and prints the keys.
     */
    public void traverse() {
        if (root != null) {
            root.traverse();
        }
    }

    /**
     * Returns the root node of the B-Tree.
     * @return the root node.
     */
    public Node<CT> getRoot() {
        return root;
    }

    /**
     * Adds a BTreeListener.
     * @param listener the listener to be added.
     */
    public void addListener(BTreeListener<CT> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a BTreeListener.
     * @param listener the listener to be removed.
     */
    public void removeListener(BTreeListener<CT> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that the B-Tree has changed.
     */
    private void notifyListeners() {
        for (BTreeListener<CT> listener : listeners) {
            listener.onTreeChanged(this);
        }
    }
}