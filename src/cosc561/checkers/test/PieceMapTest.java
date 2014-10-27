package cosc561.checkers.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap;
import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;

public class PieceMapTest {
	
	BoardState board;
	PieceMap pieces;
	
	Grid grid = Grid.getInstance();
	
	@Before
	public void setUp() throws IllegalMoveException {
		board = new BoardState(PlayerColor.RED).addStartingPieces();
		pieces = board.getPieces();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testIterateAll() {
		int i=0;
		
		for (Piece piece : pieces) {
			i++;
		}
		
		assertEquals(24, i);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testIterateRed() {
		int i=0;
		
		for (Piece piece : pieces.iterate(PlayerColor.RED)) {
			i++;
		}
		
		assertEquals(12, i);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testIteratePartialBoardRed() throws IllegalMoveException {
		System.out.println(grid);
		board.removePiece(grid.getSpaceById(21));
		board.removePiece(grid.getSpaceById(10));
		board.removePiece(grid.getSpaceById(24));
		board.removePiece(grid.getSpaceById(31));
		
		int i=0;
		
		for (Piece piece : pieces.iterate(PlayerColor.RED)) {
			i++;
		}
		
		assertEquals(9, i);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testIteratePartialBoardAll() throws IllegalMoveException {
		System.out.println(grid);
		board.removePiece(grid.getSpaceById(21));
		board.removePiece(grid.getSpaceById(10));
		board.removePiece(grid.getSpaceById(24));
		board.removePiece(grid.getSpaceById(31));
		
		int i=0;
		
		for (Piece piece : pieces) {
			i++;
		}
		
		assertEquals(20, i);
	}

	@SuppressWarnings("unused")
	@Test
	public void testIteratePartialBoardSpaces() throws IllegalMoveException {
		System.out.println(grid);
		board.removePiece(grid.getSpaceById(21));
		board.removePiece(grid.getSpaceById(10));
		board.removePiece(grid.getSpaceById(24));
		board.removePiece(grid.getSpaceById(31));
		
		int i=0;
		
		for (Entry piece : pieces.iterateSpaces(PlayerColor.RED)) {
			i++;
		}
		
		assertEquals(9, i);
	}
}
