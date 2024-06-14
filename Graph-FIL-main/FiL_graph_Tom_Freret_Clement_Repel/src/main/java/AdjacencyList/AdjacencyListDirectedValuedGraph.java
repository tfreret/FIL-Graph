package AdjacencyList;

import java.util.*;

import GraphAlgorithms.BinaryHeapEdge;
import GraphAlgorithms.GraphTools;
import Nodes.DirectedNode;
import Collection.Triple;


public class AdjacencyListDirectedValuedGraph extends AdjacencyListDirectedGraph {

	//--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------

	public AdjacencyListDirectedValuedGraph(int[][] matrixVal) {
    	super();
    	this.order = matrixVal.length;
        this.nodes = new ArrayList<DirectedNode>();
        for (int i = 0; i < this.order; i++) {
            this.nodes.add(i, this.makeNode(i));
        }
        for (DirectedNode n : this.getNodes()) {
            for (int j = 0; j < matrixVal[n.getLabel()].length; j++) {
            	DirectedNode nn = this.getNodes().get(j);
                if (matrixVal[n.getLabel()][j] != 0) {
                    n.getSuccs().put(nn,matrixVal[n.getLabel()][j]);
                    nn.getPreds().put(n,matrixVal[n.getLabel()][j]);
                    this.m++;
                }
            }
        }            	
    }

    // ------------------------------------------
    // 				Accessors
    // ------------------------------------------


    /**
     * Adds the arc (from,to) with cost  if it is not already present in the graph
     */
    public void addArc(DirectedNode from, DirectedNode to, int cost) {
        if (!from.getSuccs().containsKey(to)) {
            from.getSuccs().put(to,cost);
            to.getPreds().put(from,cost);
            this.m++;
        }
    }

    /**
     * Removes the arc (from,to) if it is present in the graph
     */
    public void removeArc(DirectedNode from, DirectedNode to) {
        if (from.getSuccs().containsKey(to)) {
            from.getSuccs().remove(to);
            to.getPreds().remove(from);
            this.m--;
        }
    }

    /**
     * Returns the cost of the arc (from,to) if it is present in the graph
     */
    public int getCost(DirectedNode from, DirectedNode to) {
        if (from.getSuccs().containsKey(to)) {
            return from.getSuccs().get(to);
        }
        return Integer.MAX_VALUE / 2;
    }

    /**
     * Modifies the cost of the arc (from,to) if it is present in the graph
     */
    public void setCost(DirectedNode from, DirectedNode to, int cost) {
        if (from.getSuccs().containsKey(to)) {
            from.getSuccs().put(to,cost);
            to.getPreds().put(from,cost);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(DirectedNode n : nodes){
            s.append("successors of ").append(n).append(" : ");
            for(DirectedNode sn : n.getSuccs().keySet()){
            	s.append("(").append(sn).append(",").append(n.getSuccs().get(sn)).append(")  ");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public boolean checkIfAllAreTrue(boolean[] mark) {
        for (boolean b : mark) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public Map<DirectedNode, Integer> dijkstra(AdjacencyListDirectedValuedGraph g, DirectedNode start) {
        int n = g.getNodes().size();
        boolean[] mark = new boolean[n];
        int[] val = new int[n];
        DirectedNode[] pred = new DirectedNode[n];

        for (int i = 0; i < n; i++) {
            mark[i] = false;
            val[i] = Integer.MAX_VALUE / 2;
            pred[i] = null;
        }

        int startLabel = start.getLabel();
        val[startLabel] = 0;
        pred[startLabel] = start;

        while (!checkIfAllAreTrue(mark)) {
            int x = 0;
            int min = Integer.MAX_VALUE / 2;

            for (int y = 0; y < n - 1; y++) {
                if (!mark[y] && val[y] < min) {
                    x = y;
                    min = val[y];
                }
            }

            if (min < Integer.MAX_VALUE / 2) {
                mark[x] = true;
                for (int y = 0; y < n - 1; y++) {
                    if (!mark[y] && val[x] + g.getCost(g.getNodes().get(x), g.getNodes().get(y)) < val[y]) {
                        val[y] = val[x] + g.getCost(g.getNodes().get(x), g.getNodes().get(y));
                        pred[y] = g.getNodes().get(x);
                    }
                }
            } else {
                break;
            }
        }

        Map<DirectedNode, Integer> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            result.put(g.getNodes().get(i), val[i]);
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] matrix = GraphTools.generateGraphData(10, 20, false, false, false, 100001);
        int[][] matrixValued = GraphTools.generateValuedGraphData(10, false, false, true, false, 100001);
        GraphTools.afficherMatrix(matrix);
        GraphTools.afficherMatrix(matrixValued);
        AdjacencyListDirectedValuedGraph al = new AdjacencyListDirectedValuedGraph(matrixValued);
        System.out.println(al);


        Map<DirectedNode, Integer> result = al.dijkstra(al, al.getNodes().get(0));

        System.out.println("Dijkstra example");
        for (Map.Entry<DirectedNode, Integer> entry : result.entrySet()) {
            System.out.println(entry.getKey().getLabel() + "\t      " + entry.getValue());
        }
    }
}
