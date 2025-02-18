package Directed;

public class DirectedGraph {

    private int V; // number of vertices in this directed graph
    private int E; // number of edges in this directed graph
    private Node<String>[] vertices; // array of node to build the graph
    
    
    // Initializes an empty directed graph with V vertices.
    @SuppressWarnings("unchecked")
    public DirectedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be non-negative");
        this.V = V;
        this.E = 0;
        vertices = new Node[V];
        for (int v = 0; v < V; v++) {
            vertices[v] = new Node<>("", new LinkedList<>());
        }
    }

    // Returns the number of vertices in this directed graph.
    public int V() {
        return V;
    }

    // Returns the number of edges in this directed graph.
    public int E() {
        return E;
    }


    // throw an IllegalArgumentException if v not in 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // Adds the directed edge v to w to this directed graph.
    public void addEdge(int v, int w, double cost, double time, double destance) {
        validateVertex(v);
        validateVertex(w);
        vertices[v].edges.add(new DirectedEdge(v, w, cost, time, destance, ""));
        E++;
    }
    
    // set the vertex content 
    public void setContent(int v, String content) {
    	validateVertex(v);
		vertices[v].content = content;
	}

    // get the vertex content 
    public String getContent(int v) {
    	validateVertex(v);
		return vertices[v].content;
	}
    
    // Return the index of the vertex passed on its content
    public int findIndex(String content) {
    	int index = -1;
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i].content.trim().equalsIgnoreCase(content.trim())) {
				index = i;
				break;
			}
		}
		return index;
	}

    // Returns the vertices adjacent from vertex v in this directed graph.
    public LinkedList<DirectedEdge> adj(int v) {
        validateVertex(v);
        return vertices[v].edges;
    }

    // Returns a string representation of the graph.
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + "\n");
        for (int v = 0; v < V; v++) {
            s.append(String.format("%d: ", v));
            for (DirectedEdge w : vertices[v].edges) {
                s.append(String.format("%d ", w.destination()));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    
    
    public Node<String>[] getVertices() {
		return vertices;
	}

	public void setVertices(Node<String>[] vertices) {
		this.vertices = vertices;
	}



	// vertex Node
     class Node <T> {
    	private T content; // content of the vertex
    	private LinkedList<DirectedEdge> edges; // directed edges incident from the vertex 
		public Node(T content, LinkedList<DirectedEdge> edges) {
			this.content = content;
			this.edges = edges;
		}
		public T getContent() {
			return content;
		}
		public void setContent(T content) {
			this.content = content;
		}
		public LinkedList<DirectedEdge> getEdges() {
			return edges;
		}
		public void setEdges(LinkedList<DirectedEdge> edges) {
			this.edges = edges;
		}
		
    }

}