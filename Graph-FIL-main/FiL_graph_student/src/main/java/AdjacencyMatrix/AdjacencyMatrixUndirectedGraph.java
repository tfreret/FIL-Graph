package AdjacencyMatrix;


import GraphAlgorithms.GraphTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import static guru.nidi.graphviz.model.Factory.*;

import AdjacencyList.AdjacencyListUndirectedGraph;

/**
 * This class represents the undirected graphs structured by an adjacency matrix.
 * It is possible to have simple and multiple graph
 */
public class AdjacencyMatrixUndirectedGraph {
	
	//--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

    protected int order;		// Number of vertices
    protected int m = 0;		// Number of edges/arcs
    protected int[][] matrix;	// The adjacency matrix

  
   
	
	//--------------------------------------------------
	// 				Constructors
	//-------------------------------------------------- 
	
	public AdjacencyMatrixUndirectedGraph() {
		this.matrix = new int[0][0];
        this.order = 0;
        this.m = 0;
	}
	
	public AdjacencyMatrixUndirectedGraph(int[][] mat) {
		this.order=mat.length;
		this.matrix = new int[this.order][this.order];
		for(int i = 0; i<this.order; i++){
			for(int j = i; j<this.order; j++){
				this.matrix[i][j] = mat[i][j];
				this.matrix[j][i] = mat[i][j];
				this.m += mat[i][j];
			}
		}	
	}
	
	public AdjacencyMatrixUndirectedGraph(AdjacencyListUndirectedGraph g) {
		this.order = g.getNbNodes(); 				
		this.m = g.getNbEdges(); 				
		this.matrix = g.toAdjacencyMatrix(); 
	}

	//--------------------------------------------------
	// 					Accessors
	//--------------------------------------------------

	/**
     * @return the matrix modeling the graph
     */
    public int[][] getMatrix() {
        return this.matrix;
    }

    /**
     * @return the number of nodes in the graph (referred to as the order of the graph)
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
	 * 
	 * @param x the vertex selected
	 * @return a list of vertices which are the neighbours of x
	 */
	public List<Integer> getNeighbours(int v) {
		List<Integer> l = new ArrayList<>();
		for(int i = 0; i<matrix[v].length; i++){
			if(matrix[v][i]>0){
				l.add(i);
			}
		}
		return l;
	}
	
	// ------------------------------------------------
	// 					Methods 
	// ------------------------------------------------		
	
	/**
     * @return true if the edge is in the graph.
     */
	public boolean isEdge(int x, int y) {
        return matrix[x][y] > 0;
    }
	
	/**
     * removes the edge (x,y) if there exists at least one between these nodes in the graph.
     */
	public void removeEdge(int x, int y) {
		if (isEdge(x, y)) {
			matrix[x][y]--;
			matrix[y][x]--;
			m--;
		}
	}

	/**
     * adds the edge (x,y), we allow the multi-graph.
     */
	public void addEdge(int x, int y) {
		matrix[x][y]++;
		matrix[y][x]++;
		m++;
	}

	
	/**
     * @return the adjacency matrix representation int[][] of the graph.
     */
	public int[][] toAdjacencyMatrix() {
		return this.matrix;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("\nAdjacency Matrix: \n");
		for (int[] ints : this.matrix) {
			for (int anInt : ints) {
				s.append(anInt).append(" ");
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}

	public void graphVis() { //TODO undirected
		MutableGraph graph = mutGraph("graph").setDirected(true);

		for (int i = 0; i < matrix.length; i++) {
			char nodeName = (char) ('A' + i);
			graph.add(mutNode(String.valueOf(nodeName)));
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 1) {
					char nodeNameI = (char) ('A' + i);
					char nodeNameJ = (char) ('A' + j);
					graph.add(mutNode(String.valueOf(nodeNameI)).addLink(mutNode(String.valueOf(nodeNameJ))));
				}
			}
		}

		try {
			Graphviz.fromGraph(graph).width(600).render(Format.PNG).toFile(new java.io.File("./target/result.png"));
			System.out.println("png created");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int[][] mat2 = GraphTools.generateGraphData(10, 35, false, true, false, 100001);
		//GraphTools.afficherMatrix(mat2);
		AdjacencyMatrixUndirectedGraph am = new AdjacencyMatrixUndirectedGraph(mat2);
		System.out.println(am);
		System.out.println("n = "+am.getNbNodes()+ "\nm = "+am.getNbEdges() +"\n");
		
		// Neighbours of vertex 2 :
		System.out.println("Neighbours of vertex 2 : ");
		List<Integer> t2 = am.getNeighbours(2);
		for (Integer integer : t2) {
			System.out.print(integer + ", ");
		}
		
		// We add three edges {3,5} :
		System.out.println("\n\nisEdge(3, 5) ? " + am.isEdge(3, 5));
		for(int i = 0; i<3;i++)
			am.addEdge(3, 5);
		
		System.out.println("\n"+am);
		
		System.out.println("\nAfter removing one edge {3,5} :");
		am.removeEdge(3,5);
		System.out.println(am);

		am.graphVis();
	}
}
