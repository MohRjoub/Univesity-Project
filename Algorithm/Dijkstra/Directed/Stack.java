package Directed;

public class Stack<T> {
    private T[] s;
    private int n = -1;

    public Stack(int capacity) {
        s = (T[])new Comparable[capacity];
    }

    public boolean isEmpty() {
        return n == -1;
    }

    public int getN() {
        return n;
    }

    public void push(T data) {
        if (n + 1 == s.length) {
            resize(2 * s.length); // double size of array
        }
        s[++n] = data;
    }

    public T pop() {
        if (!isEmpty()) {
            T item = s[n];
            s[n] = null;
            n--;
            if (n > 0 && n == s.length / 4) {
                resize(s.length / 2); // shrink size of array
            }
            return item;
        }
        return null;
    }

    private void resize(int capacity) {
        T[] copy = (T[])new Comparable[capacity];
        for (int i = 0; i <= n; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    public String toString() {
        String res = "Top-->";
        for (int i = n; i >= 0; i--) {
            res += "[" + s[i] + "]-->";
        }
        return res + "Null";
    }
}
