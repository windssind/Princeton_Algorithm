/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> sequence = new LinkedList<>();
        fillWithChars(sequence);
        while (!BinaryStdIn.isEmpty()) {
            char inChar = BinaryStdIn.readChar();
            int hitIndex = sequence.indexOf(inChar);
            sequence.remove(inChar);
            sequence.add(0, inChar);
            BinaryStdOut.write(hitIndex);
        }
        BinaryStdIn.close();
    }


    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> sequence = new LinkedList<>();
        fillWithChars(sequence);
        while (!BinaryStdIn.isEmpty()) {
            int index = (int) BinaryStdIn.readChar();
            char outByte = sequence.get(index);
            BinaryStdOut.write(outByte);
            // move to front
            sequence.remove(index);
            sequence.add(0, outByte);
        }
        BinaryStdIn.close();
    }

    private static void fillWithChars(LinkedList<Character> sequence) {
        for (int i = 0; i < 256; ++i) {
            sequence.add((char) i);
        }
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else decode();
    }

}
