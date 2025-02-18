package Directed;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class LinkedList<T> implements Iterable<T> {
    private Node<T> first;    // beginning of LinkedList
    private int n;               // number of elements in LinkedList

    // Initializes an empty LinkedList.
    public LinkedList() {
        first = null;
        n = 0;
    }

    // Returns true if this LinkedList is empty.
    public boolean isEmpty() {
        return first == null;
    }

    // Returns the number of items in this LinkedList.
    public int size() {
        return n;
    }

    // Adds the item to this LinkedList.
    public void add(T data) {
        Node<T> temp = first;
        first = new Node<T>(data);
        first.next = temp;
        n++;
    }


    // Returns an iterator that iterates over the items in this LinkedList
    public Iterator<T> iterator()  {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<T> {
        private Node<T> current;

        public LinkedIterator(Node<T> first) {
            current = first;
        }

        public boolean hasNext()  {
            return current != null;
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = current.data;
            current = current.next;
            return item;
        }
    }
    
    // helper class
    private class Node<T> {
        private T data;
        private Node<T> next;
        public Node(T data) {
        	this.data = data;
		}
    }
}