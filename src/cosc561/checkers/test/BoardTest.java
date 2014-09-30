package cosc561.checkers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.Space;

public class BoardTest {

	private BoardState board;

	@Before
	public void setUp() {
		board = new BoardState();
	}

	@Test
	public void gameOverFalse() {
		// Setup
		board = board.addStartingPieces();
		// Test
		boolean gameOver = board.gameOver();

		// Validate
		assertFalse(gameOver);
	}

	@Test
	public void gameOverTrue() {
		// Setup
		board.addPiece(22, new Piece(PlayerColor.RED));
		board.addPiece(18, new Piece(PlayerColor.BLACK));
		boolean gameOver = board.gameOver();
		assertFalse(gameOver);

		// Test
		board.removePiece(22);
		gameOver = board.gameOver();

		// Validate
		assertTrue(gameOver);
	}

	@Test
	public void getLegalMovesEmptySpace() {
		board = board.addStartingPieces();

		List<Space> moves = board.getLegalMoves(18);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesNoOpponents() {
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
	public void getLegalMovesNoOpponentsEdge() {
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
	public void getLegalMovesFriendlyBlocked() {
		board = board.addStartingPieces();

		// RED
		List<Space> moves = board.getLegalMoves(7);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesEnemyBlocked() {
		board = board.addStartingPieces();

		// RED
		List<Space> moves = board.getLegalMoves(7);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesOneJumpForced() {
		// Setup - RED
		board.addPiece(22, new Piece(PlayerColor.RED));
		board.addPiece(18, new Piece(PlayerColor.BLACK));

		// Call Method - RED
		List<Space> moves = board.getLegalMoves(22);

		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(1, moves.size());
		assertMoves(moves, 15);

		// Setup - BLACK
		board.addPiece(3, new Piece(PlayerColor.BLACK));
		board.addPiece(7, new Piece(PlayerColor.RED));

		// Call Method - BLACK
		moves = board.getLegalMoves(3);

		// Validate - BLACK
		assertEquals(1, moves.size());
		assertMoves(moves, 10);
	}

	@Test
	public void getLegalMovesMultiJump() {
		// Setup - RED
		Piece jumper = new Piece(PlayerColor.RED);
		Piece dupe1 = new Piece(PlayerColor.BLACK);
		Piece dupe2 = new Piece(PlayerColor.BLACK);
		Piece dupe3 = new Piece(PlayerColor.BLACK);
		board.addPiece(29, jumper);
		board.addPiece(25, dupe1);
		board.addPiece(18, dupe2);
		board.addPiece(11, dupe3);

		// Call Method - RED
		List<Space> moves = board.getLegalMoves(29);

		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(1, moves.size());
		assertMoves(moves, 8);

		// Add another piece
		Piece dupe3b = new Piece(PlayerColor.BLACK);
		board.addPiece(10, dupe3b);

		// Call Method - RED
		moves = board.getLegalMoves(29);

		// Validate - RED
		assertEquals(2, moves.size());
		assertMoves(moves, 8);
		assertMoves(moves, 6);

		// Add another piece
		Piece dupe2b = new Piece(PlayerColor.BLACK);
		board.addPiece(17, dupe2b);

		// Call Method - RED
		moves = board.getLegalMoves(29);

		// Validate - RED
		assertEquals(3, moves.size());
		assertMoves(moves, 8);
		assertMoves(moves, 6);
		assertMoves(moves, 13);
	}

	@Test
	public void getLegalMovesKingNoJump() {
		board = board.addStartingPieces();

		// BLACK
		board.kingPiece(10);
		board.movePiece(10, 18);

		List<Space> moves = board.getLegalMoves(18);

		assertEquals(2, moves.size());
		assertMoves(moves, 14, 15);

		// RED
		board.kingPiece(23);
		board.movePiece(23, 15);

		moves = board.getLegalMoves(15);

		assertEquals(2, moves.size());
		// Piece is blocked by black king, but can take his place
		assertMoves(moves, 10, 19);
	}

	@Test
	public void getLegalMovesNoOpponentsKing() {
		Piece king = new Piece(PlayerColor.RED);
		king.setKing();

		board.addPiece(18, king);

		List<Space> moves = board.getLegalMoves(18);

		assertEquals(4, moves.size());
		assertMoves(moves, 14, 15, 22, 23);
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

}
