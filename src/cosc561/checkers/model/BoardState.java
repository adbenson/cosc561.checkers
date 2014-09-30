package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.model.PlayerTurn.Change;

public class BoardState {

	private static Grid grid = Grid.getInstance();
	
	private final BoardState previous;
	private final Piece[] pieces;
	private final PlayerTurn turn;
	
	private boolean played;
	
	public BoardState() {
		pieces = new Piece[Grid.USED_SPACES + 1];
		previous = null;
		turn = new PlayerTurn();
		played = true;
	}
	
	public BoardState(BoardState board, PlayerTurn turn) {
		this.pieces = board.pieces.clone();
		this.previous = board;
		this.played = false;
		this.turn = turn;
		
		apply(turn);
	}
	
	private void apply(PlayerTurn turn) {
		for (Change change : turn.getMoves()) {
			change.applyTo(this);
		}
	}

	public BoardState addStartingPieces() {
		PlayerTurn turn = new PlayerTurn();
		
		BoardState newBoard = new BoardState(this, turn);
		
		for(Space space : grid.getSpaces()) {
			Color color = Color.getColorForSpace(space.id);
			if (color != null) {
				newBoard.turn.add(new Piece(color), space);
			}
		}
		
		newBoard.apply(turn);
		
		return newBoard;
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
	
	public Piece[] getPieces() {
		return pieces;
	}
	
	public void removePiece(Space space) {
		removePiece(space.id);
	}
	
	public void removePiece(int id) {
		pieces[id] = null;
		
		//Check for end game
		gameOver();
	}
	
	public void movePiece(Space from, Space to) {
		pieces[to.id] = pieces[from.id];
		pieces[from.id] = null;
	}

	public void movePiece(int from, int to) {
		pieces[to] = pieces[from];
		pieces[from] = null;
	}
	
	public boolean gameOver() {
		Color winningColor = null;
		boolean gameOver = true;
		for (int p = 0; p < pieces.length; p++) {
			Piece piece = pieces[p];
			if (piece != null) {
				if (winningColor == null) { 
					winningColor = piece.color;
				}
				// Both colors must be on the board for the game to be still going
				if (winningColor != piece.color) {
					gameOver = false;
				}
			}
		}
		return gameOver;
	}
	
	public void kingPiece(Space space) {
		kingPiece(space.id);
	}
	
	public void kingPiece(int id) {
		pieces[id].setKing();
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
		return getLegalMoves(space, null, null);
	}

	private List<Space> getLegalMoves(Space space, Piece piece, Space jumpedSpace) {
		boolean hasJumped = (jumpedSpace != null);
		piece = (piece == null) ? getPiece(space) : piece;
	
		List<Space> moves = new ArrayList<Space>();
		List<Space> jumps = new ArrayList<Space>();

		if (piece != null) {
			for (Direction direction : piece.getDirections()) {
				Space adjacent = grid.getAdjacent(space, direction);
				if (adjacent != jumpedSpace) {
					// Edge spaces won't have some adjacent spaces
					if (adjacent != null) {
						if (!isEmpty(adjacent)) {
							if (piece.isOpponent(getPiece(adjacent))) {
								Space landingSpace = grid.getAdjacent(adjacent, direction);
								if (landingSpace != null && isEmpty(landingSpace)) {
									// Start the recursive jump search
									jumps.addAll(getLegalMoves(landingSpace, piece, adjacent));
								}
								// Opponent piece is against a side, can't jump
							}
							// Friendly piece, do nothing
						} else {
							moves.add(adjacent);
						}
					}
					// No adjacent piece so we can't do a jump
				}
				// Don't allow a King to jump back to his original spot.
			}
		}
		
		
		if (hasJumped) { 
			if (jumps.isEmpty()) {
				// A jump has been made. No more are available.
				jumps.add(space);
			}
		} else {
			if (jumps.isEmpty()) {
				// If no jump was ever available, then move normally
				jumps = moves;
			}
		}
		return jumps;
	}
	
	private List<BoardState> getPossibleStates(int id) {
		List<BoardState> states = new ArrayList<>();
		
		return states;
	}

	public List<BoardState> getAllPossibleStates(Color color) {
		List<BoardState> states = new ArrayList<>();
		
		for (int id = 1; id < pieces.length; id++) {
			if (pieces[id].color == color) {
				states.addAll(getPossibleStates(id));
			}
		}
		
		return states;
	}

	public PlayerTurn getOriginatingMove() {
		BoardState state = this;
		
		//Find the state after the latest played state
		while (state.previous != null && !state.previous.played) {
			state = state.previous;
		}
		
		return state.turn;
	}

	public void setPlayed() {
		played = true;
	}

	public String toString() {
		return grid.toString(this);
	}

}
