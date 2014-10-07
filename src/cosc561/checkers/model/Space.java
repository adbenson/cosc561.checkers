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
	
	public boolean canKing(PlayerColor color) {
		return isEndRow() && !onHomeSide(color);
	}

	public boolean inHomeRow(PlayerColor color) {
		return isEndRow() && onHomeSide(color);
	}
	
	public boolean onHomeSide(PlayerColor color) {
		return PlayerColor.getColorForStartingSpace(id) == color;
	}
	
	public boolean inNeutralZone() {
		return PlayerColor.getColorForStartingSpace(id) == null;
	}
	
	public boolean isEndRow() {
		return row == 0 || row == Grid.SPACES_PER_SIDE - 1;
	}
}