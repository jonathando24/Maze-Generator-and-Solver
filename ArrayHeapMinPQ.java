package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.

    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    Map<T, Integer> map = new HashMap<>();

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(null);
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        map.put(temp.getItem(), b);
        map.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null) {
            throw new IllegalArgumentException("Null item not allowed");
        }
        if (map.containsKey(item)) {
            throw new IllegalArgumentException("Duplicate item");
        }
        int i = items.size();
        items.add(new PriorityNode<>(item, priority));
        map.put(item, i);
        percolateUp(i);
    }

    private void percolateUp(int i) {
        while (i > START_INDEX) {
            int parent = i/2;
            if (items.get(i).getPriority() < items.get(parent).getPriority()) {
                swap(i, parent);
                i = parent;
            }
            else {
                i = 0;
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(1).getItem();
    }

    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        T out = items.get(1).getItem();
        items.set(START_INDEX, items.get(items.size() - 1));
        items.remove(items.size() - 1);
        map.remove(out);
        percolateDown(START_INDEX);
        return out;
    }

    private void percolateDown(int i) {
        while (i < items.size()) {
            if (2*i + 1 < items.size() && items.get(2*i + 1).getPriority() < items.get(2*i).getPriority()
                    && items.get(2*i + 1).getPriority() < items.get(i).getPriority()) {
                swap(i, 2*i + 1);
                i = 2*i + 1;
            }
            else if (2*i < items.size() && items.get(2*i).getPriority() < items.get(i).getPriority()) {
                swap(i, 2*i);
                i = 2*i;
            }
            else {
                i = items.size();
            }
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        Integer i = map.get(item);
        if (i == null) {
            throw new NoSuchElementException();
        }

        items.get(i).setPriority(priority);
        percolateUp(i);
        percolateDown(i);
    }

    @Override
    public int size() {
        return items.size() - 1;
    }
}
