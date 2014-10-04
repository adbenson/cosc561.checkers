package cosc561.checkers.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PieceMap implements Iterable<Map.Entry<Space, Piece>> {
	
	private Piece[] pieces;
	private static Grid grid = Grid.getInstance();
	
	public PieceMap() {
		pieces = new Piece[Grid.USED_SPACES + 1];
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

	public void add(Space space, Piece piece) throws IllegalMoveException {
		if (!hasPiece(space)) {
			pieces[space.id] = piece;
		} else {
			throw new IllegalMoveException("Cannot add piece to "+space+" : already taken by "+pieces[space.id]);
		}
	}

	public PieceMap(PieceMap pieces) {
		this.pieces = pieces.pieces.clone();
	}
	
	public Piece[] getPieces(){
		return this.pieces;
	}

	public int getSize() {
		return pieces.length;
	}
	
	@Override
	public Iterator<Map.Entry<Space, Piece>> iterator() {
		return new Iterator<Map.Entry<Space, Piece>>() {
			int id = 1;

			@Override
			public boolean hasNext() {
				return id < pieces.length;
			}

			@Override
			public Entry<Space, Piece> next() {
				final int current = id;
				id++;
				return new Map.Entry<Space, Piece>() {

					@Override
					public Space getKey() {
						return grid.getSpaceById(current);
					}

					@Override
					public Piece getValue() {
						return pieces[current];
					}

					@Override
					public Piece setValue(Piece value) {
						throw new UnsupportedOperationException("No.");
					}
					
				};
				
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("No.");
			}
			
		};
	}
	
	public class IllegalMoveException extends Exception {
		private static final long serialVersionUID = 1L;

		public IllegalMoveException(String msg) {
			super(msg);
		}
		
		public IllegalMoveException(String msg, Throwable t) {
			super(msg, t);
		}
	}
	
}

