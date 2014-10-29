package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.model.PieceMap.IllegalMoveException;

public class PlayerTurn {
		
	private final List<Change> changes;
	public final PlayerColor player;
	public final int turnNumber;
	
	public PlayerTurn(PlayerColor player, int turnNumber) {
		this.changes = new ArrayList<>();
		this.player = player;
		this.turnNumber = turnNumber;
	}
	
	public PlayerTurn(PlayerTurn that) {
		this.player = that.player;
		this.turnNumber = that.turnNumber;
		//Should be safe to not deep-copy the changes themselves, as they are immutable
		this.changes = new ArrayList<>(that.changes);
	}

	public void addChange(Change change) {
		this.changes.add(change);
	}
	
	public boolean hasChange(Change change) {
		boolean match = false;
		for (Change thisChange : changes) {
			if (thisChange.equals(change)) {
				match = true;
			}
		}
		return match;
	}
	
	public List<Change> getChanges() {
		return changes;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PlayerTurn) {
			return false;
		}
		
		PlayerTurn that = (PlayerTurn)other;
		if (this.changes.size() != that.changes.size()) {
			return false;
		}
		
		for (Change thisChange : changes) {
			if (!that.hasChange(thisChange)) {
				return false;
			}
		}
		
		return true;
	}
	
	public String toString() {
		String turn = "** Turn #"+turnNumber+": "+player + " **\n";
		for (Change change : changes) {
			turn += change + "\n";
		}
		return turn;
	}
	
	public static abstract class Change {
		public final Piece piece;
		public final Space from;
		public final Space to;
		public final Remove capture;
		
		public Change(Piece piece, Space from, Space to, Remove remove) {
			this.piece = piece;
			this.from = from;
			this.to = to;
			this.capture = remove;
		}

		public abstract void applyTo(PieceMap pieces) throws IllegalMoveException;
		
		@Override
		public abstract String toString();
		
		@Override
		public boolean equals(Object other) {
			if (!this.getClass().equals(other.getClass())) {
				return false;
			}
			Change that = (Change)other;
			return (this.piece == that.piece) &&
					(this.to == that.to) &&
					(this.from == that.from) &&
					(this.capture == that.capture);
					
		}
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
			super(piece, remove, null, null);
		}

		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.remove(from);
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

		public Move reverse() {
			return new Move(piece, to, from);
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
		public Jump(Piece piece, Space from, Space to, Remove capture) {
			super(piece, from, to, capture);
		}

		public Jump(Piece piece, Space from, Space to, Piece captured, Space capture) {
			this(piece, from, to, new Remove(captured, capture));
		}

		@Override
		public void applyTo(PieceMap pieces) throws IllegalMoveException {
			pieces.move(from, to);
			capture.applyTo(pieces);
		}

		@Override
		public String toString() {
			return "Jumped "+piece+" from "+from+" to "+to+",\n\tCapturing "+capture.piece+" at "+capture.from;
		}
		
	}
	
}
