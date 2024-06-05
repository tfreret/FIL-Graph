package GraphAlgorithms;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import AdjacencyList.AdjacencyListDirectedGraph;
import Nodes.DirectedNode;

public class GraphToolsList  extends GraphTools {

	//private static int _DEBBUG = 0;
	//private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;


	public GraphToolsList(){
		super();
	}

	public static List<DirectedNode> BFS(AdjacencyListDirectedGraph graph, int start) {
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

	// DFS
	static void explorerSommet(DirectedNode s, Set<DirectedNode> a) {
		debut[s.getLabel()] = cpt;
		cpt++;
		a.add(s);
		System.out.println("Début: " + s.getLabel());

		for (DirectedNode succ : s.getSuccs().keySet()) {
			if (!a.contains(succ)) {
				System.out.println(succ.getLabel());
				explorerSommet(succ, a);
			}
		}
		fin[s.getLabel()] = cpt;
		cpt++;
		order_CC.add(s.getLabel());
		System.out.println("Fin: " + s.getLabel());
	}

	public static void explorerGraphe(AdjacencyListDirectedGraph graph) {
		int nbNodes = graph.getNbNodes();
		debut = new int[nbNodes];
		fin = new int[nbNodes];
		order_CC = new ArrayList<>();
		Set<DirectedNode> visited = new HashSet<>();
		cpt = 0;

		for (DirectedNode node : graph.getNodes()) {
			if (!visited.contains(node)) {
				explorerSommet(node, visited);
			}
		}
	}

	static void explorerSommetBis(DirectedNode s, Set<DirectedNode> a) {
		a.add(s);
		System.out.print(s.getLabel() + " ");
		for (DirectedNode succ : s.getSuccs().keySet()) {
			if (!a.contains(succ)) {
				explorerSommetBis(succ, a);
			}
		}
	}

	public static void explorerGrapheBis(AdjacencyListDirectedGraph graph, List<Integer> ordreFin) {
		Set<DirectedNode> visited = new HashSet<>();
		// inversion order_cc
		Collections.reverse(ordreFin);

		for (int label : ordreFin) {
			DirectedNode node = graph.getNodes().get(label);
			if (!visited.contains(node)) {
				System.out.print("Composante fortement connexe: ");
				explorerSommetBis(node, visited);
				System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100);
		GraphTools.afficherMatrix(Matrix);
		AdjacencyListDirectedGraph al = new AdjacencyListDirectedGraph(Matrix);
		//System.out.println(al);

		// A completer
		System.out.println(BFS(al, 0));
		System.out.println();

		explorerGraphe(al);
		System.out.println();

		System.out.println("Début: " + Arrays.toString(debut));
		System.out.println("Fin: " + Arrays.toString(fin));
		System.out.println("Ordre de fin: " + order_CC);
		System.out.println();

		AdjacencyListDirectedGraph alInversed = al.computeInverse();
		//System.out.println(alInversed);
		explorerGrapheBis(alInversed, order_CC);
	}
}
