package cosc561.checkers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cosc561.checkers.model.Board;
import cosc561.checkers.model.Board.Space;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.Piece.Color;

public class BoardTest {
	
	private Board board;
	
	@Before
	public void setUp() {
		board = new Board();
	}

	@Test
	public void getLegalMovesEmptySpace() {
		board.addStartingPieces();
		
		List<Space> moves = board.getSpace(18).getLegalMoves();
		
		assertEquals(moves.size(), 0);
	}
	
	@Test
	public void getLegalMovesNoOpponents() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getSpace(22).getLegalMoves();
		
		assertEquals(moves.size(), 2);
		
		assertTrue(hasId(moves, 17));
		assertTrue(hasId(moves, 18));
		
		//BLACK
		moves = board.getSpace(10).getLegalMoves();
		
		assertEquals(moves.size(), 2);
		
		assertTrue(hasId(moves, 14));
		assertTrue(hasId(moves, 15));
	}
	
	@Test
	public void getLegalMovesNoOpponentsEdge() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getSpace(21).getLegalMoves();
		
		assertEquals(moves.size(), 1);
		assertTrue(hasId(moves, 17));
		
		//BLACK
		moves = board.getSpace(12).getLegalMoves();
		
		assertEquals(moves.size(), 1);
		assertTrue(hasId(moves, 16));
	}
	
	@Test
	public void getLegalMovesFriendlyBlocked() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getSpace(7).getLegalMoves();
		
		assertEquals(moves.size(), 0);
	}
	
	@Test
	public void getLegalMovesEnemyBlocked() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getSpace(7).getLegalMoves();
		
		assertEquals(moves.size(), 0);
	}
	
	private boolean hasId(List<Space> list, int id) {
		for(Space s : list) {
			if (s.id == id) {
				return true;
			}
		}
		
		return false;
	}

}
