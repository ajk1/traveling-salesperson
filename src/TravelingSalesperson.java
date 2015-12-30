import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import graphLocations.*;

/**
 * 
 */

/**
 * Reads from an excel file containing crime data in Pittsburgh.
 * The user enters row numbers to read from and the program outputs both
 * a polynomial-time estimate for shortest cycle through all locations,
 * and an exhaustive search for the optimal path (note that problem is 
 * NP-hard and will require a lot of time for more than 8 or 9 locations).
 * The .kml file is printed which can then be entered into Google Earth.
 * @author Alex
 */
public class TravelingSalesperson {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		File file = new File("CrimeLatLonXY1990.csv");
		System.out.println("Enter start index");
		int start = sc.nextInt();
		while (start < 0) { //check start validity
			System.out.println("Please enter a nonnegative integer.");
			System.out.println("Enter start index");
			start = sc.nextInt();
		}
		System.out.println("Enter end index");
		int end = sc.nextInt();
		while (end < start) { //check end validity
			System.out.println("Please enter an integer larger than start index.");
			System.out.println("Enter end index");
			end = sc.nextInt();
		}
		System.out.println("Crime records processed:");
		int size = end - start + 1;
		WeightedGraph graph = new WeightedGraph(size);
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			int i=-1;
			for (i=-1; i<start; i++){ //read lines until start index
				br.readLine();
			}
			while (i<=end && (line = br.readLine())!=null) {
				System.out.println((i-start) +": "+ line);
				graph.addVertex(line, i-start); //add vertices until end index
				i++;
			}
			br.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		for(int i=0; i<size; i++){ //compute the distance between every vertex pair
			for(int j=0; j<size; j++){
				graph.setDist(i, j);
			}
		}
		graph.printMatrix();
		
		//calculate and print minimum spanning tree
		int root = 0;
		Edge[] MST = graph.getMST(root);
		System.out.println("\nMinimum Spanning Tree (edge set with distance):");
		double spanLength=0;
		for (Edge e : MST) {
			System.out.println(e);
			spanLength+= e.getDistance();
		}
		System.out.println("Spanning Length: "+ spanLength);
		
		//calculate and print near-optimal hamiltonian cycle
		int[] approxCycle = graph.minCycleApprox();
		System.out.print("\nHamiltonian Cycle (not necessarily optimal): \n" +
				approxCycle[0]);
		for (int i=1; i<size+1; i++) {
			System.out.print(", " + approxCycle[i]);
		}
		double cycleLength = 0;
		for (int i=0; i<size; i++) {
			cycleLength += graph.getDist(approxCycle[i], approxCycle[i+1]);
		}
		System.out.println("\nCycle Length: "+ cycleLength +" miles");
		
		//calculate and print optimal hamiltonian cycle
		int[] exactCycle = graph.minCycleExact();
		System.out.print("\nHamiltonian Cycle (minimal length): \n" +
				exactCycle[0]);
		for (int i=1; i<size+1; i++) {
			System.out.print(", " + exactCycle[i]);
		}
		cycleLength = 0;
		for (int i=0; i<size; i++) {
			cycleLength += graph.getDist(exactCycle[i], exactCycle[i+1]);
		}
		System.out.println("\nCycle Length: "+ cycleLength +" miles");
		
		//generate KML file for Google Earth
		System.out.println("Graph cycle written to file: " +
				graph.mapToGoogleEarth(approxCycle, exactCycle));
		
		sc.close();
	}

}
