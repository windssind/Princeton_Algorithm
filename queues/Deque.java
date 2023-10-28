/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // 用LinkedList
    // declare a inner class
    private class Node {
        public Item item;
        public Node next;
        public Node prev;

        Node(Item item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
    }

    private class myIterator implements Iterator<Item> {
        private Node currentItem;
        private int size = Deque.this.size;

        myIterator() {
            currentItem = Deque.this.first;
        }

        // 这个有点多余的
        public boolean hasNext() {
            return currentItem != null;
        }

        public Item next() {
            if (hasNext()) {
                Item outItem = currentItem.item;
                currentItem = currentItem.next;
                return outItem;
            }
            else {
                throw new NoSuchElementException("No next element\n");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Not support remove\n");
        }
    }

    private Node first;
    private Node last;
    private int size; // How to build in ptr

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }


    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Empty argument\n");
        // wrap first
        Node newItem = new Node(item);
        if (!isEmpty()) {
            first.prev = newItem;
        }
        newItem.next = first;
        newItem.prev = null;
        first = newItem;
        size++;
        if (size == 1) last = newItem;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Empty argument\n");
        // wrap last
        Node newItem = new Node(item);
        newItem.prev = last;
        newItem.next = null;
        if (size > 0) last.next = newItem;
        last = newItem;
        size++;
        if (size == 1) first = newItem;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("No element in the queue\n");
        if (size == 1) {
            Item itemRemoved = first.item;
            first = null;
            last = null;
            size--;
            return itemRemoved;
        }
        Item itemRemoved = first.item;
        first = first.next;
        first.prev = null;
        size--;
        if (size == 1) last = first;
        return itemRemoved;
    }


    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("No element in the queue\n");
        if (size == 1) {
            Item itemRemoved = last.item;
            first = null;
            last = null;
            size--;
            return itemRemoved;
        }
        Item itemRemoved = last.item;
        last = last.prev;
        last.next = null;
        size--;
        if (size == 1) last = first;
        return itemRemoved;
    }

    // return an iterator over items in order from front to back
    // die
    public Iterator<Item> iterator() {
        return new myIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> strings = new Deque<String>();
        for (int i = 0; i < 5; ++i) {
            strings.addLast(String.valueOf(i));
        }
        for (int i = 0; i < 5; ++i) {
            strings.removeFirst();
        }
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
