/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private class myIterator implements Iterator<Item> {
        private int currentIndex;
        private Item[] iteratorArray;

        myIterator() { // 允许线性时间构造
            currentIndex = 0;
            iteratorArray = (Item[]) new Object[size];
            for (int i = 0, j = head; i < size; ++i, ++j) {
                iteratorArray[i] = QueueArray[j];
            }
            StdRandom.shuffle(iteratorArray);
        }

        public boolean hasNext() {
            return currentIndex < size;
        }

        public Item next() {
            if (hasNext()) {
                return iteratorArray[currentIndex++];
            }
            else {
                throw new NoSuchElementException("No element\n");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Not support remove");
        }
    }

    private Item[] QueueArray;
    private int head;
    private int tail;// point to next plus one
    private int size;
    private int capacity;

    // construct an empty randomized queue
    private void swap(int index1, int index2) {
        if (index1 == index2) return;
        Item tmp = QueueArray[index1];
        QueueArray[index1] = QueueArray[index2];
        QueueArray[index2] = tmp;
    }

    private void resizeArray(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = head, j = 0; j < size; ++j, ++i) {
            newArray[j] = QueueArray[i];
        }
        QueueArray = newArray;
        head = 0;
        tail = head + size;
        this.capacity = capacity;
    }

    public RandomizedQueue() {
        head = 0;
        tail = 0;
        size = 0;
        capacity = 4;
        QueueArray = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (isEmpty()) {
            QueueArray[tail] = item;
            tail++;
            size++;
            return;
        }
        QueueArray[tail++] = item;
        int swapIndex = StdRandom.uniformInt(head, tail);
        swap(tail - 1, swapIndex);
        size++;
        if (size >= capacity || tail >= capacity) {
            resizeArray(capacity * 2);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("No element out\n");
        Item returnItem = QueueArray[head];
        QueueArray[head] = null;
        size--;
        head++;
        if (size == 0) tail = head + 1;
        if (size < capacity / 4) {
            resizeArray(capacity / 2);
        }
        if (tail >= capacity) {
            resizeArray(capacity * 2);
        }
        return returnItem;
    }


    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("No sample out\n");
        return QueueArray[StdRandom.uniformInt(head, tail)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new myIterator();
    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        for (int i = 0; i < 5; ++i) {
            queue.enqueue(String.valueOf(i));
        }

        /*for (int i = 0; i < 10000; ++i) {
            System.out.println(queue.dequeue());
        }*/
        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
