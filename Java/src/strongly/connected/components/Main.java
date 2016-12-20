package strongly.connected.components;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
	
	private static int index;									// current index of sorted vertices...
	private static final int SIZE = 26;							// for pre-allocation of memory for 26 alphabets...
	private static int[][] matrix = new int[SIZE][SIZE];		// advantage: easy to implement, can check for an edge in constant time...
																// disadvantage: not space efficient, no quick way to determine the vertices adjacent from another vertex...
	
	private static String stronglyConnectedComponents;
	private static Vertex[] vertices = new Vertex[SIZE], sortedVertices;
	
	public static void main(String[] args) {
		Main main = new Main();
		
		try {
			main.loadGraph("input.txt");
		}
		catch (Exception exc) {
			exc.printStackTrace();
			
			return;
		}
		
		for (Vertex vertex : vertices) {
			if (vertex != null && !vertex.visited) {
				main.depthFirstSearch(vertex, vertices);
			}
		}
		
		main.topologicallySortVertices(sortedVertices);
		main.transpose(matrix);
		
		index = Vertex.time = 0;
		
		for (Vertex vertex : sortedVertices) {		// re-initializing vertices...
			vertex.visited = false;
		}
		
		for (Vertex vertex : sortedVertices) {
			if (!vertex.visited) {
				stronglyConnectedComponents = "";
				
				main.depthFirstSearch(vertex, sortedVertices);
				System.out.println(stronglyConnectedComponents);
			}
		}
	}
	
	private void loadGraph(String fileName) throws Exception {
		int[] indices = new int[2];
		
		String text;
		String[] substrings;
		BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"));
		
		while ((text = bufferedReader.readLine()) != null) {
			if (sortedVertices == null) {
				sortedVertices = new Vertex[Integer.parseInt(text)];
			}
			else {
				substrings = text.split("\\s+");		// "\\s+" is the regular expression for space...
				
				for (int i = 0; i < substrings.length; i++) {
					indices[i] = toIndex(substrings[i].charAt(0));
					
					if (vertices[indices[i]] == null) {
						vertices[indices[i]] = new Vertex();
						vertices[indices[i]].index = indices[i];
					}
				}
				
				matrix[indices[0]][indices[1]] = 1;		// for directed graph...
														// matrix[indices[0]][indices[1]] = matrix[indices[1]][indices[0]] = 1;		// for undirected graph...
			}
		}
		
		bufferedReader.close();
	}
	
	private void topologicallySortVertices(Vertex[] vertices) {		// using insertion sort...
		int i, j;
		Vertex key;
		
		for (i = 1; i < vertices.length; i++) {
			key = vertices[i];
			j = i - 1;
			
			while ((j >= 0) && (vertices[j].finishingTime < key.finishingTime)) {
				vertices[j + 1] = vertices[j];
				j = j - 1;
			}
			
			vertices[j + 1] = key;
		}
	}
	
	private void transpose(int[][] matrix) {
		int temp;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < i; j++) {
				temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
	}
	
	private void depthFirstSearch(Vertex element, Vertex[] vertices) {
		Vertex.time++;
		element.startingTime = Vertex.time;
		element.visited = true;
		
		for (Vertex vertex : vertices) {
			if (vertex != null && matrix[element.index][vertex.index] == 1 && !vertex.visited) {
				depthFirstSearch(vertex, vertices);
			}
		}
		
		Vertex.time++;
		element.finishingTime = Vertex.time;
		
		if (vertices != sortedVertices) {
			sortedVertices[index] = element;
			index++;
		}
		else {
			if (stronglyConnectedComponents.length() != 0) {
				stronglyConnectedComponents = ", " + stronglyConnectedComponents;
			}
			
			stronglyConnectedComponents = toCharacter(element.index) + stronglyConnectedComponents;
		}
	}
	
	private static int toIndex(char character) {
		return Character.toLowerCase(character) - 97;
	}
	
	public static char toCharacter(int index) {
		return (char) (index + 97);
	}
	
}