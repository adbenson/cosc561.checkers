package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerTurn.*;

public class BoardState implements Printable {
	
	private static long lastId = 1;

	private static Grid grid = Grid.getInstance();
	
	private final BoardState previous;
	private final int depth;
	private final PlayerTurn turn;
	public final long uid;
	
	private boolean played;
	
	private PieceMap pieces;
	
	private boolean endgame;

	public BoardState(PlayerColor firstPlayer) {
		pieces = new PieceMap();
		previous = null;
		depth = 1;
		turn = new PlayerTurn(firstPlayer);
		played = true;
		uid = lastId++;
		
		setPlayed();
	}
	
	public BoardState(BoardState board, PlayerColor color) {
		this.pieces = new PieceMap(board.pieces);
		this.previous = board;
		this.depth = board.depth + 1;
		this.played = false;
		this.turn = new PlayerTurn(color);
		uid = lastId++;
	}

	public boolean equals(Object other) {
		if (! (other instanceof BoardState)) {
			return false;
		}
		
		return pieces.equals(((BoardState)other).pieces);
	}

	public PieceMap getPieces() {
		return pieces;
	}
	
	private void apply(PlayerTurn turn) throws IllegalMoveException {
		for (Change change : turn.getChanges()) {
			apply(change);
		}
	}
	
	private void apply(Change change) throws IllegalMoveException {
		change.applyTo(this.pieces);
		turn.addChange(change);
		
		if (change.to != null && pieces.shouldKing(change.to)) {
			kingPiece(change.piece, change.to);
		}
	}

	public PlayerTurn getTurn() {
		return turn;
	}

	public BoardState addStartingPieces() throws IllegalMoveException {		
		for(Space space : grid.getSpaces()) {
			PlayerColor color = PlayerColor.getColorForStartingSpace(space.id);
			if (color != null) {
				addPiece(Piece.get(color), space);
			}
		}

		return this;
	}
	
	public Piece getPiece(Space space) {
		return pieces.get(space);
	}
	
	public void addPiece(Piece piece, Space space) throws IllegalMoveException {
		apply(new Add(piece, space));
	}

	public void removePiece(Piece piece, Space space) throws IllegalMoveException  {
		apply(new Remove(piece, space));
	}
	
	public void movePiece(Piece piece, Space from, Space to) throws IllegalMoveException {
		apply(new Move(piece, from, to));
	}
	
	public void jumpPiece(Piece piece, Space from, Space to, Space capture) throws IllegalMoveException {
		apply(new Jump(piece, from, to, capture));
	}
	
	public void kingPiece(Piece piece, Space space) throws IllegalMoveException {
		apply(new King(piece, space));
	}
	
	private boolean isRepeat(Space space) {
		//Trivial search did this space have the same thing in it last turn?
		if (depth > 3 && previous.previous.getPiece(space) == getPiece(space)) {
			return this.repeats();
		}
		
		return false;
	}
	
	private boolean repeats() {
		return true;
	}

	public boolean isEndgame() {
		return endgame;
	}
	
	public boolean isEmpty(Space space) {
		return !pieces.hasPiece(space);
	}

	public List<BoardState> getAllPossibleStates(PlayerColor color) throws IllegalMoveException {
		List<BoardState> states = new ArrayList<>();
		
		for (Entry entry : pieces.iterateSpaces(color)) {
			states.addAll(getPossibleStates(entry.space, color));
		}
		
		if (states.size() < 1) {
			endgame = true;
		}
		
		return states;
	}


	public List<BoardState> getPossibleStates(Space space, PlayerColor color) throws IllegalMoveException {
		List<BoardState> states = new ArrayList<>();

		Piece piece = pieces.get(space);
		if (piece == null) {
			return states;
		}

		ArrayList<Space> emptyAdjacents = new ArrayList<Space>();
		ArrayList<Jump> jumpOptions = new ArrayList<Jump>();
		
		//For Each Direction
		for (Direction direction : piece.getDirections()) {
			//Track the adjacent spaces
			Space adjacent = grid.getAdjacent(space, direction);
			
			if (adjacent != null) {					
				if (isEmpty(adjacent)) {
					//log any open spaces
					emptyAdjacents.add(adjacent);
				} else {
					//log any filled spaces and look for jump options
					if (piece.isOpponent(getPiece(adjacent))) {
						Space landingSpace = grid.getAdjacent(adjacent, direction);
						if (landingSpace != null && isEmpty(landingSpace)) {
							Jump jump = new Jump(piece, space, landingSpace, adjacent);
							jumpOptions.add(jump);
						}
					}
				}
			}
		}
		
		//FORCE JUMP LOGIC
		if (jumpOptions.isEmpty() && !emptyAdjacents.isEmpty()) {
			//Consider empty spaces only when no jumps are available
			for (Space emptySpace : emptyAdjacents) {
				BoardState state = new BoardState(this, color);
				state.movePiece(piece, space, emptySpace);
				
				//Only kings can repeat moves
				if (!piece.isKing() || !state.isRepeat(space)) {
					states.add(state);
				}
			}

		} else if (!jumpOptions.isEmpty()) {
			for (Jump jump: jumpOptions) { 
				BoardState state = new BoardState(this, color);
				state.apply(jump);
				states.addAll(state.findJumpOptionStates(jump.to, piece, color));
			}
		}
		
		return states;
	}
	
	private ArrayList<BoardState> findJumpOptionStates(Space space, Piece piece, PlayerColor color) throws IllegalMoveException {
		
		ArrayList<BoardState> statesToAdd = new ArrayList<BoardState>();
		
		ArrayList<Jump> jumpOptions = new ArrayList<Jump>();
		
		//For Each Direction
		for (Direction direction : piece.getDirections()) {
			Space adjacent = grid.getAdjacent(space, direction);

			//If the adjacent space is an enemy
			if (adjacent != null && !isEmpty(adjacent) && piece.isOpponent(getPiece(adjacent))) {
				
				//log any filled spaces and look for jump options
				Space landingSpace = grid.getAdjacent(adjacent, direction);
				
				if (landingSpace != null && isEmpty(landingSpace)) {
					Jump jump = new Jump(piece, space, landingSpace, adjacent);
					jumpOptions.add(jump);
				}//no where to land
				
			}//adjacent space was null or empty
		}
		
		if (!jumpOptions.isEmpty()) {
			for (Jump jump: jumpOptions) { 
				BoardState state = new BoardState(this, color);
				state.apply(jump);
				statesToAdd.addAll(state.findJumpOptionStates(jump.to, piece, color));
			}
		} else {
			//no options were found. multi jump ends here.
			//just send back the original state that was passed here
			statesToAdd.add(this);
		}

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

	@Override
	public String toString(Space space) {
		return pieces.toString(space);
	}
}
