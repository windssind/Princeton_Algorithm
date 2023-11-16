/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solver {
    private search_Node goal;
    private int N;
    private boolean solvable;
    private Board initial_Board;
    private ArrayList<Board> boardArrayList;

    private class search_Node {
        Board board;
        int move;
        search_Node prev;
        int distance;

        search_Node(Board board, int move, search_Node prev) {
            this.board = board;
            this.move = move;
            this.prev = prev;
            this.distance = board.manhattan();
        }
    }


    private class Manhantan_Comparator implements Comparator<search_Node> {
        public int compare(search_Node b1, search_Node b2) {
            if (b1.distance + b1.move
                    < b2.distance + b2.move) return -1;
            else if (b1.distance + b1.move
                    == b2.distance + b2.move) return 0;
            else return 1;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<search_Node> open_List;
        MinPQ<search_Node> open_List_Twin;
        initial_Board = initial;
        N = initial_Board.dimension();
        search_Node root = new search_Node(initial_Board, 0, null);
        search_Node root_Twin = new search_Node(initial_Board.twin(), 0, null);
        open_List = new MinPQ<search_Node>(new Manhantan_Comparator());
        open_List_Twin = new MinPQ<search_Node>(new Manhantan_Comparator());
        open_List.insert(root);
        open_List_Twin.insert(root_Twin);
        while (true) {
            search_Node parent = open_List.delMin();
            search_Node parent_Twin = open_List_Twin.delMin();
            if (parent.board.isGoal()) {
                goal = parent;
                solvable = true;
                break;
            }
            for (Board board : parent.board.neighbors()) {
                // 只要删除一个和上一个相同的就可以了，虽然不能完全排除已经visit的，但也已经很有效了
                if (parent.prev == null || (parent.prev != null && (!board.equals(
                        parent.prev.board)))) {
                    search_Node new_search_Node = new search_Node(board, parent.move + 1, parent);
                    open_List.insert(new_search_Node);
                }
                // 这里有bug，会陷入回环
            }


            if (parent_Twin.board.isGoal()) {
                goal = null;
                solvable = false;
                break;
            }
            for (Board board : parent_Twin.board.neighbors()) {
                if (parent_Twin.prev == null || (parent_Twin.prev != null && (!board.equals(
                        parent_Twin.prev.board)))) {
                    search_Node new_search_Node_Twin = new search_Node(board, parent_Twin.move + 1,
                                                                       parent_Twin);
                    open_List_Twin.insert(new_search_Node_Twin);
                }
                // 这里有bug，会陷入回环
            }
        }
        /*try {
            Thread.sleep(Integer.MAX_VALUE);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        /*while (true) {
            if (!solvable) break;
            ;
        }*/
        if (!solvable) return;
        boardArrayList = new ArrayList<Board>();
        search_Node point = goal;
        while (point != null) {
            boardArrayList.add(point.board);
            point = point.prev;
        }
        Collections.reverse(boardArrayList);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // 目标的逆序对肯定是偶数。如果初始棋盘的N为奇数，那么初始为偶数逆序对才有解。如果N为偶数，那么上下移动会改变逆序，只有恰好
        /*int initial_reverse_pair_Num = reverse_pair(initial_Board.map);
        // 奇数的情况
        if (N % 2 == 1) {
            if (initial_reverse_pair_Num % 2 == 0) return true;
            else return false;
        }
        // 偶数的情况
        else {
            int blank_distance = N - initial_Board.zero_Row - 1;
            if ((initial_reverse_pair_Num + blank_distance) % 2 == 0) return true;
            else return false;
        }*/
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return goal.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        return boardArrayList;
    }

    /*private int reverse_pair(int map[][]) {
        // 用归并来统计是时间复杂度最小的，但是需要额外的一个数组作为  那我还是直接用O(n^2)的算法来计算好了
        int count = 0;
        for (int i_1 = 0; i_1 < N; ++i_1) {
            for (int j_1 = 0; j_1 < N; ++j_1) {
                int pair_r = map[i_1][j_1];
                if (pair_r == 0) continue;
                int max_Index = i_1 * N + j_1;
                for (int i_2 = 0, point = 0; i_2 <= i_1; ++i_2) {
                    for (int j_2 = 0; point < max_Index && j_2 < N; ++j_2, ++point) {
                        int pair_l = map[i_2][j_2];
                        if (pair_l == 0) continue;
                        if (pair_l > pair_r) count++;
                    }
                }
            }
        }
        return count;
    }*/

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
            System.out.println("second\n");
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


}
