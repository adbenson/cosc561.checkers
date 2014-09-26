package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private static Grid grid = Grid.getInstance();

	private Piece[] pieces;
	
	public Board() {
		pieces = new Piece[Grid.USED_SPACES + 1];
	}
	
	public void addStartingPieces() {
		for(Space space : grid.getSpaces()) {
			Piece.Color color = Piece.Color.getColorForSpace(space.id);
			if (color != null) {
				addPiece(space, new Piece(color));
			}
		}
	}
	
	public void addPiece(int id, Piece piece) {
		pieces[id] = piece;
	}
	
	public void addPiece(Space space, Piece piece) {
		addPiece(space.id, piece);
	}
	
	public Piece getPiece(Space space) {
		return pieces[space.id];
	}
	
	
	}
	
	public Space getSpace(int id) {
		return grid.getSpaceById(id);
	}
	
	public boolean isEmpty(Space space) {
		return pieces[space.id] == null;
	}
	
	public List<Space> getLegalMoves(int id) {
		return getLegalMoves(getSpace(id));
	}
	
	public List<Space> getLegalMoves(Space space) {
		Piece piece = getPiece(space);

		List<Space> moves = new ArrayList<Space>();

		if (piece != null) {
			
			for (Direction direction : piece.getDirections()) {
				
				Space candidate = grid.getAdjacent(space, direction);
				
				//Edge spaces won't have some adjacent spaces
				if (candidate != null) {
					
					if (!isEmpty(candidate)) {
						if (piece.isOpponent(getPiece(candidate))) {
							Space target = grid.getAdjacent(candidate, direction);
							if (target != null && isEmpty(target)) {
								//Make the jump!
								moves.add(target);
							}
							//Opponent piece is against a side, can't jump
						}						
						//Friendly piece, do nothing
					}
					else {
						moves.add(candidate);
					}
				}
			}
		}
		
		return moves;
	}
	}

}
