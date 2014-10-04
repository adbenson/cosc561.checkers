package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.model.PieceMap.IllegalMoveException;

public class PlayerTurn {
		
	private final List<Change> moves;
	public final PlayerColor player;
	
	public PlayerTurn(PlayerColor player) {
		this.moves = new ArrayList<>();
		this.player = player;
	}
	
	public void add(Piece piece, Space to) {
		moves.add(new Add(piece, to));
	}
	
	public void remove(Space from) {
		moves.add(new Remove(from));
	}
	
	public void move(Space from, Space to) {
		moves.add(new Move(from, to));
	}
	
	public List<Change> getMoves() {
		return moves;
	}
	
	public abstract class Change {
		public final Piece piece;
		public final Space from;
		public final Space to;
		
		public Change(Piece piece, Space from, Space to) {
			this.piece = piece;
			this.from = from;
			this.to = to;
		}

		public abstract void applyTo(BoardState boardState) throws IllegalMoveException;
		
	}
	
	public class Add extends Change {
		public Add(Piece piece, Space to) {
			super(piece, null, to);
		}

		@Override
		public void applyTo(BoardState board) throws IllegalMoveException {
			board.addPiece(to, piece);
		}
	}
	
	public class Remove extends Change {
		public Remove(Space from) {
			super(null, from, null);
		}

		@Override
		public void applyTo(BoardState board) throws IllegalMoveException {
			board.removePiece(from);
		}
	}
	
	public class Move extends Change {
		public Move(Space from, Space to) {
			super(null, from, to);
		}

		@Override
		public void applyTo(BoardState board) throws IllegalMoveException {
			board.movePiece(from, to);
		}
	}
	
	public class King extends Change {
		public King(Space space) {
			super(null, null, space);
		}
		
		@Override
		public void applyTo(BoardState board) throws IllegalMoveException {
//			board.kingPiece(to);
		}
	}
	
}
