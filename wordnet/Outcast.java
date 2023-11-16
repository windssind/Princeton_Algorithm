/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;

    }
    // constructor takes a WordNet object

    /**
     * given an array of WordNet nouns, return an outcast
     */

    public String outcast(String[] nouns) {
        int nouns_Len = nouns.length;
        int distance[][] = new int[nouns_Len][nouns_Len];
        for (int i = 0; i < nouns_Len; ++i) {
            for (int j = 0; j < nouns_Len; ++j) {
                if (j < i) distance[i][j] = distance[j][i];
                else if (j == i) distance[i][j] = 0;
                else distance[i][j] = wordNet.distance(nouns[i], nouns[j]);
            }
        }
        // 再把得到的数据累加
        int min_index = -1;
        int max_distance = -1;
        for (int i = 0; i < nouns_Len; ++i) {
            int total_distance = 0;
            for (int j = 0; j < nouns_Len; ++j) {
                int distance_Of_Two = distance[i][j];
                if (distance_Of_Two != -1) total_distance += distance_Of_Two;
            }
            if (total_distance > max_distance) {
                max_distance = total_distance;
                min_index = i;
            }
        }
        return nouns[min_index];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    /*private boolean noun_in_wordnet(String nouns[]) {
        int len = nouns.length;
        for (int i = 0; i < len; ++i) {
            if (!wordNet.isNoun(nouns[i])) return false;
        }
        return true;
    }*/
}  // see test client below

// TODO： How to make outcast immutable ?
