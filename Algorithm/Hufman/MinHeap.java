package application;

import java.util.NoSuchElementException;
// minHeap class
public class MinHeap<T extends Comparable<T>> {
    private T[] pq;
    private int n; // number of items in the heap

    
    public MinHeap(int initCapacity) {
        pq = (T[]) new Comparable[initCapacity + 1];
        n = 0;
    }

    //Initializes an empty min heap.
    public MinHeap() {
        this(1);
    }

    // return if the heap is empty or not
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items in the heap
    public int size() {
        return n;
    }

    // return the minimum value in the heap
    public T min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    // resize the array to have the given capacity
    private void resize(int capacity) {
        assert capacity > n;
        T[] temp = (T[]) new Comparable[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    // add new item to the heap
    public void insert(T data) {
        // resize the array if necessary
        if (n == pq.length - 1) resize(2 * pq.length);

        // add the item and percolate it up to maintain heap invariant
        pq[++n] = data;
        swim(n);
    }

    // delete the minimum value in the heap
    public T delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        T min = pq[1];
        exch(1, n--);
        sink(1);
        pq[n+1] = null;
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
        return min;
    }

    //  moving a node upward until it reaches the correct position
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k/2, k);
            k = k/2;
        }
    }

    // moving a node downward until it reaches the correct position
    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++; // check which of the child is minimum
            if (!greater(k, j)) break; // if the parent is not grater than its children
            exch(k, j);
            k = j;
        }
    }

    // check if i greater the j
    private boolean greater(int i, int j) {
            return pq[i].compareTo(pq[j]) > 0;
    }

    // swap i and j
    private void exch(int i, int j) {
        T swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

}

