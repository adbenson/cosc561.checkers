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
	public final long uid;
	
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
		for (Change change : turn.getMoves()) {
			change.applyTo(this);
		}
	}

	public BoardState addStartingPieces() throws IllegalMoveException {		
		for(Space space : grid.getSpaces()) {
			PlayerColor color = PlayerColor.getColorForStartingSpace(space.id);
			if (color != null) {
				addPiece(space, Piece.get(color));
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
		if (shouldKing(to)) {
			pieces.king(to);
		}
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
	
	public boolean isEmpty(Space space) {
		return !pieces.hasPiece(space);
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
								Jump jump = new Jump(space, adjacent, landingSpace);
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
					state.movePiece(space, emptySpace);
					states.add(state);  
				}
			} else if (!jumpOptions.isEmpty()) {
//				if (jumpOptions.size() == 1) {
//					Jump jump = jumpOptions.get(0);
//					states.addAll(findJumpOptionStates(jump, piece));
//				} else {
//					
//					
//				}
				for (Jump jump: jumpOptions) { 
					BoardState state = new BoardState(this, color);
					state.movePiece(space, jump.landing);
					state.removePiece(jump.capture);
					states.addAll(findJumpOptionStates(jump, piece, state, color));
				}
				
			}

			
		}
		
		return states;
	}

	private boolean shouldKing(Space space) {
		Piece piece = pieces.get(space);
		return (!piece.isKing() && grid.canKing(space, piece.color));
	}

	private ArrayList<BoardState> findJumpOptionStates(Jump jump, Piece piece, BoardState state, PlayerColor color) throws IllegalMoveException {
		
		ArrayList<BoardState> statesToAdd = new ArrayList<BoardState>();
		
			ArrayList<Jump> jumpOptions = new ArrayList<Jump>();
			
			//For Each Direction
			for (Direction direction : piece.getDirections()) {
				Space adjacent = grid.getAdjacent(jump.landing, direction);

				//Make sure we don't consider jumping back over the piece we just captured
				if (adjacent != null && adjacent != jump.capture) {					
					
					if (!isEmpty(adjacent)) {
						//log any filled spaces and look for jump options
						if (piece.isOpponent(getPiece(adjacent))) {
							Space landingSpace = grid.getAdjacent(adjacent, direction);
							if (landingSpace != null && isEmpty(landingSpace)) {
								Jump newJump = new Jump(jump.landing, adjacent, landingSpace);
								jumpOptions.add(newJump);
							}//no where to land
						}//piece was adjacent but not our enemy
					}//adjacent space was empty
				}//adjacent space is null or where we came from
			}
			
			if (!jumpOptions.isEmpty()) {
				for (Jump jumpOption: jumpOptions) { 
					BoardState newState = new BoardState(state, color);
					newState.movePiece(jumpOption.origin, jumpOption.landing);
					newState.removePiece(jumpOption.capture);
					statesToAdd.addAll(findJumpOptionStates(jumpOption, piece, newState, color));
				}
			} else {
				//no options were found. multi jump ends here.
				//just send back the original state that was passed here
				statesToAdd.add(state);
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
	

	public PieceMap getPieceMap() {
		return pieces;
	}

}
