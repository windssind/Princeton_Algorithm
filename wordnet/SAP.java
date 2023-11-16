/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.Iterator;

public class SAP {
    // 这里要设计成SAP自己要维护一个G对象，不然会修改外部传过来的G
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths v_Sets = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths w_Sets = new BreadthFirstDirectedPaths(G, w);
        int ver_Num = G.V();
        int ancestor = -1;
        int shortest_length = Integer.MAX_VALUE;
        for (int i = 0; i < ver_Num; ++i) {
            if (v_Sets.hasPathTo(i) && w_Sets.hasPathTo(i) && (v_Sets.distTo(i) + w_Sets.distTo(i)
                    < shortest_length)) {
                ancestor = i;
                shortest_length = v_Sets.distTo(i) + w_Sets.distTo(i);
            }
        }
        return shortest_length == Integer.MAX_VALUE ? -1 : shortest_length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths v_Sets = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths w_Sets = new BreadthFirstDirectedPaths(G, w);
        int ver_Num = G.V();
        int ancestor = -1;
        int shortest_length = Integer.MAX_VALUE;
        for (int i = 0; i < ver_Num; ++i) {
            if (v_Sets.hasPathTo(i) && w_Sets.hasPathTo(i) && (v_Sets.distTo(i) + w_Sets.distTo(i)
                    < shortest_length)) {
                ancestor = i;
                shortest_length = v_Sets.distTo(i) + w_Sets.distTo(i);
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (CornerCheck(v) || CornerCheck(w)) return -1;
        BreadthFirstDirectedPaths v_Sets = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths w_Sets = new BreadthFirstDirectedPaths(G, w);
        int ver_Num = G.V();
        int shortest_length = Integer.MAX_VALUE;
        for (int i = 0; i < ver_Num; ++i) {
            if (v_Sets.hasPathTo(i) && w_Sets.hasPathTo(i) && (v_Sets.distTo(i) + w_Sets.distTo(i)
                    < shortest_length)) {
                shortest_length = v_Sets.distTo(i) + w_Sets.distTo(i);
            }
        }
        return shortest_length == Integer.MAX_VALUE ? -1 : shortest_length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // TODO: 根据多源BFS找到最短的路径 , 搜索的过程V+E ， 查询共同祖先过程V
        if (CornerCheck(v) || CornerCheck(w)) return -1;
        BreadthFirstDirectedPaths v_Sets = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths w_Sets = new BreadthFirstDirectedPaths(G, w);
        int ver_Num = G.V();
        int ancestor = -1;
        int shortest_length = Integer.MAX_VALUE;
        for (int i = 0; i < ver_Num; ++i) {
            if (v_Sets.hasPathTo(i) && w_Sets.hasPathTo(i) && (v_Sets.distTo(i) + w_Sets.distTo(i)
                    < shortest_length)) {
                ancestor = i;
                shortest_length = v_Sets.distTo(i) + w_Sets.distTo(i);
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    /*public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);F
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            ArrayList < Integer > w= new ArrayList<Integer>();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }*/

    private boolean CornerCheck(Iterable<Integer> v) {
        if (v == null) throw new IllegalArgumentException();
        Iterator<Integer> iter = v.iterator();
        if (!iter.hasNext()) return true;
        while (iter.hasNext()) {
            Integer next = iter.next();
            if (next == null || next >= G.V() || next < 0) throw new IllegalArgumentException();
        }
        return false;
    }
}
