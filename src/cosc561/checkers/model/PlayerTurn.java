package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.model.PieceMap.IllegalMoveException;

public class PlayerTurn {
		
	private final List<Change> changes;
	public final PlayerColor player;
	
	public PlayerTurn(PlayerColor player) {
		this.changes = new ArrayList<>();
		this.player = player;
	}
	
	public void addChange(Change change) {
		this.changes.add(change);
	}
	
	public List<Change> getChanges() {
		return changes;
	}
	
	public String toString() {
		String turn = player + " turn:\n";
		for (Change change : changes) {
			turn += change + "\n";
		}
		return turn;
	}
	
	public static abstract class Change {
		public final Piece piece;
		public final Space from;
		public final Space to;
		public final Space remove;
		
		public Change(Piece piece, Space from, Space to, Space remove) {
			this.piece = piece;
			this.from = from;
			this.to = to;
			this.remove = remove;
		}

		public abstract void applyTo(PieceMap pieces) throws IllegalMoveException;
		
		@Override
		public abstract String toString();
	}
	
	public static class Add extends Change {
		public Add(Piece piece, Space to) {
			super(piece, null, to, null);
		}

		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.add(piece, to);
		}

		@Override
		public String toString() {
			return "Add "+piece+" to "+to;
		}
	}
	
	public static class Remove extends Change {
		public Remove(Piece piece, Space remove) {
			super(piece, null, null, remove);
		}

		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.remove(remove);
		}

		@Override
		public String toString() {
			return "Remove "+piece+" from "+from;
		}
	}
	
	public static class Move extends Change {
		public Move(Piece piece, Space from, Space to) {
			super(piece, from, to, null);
		}

		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.move(from, to);
		}

		@Override
		public String toString() {
			return "Move "+piece+" from "+from+" to "+to;
		}
	}
	
	public static class King extends Change {
		public King(Piece piece, Space space) {
			super(piece, null, space, null);
		}
		
		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.king(to);
		}

		@Override
		public String toString() {
			return "Kinged "+piece+" at "+to;
		}
	}

	public static class Jump extends Change {
		public Jump(Piece piece, Space from, Space to, Space capture) {
			super(piece, from, to, capture);
		}

		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.move(from, to);
			pieces.remove(remove);
		}

		@Override
		public String toString() {
			return "Jumped "+piece+" from "+from+" to "+to+" capturing "+remove;
		}
		
	}
	
}
