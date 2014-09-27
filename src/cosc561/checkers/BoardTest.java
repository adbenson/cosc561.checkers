package cosc561.checkers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cosc561.checkers.model.Board;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.Piece.Color;
import cosc561.checkers.model.Space;

public class BoardTest {
	
	private Board board;
	
	@Before
	public void setUp() {
		board = new Board();
	}

	@Test
	public void getLegalMovesEmptySpace() {
		board.addStartingPieces();
		
		List<Space> moves = board.getLegalMoves(18);
		
		assertEquals(moves.size(), 0);
	}
	
	@Test
	public void getLegalMovesNoOpponents() {
		board.addStartingPieces();
		//RED
		List<Space> moves = board.getLegalMoves(22);
		
		assertEquals(moves.size(), 2);

		assertMoves(moves, 17, 18);
		
		//BLACK
		moves = board.getLegalMoves(10);
		
		assertEquals(moves.size(), 2);
		
		assertMoves(moves, 14, 15);
	}
	
	@Test
	public void getLegalMovesNoOpponentsEdge() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getLegalMoves(21);
		
		assertEquals(moves.size(), 1);
		assertMoves(moves, 17);
		
		//BLACK
		moves = board.getLegalMoves(12);
		
		assertEquals(moves.size(), 1);
		assertMoves(moves, 16);
	}

	@Test
	public void getLegalMovesFriendlyBlocked() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getLegalMoves(7);
		
		assertEquals(moves.size(), 0);
	}
	
	@Test
	public void getLegalMovesEnemyBlocked() {
		board.addStartingPieces();
		
		//RED
		List<Space> moves = board.getLegalMoves(7);
		
		assertEquals(moves.size(), 0);
	}
	
	@Test
	public void getLegalMovesOneJumpForced() { 
		//Setup - RED
		board.addStartingPieces();
		board.movePiece(11, 18);
		
		//Call Method - RED
		List<Space> moves = board.getLegalMoves(22);
		

		//Validate - RED
		//The jump is forced so only show one legal move
		assertEquals(moves.size(), 1);
		assertMoves(moves, 15);
		
		//Setup - BLACK
		board.movePiece(24, 16);
		
		//Call Method - BLACK
		moves = board.getLegalMoves(12);
		
		//Validate - BLACK
		assertEquals(moves.size(), 1);
		assertMoves(moves, 19);
	}
	
	@Test 
	public void getLegalMovesMultiJump() {
		//Setup - RED
		Piece jumper = new Piece(Color.RED);
		Piece dupe1 = new Piece(Color.BLACK);
		Piece dupe2 = new Piece(Color.BLACK);
		Piece dupe3 = new Piece(Color.BLACK);
		board.addPiece(29, jumper);
		board.addPiece(25, dupe1);
		board.addPiece(18, dupe2);
		board.addPiece(11, dupe3);

		//Call Method - RED
		List<Space> moves = board.getLegalMoves(29);
		
		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(moves.size(), 1);
		assertMoves(moves, 8);
		
		//Add another piece
		Piece dupe3b = new Piece(Color.BLACK);
		board.addPiece(10, dupe3b);
		
		//Call Method - RED
		moves = board.getLegalMoves(29);

		// Validate - RED
		assertEquals(moves.size(), 2);
		assertMoves(moves, 8);
		assertMoves(moves, 6);


	}
	
	@Test
	public void getLegalMovesKingNoJump() {
		board.addStartingPieces();
		
		//BLACK
		board.kingPiece(10);
		board.movePiece(10, 18);
		
		List<Space> moves = board.getLegalMoves(18);
		
		assertEquals(moves.size(), 2);
		assertMoves(moves, 14, 15);
		
		//RED
		board.kingPiece(23);
		board.movePiece(23, 15);

		moves = board.getLegalMoves(15);
		
		assertEquals(moves.size(), 2);
		//Piece is blocked by black king, but can take his place
		assertMoves(moves, 10, 19);
	}
	
	@Test
	public void getLegalMovesNoOpponentsKing() {
		Piece king = new Piece(Color.RED);
		king.setKing();
		
		board.addPiece(18, king);
		
		List<Space> moves = board.getLegalMoves(18);
		
		assertEquals(moves.size(), 4);
		assertMoves(moves, 14, 15, 22, 23);
	}
	
	
	private void assertMoves(List<Space> moves, int... spaceIds) {
		for (int id : spaceIds) {
			assertTrue("Move to space "+ id +" not found in ("+moves+")", hasMove(moves, id));
		}
	}
	
	private boolean hasMove(List<Space> list, int id) {
		for(Space s : list) {
			if (s.id == id) {
				return true;
			}
		}
		
		return false;
	}

}
