package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	private static Grid grid = Grid.getInstance();

	
	
	
	
	
	private HashMap<Integer, Space> spaces;
	
	public Board() {
		initializeSpaces();
	}
	
	public void addStartingPieces() {
		for(Space space : spaces.values()) {
			if (space.id <= LAST_BLACK) {
				space.piece = new Piece(Piece.Color.BLACK);
			}
			if (space.id >= FIRST_RED) {
				space.piece = new Piece(Piece.Color.RED);
			}
		}
	}
	
	}
	
	public Space getSpace(int i) {
		return spaces.get(i);
	}
	
			
				
					
					}
				}
			}
		}
		
	}

}
