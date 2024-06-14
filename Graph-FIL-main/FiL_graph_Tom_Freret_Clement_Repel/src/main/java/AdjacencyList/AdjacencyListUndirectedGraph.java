package AdjacencyList;

import java.util.*;

import GraphAlgorithms.BinaryHeapEdge;
import GraphAlgorithms.GraphTools;
import Nodes.UndirectedNode;
import Collection.Triple;


public class AdjacencyListUndirectedGraph {

	//--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

	protected List<UndirectedNode> nodes;
    protected int order;
    protected int m;

    
    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------
    
	public AdjacencyListUndirectedGraph() {
		 this.nodes = new ArrayList<>();
		 this.order = 0;
	     this.m = 0;
	}
	
	public AdjacencyListUndirectedGraph(List<UndirectedNode> nodes) {
		this.nodes = nodes;
        this.order = nodes.size();
        this.m = 0;
        for (UndirectedNode i : nodes) {
            this.m += i.getNbNeigh();
        }
    }

    public AdjacencyListUndirectedGraph(int[][] matrix) {
        this.order = matrix.length;
        this.nodes = new ArrayList<>();
        for (int i = 0; i < this.order; i++) {
            this.nodes.add(this.makeNode(i));
        }
        for (UndirectedNode n : this.getNodes()) {
            for (int j = n.getLabel(); j < matrix[n.getLabel()].length; j++) {
            	UndirectedNode nn = this.getNodes().get(j);
                if (matrix[n.getLabel()][j] != 0) {
                    n.getNeighbours().put(nn,0);
                    nn.getNeighbours().put(n,0);
                    this.m++;
                }
            }
        }
    }

    public AdjacencyListUndirectedGraph(AdjacencyListUndirectedGraph g) {
        super();
        this.order = g.getNbNodes();
        this.m = g.getNbEdges();
        this.nodes = new ArrayList<>();
        for (UndirectedNode n : g.getNodes()) {
            this.nodes.add(makeNode(n.getLabel()));
        }
        for (UndirectedNode n : g.getNodes()) {
        	UndirectedNode nn = this.getNodes().get(n.getLabel());
            for (UndirectedNode sn : n.getNeighbours().keySet()) {
            	UndirectedNode snn = this.getNodes().get(sn.getLabel());
                nn.getNeighbours().put(snn,0);
                snn.getNeighbours().put(nn,0);
            }
        }
    }
    

    // ------------------------------------------
    // 				Accessors
    // ------------------------------------------
    
    /**
     * Returns the list of nodes in the graph
     */
    public List<UndirectedNode> getNodes() {
        return nodes;
    }

    /**
     * Returns the number of nodes in the graph (referred to as the order of the graph)
     */
    public int getNbNodes() {
        return this.order;
    }
    
    /**
     * @return the number of edges in the graph
     */ 
    public int getNbEdges() {
        return this.m;
    }

    /**
     * @return true if there is an edge between x and y
     */
    public boolean isEdge(UndirectedNode x, UndirectedNode y) {
        return x.getNeighbours().containsKey(y);
    }

    /**
     * Removes edge (x,y) if there exists one
     */
    public void removeEdge(UndirectedNode x, UndirectedNode y) {
    	if (isEdge(x,y)) {
            x.getNeighbours().remove(y);
            y.getNeighbours().remove(x);
            this.m--;
    	}
    }

    /**
     * Adds edge (x,y), requires that nodes x and y already exist
     * In non-valued graph, every edge has a cost equal to 0
     */
    public void addEdge(UndirectedNode x, UndirectedNode y) {
    	if(!isEdge(x,y)){
            x.getNeighbours().put(y, 0);
            y.getNeighbours().put(x, 0);
            this.m++;
    	}
    }

    //--------------------------------------------------
    // 					Methods
    //--------------------------------------------------
    
    /**
     * Method to generify node creation
     * @param label of a node
     * @return a node typed by A extends UndirectedNode
     */
    public UndirectedNode makeNode(int label) {
        return new UndirectedNode(label);
    }

    /**
     * @return the corresponding nodes in the list this.nodes
     */
    public UndirectedNode getNodeOfList(UndirectedNode v) {
        return this.getNodes().get(v.getLabel());
    }
    
    /**
     * @return a matrix representation of the graph 
     */
    public int[][] toAdjacencyMatrix() {
        int[][] mat = new int[this.order][this.order];
        for (int i = 0; i < this.order; i++) {
            for (int j = 0; j < this.order; j++) {
                mat[i][j] = 0;
            }
        }
        for (UndirectedNode n : this.getNodes()) {
            for (UndirectedNode sn : n.getNeighbours().keySet()) {
                mat[n.getLabel()][sn.getLabel()] = 1;
            }
        }
        return mat;
    }

    
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (UndirectedNode n : nodes) {
            s.append("neighbours of ").append(n).append(" : ");
            for (UndirectedNode sn : n.getNeighbours().keySet()) {
                s.append(sn).append(" ");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    private void addEdgesToHeap(BinaryHeapEdge heap, UndirectedNode node, Set<UndirectedNode> inTree) {
        for (Map.Entry<UndirectedNode, Integer> entry : node.getNeighbours().entrySet()) {
            UndirectedNode destination = entry.getKey();
            int weight = entry.getValue();
            if (!inTree.contains(destination)) {
                heap.insert(node, destination, weight);
            }
        }
    }


    public Map<UndirectedNode, UndirectedNode> prim(UndirectedNode start) {
        BinaryHeapEdge edgesHeap = new BinaryHeapEdge();
        Map<UndirectedNode, UndirectedNode> parent = new HashMap<>();
        Set<UndirectedNode> inTree = new HashSet<>();

        // Arbre de départ
        inTree.add(start);
        addEdgesToHeap(edgesHeap, start, inTree);

        while (!edgesHeap.isEmpty() && inTree.size() < this.nodes.size()) {
            Triple<UndirectedNode, UndirectedNode, Integer> minEdge = edgesHeap.remove();

            if (!inTree.contains(minEdge.getSecond())) {
                parent.put(minEdge.getSecond(), minEdge.getFirst());
                inTree.add(minEdge.getSecond());

                addEdgesToHeap(edgesHeap, minEdge.getSecond(), inTree);
            }
        }
        return parent;
    }

    public static void main(String[] args) {
        int[][] mat = GraphTools.generateGraphData(10, 20, false, true, false, 100001);
        GraphTools.afficherMatrix(mat);
        AdjacencyListUndirectedGraph al = new AdjacencyListUndirectedGraph(mat);
        System.out.println(al);

        Map<UndirectedNode, UndirectedNode> arbreMin = al.prim(al.getNodes().get(0));

        System.out.println("Prim example");
        for (Map.Entry<UndirectedNode, UndirectedNode> entry : arbreMin.entrySet()) {
            if (entry.getValue() != null) {
                System.out.println(entry.getValue().getLabel() + " - " + entry.getKey().getLabel());
            }
        }
    }
}
