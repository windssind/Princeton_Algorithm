import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.HashMap;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class WordNet {
    // 一个noun可以对应多个id
    private HashMap<String, ArrayList<Integer>> vertexs;
    // 一个id可以有多个上义词
    private HashMap<Integer, ArrayList<Integer>> edges;

    private HashMap<Integer, String> synsets;
    private Digraph G;

    private SAP tackler;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In vertex_in = new In(synsets);
        In edges_in = new In(hypernyms);
        vertexs = new HashMap<String, ArrayList<Integer>>();
        edges = new HashMap<Integer, ArrayList<Integer>>();
        this.synsets = new HashMap<Integer, String>();
        String curLine;
        int vertex_num = 0;
        // 初始化顶点集 但是这样时间复杂度会很大吗？并不会，哈希表保证了查询都是O（1）
        while ((curLine = vertex_in.readLine()) != null) {
            String args[] = curLine.split(",");

            int id = Integer.parseInt(args[0]);
            String synset = args[1];

            // 构建id -> Synset
            this.synsets.put(id, synset);

            String nouns[] = synset.split(" ");
            for (String noun : nouns) {
                if (vertexs.containsKey(noun)) {
                    ArrayList<Integer> tmp = vertexs.get(noun);
                    vertexs.get(noun).add(id);
                }
                else {
                    vertexs.put(noun, new ArrayList<Integer>() {
                        {
                            add(id);
                        }
                    });
                }
            }

            vertex_num++;
        }
        // 根据顶点数量创建一个图
        G = new Digraph(vertex_num);

        // 初始化边集
        while ((curLine = edges_in.readLine()) != null) {
            String args[] = curLine.split(",");
            int id_v = Integer.parseInt(args[0]);
            ArrayList<Integer> Sets_w = new ArrayList<Integer>();
            int len_w = args.length;
            for (int i = 1; i < len_w; ++i) {
                int id_w = Integer.parseInt(args[i]);
                Sets_w.add(id_w);
                G.addEdge(id_v, id_w);
            }
            edges.put(id_v, Sets_w);
            ;
        }

        // 初始化SAP
        if (!isDAG(G)) throw new IllegalArgumentException();
        tackler = new SAP(G);
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return vertexs.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return vertexs.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return tackler.length(vertexs.get(nounA), vertexs.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int id = tackler.ancestor(vertexs.get(nounA), vertexs.get(nounB));
        if (id != -1) return synsets.get(id);
        else return "no common ancestor\n";
    }

    public static void main(String args[]) {
        WordNet wordNet = new WordNet(args[0], args[1]);
    }

    private boolean isDAG(Digraph G) {
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) return false;
        int rootNum = 0;
        int vertex_Num = G.V();
        for (int i = 0; i < vertex_Num; ++i) {
            if (G.outdegree(i) == 0) rootNum++;
            if (rootNum >= 2) return false;
        }
        return true;
    }

    // do unit testing of this class
    // public static void main(String[] args)
}

//
