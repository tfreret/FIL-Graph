package GraphAlgorithms;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import AdjacencyList.AdjacencyListDirectedGraph;
import Nodes.DirectedNode;

public class GraphToolsList  extends GraphTools {

	private static int _DEBBUG = 0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public GraphToolsList(){
		super();
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------



	// ------------------------------------------
	// 				Methods
	// ------------------------------------------

	public static List<DirectedNode> bfs(AdjacencyListDirectedGraph graph, int start) {
		// Création de la file, des sommets visités et de l'ordre de visite
		Queue<DirectedNode> fifo = new ArrayBlockingQueue<>(graph.getNbNodes());
		boolean[] visited = new boolean[graph.getNbNodes()];
		List<DirectedNode> visitedOrder = new ArrayList<>();
		DirectedNode node = graph.getNodes().get(start);
		fifo.add(node);

		// Ajoute sommet départ dans la file et le marque comme visité
		visited[start] = true;
		visitedOrder.add(node);

		// Parcours en largeur, ajoute les successeurs non visités dans la file
		while (!fifo.isEmpty()) {
			node = fifo.poll();
			System.out.println("Visited node: " + node.getLabel());
			for (DirectedNode succ : node.getSuccs().keySet()) {
				int succLabel = succ.getLabel();
				if (!visited[succLabel]) {
					fifo.add(succ);
					visited[succLabel] = true;
					visitedOrder.add(succ);
				}
			}
		}

		return visitedOrder;
	}


	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100);
		GraphTools.afficherMatrix(Matrix);
		AdjacencyListDirectedGraph al = new AdjacencyListDirectedGraph(Matrix);
		System.out.println(al);

		// A completer
		//System.out.println(bfs(al, 0));

		//System.out.println(dfs(al, 0));
	}
}
