/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    // 字典树
    private Trie valid_Words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        valid_Words = new Trie();
        for (String word : dictionary) {
            valid_Words.insert(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> words = new HashSet<>();
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                boolean marked[][] = new boolean[board.rows()][board.cols()];
                marked[i][j] = true;
                String Initializer = String.valueOf(board.getLetter(i, j));
                if (Initializer.equals("Q")) Initializer = "QU";
                DFS_Words(words, Initializer, board,
                          marked, i, j);
            }
        }
        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (valid_Words.contains(word)) {
            int len = word.length();
            switch (len) {
                case 1:
                case 2:
                    score = 0;
                    break;
                case 3:
                case 4:
                    score = 1;
                    break;
                case 5:
                    score = 2;
                    break;
                case 6:
                    score = 3;
                    break;
                case 7:
                    score = 5;
                    break;
                default:
                    score = 11;
                    break;
            }
        }
        return score;
    }

    /**
     * 获取所有的有可能hit的words
     *
     * @param words
     * @param prefix 传进来的prefix是已经包含了cur_Row 和 cur_Col的字符的
     */
    private void DFS_Words(HashSet<String> words, String prefix, BoggleBoard board,
                           boolean[][] marked, int cur_Row, int cur_Col) {
        // 用当前这个prefix去找已经没有了
        if (!validate(cur_Row, cur_Col, board.rows(), board.cols()))
            throw new IllegalArgumentException("function: DFS_WORDS\n");
        if (!valid_Words.isPrefix(prefix)) return;
        if (prefix.length() >= 3 && valid_Words.contains(prefix)) {
            words.add(prefix);
        }
        marked[cur_Row][cur_Col] = true;
        for (int i = cur_Row - 1; i < cur_Row + 2; ++i) {
            for (int j = cur_Col - 1; j < cur_Col + 2; ++j) {
                if (i == cur_Row && j == cur_Col) continue;
                if (!validate(i, j, board.rows(), board.cols())) continue;
                if (marked[i][j]) continue;
                String Adder = String.valueOf(board.getLetter(i, j));
                if (Adder.equals("Q")) Adder = "QU";
                DFS_Words(words, prefix + Adder, board,
                          marked, i, j);
            }
        }
        marked[cur_Row][cur_Col] = false;
    }

    private boolean validate(int row, int col, int row_Max, int col_Max) {
        return !(row < 0 || row >= row_Max || col < 0 || col >= col_Max);
    }

    // 这里是一个性能消耗的点，尤其是对内存，有没有什么好的方法解决呢
    /*private boolean[][] new_Array_Marked(boolean[][] origin, int marked_Row, int marked_Col) {
        int row = origin.length;
        int col = origin[0].length;
        boolean[][] new_Array = new boolean[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                new_Array[i][j] = origin[i][j];
            }
        }
        new_Array[marked_Row][marked_Col] = true;
        return new_Array;
    }*/

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

// TODO： 速度太慢了，就是在搜索是否有共同前缀那里很慢。。
// TODO: 处理Qu
