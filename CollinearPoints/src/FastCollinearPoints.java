import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Quick;

import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private LineSegment[] segments = null;

    /**
     * finds all line segments containing 4 points
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        final Point[] arr = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            arr[i] = points[i];
        }

        Quick.sort(arr);
        Point prev = arr[0];
        for (int i = 1; i < points.length; i++) {
            if (arr[i].compareTo(prev) == 0) {
                throw new IllegalArgumentException();
            }
            prev = arr[i];
        }

        this.points = arr;
    }

    /**
     * @return the number of line segments
     */
    public int numberOfSegments() {
        if (segments == null) {
            calcLineSegments();
        }
        return segments.length;
    }

    /**
     * @return the line segments
     */
    public LineSegment[] segments() {
        if (segments == null) {
            calcLineSegments();
        }
        return segments.clone();
    }

    private void calcLineSegments() {
        final LinkedQueue<LineSegment> tmpSegments = new LinkedQueue<>();
        final Point[] aux = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int k = 0; k < aux.length; k++) {
                aux[k] = points[k];
            }

            final Point p = points[i];
            Arrays.sort(aux, p.slopeOrder());
            int startIndex = 0;
            int cnt = 1;
            double prevSlope = p.slopeTo(aux[0]);
            for (int j = 1; j < aux.length; j++) {
                final double curSlope = p.slopeTo(aux[j]);
                if (prevSlope == curSlope) {
                    cnt++;
                } else {
                    if (cnt >= 3) {
                        if (p.compareTo(aux[startIndex]) < 0) {
                            tmpSegments.enqueue(new LineSegment(p, aux[j - 1]));
                        }
                    }
                    cnt = 1;
                    startIndex = j;
                    prevSlope = curSlope;
                }
            }
            if (cnt >= 3) {
                if (p.compareTo(aux[startIndex]) < 0) {
                    tmpSegments.enqueue(new LineSegment(p, aux[aux.length - 1]));
                }
            }
        }

        segments = new LineSegment[tmpSegments.size()];
        int index = 0;
        for (LineSegment seg : tmpSegments) {
            segments[index++] = seg;
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.02);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdDraw.setPenRadius(0.01);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}