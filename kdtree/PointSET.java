/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * PointSET相当于用一维的去解决，插入的操作都是logn
 **/

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<Point2D>();
    }                          // construct an empty set of points

    public boolean isEmpty() {
        return points.size() == 0;
    }                      // is the set empty?

    public int size() {
        return points.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }            // does the set contain point p?

    public void draw() { // draw all points to standard draw
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {// all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> points_In_Rect = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) points_In_Rect.add(p);
        }
        return points_In_Rect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double min_Distance = Double.MAX_VALUE;
        Point2D nearest_p = null;
        for (Point2D p_ : points) {
            double tmp_distance = p.distanceTo(p_);
            if (tmp_distance < min_Distance) {
                min_Distance = tmp_distance;
                nearest_p = p_;
            }
        }
        return nearest_p;
    }          // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {// unit testing of the methods (optional)

    }
}

