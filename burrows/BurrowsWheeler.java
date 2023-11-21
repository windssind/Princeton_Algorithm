/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {


    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    // 输出转换后的first以及字符串
    public static void transform() {
        StringBuilder in = new StringBuilder();
        String input;
        while (!BinaryStdIn.isEmpty()) {
            char Adder = BinaryStdIn.readChar();
            in.append(Adder);
        }
        input = in.toString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
        int length = circularSuffixArray.length();

        // 统计每个字符出现的次数，找到first并且初始化F和L数组
        int first = 0;
        for (int i = 0; i < length; ++i) {
            if (circularSuffixArray.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 1; i < length; ++i) {
            BinaryStdOut.write(input.charAt((circularSuffixArray.index(i) - 1 + length)
                                                    % length)); // 原始字符串中，前面一个字符代表的就是转换后的最后一个字符
        }
        BinaryStdOut.write(input.charAt(0)); // 开头的肯定在第一位
    }


    /**
     * 传进来的参数有 t[]以及first，需要转化回原始的字符串
     * 很奇妙，仅仅通过这两个就可以恢复原来的字符串阿
     */
    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        ArrayList<Character> t = new ArrayList<Character>();
        while (!BinaryStdIn.isEmpty()) {
            t.add(BinaryStdIn.readChar());
        }
        int length = t.size();
        int next[] = new int[length];
        constructNext(next, length, first, t);
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    private static void constructNext(int[] next, int length, int first, ArrayList<Character> t) {
        // 首先初始化F， F数组的初始化很简单，就是t进行排序之后的数组
        char F[] = new char[length];
        for (int i = 0; i < length; ++i) {
            F[i] = t.get(i);
        }
        Arrays.sort(F);

        // 用于统计每个字符出现的次数
        HashMap<Character, Integer> count = new HashMap<Character, Integer>();
        for (int i = 0; i < length; ++i) {
            if (count.containsKey(F[i])) {
                count.put(F[i], count.get(F[i]) + 1);
            }
            else {
                count.put(F[i], 1);
            }
        }

        // 构建next数组
        boolean[] marked = new boolean[length];
        for (int i = 0; i < length; ++i) {
            if (marked[i]) continue;
            if (count.get(F[i]) == 1) {
                next[i] = searchChar(t, 0, F[i]);
                marked[i] = true;
            }
            // 存在多种映射
            else {
                int size = count.get(F[i]);
                int hitIndex = -1;
                for (int j = 0; j < size; ++j) {
                    hitIndex = searchChar(t, hitIndex + 1, F[i]);
                    next[i] = hitIndex;
                    marked[hitIndex] = true;
                }
            }
        }
        // 按照next数组的顺序返回原始字符串
        int ptr = first;
        for (int i = 0; i < length; ++i) {
            BinaryStdOut.write(t.get(ptr));
            ptr = next[ptr];
        }
    }

    private static int searchChar(ArrayList<Character> t, int beginIndex, char goal) {
        int length = t.size();
        for (int i = beginIndex; i < length; ++i) {
            if (t.get(i) == goal) return i;
        }
        throw new RuntimeException("function: searchChar failed\n");
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else inverseTransform();
    }

}
