import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    /*char F[];
    char L[];
    ArrayList<String> original;
    ArrayList<String> sorted;

    int next[];*/
    private int index[];
    private String string;
    private int length;
    // circular suffix array of s
    // encoding

    // 不需要创建新的对象就可以得到最终SortedSuffix数组的信息
    private class SortedSuffixOrder implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            for (int i = 0; i < length(); i++) {
                char c1 = string.charAt((i + a) % length());
                char c2 = string.charAt((i + b) % length());
                if (c1 > c2) return 1;
                if (c1 < c2) return -1;
            }
            return 0;
        }
    }

    public CircularSuffixArray(String s) {
        string = s;
        length = string.length();
        index = new int[length];
        Arrays.sort(index);
            /*F = new char[length];
            L = new char[length];*/

        // 统计每个字符出现的次数，找到first并且初始化F和L数组
            /*int first;
            HashMap<Character, Integer> count = new HashMap<Character, Integer>();
            for (int i = 0; i < length; ++i) {
                String suffix = sorted.get(i);
                F[i] = suffix.charAt(0);
                L[i] = suffix.charAt(length - 1);
                if (count.containsKey(L[i])) {
                    count.put(L[i], count.get(L[i]) + 1);
                }
                else {
                    count.put(L[i], 1);
                    if (suffix.equals(sAdderOne)) first = i;
                }
            }

            // 初始化next数组
            next = new int[length];
            boolean []marked = new boolean[length];
            for (int i = 0; i < length; ++i) {
                if (marked[i]) continue;
                if (count.get(F[i]) == 1) {
                    next[i] = searchChar(L, 0, F[i]);
                    marked[i] = true;
                }
                // 存在多种映射
                else {
                    int size = count.get(F[i]);
                    int hitIndex = -1;
                    for (int j = 0; j < size; ++j) {
                        hitIndex = searchChar(L, hitIndex + 1, F[i]);
                        next[i] = hitIndex;
                        marked[hitIndex] = true;
                    }
                }
            }*/
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return index[i];
            /*if (i == 0) return length();
            int ptr = next[0];
            int j;
            for (j= 0; j < length()+1; ++j){
                if (ptr == i) break;
                ptr = next[ptr];
            }
            return j;*/
    }

        /*private int searchChar(char[]L, int beginIndex, char goal){
            int length = L.length;
            for (int i = beginIndex; i < length; ++i) {
                if (L[i] == goal) return i;
            }
            throw new RuntimeException("function: searchChar failed\n");
        }*/

    // decoding
    // unit testing (required)
        /*public static void main(String[] args) {

        }*/

}

