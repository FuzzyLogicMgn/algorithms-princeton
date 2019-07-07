import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points = new TreeSet<>();

    /**
     * construct an empty set of points
     */
    public PointSET() {

    }

    /**
     * @return is the set empty?
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return points.size();
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

        points.add(p);
    }

    /**
     * @param p
     * @return does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return points.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        for (Point2D p : points) {
            p.draw();
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
        for (Point2D p : points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
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

        double minDist = 0;
        Point2D minPoint = null;
        for (Point2D other : points) {
            double dist = p.distanceSquaredTo(other);
            if (dist < minDist || minPoint == null) {
                minDist = dist;
                minPoint = other;
            }
        }
        return minPoint;
    }

    /**
     * unit testing of the methods (optional)
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}