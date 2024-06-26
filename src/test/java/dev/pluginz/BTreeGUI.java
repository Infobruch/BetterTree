package dev.pluginz;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * BTreeGUI is a class that provides a graphical user interface for visualizing a B-Tree.
 * It extends JPanel and implements BTree.BTreeListener<ComparableContentImpl>.
 */
public class BTreeGUI extends JPanel implements BTree.BTreeListener<ComparableContentImpl> {

    private BTree<ComparableContentImpl> bTree;

    /**
     * BTreeGUI constructor.
     * @param bTree the BTree to be visualized.
     */
    public BTreeGUI(BTree<ComparableContentImpl> bTree) {
        this.bTree = bTree;
        this.bTree.addListener(this);
    }

    /**
     * Overrides the paintComponent method from JPanel.
     * This method is called when the JPanel needs to be repainted.
     * @param g the Graphics object to protect.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.setBackground(Color.BLACK);
        if (bTree != null) {
            drawTree(g, bTree.getRoot(), getWidth() / 2, 30, calculateInitialXOffset(bTree.getRoot(), getWidth() / 2));
        }
    }

    private int calculateInitialXOffset(BTree<ComparableContentImpl>.Node<ComparableContentImpl> node, int width) {
        if (node == null || node.isLeaf()) {
            return width / 4;
        } else {
            int maxOffset = 0;
            for (BTree<ComparableContentImpl>.Node<ComparableContentImpl> child : node.getChildren()) {
                if (child != null) {
                    maxOffset = Math.max(maxOffset, calculateInitialXOffset(child, width / 2));
                }
            }
            return maxOffset;
        }
    }

    /**
     * Draws the B-Tree on the JPanel.
     * @param g the Graphics object to protect.
     * @param node the root node of the B-Tree.
     * @param x the x-coordinate of the root node.
     * @param y the y-coordinate of the root node.
     * @param xOffset the horizontal distance between nodes.
     */
    private void drawTree(Graphics g, BTree<ComparableContentImpl>.Node<ComparableContentImpl> node, int x, int y, int xOffset) {
        if (node != null) {
            int childXOffset = xOffset / 2;
            int currentX = x - (xOffset * (node.getNumKeys() - 1)) / 2;

            for (int i = 0; i < node.getNumKeys() + 1; i++) {
                if (node.getChildren()[i] != null) {
                    int childX = currentX + i * xOffset;
                    g.drawLine(x, y, childX, y + 50);
                    drawTree(g, node.getChildren()[i], childX, y + 50, childXOffset);
                }
            }

            for (int i = 0; i < node.getNumKeys(); i++) {
                g.setColor(Color.BLACK);
                g.fillRect(x - 15 + i * 30, y - 15, 30, 30);
                g.setColor(Color.WHITE);
                g.drawRect(x - 15 + i * 30, y - 15, 30, 30);
                g.drawString(node.getKeys()[i].toString(), x - 10 + i * 30, y + 5);
            }
        }
    }

    /**
     * Called when the B-Tree changes.
     * @param tree the BTree that has changed.
     */
    @Override
    public void onTreeChanged(BTree<ComparableContentImpl> tree) {
        repaint();
    }

    /**
     * The main method that runs the B-Tree visualization.
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        BTree<ComparableContentImpl> bTree = new BTree<>(ComparableContentImpl.class);
        /*
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
        */
        Random random = new Random();
        ComparableContentImpl[] values = new ComparableContentImpl[15];
        for (int i = 0; i < values.length; i++) {
            values[i] = new ComparableContentImpl(random.nextInt(201)); // Generates a random integer between 0 and 200
        }

        JFrame frame = new JFrame("B-Tree Visualization");
        BTreeGUI panel = new BTreeGUI(bTree);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Use SwingWorker to animate the insertion of values
        new SwingWorker<Void, ComparableContentImpl>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (ComparableContentImpl value : values) {
                    publish(value);
                    Thread.sleep(1000); // Adjust delay as needed
                }
                return null;
            }

            /**
             * This method is called by SwingWorker when there are intermediate results to process.
             * In this case, it inserts the values published by doInBackground() into the B-Tree.
             * @param chunks a list of ComparableContentImpl objects to be inserted into the B-Tree.
             */
            @Override
            protected void process(List<ComparableContentImpl> chunks) {
                for (ComparableContentImpl value : chunks) {
                    bTree.insert(value);
                }
            }


            /**
             * This method is called by SwingWorker when the background task is finished.
             * It attempts to retrieve the result of the computation, and prints any exceptions that occurred.
             */
            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
