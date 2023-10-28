import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class FastCollinearPoints {
    private ArrayList<LineSegment> Segments;
    private int num_Of_Segments;

    public FastCollinearPoints(Point[] points) {
        Segments = new ArrayList<LineSegment>();
        num_Of_Segments = 0;
        int len = points.length;
        Point[] pointsCopy = Arrays.copyOf(points, len);
        checkForNull(points);
        Arrays.sort(pointsCopy);
        if (isDuplicate(pointsCopy)) throw new IllegalArgumentException("repeat points");
        for (int i = 0; i < len; ++i) {
            Arrays.sort(pointsCopy, points[i].slopeOrder());
            for (int j = 0; j < len; ) {
                if (pointsCopy[j].equals(points[i])) {
                    j++;
                    continue;
                }
                double slope = pointsCopy[j].slopeTo(points[i]);
                double subLineLen = 0;
                int k;
                for (k = j; k < len; ++k) {
                    if (pointsCopy[k].slopeTo(points[i]) == slope) {
                        subLineLen++;
                        if (k != len - 1) continue;
                        else k++;
                    }
                    // 这里代表最后一个或者已经不相同
                    if (subLineLen >= 3) { // 这里的意思是可以构成collinear
                        if (isLower(Arrays.copyOfRange(pointsCopy, j, k),
                                    points[i])) {         // 只有当基点是最小的，才保存这个
                            Segments.add(new LineSegment(points[i],
                                                         FindMax(Arrays.copyOfRange(pointsCopy, j,
                                                                                    k))));
                            num_Of_Segments++;
                        }
                    }
                    // TODO：bug原因是k的定义不明确
                    break;
                }
                j = k; // 更新新的遍历起点
            }
        }   // finds all line segments containing 4 or more points
    }

    public int numberOfSegments() {
        return num_Of_Segments;
    }

    public LineSegment[] segments() {          // the line segments
        return Segments.toArray(new LineSegment[Segments.size()]);
    }

    private boolean isLower(Point[] subpoints, Point base) {
        int len = subpoints.length;
        for (int i = 0; i < len; ++i) {
            if (base.compareTo(subpoints[i]) > 0) return false;
        }
        return true;
    }

    private Point FindMax(Point[] subpoints) {
        if (subpoints == null) {
            throw new IllegalArgumentException("Find Max : null Argument\n");
        }
        int len = subpoints.length;
        Point Max = subpoints[0];
        for (int i = 1; i < len; ++i) {
            if (subpoints[i].compareTo(Max) > 0) {
                Max = subpoints[i];
            }
        }
        return Max;
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
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private boolean isDuplicate(Point[] points) { // 判断是否有重复数字，先排列，后比较
        int len = points.length;
        for (int i = 0; i < len - 1; ++i) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    private void checkForNull(Point[] points) {
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("null point\n");
        }
    }

}