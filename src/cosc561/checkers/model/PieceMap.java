package cosc561.checkers.model;

import java.util.Arrays;
import java.util.Iterator;

public class PieceMap implements Iterable<Piece>, Printable {
	
	private Piece[] pieces;
	private static Grid grid = Grid.getInstance();
	
	public PieceMap() {
		pieces = new Piece[Grid.USED_SPACES + 1];
	}
	
	public PieceMap(PieceMap pieces) {
		this.pieces = pieces.pieces.clone();
	}
	
	public void king(Space space) throws IllegalMoveException {
		if (hasPiece(space) && !pieces[space.id].isKing()) {
			pieces[space.id] = pieces[space.id].getKing();
		} else if (!hasPiece(space)) {
			throw new IllegalMoveException("Cannot king piece in "+space+". No piece in that space");
		} else {
			throw new IllegalMoveException("Cannot king piece in "+space+". Piece is already king");
		}
	}

	public boolean hasPiece(Space space) {
		return pieces[space.id] != null;
	}
	
	
	public void move(Space from, Space to) throws IllegalMoveException {
		if (hasPiece(from) && !hasPiece(to)) {
			pieces[to.id] = pieces[from.id];
			pieces[from.id] = null;
		} else if (!hasPiece(from)){
			throw new IllegalMoveException("Cannot move piece from "+from+" to "+to+". No piece in that space");
		} else {
			throw new IllegalMoveException("Cannot move piece from "+from+" to "+to+". Already a piece in that space");
		}
	}

	public void remove(Space space) throws IllegalMoveException {
		if (hasPiece(space)) {
			pieces[space.id] = null;
		} else {
			throw new IllegalMoveException("Cannot remove piece from "+space+". No piece in that space");
		}
	}

	public Piece get(Space space) {
		return pieces[space.id];
	}

	public void add(Piece piece, Space space) throws IllegalMoveException {
		if (!hasPiece(space)) {
			pieces[space.id] = piece;
		} else {
			throw new IllegalMoveException("Cannot add piece to "+space+" : already taken by "+pieces[space.id]);
		}
	}

	public boolean shouldKing(Space space) {
		Piece piece = get(space);
		return (!piece.isKing() && grid.canKing(space, piece.color));
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof PieceMap)) {
			return false;
		}
		
		return Arrays.equals(pieces, ((PieceMap)other).pieces);
	}
	
	public String toString(Space space) {
		Piece piece = pieces[space.id];
		return (piece == null)? "  " : piece.toString();
	}
	
	public String toString() {
		return grid.toString(this);
	}
	
	@Override
	public Iterator<Piece> iterator() {
		return new PieceIterator(null, false);
	}
	
	public Iterable<Piece> iterate(final PlayerColor color) {
		return new Iterable<Piece>() {
			@Override
			public Iterator<Piece> iterator() {
				return new PieceIterator(color, false);
			}
		};
	}
	
	public Iterable<Entry> iterateSpaces(final PlayerColor color) {
		return new Iterable<Entry>() {
			@Override
			public Iterator<Entry> iterator() {
				return new SpaceIterator(color, false);
			}
		};
	}
	
	public class Entry {
		public final Space space;
		public final Piece piece;
		
		private Entry(Space space, Piece piece) {
			this.space = space;
			this.piece = piece;
		}
	}
	
	private abstract class BoardIterator<Type> implements Iterator<Type> {
		private PlayerColor color;
		private boolean includeEmpty;
		
		private int i;
		private int last;
		
		public BoardIterator(PlayerColor color, boolean includeEmpty) {
			super();
			this.color = color;
			this.includeEmpty = includeEmpty;
			
			i = 0;
			last = pieces.length - 1;
			
			iterate();
		}
		
		@Override
		public boolean hasNext() {
			return i <= last;
		}
		
		private void iterate() {
			while (i <= last && !shouldInclude()) {
				i++;
			}
		}
		
		private boolean shouldInclude() {
			if (pieces[i] == null) {
				return includeEmpty;
			}
			else if (color != null) {
				return color == pieces[i].color;
			}

			return true;
		}

		@Override
		public Type next() {
			int returnIndex = i;
			
			i++;
			iterate();
			
			return getElement(returnIndex);
		}
		
		public abstract Type getElement(int i);

		@Override
		public void remove() {
			throw new UnsupportedOperationException("No.");
		}
		
	}
	
	private class SpaceIterator extends BoardIterator<Entry> {

		public SpaceIterator(PlayerColor color, boolean includeEmpty) {
			super(color, includeEmpty);
		}

		@Override
		public Entry getElement(int i) {
			return new Entry(grid.getSpaceById(i), pieces[i]);
		}
		
	}
	
	private class PieceIterator extends BoardIterator<Piece> {

		public PieceIterator(PlayerColor color, boolean includeEmpty) {
			super(color, includeEmpty);
		}

		@Override
		public Piece getElement(int i) {
			return pieces[i];
		}
		
	}
	
	public class IllegalMoveException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public IllegalMoveException(String msg) {			
			this(msg, null);
		}
		
		public IllegalMoveException(String msg, Throwable t) {
			super(msg, t);
			System.err.println(msg);
			System.err.println(PieceMap.this.toString());
		}
	}

}

