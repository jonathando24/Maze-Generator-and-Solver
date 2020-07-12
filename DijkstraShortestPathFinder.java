package graphs.shortestpaths;

import graphs.BaseEdge;

import graphs.Graph;

import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see ShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    implements ShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        //return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
        You'll also need to change the part of the class declaration that says
        `ArrayHeapMinPQ<T extends Comparable<T>>` to `ArrayHeapMinPQ<T>`.
         */

        return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public ShortestPath<V, E> findShortestPath(G graph, V start, V end) {
        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        Map<V, E> edgeToV = new HashMap<>();
        Map<V, Double> distToV = new HashMap<>();

        DoubleMapMinPQ<V> orderedPerimeter = new DoubleMapMinPQ<>();

        for (E current : graph.outgoingEdgesFrom(start)) {
            distToV.put(current.to(), Double.POSITIVE_INFINITY);
        }

        orderedPerimeter.add(start, 0);
        distToV.put(start, 0.0);

        V from = null;
        while (!orderedPerimeter.isEmpty() && !end.equals(from)) {
            from = orderedPerimeter.removeMin();
            for (E e : graph.outgoingEdgesFrom(from)) {
                V to = e.to();
                if (!distToV.containsKey(to)) {
                    distToV.put(to, Double.POSITIVE_INFINITY);
                }
                double oldDist = distToV.get(to);
                double newDist = distToV.get(from) + e.weight();
                if (newDist < oldDist) {
                    edgeToV.put(to, e);
                    distToV.put(to, newDist);
                    if (orderedPerimeter.contains(to)) {
                        orderedPerimeter.changePriority(to, newDist);
                    }
                    else {
                        orderedPerimeter.add(to, newDist);
                    }
                }
            }
        }

        if (!edgeToV.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        else {
            V current = end;
            List<E> path = new ArrayList<>();
            while (!current.equals(start)) {
                path.add(edgeToV.get(current));
                current = edgeToV.get(current).from();
            }

            Collections.reverse(path);

            return new ShortestPath.Success<>(path);
        }
    }

}
