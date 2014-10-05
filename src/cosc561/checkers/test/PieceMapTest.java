package cosc561.checkers.test;

import static org.junit.Assert.*;

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
	
	@Test
	public void testIterateAll() {
		int i=0;
		
		for (Piece piece : pieces) {
			i++;
		}
		
		assertEquals(24, i);
	}
	
	@Test
	public void testIterateRed() {
		int i=0;
		
		for (Piece piece : pieces.iterate(PlayerColor.RED)) {
			i++;
		}
		
		assertEquals(12, i);
	}
	
	@Test
	public void testIteratePartialBoardRed() throws IllegalMoveException {
		System.out.println(grid);
		board.removePiece(null, grid.getSpaceById(21));
		board.removePiece(null, grid.getSpaceById(10));
		board.removePiece(null, grid.getSpaceById(24));
		board.removePiece(null, grid.getSpaceById(31));
		
		int i=0;
		
		for (Piece piece : pieces.iterate(PlayerColor.RED)) {
			i++;
		}
		
		assertEquals(9, i);
	}
	
	@Test
	public void testIteratePartialBoardAll() throws IllegalMoveException {
		System.out.println(grid);
		board.removePiece(null, grid.getSpaceById(21));
		board.removePiece(null, grid.getSpaceById(10));
		board.removePiece(null, grid.getSpaceById(24));
		board.removePiece(null, grid.getSpaceById(31));
		
		int i=0;
		
		for (Piece piece : pieces) {
			i++;
		}
		
		assertEquals(20, i);
	}

	@Test
	public void testIteratePartialBoardSpaces() throws IllegalMoveException {
		System.out.println(grid);
		board.removePiece(null, grid.getSpaceById(21));
		board.removePiece(null, grid.getSpaceById(10));
		board.removePiece(null, grid.getSpaceById(24));
		board.removePiece(null, grid.getSpaceById(31));
		
		int i=0;
		
		for (Entry piece : pieces.iterateSpaces(PlayerColor.RED)) {
			i++;
		}
		
		assertEquals(9, i);
	}
}
