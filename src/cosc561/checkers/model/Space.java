package cosc561.checkers.model;


public class Space {
	
	public final int id;
	
	public final int row;
	public final int column;
	
	public Space(int id, int row, int column) {
		this.id = id;
		this.row = row;
		this.column = column;
	}

	public String toString() {
		return id+"("+row+","+column+")";
	}
}