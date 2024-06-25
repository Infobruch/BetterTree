package dev.pluginz;

import dev.pluginz.interfaces.ComparableContent;

import java.util.ArrayList;
import java.util.List;

/**
 * BTree is a generic class that represents a B-Tree data structure.
 * The B-Tree nodes store objects of a generic type CT that extends ComparableContent<CT>.
 */
public class BTree<CT extends ComparableContent<CT>> {

    /**
     * BTreeListener is an interface for classes that want to be notified when the BTree changes.
     */
    public interface BTreeListener<CT extends ComparableContent<CT>> {
        void onTreeChanged(BTree<CT> tree);
    }

    /**
     * Node is a nested class that represents a node in the B-Tree.
     */
    public class Node<CT extends ComparableContent<CT>> {
        private static final int T = 3; // Minimum degree (can be adjusted)
        private CT[] keys;
        private Node<CT>[] children;
        private int numKeys;
        private boolean isLeaf;

        /**
         * Node constructor.
         * @param isLeaf indicates whether the node is a leaf node.
         * @param clazz the Class object representing CT.
         */
        @SuppressWarnings("unchecked")
        Node(boolean isLeaf, Class<CT> clazz) {
            this.isLeaf = isLeaf;
            this.keys = (CT[]) java.lang.reflect.Array.newInstance(clazz, 2 * T - 1);
            this.children = (Node<CT>[]) java.lang.reflect.Array.newInstance(Node.class, 2 * T);
            this.numKeys = 0;
        }

        // Getters
        public CT[] getKeys() {
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
    private Class<CT> clazz;
    private List<BTreeListener<CT>> listeners = new ArrayList<>();

    /**
     * BTree constructor.
     * @param clazz the Class object representing CT.
     */
    public BTree(Class<CT> clazz) {
        this.root = new Node<>(true, clazz);
        this.clazz = clazz;
    }

    /**
     * Inserts a key into the B-Tree.
     * @param key the key to be inserted.
     */
    public void insert(CT key) {
        Node<CT> r = root;
        if (r.getNumKeys() == 2 * Node.T - 1) {
            Node<CT> s = new Node<>(false, clazz);
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
            Node<CT> z = new Node<>(y.isLeaf(), clazz);
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
        if (node.isLeaf()) {
            while (i >= 0 && key.isLess(node.getKeys()[i])) {
                node.getKeys()[i + 1] = node.getKeys()[i];
                i--;
            }
            node.getKeys()[i + 1] = key;
            node.setNumKeys(node.getNumKeys() + 1);
        } else {
            while (i >= 0 && key.isLess(node.getKeys()[i])) {
                i--;
            }
            i++;
            if (node.getChildren()[i].getNumKeys() == 2 * Node.T - 1) {
                splitChild(node, i);
                if (key.isGreater(node.getKeys()[i])) {
                    i++;
                }
            }
            insertNonFull(node.getChildren()[i], key);
        }
        notifyListeners();
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
            while (i < node.getNumKeys() && key.isGreater(node.getKeys()[i])) {
                i++;
            }
            if (i < node.getNumKeys() && key.isEqual(node.getKeys()[i])) {
                return node.getKeys()[i];
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