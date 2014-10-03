package cosc561.checkers.test;

import static cosc561.checkers.model.PlayerColor.BLACK;
import static cosc561.checkers.model.PlayerColor.RED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.Space;

public class BoardTest {

	private BoardState board;

	@Before
	public void setUp() throws IllegalMoveException {
		board = new BoardState(RED);
	}

	@Test
	public void gameOverFalse() throws IllegalMoveException {
		// Setup
		board = board.addStartingPieces();
		// Test
		boolean gameOver = board.gameOver();

		// Validate
		assertFalse(gameOver);
	}

	@Test
	public void gameOverTrue() throws IllegalMoveException {
		// Setup
		board.addPiece(space(22), new Piece(RED));
		board.addPiece(space(18), new Piece(BLACK));
		boolean gameOver = board.gameOver();
		assertFalse(gameOver);

		// Test
		board.removePiece(space(22));
		gameOver = board.gameOver();

		// Validate
		assertTrue(gameOver);
	}

	@Test
	public void getLegalMovesEmptySpace() throws IllegalMoveException {
		board = board.addStartingPieces();

		List<Space> moves = board.getLegalMoves(18);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesNoOpponents() throws IllegalMoveException {
		board = board.addStartingPieces();
		// RED
		List<Space> moves = board.getLegalMoves(22);

		assertEquals(2, moves.size());

		assertMoves(moves, 17, 18);

		// BLACK
		moves = board.getLegalMoves(10);

		assertEquals(2, moves.size());

		assertMoves(moves, 14, 15);
	}

	@Test
	public void getLegalMovesNoOpponentsEdge() throws IllegalMoveException {
		board = board.addStartingPieces();

		// RED
		List<Space> moves = board.getLegalMoves(21);

		assertEquals(1, moves.size());
		assertMoves(moves, 17);

		// BLACK
		moves = board.getLegalMoves(12);

		assertEquals(1, moves.size());
		assertMoves(moves, 16);
	}

	@Test
	public void getLegalMovesFriendlyBlocked() throws IllegalMoveException {
		board = board.addStartingPieces();

		// RED
		List<Space> moves = board.getLegalMoves(7);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesEnemyBlocked() throws IllegalMoveException {
		board = board.addStartingPieces();

		// RED
		List<Space> moves = board.getLegalMoves(7);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesOneJumpForced() throws IllegalMoveException {
		// Setup - RED
		board.addPiece(space(22), new Piece(RED));
		board.addPiece(space(18), new Piece(BLACK));

		// Call Method - RED
		List<Space> moves = board.getLegalMoves(22);

		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(1, moves.size());
		assertMoves(moves, 15);

		// Setup - BLACK
		board.addPiece(space(3), new Piece(BLACK));
		board.addPiece(space(7), new Piece(RED));

		// Call Method - BLACK
		moves = board.getLegalMoves(3);

		// Validate - BLACK
		assertEquals(1, moves.size());
		assertMoves(moves, 10);
	}

	@Test
	public void getLegalMovesMultiJump() throws IllegalMoveException {
		// Setup - RED
		Piece jumper = new Piece(RED);
		Piece dupe1 = new Piece(BLACK);
		Piece dupe2 = new Piece(BLACK);
		Piece dupe3 = new Piece(BLACK);
		board.addPiece(space(29), jumper);
		board.addPiece(space(25), dupe1);
		board.addPiece(space(18), dupe2);
		board.addPiece(space(11), dupe3);

		// Call Method - RED
		List<Space> moves = board.getLegalMoves(29);

		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(1, moves.size());
		assertMoves(moves, 8);

		// Add another piece
		Piece dupe3b = new Piece(BLACK);
		board.addPiece(space(10), dupe3b);

		// Call Method - RED
		moves = board.getLegalMoves(29);

		// Validate - RED
		assertEquals(2, moves.size());
		assertMoves(moves, 8);
		assertMoves(moves, 6);

		// Add another piece
		Piece dupe2b = new Piece(BLACK);
		board.addPiece(space(17), dupe2b);

		// Call Method - RED
		moves = board.getLegalMoves(29);

		// Validate - RED
		assertEquals(3, moves.size());
		assertMoves(moves, 8);
		assertMoves(moves, 6);
		assertMoves(moves, 13);
	}

	@Test
	public void getLegalMovesKingNoJump() throws IllegalMoveException {
		board = board.addStartingPieces();

		// BLACK
		board.kingPiece(space(10));
		board.movePiece(space(10), space(18));

		List<Space> moves = board.getLegalMoves(18);

		assertEquals(2, moves.size());
		assertMoves(moves, 14, 15);

		// RED
		board.kingPiece(space(23));
		board.movePiece(space(23), space(15));

		moves = board.getLegalMoves(15);

		assertEquals(2, moves.size());
		// Piece is blocked by black king, but can take his place
		assertMoves(moves, 10, 19);
	}

	@Test
	public void getLegalMovesNoOpponentsKing() throws IllegalMoveException {
		Piece king = new Piece(RED);
		king.setKing();

		board.addPiece(space(18), king);

		List<Space> moves = board.getLegalMoves(18);

		assertEquals(4, moves.size());
		assertMoves(moves, 14, 15, 22, 23);
	}
	
	private Space space(int id) {
		return Grid.getInstance().getSpaceById(id);
	}

	private void assertMoves(List<Space> moves, int... spaceIds) {
		for (int id : spaceIds) {
			assertTrue("Move to space " + id + " not found in (" + moves + ")", hasMove(moves, id));
		}
	}

	private boolean hasMove(List<Space> list, int id) {
		for (Space s : list) {
			if (s.id == id) {
				return true;
			}
		}

		return false;
	}
	
	@Test 
	public void testGetFirstUnplayed() throws IllegalMoveException {
//		BoardState b1 = new BoardState(board, new PlayerTurn(RED));
//		BoardState b2 = new BoardState(b1, new PlayerTurn(RED));
//		b2.setPlayed();
//		
//		BoardState b3 = new BoardState(b2, new PlayerTurn(RED));
//		BoardState b4 = new BoardState(b3, new PlayerTurn(RED));
//		
//		BoardState unplayed = b4.getFirstUnplayed();
//		assertEquals(b3, unplayed);
	}

}
