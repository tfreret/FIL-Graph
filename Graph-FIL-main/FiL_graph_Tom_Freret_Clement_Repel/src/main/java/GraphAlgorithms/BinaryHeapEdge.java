package GraphAlgorithms;

import java.util.*;

import AdjacencyList.AdjacencyListUndirectedGraph;
import AdjacencyList.AdjacencyListUndirectedValuedGraph;
import Collection.Triple;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class BinaryHeapEdge {

	/**
	 * A list structure for a faster management of the heap by indexing
	 *
	 */
	private  List<Triple<UndirectedNode,UndirectedNode,Integer>> binh;

	public BinaryHeapEdge() {
		this.binh = new ArrayList<>();
	}

	public boolean isEmpty() {
		return binh.isEmpty();
	}

	/**
	 * Insert a new edge in the binary heap
	 *
	 * @param from one node of the edge
	 * @param to one node of the edge
	 * @param val the edge weight
	 */
	public void insert(UndirectedNode from, UndirectedNode to, int val) {
		binh.add(new Triple<>(from, to, val));
		int current = binh.size() - 1;

		while (current > 0) {
			int parent = (current - 1) / 2;
			if (binh.get(current).getThird() >= binh.get(parent).getThird()) {
				break;
			}
			swap(current, parent);
			current = parent;
		}
	}


	/**
	 * Removes the root edge in the binary heap, and swap the edges to keep a valid binary heap
	 *
	 * @return the edge with the minimal value (root of the binary heap)
	 *
	 */
	public Triple<UndirectedNode,UndirectedNode,Integer> remove() {
		if (isEmpty()) {
			throw new IllegalStateException("Heap is empty");
		}

		Triple<UndirectedNode, UndirectedNode, Integer> root = binh.get(0);
		int lastIndex = binh.size() - 1;
		binh.set(0, binh.get(lastIndex));
		binh.remove(lastIndex);

		int current = 0;
		while (!isLeaf(current)) {
			int bestChildPos = getBestChildPos(current);
			if (bestChildPos == Integer.MAX_VALUE || binh.get(current).getThird() <= binh.get(bestChildPos).getThird()) {
				break;
			}
			swap(current, bestChildPos);
			current = bestChildPos;
		}

		return root;

	}


	/**
	 * From an edge indexed by src, find the child having the least weight and return it
	 *
	 * @param src an index of the list edges
	 * @return the index of the child edge with the least weight
	 */
	private int getBestChildPos(int src) {
		int left = 2 * src + 1;
		int right = 2 * src + 2;
		int lastIndex = binh.size() - 1;

		if (left > lastIndex) {
			return Integer.MAX_VALUE; // src is a leaf
		}

		if (right > lastIndex) {
			return left; // Only left child exists
		}

		return (binh.get(left).getThird() < binh.get(right).getThird()) ? left : right;
	}

	private boolean isLeaf(int src) {
		int lastIndex = binh.size() - 1;
		return (2 * src + 1 > lastIndex);
	}


	/**
	 * Swap two edges in the binary heap
	 *
	 * @param father an index of the list edges
	 * @param child an index of the list edges
	 */
	private void swap(int father, int child) {
		Triple<UndirectedNode,UndirectedNode,Integer> temp = new Triple<>(binh.get(father).getFirst(), binh.get(father).getSecond(), binh.get(father).getThird());
		binh.get(father).setTriple(binh.get(child));
		binh.get(child).setTriple(temp);
	}


	/**
	 * Create the string of the visualisation of a binary heap
	 *
	 * @return the string of the binary heap
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Triple<UndirectedNode,UndirectedNode,Integer> no: binh) {
			s.append(no).append(", ");
		}
		return s.toString();
	}


	private String space(int x) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<x; i++) {
			res.append(" ");
		}
		return res.toString();
	}

	/**
	 * Print a nice visualisation of the binary heap as a hierarchy tree
	 *
	 */
	public void lovelyPrinting(){
		int nodeWidth = this.binh.get(0).toString().length();
		int depth = 1+(int)(Math.log(this.binh.size())/Math.log(2));
		int index=0;

		for(int h = 1; h<=depth; h++){
			int left = ((int) (Math.pow(2, depth-h-1)))*nodeWidth - nodeWidth/2;
			int between = ((int) (Math.pow(2, depth-h))-1)*nodeWidth;
			int i =0;
			System.out.print(space(left));
			while(i<Math.pow(2, h-1) && index<binh.size()){
				System.out.print(binh.get(index) + space(between));
				index++;
				i++;
			}
			System.out.println("");
		}
		System.out.println("");
	}

	// ------------------------------------
	// 					TEST
	// ------------------------------------

	/**
	 * Recursive test to check the validity of the binary heap
	 *
	 * @return a boolean equal to True if the binary tree is compact from left to right
	 *
	 */
	private boolean test() {
		return this.isEmpty() || testRec(0);
	}

	private boolean testRec(int root) {
		int lastIndex = binh.size()-1;
		if (isLeaf(root)) {
			return true;
		} else {
			int left = 2 * root + 1;
			int right = 2 * root + 2;
			if (right >= lastIndex) {
				return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left);
			} else {
				return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left)
						&& binh.get(right).getThird() >= binh.get(root).getThird() && testRec(right);
			}
		}
	}

	public static void main(String[] args) {
		BinaryHeapEdge jarjarBin = new BinaryHeapEdge();
		System.out.println(jarjarBin.isEmpty() + "\n");
		int k = 10;
		int m = k;
		int min = 2;
		int max = 20;
		while (k > 0) {
			int rand = min + (int) (Math.random() * ((max - min) + 1));
			jarjarBin.insert(new UndirectedNode(k), new UndirectedNode(k + 30), rand);
			k--;
		}
		jarjarBin.lovelyPrinting();
		jarjarBin.remove();
		jarjarBin.lovelyPrinting();
	}
}

