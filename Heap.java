package heaps;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

/** An instance is a max-heap or a min-heap of distinct values of type T <br>
 * with priorities of type double. */
public class Heap<T> {

    protected final boolean isMinHeap;
    protected Item[] c;
    protected int size;
    protected HashMap<T, Integer> map;

    /** Constructor: an empty heap with capacity 10. <br>
     * It is a min-heap if isMin is true and a max-heap if isMin is false. */
    public Heap(boolean isMin) {
        isMinHeap= isMin;
        c= createItemArray(10);
        map= new HashMap<>();
    }

    /** Add v with priority p to the heap. <br>
     * Throws an illegalArgumentException if v is already in the heap. <br>
     * The expected time is logarithmic and <br>
     * the worst-case time is linear in the size of the heap. */
    public void add(T v, double p) throws IllegalArgumentException {
        if (map.get(v) != null) { throw new IllegalArgumentException(); }
        fixCapacity();
        Item pNew= new Item(v, p);
        c[size]= pNew;
        map.put(v, size);
        size++ ;
        bubbleUp(size - 1);
    }

    /** If size = length of c, double the length of array c. <br>
     * The worst-case time is proportional to the length of c. */
    protected void fixCapacity() {
        int clength= c.length;
        if (size == clength) {
            c= Arrays.copyOf(c, clength * 2);
        }
    }

    /** Return the size of this heap. <br>
     * This operation takes constant time. */
    public int size() { // Do not change this method
        return size;
    }

    /** Swap c[h] and c[k]. <br>
     * Precondition: 0 <= h < heap-size, 0 <= k < heap-size. */
    void swap(int h, int k) {
        assert 0 <= h && h < size && 0 <= k && k < size;
        Item store= c[h];
        c[h]= c[k];
        map.put(c[k].value, h);
        c[k]= store;
        map.put(store.value, k);
    }

    /** If a value with priority p1 belongs above a value with priority p2 in the heap, <br>
     * return 1.<br>
     * If priority p1 and priority p2 are the same, return 0. <br>
     * If a value with priority p1 should be below a value with priority p2 in the heap,<br>
     * return -1.<br>
     * This is based on what kind of a heap this is, <br>
     * ... E.g. a min-heap, the value with the smallest priority is in the root.<br>
     * ... E.g. a max-heap, the value with the largest priority is in the root. */
    public int compareTo(double p1, double p2) {
        if (p1 == p2) return 0;
        if (isMinHeap) { return p1 < p2 ? 1 : -1; }
        return p1 > p2 ? 1 : -1;
    }

    /** If c[h] should be above c[k] in the heap, return 1. <br>
     * If c[h]'s priority and c[k]'s priority are the same, return 0. <br>
     * If c[h] should be below c[k] in the heap, return -1. <br>
     * This is based on what kind of a heap this is, <br>
     * ... E.g. a min-heap, the value with the smallest priority is in the root. <br>
     * ... E.g. a max-heap, the value with the largest priority is in the root. */
    public int compareTo(int h, int k) {
        return compareTo(c[h].priority, c[k].priority);
    }

    /** If h >= size, return.<br>
     * Otherwise, bubble c[h] up the heap to its right place. <br>
     * Precondition: 0 <= h and, if h < size, <br>
     * ... the class invariant is true, except perhaps that <br>
     * ... c[h] belongs above its parent (if h > 0) in the heap. */
    void bubbleUp(int h) {
        if (h >= size) return;
        int i= h;
        boolean invar= false;
        while (i > 0 && !invar) {
            if (compareTo(i, (i - 1) / 2) == 1) {
                swap(i, (i - 1) / 2);
            } else {
                invar= true;
            }
            i= (i - 1) / 2;
        }
    }

    /** If this is a min-heap, return the heap value with lowest priority. <br>
     * If this is a max-heap, return the heap value with highest priority.<br>
     * Do not change the heap. <br>
     * This operation takes constant time. <br>
     * Throws a NoSuchElementException if the heap is empty. */
    public T peek() {
        if (size == 0) { throw new NoSuchElementException(); }
        return c[0].value;
    }

    /** If h < 0 or size <= h, return.<br>
     * Otherwise, Bubble c[h] down in heap until the class invariant is true. <br>
     * If there is a choice to bubble down to both the left and right children <br>
     * (because their priorities are equal), choose the left child. <br>
     *
     * Precondition: If 0 <= h < size, the class invariant is true except that <br>
     * perhaps c[h] belongs below one or both of its children. */
    void bubbleDown(int h) {
        if (h < 0 || h >= size) { return; }
        int i= h;
        boolean invar= false;
        int child= 1 + 2 * i;
        while (child < size && !invar) {
            if (child + 1 < size && compareTo(child + 1, child) == 1) {
                child= child + 1;
            }
            if (compareTo(i, child) == -1) {
                swap(i, child);
            } else {
                invar= true;
            }
            i= child;
            child= 1 + 2 * i;
        }
    }

    /** If this is a min-heap, remove and return heap value with lowest priority. <br>
     * If this is a max-heap, remove and return heap value with highest priority. <br>
     * Expected time: logarithmic. Worst-case time: linear in the size of the heap.<br>
     * Throws a NoSuchElementException if the heap is empty. */
    public T poll() {
        if (size == 0) { throw new NoSuchElementException(); }
        T root= c[0].value;
        swap(0, size - 1);
        map.remove(root);
        size= size - 1;
        bubbleDown(0);
        return root;
    }

    /** Change the priority of value v to p. <br>
     * Expected time: logarithmic. Worst-case time: linear in the size of the heap.<br>
     * Throws an IllegalArgumentException if v is not in the heap. */
    public void changePriority(T v, double p) {
        Integer pos= map.get(v);
        if (pos == null) { throw new IllegalArgumentException(); }
        double oldP= c[pos].priority;
        c[pos].priority= p;
        if (compareTo(p, oldP) == 1) {
            bubbleUp(pos);
        }
        if (compareTo(p, oldP) == -1) {
            bubbleDown(pos);
        }
    }

    /** Return the heap values (only, not the priorities) in form [5, 3, 2]. */
    public String toStringValues() {
        StringBuilder resb= new StringBuilder("[");
        for (int h= 0; h < size; h= h + 1) {
            if (h > 0) resb.append(", ");
            resb.append(c[h].value);
        }
        return resb.append(']').toString();
    }

    /** Return the heap priorities in form [5.0, 3.0, 2.0]. */
    public String toStringPriorities() {
        StringBuilder resb= new StringBuilder("[");
        for (int h= 0; h < size; h= h + 1) {
            if (h > 0) resb.append(", ");
            resb.append(c[h].priority);
        }
        return resb.append(']').toString();
    }

    Item[] createItemArray(int m) {
        return (Item[]) Array.newInstance(Item.class, m);
    }

    /** An object of class Item houses a value and a priority. */
    class Item {
        protected T value;             // The value
        protected double priority;   // The priority

        /** An instance with value v and priority p. */
        protected Item(T v, double p) {
            value= v;
            priority= p;
        }

        /** Return a representation of this object. */
        @Override
        public String toString() {
            return "(" + value + ", " + priority + ")";
        }

        /** = "this and ob are of the same class and have equal val and priority fields." */
        @Override
        public boolean equals(Object ob) {
            if (ob == null || getClass() != ob.getClass()) return false;
            Item obe= (Item) ob;
            return value == obe.value && priority == obe.priority;
        }
    }
}
