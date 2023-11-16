/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private class Node {
        Node left;
        Node right;
        int level;
        Point2D point2D;
        RectHV rect;

        Node(Node left, Node right, int level, Point2D point2D, RectHV rect) {
            this.left = left;
            this.right = right;
            this.level = level;
            this.point2D = point2D;
            this.rect = rect;
        }

    }


    private Node root;
    private int len;

    public KdTree() {
        root = null;
        len = 0;
    }            // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    }                      // is the set empty?

    public int size() { // 维护一个成员变量，每一次Insert加1
        return len;
    }                         // number of points in the set

    // only when insert point not in tree
    // insert的时候出现了bug
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node search_Node = root;
        if (root == null) {
            len++;
            root = new Node(null, null, 0, p, new RectHV(0, 0, 1, 1));
        }

        while (search_Node != null) {
            boolean is_Smaller;
            if (compare(p, search_Node) < 0) { // if smaller
                is_Smaller = true;
                if (search_Node.left == null) {
                    RectHV split_Rect;
                    split_Rect = split_Rect(search_Node, is_Smaller);
                    search_Node.left = new Node(null, null, search_Node.level + 1, p, split_Rect);
                    len++;
                    return;
                }
                search_Node = search_Node.left;
            }
            else if (compare(p, search_Node) >= 0) {
                if (search_Node.point2D.x() == p.x() && search_Node.point2D.y() == p.y())
                    return; // same return
                is_Smaller = false;
                if (search_Node.right == null) {
                    RectHV split_Rect;
                    split_Rect = split_Rect(search_Node, is_Smaller);
                    search_Node.right = new Node(null, null, search_Node.level + 1, p, split_Rect);
                    len++;
                    return;
                }
                search_Node = search_Node.right;
            }
        }
    }

    private int compare(Point2D p, Node curNode) {
        if (curNode.level % 2 == 0) { // even num , judge by vertical
            if (p.x() < curNode.point2D.x()) return -1;
            else if (p.x() == curNode.point2D.x()) return 0;
            else return 1;
        }
        else {
            if (p.y() < curNode.point2D.y()) return -1;
            else if (p.y() == curNode.point2D.y()) return 0;
            else return 1;
        }
    }

    private RectHV split_Rect(Node search_Node, boolean is_smaller) {
        double x_min, y_min, x_max, y_max;
        if (search_Node.level % 2 == 0) {
            if (is_smaller) {
                x_min = search_Node.rect.xmin();
                y_min = search_Node.rect.ymin();
                x_max = search_Node.point2D.x();
                y_max = search_Node.rect.ymax();
            }
            else { // 这个是大一点的
                x_min = search_Node.point2D.x();
                y_min = search_Node.rect.ymin();
                x_max = search_Node.rect.xmax();
                y_max = search_Node.rect.ymax();
            }
        }
        else {
            if (is_smaller) {
                x_min = search_Node.rect.xmin();
                y_min = search_Node.rect.ymin();
                x_max = search_Node.rect.xmax();
                y_max = search_Node.point2D.y();
            }
            else { // 这个是大一点的
                x_min = search_Node.rect.xmin();
                y_min = search_Node.point2D.y();
                x_max = search_Node.rect.xmax();
                y_max = search_Node.rect.ymax();
            }
        }
        return new RectHV(x_min, y_min, x_max, y_max);
    }

    // TODO:finish left functions
    public boolean contains(Point2D p) {// does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    public void draw() {// draw all points to standard draw
        if (root == null) return;
        draw(root);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        if (rect == null) throw new IllegalArgumentException();
        range(root, rect, points); // helper function
        return points;
    }            // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        double Min_distance[] = { root.point2D.distanceTo(p) };
        Point2D ret = nearest_Point(root, p, Min_distance);
        if (ret == null) ret = root.point2D;
        return ret;
    }             // a nearest neighbor in the set to point p; null if the set is empty


    private boolean contains(Node root, Point2D p) {
        if (root == null) return false;
        if (p.compareTo(root.point2D) == 0) {
            return true;
        }
        else if (compare(p, root) < 0) {
            // left
            return contains(root.left, p);
        }
        else {
            return contains(root.right, p);
        }
    }

    private void draw_Point(Point2D p) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(p.x(), p.y());
    }

    private void draw(Node root) {
        if (root == null) return;
        draw_Line(root);
        draw_Point(root.point2D);
        draw(root.left);
        draw(root.right);
    }

    private void draw_Line(Node search_Node) {
        if (search_Node.level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(search_Node.point2D.x(), search_Node.rect.ymin(), search_Node.point2D.x(),
                         search_Node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(search_Node.rect.xmin(), search_Node.point2D.y(), search_Node.rect.xmax(),
                         search_Node.point2D.y());
        }
    }

    private void range(Node root, RectHV rect, ArrayList<Point2D> points) {
        if (root == null) return;
        if (!rect.intersects(root.rect)) return;
        if (rect.contains(root.point2D)) {
            points.add(root.point2D);
        }
        range(root.left, rect, points);
        range(root.right, rect, points);
    }

    // return a point that is the shortest distance that is shorter than given distance
    // NOTE： if the root is still not smaller than Min_Distance , then you should return null
    private Point2D nearest_Point(Node root, Point2D p, double Min_distance[]) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        if (!root.rect.contains(p) && root.rect.distanceTo(p) >= Min_distance[0]) return null;
        Double distance = p.distanceTo(root.point2D);
        Point2D p1 = null;
        if (distance < Min_distance[0]) {
            Min_distance[0] = distance;
            p1 = root.point2D;
        }
        Point2D p2 = null;
        Point2D p3 = null;
        if (compare(p, root) < 0) {
            p2 = nearest_Point(root.left, p, Min_distance);
            p3 = nearest_Point(root.right, p, Min_distance);
        }
        else {
            p3 = nearest_Point(root.right, p, Min_distance);
            p2 = nearest_Point(root.left, p, Min_distance);
        }

        if (p2 == null && p3 == null) return p1;
        if (p2 == null) return p3;
        if (p3 == null) return p2;
        if (p.distanceTo(p2) < p.distanceTo(p3)) {
            return p2;
        }
        else {
            return p3;
        }
    }

    public static void main(String args[]) {
        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);
        /*for (int i = 0; i < 3; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            tree.insert(new Point2D(x, y));
        }*/
        tree.insert(p1);
        ;
        tree.insert(p2);
        tree.insert(p3);
        tree.insert(p4);
        tree.insert(p5);
        System.out.println(tree.nearest(new Point2D(0.87, 0.58)));
    }
    // TODO: 上面两段哪里有问题，rect的tostring方法
}

