package final_project;

/**
 * Basically just a 4d vector. Represents 2 points, one the starting point and one the ending
 * 
 * @author Zachary
 *
 */
public class Move {
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	
	public Move(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public int getX1() {
		return x1;
	}
	
	public int getX2() {
		return x2;
	}
	
	public int getY1() {
		return y1;
	}
	
	public int getY2() {
		return y2;
	}
	
	public String toString() {
		return "" + x1 + "," + y1 + "," + x2 + "," + y2;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Move) {
			Move other = (Move) o;
			return this.x1 == other.x1 && this.x2 == other.x2 && this.y1 == other.y1 && this.y2 == other.y2;
	               
		}    
		return false;    
	}
	
	@Override
	public int hashCode() {
		return x1 * 31 + x2 * 37 + y1 * 53 + y2 * 47;
	}
}
