import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Quick;


public class BruteCollinearPoints {

    private final Point[] points;
    private LineSegment[] segments = null;

    /**
     * finds all line segments containing 4 points
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
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
        final Point[] aux = new Point[4];
        final LinkedQueue<LineSegment> tmpSegments = new LinkedQueue<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int z = k + 1; z < points.length; z++) {
                        aux[0] = points[i];
                        aux[1] = points[j];
                        aux[2] = points[k];
                        aux[3] = points[z];
                        if (isCollinear(aux)) {
                            tmpSegments.enqueue(new LineSegment(aux[0], aux[3]));
                        }
                    }
                }
            }
        }

        segments = new LineSegment[tmpSegments.size()];
        int index = 0;
        for (LineSegment seg : tmpSegments) {
            segments[index++] = seg;
        }
    }

    private boolean isCollinear(final Point[] aux) {
        double prevSlop = aux[0].slopeTo(aux[1]);
        for (int i = 1; i < aux.length - 1; i++) {
            double curSlope = aux[i].slopeTo(aux[i + 1]);
            if (prevSlop != curSlope) {
                return false;
            } else {
                prevSlop = curSlope;
            }
        }
        return true;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdDraw.setPenRadius(0.01);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}