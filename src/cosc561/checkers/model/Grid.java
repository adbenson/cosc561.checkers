package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid implements Printable {
	
	public static final boolean FIRST_SPACE_WHITE = true;
	public static final int SPACES_PER_SIDE = 8;
	public static final int TOTAL_SPACES = SPACES_PER_SIDE * SPACES_PER_SIDE;
	//Every other space is white and won't really exist
	public static final int USED_SPACES = TOTAL_SPACES / 2;
	
	private static Grid instance;
	
	private Space[][] grid;
	
	private List<Space> spacesById;
	
	private Map<Space, Map<Direction, Space>> adjacencies;
	
	private Grid() {
		spacesById = new ArrayList<Space>(USED_SPACES);
		
		grid = new Space[SPACES_PER_SIDE][SPACES_PER_SIDE];
		
		adjacencies = new HashMap<>();
		
		fillGrid();
		
		findAllAdjacencies();
		
	}
	
	private void fillGrid() {
		int spaceId = 1;
		
		for (int row = 0; row < SPACES_PER_SIDE; row++) {
			for (int col = 0; col < SPACES_PER_SIDE; col++) {
				
				//If both coordinates have the same parity, 
				// the space should be blank IF the first space should be white
				if (FIRST_SPACE_WHITE == (row % 2 != col % 2)) {
					Space space = new Space(spaceId, row, col);
					
					//ArrayList is zero-based, so we'll go with that here
					//and shift them as necessary
					spacesById.add(spaceId - 1, space);
					grid[row][col] = space;
					
					spaceId++;
				}
			}
		}
	}
	
	public static Grid getInstance() {
		if (instance == null) {
			instance = new Grid();
		}
		
		return instance;
	}
	
	public Collection<Space> getSpaces() {
		//We return a generic collection because the user
		// could try to recall a space by id from a list,
		// not realizing the difference in indexing.
		return spacesById;
	}
	
	public Space getSpaceById(int id) {
		//Spaces are 1-indexed, ArrayList is 0-indexed
		return spacesById.get(id - 1);
	}
	
	public Space getSpaceByCoordinates(int row, int column) {
		return grid[row][column];
	}
	
	public Map<Direction, Space> getAdjacent(Space space) {
		return adjacencies.get(space);
	}
	
	public Space getAdjacent(Space space, Direction direction) {
		return adjacencies.get(space).get(direction);
	}
	
	private void findAllAdjacencies() {
		for (Space space : spacesById) {
			adjacencies.put(space, findAdjacencies(space));
		}
	}
	
	public Map<Direction, Space> findAdjacencies(Space space) {
		Map<Direction, Space> adjacent = new HashMap<>();
		Space that;
		
		if (space.row > 0 && space.column > 0) {
			that = grid[space.row - 1][space.column - 1];
			adjacent.put(Direction.NW, that);
		}
		if (space.row > 0 && space.column < SPACES_PER_SIDE - 1) {
			that = grid[space.row - 1][space.column + 1];
			adjacent.put(Direction.NE, that);
		}
		if (space.row < SPACES_PER_SIDE - 1 && space.column < SPACES_PER_SIDE - 1) {
			that = grid[space.row + 1][space.column + 1];
			adjacent.put(Direction.SE, that);
		}
		if (space.row < SPACES_PER_SIDE - 1 && space.column > 0) {
			that = grid[space.row + 1][space.column - 1];
			adjacent.put(Direction.SW, that);
		}
		
		return adjacent;
	}
	
	public String toString() {
		return toString(this);
	}
	
	public String toString(Printable board) {
		StringBuilder string = new StringBuilder();
		string.append("   ");
		for (int col=0; col<SPACES_PER_SIDE; col++) {
			string.append(col + " ");
		}
		string.append("\n");
		
		for (int row=0; row<SPACES_PER_SIDE; row++) {
			
			string.append(row + " ");
			
			for (int col=0; col<SPACES_PER_SIDE; col++) {
				Space space = grid[row][col];
				
				if (space == null) {
					string.append("[]");
				}
				else {
					string.append(board.toString(space));
				}
			}
			string.append("\n");
		}
		
		return string.toString();
	}

	@Override
	public String toString(Space space) {
		return ((space.id < 10)? " " : "") + space.id;
	}

}
