import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size = 0;

    /**
     * construct an empty set of points
     */
    public KdTree() {

    }

    /**
     * @return is the set empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root, p, NodeType.VERTICAL, 0, 1, 0, 1);
    }

    private Node insert(Node node, Point2D p, NodeType type, double xmin, double xmax, double ymin, double ymax) {
        if (node == null) {
            final RectHV firstRect;
            if (type == NodeType.VERTICAL) {
                firstRect = new RectHV(xmin, ymin, p.x(), ymax);
            } else {
                firstRect = new RectHV(xmin, ymin, xmax, p.y());
            }
            final RectHV secondRect;
            if (type == NodeType.VERTICAL) {
                secondRect = new RectHV(p.x(), ymin, xmax, ymax);
            } else {
                secondRect = new RectHV(xmin, p.y(), xmax, ymax);
            }
            node = new Node(p, type, firstRect, secondRect);
            size++;
        }

        if (!node.point.equals(p)) {
            final int cmp = node.compareTo(p);
            if (cmp > 0) {
                if (type == NodeType.VERTICAL) {
                    node.first = insert(node.first, p, type.opposite(), xmin, node.point.x(), ymin, ymax);
                } else {
                    node.first = insert(node.first, p, type.opposite(), xmin, xmax, ymin, node.point.y());
                }
            } else {
                if (type == NodeType.VERTICAL) {
                    node.second = insert(node.second, p, type.opposite(), node.point.x(), xmax, ymin, ymax);
                } else {
                    node.second = insert(node.second, p, type.opposite(), xmin, xmax, node.point.y(), ymax);
                }
            }
        }
        return node;
    }

    /**
     * @param p
     * @return does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return search(root, p) != null;
    }

    private Node search(Node node, Point2D p) {
        if (node == null) {
            return null;
        }
        if (node.point.compareTo(p) != 0) {
            final int cmp = node.compareTo(p);
            if (cmp > 0) {
                return search(node.first, p);
            } else {
                return search(node.second, p);
            }
        }
        return node;
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        draw(root, 0, 1, 0, 1);
    }

    private void draw(Node node, double xmin, double xmax, double ymin, double ymax) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        node.point.draw();
        StdDraw.setPenRadius(0.005);
        if (node.type == NodeType.VERTICAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(node.point.x(), ymin).drawTo(new Point2D(node.point.x(), ymax));

            draw(node.first, xmin, node.point.x(), ymin, ymax);
            draw(node.second, node.point.x(), xmax, ymin, ymax);
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(xmin, node.point.y()).drawTo(new Point2D(xmax, node.point.y()));

            draw(node.first, xmin, xmax, ymin, node.point.y());
            draw(node.second, xmin, xmax, node.point.y(), ymax);
        }
    }

    /**
     * @param rect
     * @return all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        final List<Point2D> result = new LinkedList<>();
        range(root, rect, result);
        return result;
    }

    private void range(Node node, RectHV rect, List<Point2D> points) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.point)) {
            points.add(node.point);
        }
        if (rect.intersects(node.firstRect())) {
            range(node.first, rect, points);
        }
        if (rect.intersects(node.secondRect())) {
            range(node.second, rect, points);
        }
    }

    /**
     * @param p
     * @return a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        final Point2D[] result = new Point2D[]{root.point};
        nearest(p, root, new RectHV(0, 0, 1, 1),
                new double[]{root.point.distanceSquaredTo(p)}, result);
        return result[0];
    }

    private void nearest(Point2D p, Node node, RectHV rect, double[] minDist, Point2D[] result) {
        if (node == null) {
            return;
        }

        if (rect.distanceSquaredTo(p) < minDist[0]) {
            final double dist = node.point.distanceSquaredTo(p);
            if (dist < minDist[0]) {
                result[0] = node.point;
                minDist[0] = dist;
            }

            if (node.compareTo(p) > 0) {
                nearest(p, node.first, node.firstRect(), minDist, result);
                nearest(p, node.second, node.secondRect(), minDist, result);
            } else {
                nearest(p, node.second, node.secondRect(), minDist, result);
                nearest(p, node.first, node.firstRect(), minDist, result);
            }
        }
    }

    private enum NodeType {
        VERTICAL, HORIZONTAL;

        NodeType opposite() {
            return this == NodeType.VERTICAL ? NodeType.HORIZONTAL : NodeType.VERTICAL;
        }
    }

    private static final class Node {

        private final NodeType type;
        private final Point2D point;
        private final RectHV firstRect;
        private final RectHV secondRect;
        private Node first;
        private Node second;

        Node(Point2D point, NodeType type, RectHV firstRect, RectHV secondRect) {
            this.type = type;
            this.point = point;
            this.firstRect = firstRect;
            this.secondRect = secondRect;
        }

        int compareTo(Point2D p) {
            if (type == NodeType.VERTICAL) {
                return Double.compare(point.x(), p.x());
            } else {
                return Double.compare(point.y(), p.y());
            }
        }

        RectHV firstRect() {
            return firstRect;
        }

        RectHV secondRect() {
            return secondRect;
        }
    }

    /**
     * unit testing of the methods (optional)
     *
     * @param args
     */
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(1.0, 1.0));
        tree.insert(new Point2D(0, 0));
        tree.insert(new Point2D(1.0, 0));
        tree.insert(new Point2D(1.0, 1.0));
        System.err.println(tree.size);

//        tree = new KdTree();
//        tree.insert(new Point2D(0.75, 0.6875));
//        tree.insert(new Point2D(0.25, 0.5));
//        tree.insert(new Point2D(0.8125, 0.8125));
//        tree.insert(new Point2D(0.5625, 0.1875));
//
//        tree.insert(new Point2D(0.0, 0.0));
//        tree.insert(new Point2D(0.1875, 0.375));
//        tree.insert(new Point2D(0.4375, 0.3125));
//        tree.insert(new Point2D(0.0625, 1.0));
//
//        tree.insert(new Point2D(0.3125, 0.25));
//        tree.insert(new Point2D(0.875, 0.9375));
//
//        tree.draw();
//
//        new Point2D(1.0, 0.125).draw();
//
//        System.err.println(new Point2D(1.0, 0.125).distanceSquaredTo(new Point2D(0.75, 0.5)));
//
//        System.err.println(new Point2D(1.0, 0.125).distanceSquaredTo(new Point2D(1, 0.5)));
//
//        System.err.println(new Point2D(1.0, 0.125).distanceSquaredTo(new Point2D(0.5625, 0.1875)));
//
//        System.err.println(tree.nearest(new Point2D(1.0, 0.125)));


        tree = new KdTree();
        tree.insert(new Point2D(0.625, 0.375));//A
        tree.insert(new Point2D(0.25, 0.75));  //B
        tree.insert(new Point2D(0.375, 0.0));  //C
        tree.insert(new Point2D(0.125, 1.0));  //D
        tree.insert(new Point2D(0.5, 0.5));    //E

        new Point2D(0.75, 0.625).draw();

        tree.draw();
        System.err.println(tree.nearest(new Point2D(0.75, 0.625)));


    }
}