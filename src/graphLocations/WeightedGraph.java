package graphLocations;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * weighted graph holds a list of vertices and an adjacency matrix of their distances
 * @author Alex
 *
 */
public class WeightedGraph {
	int size;
	Vertex[] vertices;
	double[][] distMatrix; //adjacency matrix that holds distance values
	Edge[] MST; //edge set for minimum spanning tree
	
	/**
	 * Creates an empty graph given an initial number of vertices
	 * @param size
	 */
	public WeightedGraph(int size) {
		this.size = size;
		distMatrix = new double[size][size];
		vertices = new Vertex[size];
		MST = new Edge[size-1]; //tree always has v-1 edges
	}
	
	public void addVertex(String line, int index) {
		vertices[index] = new Vertex(line);
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	/**
	 * calculates and sets distance between vertex a and b using their x and y values
	 * @param a
	 * @param b
	 */
	public void setDist(int a, int b) {
		double xDistSqrd = Math.pow(vertices[a].getX()-vertices[b].getX(), 2);
		double yDistSqrd = Math.pow(vertices[a].getY()-vertices[b].getY(), 2);
		double diagDist = Math.pow(xDistSqrd+yDistSqrd, 0.5);
		diagDist = diagDist/5280; //convert feet to miles
		distMatrix[a][b] = diagDist;
	}
	
	/**
	 * gets distance between vertex indices a and b
	 * @param a
	 * @param b
	 * @return
	 */
	public double getDist(int a, int b) {
		if(a<size && b<size)
			return distMatrix[a][b];
		else 
			return 0.0d;
	}

	/**
	 * print's the graph's adjacency matrix
	 */
	public void printMatrix() {
		System.out.println("\nAdjacency (Distance) Matrix:");
		for (int i=0; i<size; i++) {
			System.out.print("\t"+ i +":");
		}
		for (int i=0; i<size; i++) {
			System.out.print("\n"+ i +":\t");
			for (int j=0; j<size; j++) {
				if (i==j) System.out.print("--" +"\t");
				else System.out.print(Math.round(distMatrix[i][j]*10000.0)/10000.0 +"\t");
			}
		}
		System.out.println();
	}
	
	/**
	 * Computes the minimum spanning tree using Prim's algorithm
	 * @param root - vertex to start MST from
	 * @return array of Edge objects
	 */
	public Edge[] getMST(int root) {
		if (root<0 || root>=size) return null;
		Edge[] MST = new Edge[size-1]; //a tree always has V-1 edges
		boolean[] solved = new boolean[size]; //parallels vertices, 
		//holds whether vertex has been added to MST yet
		for (int i=0; i<size; i++) { //initialize all vertices except root to false
			solved[i] = false;
		}
		solved[root] = true;
		
		for (int iter=0; iter<size-1; iter++) { //adds new edge to MST with each iteration
			double minDist = Double.MAX_VALUE;
			int[] minEdge = new int[2]; //indices 0 and 1 hold a solved and unsolved vertex respectively
			for (int i=0; i<size; i++) { //loop through all solved vertices
				if (solved[i]) { //ensure that i is a solved vertex
					for (int j=0; j<size; j++) { //loop through all unsolved vertices
						if (!solved[j]) { //ensure that j is an unsolved vertex
							//keep track of closest unsolved vertex to set of solved vertices
							if (distMatrix[i][j] < minDist) {
								minDist = distMatrix[i][j]; 
								minEdge[0] = i; //i is solved vertex
								minEdge[1] = j; //j is unsolved vertex
							}
						}
					}
				}
			}
			solved[minEdge[1]] = true; //make the unsolved vertex solved
			MST[iter] = new Edge(minEdge[0], minEdge[1], minDist);
		}
		this.MST = MST;
		return MST;
	}
	
	/**
	 * Computes a near-optimal hamiltonian walk using an 
	 * iterative pre-order traversal of the minimum spanning tree
	 * @return array of vertex indices
	 */
	public int[] minCycleApprox() {
		//1. add root to cycle and to stack
		//2. look at top of stack: does it have a child not already in cycle?
		//		if so then add the child to cycle and to stack
		//		if not then pop the parent from stack
		//3. repeat 2 until stack is empty
		if (MST[0] == null) this.getMST(0);
		Stack cycle = new Stack(); //list of vertices in order of cycle
		Stack toTraverse = new Stack(); //vertices that still require depth-first traversal
		cycle.push(MST[0].getP()); //add root of MST to cycle
		toTraverse.push(MST[0].getP());
		while (!toTraverse.isEmpty()) {
			int parent = toTraverse.peek();
			int child = -1;
			//check if parent has a child that's not already in the cycle
			for (int i=0; i<MST.length; i++) {
				if (MST[i].getP() == parent && !cycle.contains(MST[i].getC())) {
					child = MST[i].getC();
					cycle.push(child);
					toTraverse.push(child);
					break; //break the loop so the child can be traversed next
				}
			}
			if (child == -1) { //if parent had no other children, pop it from stack
				toTraverse.pop();
			}
		}
		cycle.push(MST[0].getP());
		return cycle.toArrayReversed();
	}
	
	/**
	 * Computes an optimal hamiltonian walk by
	 * recursively checking every permutation of vertices
	 * @return array of vertex indices
	 */
	public int[] minCycleExact() {
		ArrayList<Integer> vIndices = new ArrayList<Integer>();
		for (int i=size-1; i>=0; i--) {
			vIndices.add(new Integer(i));
		}
		List<List<Integer>> permutations = permute(vIndices);
		double minDist = Double.MAX_VALUE;
		int minIndex = 0;
		for (List<Integer> perm : permutations) {
			double distance = 0;
			perm.add(perm.get(0));
			for (int i=0; i<size; i++) {
				distance += distMatrix[perm.get(i)][perm.get(i+1)];
			}
			if (distance < minDist) {
				minDist = distance;
				minIndex = permutations.indexOf(perm);
			}
		}
		int[] cycle = new int[size+1];
		for (int i=0; i<size+1; i++) {
			cycle[i] = permutations.get(minIndex).get(i);
		}
		return cycle;
	}
	
	/**
	 * recursively permutates a list of vertex indices and returns all possible orders
	 * @param original
	 * @return list of permutation lists
	 */
	public List<List<Integer>> permute(List<Integer> original) {
		if (original.size() == 0) { 
			 List<List<Integer>> result = new ArrayList<List<Integer>>();
			 result.add(new ArrayList<Integer>());
			 return result;
		 }
		 Integer firstElement = original.remove(0);
		 List<List<Integer>> returnValue = new ArrayList<List<Integer>>();
		 List<List<Integer>> permutations = permute(original);
		 for (List<Integer> smallerPermutated : permutations) {
			 for (int index=0; index <= smallerPermutated.size(); index++) {
		         List<Integer> temp = new ArrayList<Integer>(smallerPermutated);
		         temp.add(index, firstElement);
		         returnValue.add(temp);
			 }
		 }
		 return returnValue;
     }
	
	/**
	 * produces a KML file that can be loaded into Google Earth
	 * to display two paths about the graph's vertices
	 * @param path
	 * @return name of file
	 */
	public String mapToGoogleEarth(int[] path1, int[] path2) {
		try {
			PrintWriter pw = new PrintWriter("PGHCrimes.kml", "UTF-8");
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			pw.println("<kml xmlns=\"http://earth.google.com/kml/2.2\">");
			pw.println("<Document>");
			pw.println("<name>Pittsburgh TSP</name><description>TSP on Crime</description><Style id=\"style6\">");
			pw.println("<LineStyle>");
			pw.println("<color>73FF0000</color>");
			pw.println("<width>5</width>");
			pw.println("</LineStyle>");
			pw.println("</Style>");
			pw.println("<Style id=\"style5\">");
			pw.println("<LineStyle>");
			pw.println("<color>507800F0</color>");
			pw.println("<width>5</width>");
			pw.println("</LineStyle>");
			pw.println("</Style>");
			pw.println("<Placemark>");
			pw.println("<name>TSP Path</name>");
			pw.println("<description>TSP Path</description>");
			pw.println("<styleUrl>#style6</styleUrl>");
			pw.println("<LineString>");
			pw.println("<tessellate>1</tessellate>");
			pw.println("<coordinates>");
			for (int i : path1) {
				pw.println(vertices[i].getLon() +","+ vertices[i].getLat() +",0.000000");
			}
			pw.println("</coordinates>");
			pw.println("</LineString>");
			pw.println("</Placemark>");
			pw.println("<Placemark>");
			pw.println("<name>Optimal Path</name>");
			pw.println("<description>Optimal Path</description>");
			pw.println("<styleUrl>#style5</styleUrl>");
			pw.println("<LineString>");
			pw.println("<tessellate>1</tessellate>");
			pw.println("<coordinates>");
			for (int i : path2) {
				pw.println((vertices[i].getLon()+.0005) +","+ (vertices[i].getLat()+.0005)
						+",0.000000");
			}
			pw.println("</coordinates>");
			pw.println("</LineString>");
			pw.println("</Placemark>");
			pw.println("</Document>");
			pw.println("</kml>");
			pw.close();
			return "PGHCrimes.kml";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
