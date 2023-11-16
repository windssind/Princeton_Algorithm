/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;

public class Board {

    private int map[][];
    private int N;
    private int zero_Row;
    private int zero_Col;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        N = tiles.length;
        map = new int[N][N]; // 注意下标的对应关系
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                map[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    zero_Row = i;
                    zero_Col = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension() + "\n");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                sb.append(' ').append(map[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int right_Num = i * N + j + 1;
                if (map[i][j] != right_Num && map[i][j] != 0) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan_distance = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                //
                int targetIndex = map[i][j] - 1;
                int right_Row = targetIndex / N;
                int right_Col = targetIndex % N;
                if (map[i][j] != 0)
                    manhattan_distance += Math.abs(right_Row - i) + Math.abs(right_Col - j);
            }
        }
        return manhattan_distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int right_Num = i * N + j + 1;
                if (map[i][j] == 0) continue;
                if (map[i][j] != right_Num) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != Board.class) return false;
        Board other = (Board) y;
        int other_N = ((Board) y).dimension();
        if (dimension() != other_N) return false;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (map[i][j] != ((Board) y).map[i][j]) return false;
            }
        }
        return true;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() { // 直接返回Arrayist就行了
        ArrayList<Board> neighbors = new ArrayList<>();
        // TODO：完成这个的构造
        if (zero_Col - 1 >= 0) {
            Board new_board = new Board(map);
            swap(new_board.map, zero_Row, zero_Col, zero_Row, zero_Col - 1);
            new_board.zero_Col -= 1;
            neighbors.add(new_board);
        }
        if (zero_Row - 1 >= 0) {
            Board new_board = new Board(map);
            swap(new_board.map, zero_Row, zero_Col, zero_Row - 1, zero_Col);
            new_board.zero_Row -= 1;
            neighbors.add(new_board);
        }
        if (zero_Col + 1 <= N - 1) {
            Board new_board = new Board(map);
            swap(new_board.map, zero_Row, zero_Col, zero_Row, zero_Col + 1);
            new_board.zero_Col += 1;
            neighbors.add(new_board);
        }
        if (zero_Row + 1 <= N - 1) {
            Board new_board = new Board(map);
            swap(new_board.map, zero_Row, zero_Col, zero_Row + 1, zero_Col);
            new_board.zero_Row += 1;
            neighbors.add(new_board);
        }
        return neighbors;
    }

    /*private class MyIterator implements Iterator<Board> {
        Board[] boards = new Board[4];
        int now_index;
        int size;

        MyIterator() {
            // TODO：完成这个的构造
            size = 0;
            if (zero_Col - 1 >= 0) {
                boards[size++] = new Board(map);
                swap(boards[size - 1].map, zero_Row, zero_Col, zero_Row, zero_Col - 1);
            }
            if (zero_Row - 1 >= 0) {
                boards[size++] = new Board(map);
                swap(boards[size - 1].map, zero_Row, zero_Col, zero_Row - 1, zero_Col);
            }
            if (zero_Col + 1 <= N - 1) {
                boards[size++] = new Board(map);
                swap(boards[size - 1].map, zero_Row, zero_Col, zero_Row, zero_Col + 1);
            }
            if (zero_Row + 1 <= N - 1) {
                boards[size++] = new Board(map);
                swap(boards[size - 1].map, zero_Row, zero_Col, zero_Row + 1, zero_Col);
            }
        }

        public Board next() {
            return boards[now_index++];
        }

        public boolean hasNext() {
            return now_index < size;
        }
    }*/

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int row_1 = -1;
        int col_1 = -1;
        int row_2 = -1;
        int col_2 = -1;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (map[i][j] != 0) {
                    row_1 = i;
                    col_1 = j;
                    break;
                }
            }
            if (row_1 != -1) break;
        }
        for (int i = row_1; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (map[i][j] != 0 && (i != row_1 || j != col_1)) {
                    row_2 = i;
                    col_2 = j;
                    break;
                }
            }
            if (row_2 != -1) break;
        }
        Board twin = new Board(map);
        swap(twin.map, row_1, col_1, row_2, col_2);
        return twin;
    }

    private void swap(int map_[][], int row_1, int col_1, int row_2, int col_2) {
        int tmp = map_[row_1][col_1];
        map_[row_1][col_1] = map_[row_2][col_2];
        map_[row_2][col_2] = tmp;

    }


    // unit testing (not graded)
    public static void main(String[] args) {
        int map_1[][] = {
                { 1, 2 },
                { 3, 0 }
        };
        int map_2[][] = {
                { 1, 0 },
                { 2, 3 }
        };
        Board board_1 = new Board(map_1).twin();
        Board board_2 = new Board(map_2).twin();
        System.out.println(board_1.toString());
    }

}
