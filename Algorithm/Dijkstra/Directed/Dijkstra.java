package Directed;


public class Dijkstra {
    private tableNode[] table;
    private DirectedGraph graph;
   
    public Dijkstra(DirectedGraph graph, int source, int destination, String filter) {
        
        initializeTable(graph);
        table[source].distTo = 0.0;
        
        for (int i = 0; i < graph.V(); i++) {
            int v = smallestUnknownDistanceVertex();
            if (v == -1) break;
            table[v].known = true;
            if (v == destination) break;
            for (DirectedEdge edge : graph.adj(v)) {
                edge.setFilter(filter);
                int w = edge.destination();
          
                if (!table[w].known) {
                    if (table[w].distTo > table[v].distTo + edge.getWeight(filter)) {
                    	table[w].distTo = table[v].distTo + edge.getWeight(filter);
                    	table[w].pathTo = edge;
                    }
                }
            }
        }
    }
    
    private void initializeTable(DirectedGraph graph) {
        this.graph = graph;
        this.table = new tableNode[graph.V()];
        
        for (int i = 0; i < table.length; i++) {
			table[i] = new tableNode();
		}

        for (int v = 0; v < graph.V(); v++) {
            table[v].distTo = Double.POSITIVE_INFINITY;
            table[v].known = false;
            table[v].pathTo = null;
        }

	}

    // method to find the unknown vertex with the smallest distance
    private int smallestUnknownDistanceVertex() {
        int min = -1;
        for (int v = 0; v < graph.V(); v++) {
            if (!table[v].known) {
                if (min == -1) {
                    min = v;
                } else if (table[v].distTo < table[min].distTo) {
                    min = v;
                }
            }
        }
        return min;
    }

    // Returns the distance of a shortest path from the source vertex to vertex v
    public double distTo(int v) {
        return table[v].distTo;
    }

    // Returns true if there is a path from the source vertex to vertex v
    public boolean hasPathTo(int v) {
        return table[v].pathTo != null;
    }

    // Returns a shortest path from the source vertex to vertex v
    public Stack<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<>(graph.V());
        for (DirectedEdge edge = table[v].pathTo; edge != null; edge = table[edge.source()].pathTo) {
            path.push(edge);
        }
        return path;
    }
    
    class tableNode {
    	private boolean known;
    	private DirectedEdge pathTo;
    	private double distTo;
    }
}