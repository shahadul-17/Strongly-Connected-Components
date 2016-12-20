package strongly.connected.components;

public class Vertex {
	
	public int index, startingTime, finishingTime;
	public static int time;
	public boolean visited = false;
	
	@Override
	public String toString() {
		return "Vertex = " + Main.toCharacter(index) + ", Starting Time = " + startingTime + ", Finishing Time = " + finishingTime;
	}
	
}