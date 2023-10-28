/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private int num_Of_Segments;
    private ArrayList<LineSegment> Segments;

    public BruteCollinearPoints(Point[] points) {
        int len = points.length;
        num_Of_Segments = 0;
        Segments = new ArrayList<LineSegment>();
        for (int i = 0; i < len - 3; ++i) {
            Point origin = points[i];
            for (int j = i + 1; j < len - 2; ++j) {
                double slope1 = origin.slopeTo(points[j]);
                for (int k = j + 1; k < len - 1; ++k) {
                    double slope2 = origin.slopeTo(points[k]);
                    if (slope1 != slope2) continue;
                    for (int m = k + 1; m < len; ++m) {
                        double slope3 = origin.slopeTo(points[m]);
                        if (slope2 == slope3) {
                            Segments.add(LineOfFour(points[i], points[j], points[k], points[m]));
                            num_Of_Segments++;
                            break;
                        }
                    }
                }
            }
        }
    }    // finds all line segments containing 4 points

    public int numberOfSegments() {
        return num_Of_Segments;
    }        // the number of line segments

    public LineSegment[] segments() {
        return Segments.toArray(new LineSegment[Segments.size()]);
    }                // the line segments

    private LineSegment LineOfFour(Point p1, Point p2, Point p3, Point p4) {
        Point LowerWinner1, LowerWinner2, LowerWinner;
        Point HigherWinner1, HigherWinner2, HigherWinner;
        LowerWinner1 = p1.compareTo(p2) < 0 ? p1 : p2;
        HigherWinner1 = p1.compareTo(p2) < 0 ? p2 : p1;
        LowerWinner2 = p3.compareTo(p4) < 0 ? p3 : p4;
        HigherWinner2 = p3.compareTo(p4) < 0 ? p4 : p3;
        ;
        LowerWinner = LowerWinner1.compareTo(LowerWinner2) < 0 ? LowerWinner1 : LowerWinner2;
        HigherWinner = HigherWinner1.compareTo(HigherWinner2) > 0 ? HigherWinner1 : HigherWinner2;
        return new LineSegment(HigherWinner, LowerWinner);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
