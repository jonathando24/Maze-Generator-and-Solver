package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> indexMap = new HashMap<>();

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
    }

    @Override
    public void makeSet(T item) {
        indexMap.put(item, pointers.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!indexMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        int rootIndex = indexMap.get(item);
        while (pointers.get(rootIndex) >= 0) {
            rootIndex = pointers.get(rootIndex);
        }

        int index = indexMap.get(item);
        while (pointers.get(index) >= 0) {
            pointers.set(index, rootIndex);
            index = pointers.get(index);
        }

        return rootIndex;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!indexMap.containsKey(item1) || !indexMap.containsKey(item2)) {
            throw new IllegalArgumentException();
        }

        int firstSet = findSet(item1);
        int secondSet = findSet(item2);
        if (firstSet == secondSet) {
            return false;
        }
        if (pointers.get(firstSet) < pointers.get(secondSet)) {
            pointers.set(firstSet, pointers.get(firstSet) + pointers.get(secondSet));
            pointers.set(secondSet, firstSet);
        }
        else {
            pointers.set(secondSet, pointers.get(firstSet) + pointers.get(secondSet));
            pointers.set(firstSet, secondSet);
        }

        return true;
    }
}
