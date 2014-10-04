package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerTurn.Change;

public class BoardState {
	
	private static long lastId = 1;

	private static Grid grid = Grid.getInstance();
	
	private final BoardState previous;
	private final PlayerTurn turn;
	private final long uid;
	
	private boolean played;
	
	private PieceMap pieces;
	
	public BoardState(PlayerColor firstPlayer) {
		pieces = new PieceMap();
		previous = null;
		turn = new PlayerTurn(firstPlayer);
		played = true;
		
		uid = lastId++;
	}
	
	public BoardState(BoardState board, PlayerColor color) {
		this.pieces = new PieceMap(board.pieces);
		this.previous = board;
		this.played = false;
		this.turn = new PlayerTurn(color);
		
		uid = lastId++;
	}


	//TODO this method is crap. I don't understand your iterable map. sorry.
	public boolean isEqualTo(BoardState anotherBoard) {
		boolean allPiecesMatch = true;
		if (this.pieces.getSize() != anotherBoard.pieces.getSize()) {
			allPiecesMatch = false;
		} else {			
			ArrayList<Space> spaces = (ArrayList<Space>) Grid.getInstance().getSpaces();
			for (Map.Entry<Space, Piece> entry : pieces) {
				Space ourKey = entry.getKey();
				Piece ourPiece = entry.getValue();
				
				Piece theirPiece = anotherBoard.pieces.get(ourKey);
				if ((ourPiece == null && theirPiece != null) || (ourPiece != null && theirPiece == null)) {
					allPiecesMatch = false;
					break;					
				} else if (ourPiece != null && theirPiece != null) {
					if (!ourPiece.toString().equals(theirPiece.toString())) {
						allPiecesMatch = false;
						break;
					}
				}
			}
		}
		return allPiecesMatch;
	}

	public PieceMap getPieces() {
		return pieces;
	}
	
	private void apply(PlayerTurn turn) throws IllegalMoveException {
		for (Change change : turn.getMoves()) {
			change.applyTo(this);
		}
	}

	public BoardState addStartingPieces() throws IllegalMoveException {		
		for(Space space : grid.getSpaces()) {
			PlayerColor color = PlayerColor.getColorForSpace(space.id);
			if (color != null) {
				addPiece(space, new Piece(color));
			}
		}

		return this;
	}
	
	public void addPiece(Space space, Piece piece) throws IllegalMoveException {
		pieces.add(space, piece);
	}
	
	public Piece getPiece(Space space) {
		return pieces.get(space);
	}

	public void removePiece(Space space) throws IllegalMoveException {
		pieces.remove(space);
		
		//TODO check for game end
	}
	
	public void movePiece(Space from, Space to) throws IllegalMoveException {
		pieces.move(from, to);
	}
	
	public void jumpPiece(Space from, Space jumpedSpace) {	
		
	}
	
	public boolean gameOver() {
		PlayerColor winningColor = null;
		boolean gameOver = true;
		for (Entry<Space, Piece> entry : pieces) {
			Piece piece = entry.getValue();
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
	
	public void kingPiece(Space space) throws IllegalMoveException {
		pieces.king(space);
	}
	
	public Space getSpace(int id) {
		return grid.getSpaceById(id);
	}
	
	public boolean isEmpty(Space space) {
		return !pieces.hasPiece(space);
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

	public List<BoardState> getAllPossibleStates(PlayerColor color) throws IllegalMoveException {
		List<BoardState> states = new ArrayList<>();
		
		for (Map.Entry<Space, Piece> entry : pieces) {
			Piece piece = entry.getValue();
			if (piece != null && piece.color == color) {
				states.addAll(getPossibleStates(entry.getKey(), color));
			}
		}
		
		return states;
	}


	//I got real confused trying to track the jump origin/jumped/landing space so i just made a jump object....
	public List<BoardState> getPossibleStates(Space space, PlayerColor color) throws IllegalMoveException {
		List<BoardState> states = new ArrayList<>();

		Piece piece = pieces.get(space);
		if (piece != null) {
			ArrayList<Space> emptyAdjacents = new ArrayList<Space>();
			ArrayList<Jump> jumpOptions = new ArrayList<Jump>();
			
			//For Each Direction
			for (Direction direction : piece.getDirections()) {
				
				//Track the adjacent space
				Space adjacent = grid.getAdjacent(space, direction);
				if (adjacent != null) {					
					if (isEmpty(adjacent)) {
						emptyAdjacents.add(adjacent);
					} else {
						if (piece.isOpponent(getPiece(adjacent))) {
							Space landingSpace = grid.getAdjacent(adjacent, direction);
							if (landingSpace != null && isEmpty(landingSpace)) {
								Jump jump = new Jump(space, adjacent, landingSpace);
								jumpOptions.add(jump);
							}
						}
					}
				}
			}
			
			if (jumpOptions.isEmpty() && !emptyAdjacents.isEmpty()) {
				//Consider empty spaces only when no jumps are available
				for (Space emptySpace : emptyAdjacents) { 
					BoardState state = new BoardState(this, color);
					state.movePiece(space, emptySpace);
					states.add(state);  
				}
			} else if (!jumpOptions.isEmpty()) {
				for (Jump jump: jumpOptions) { 
					BoardState state = new BoardState(this, color);
					state.movePiece(space, jump.landing);
					state.removePiece(jump.capture);
					states.add(state);
				}
				
			}

			
		}
		
		return states;
	}

	private ArrayList<BoardState> findJumpOptionStates(Space jumpable, Piece piece) {
		
		ArrayList<BoardState> statesToAdd = new ArrayList<BoardState>();
		
		
		
		
		return statesToAdd;
	}
	
	public void setPlayed() {
		played = true;
	}

	public String toString() {
		return "Board #" + uid + "\n" + grid.toString(this);
	}

	public BoardState getFirstUnplayed() {
		BoardState state = this;
		
		//Find the state after the latest played state
		while (state.previous != null && !state.previous.played) {
			state = state.previous;
		}
		
		return state;
	}
	

	public PieceMap getPieceMap() {
		return pieces;
	}

}
