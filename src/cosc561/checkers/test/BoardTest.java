package cosc561.checkers.test;

import static cosc561.checkers.model.PlayerColor.BLACK;
import static cosc561.checkers.model.PlayerColor.RED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap;
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
		board.addPiece(space(22), Piece.get(RED));
		board.addPiece(space(18), Piece.get(BLACK));
		boolean gameOver = board.gameOver();
		assertFalse(gameOver);

		// Test
		board.removePiece(space(22));
		gameOver = board.gameOver();

		// Validate
		assertTrue(gameOver);
	}

//	@Test
//	public void getLegalMovesEmptySpace() throws IllegalMoveException {
//		board = board.addStartingPieces();
//
//		List<Space> moves = board.getLegalMoves(18);
//
//		assertEquals(0, moves.size());
//	}
//
//	@Test
//	public void getLegalMovesNoOpponents() throws IllegalMoveException {
//		board = board.addStartingPieces();
//		// RED
//		List<Space> moves = board.getLegalMoves(22);
//
//		assertEquals(2, moves.size());
//
//		assertMoves(moves, 17, 18);
//
//		// BLACK
//		moves = board.getLegalMoves(10);
//
//		assertEquals(2, moves.size());
//
//		assertMoves(moves, 14, 15);
//	}
//
//	@Test
//	public void getLegalMovesNoOpponentsEdge() throws IllegalMoveException {
//		board = board.addStartingPieces();
//
//		// RED
//		List<Space> moves = board.getLegalMoves(21);
//
//		assertEquals(1, moves.size());
//		assertMoves(moves, 17);
//
//		// BLACK
//		moves = board.getLegalMoves(12);
//
//		assertEquals(1, moves.size());
//		assertMoves(moves, 16);
//	}
//
//	@Test
//	public void getLegalMovesFriendlyBlocked() throws IllegalMoveException {
//		board = board.addStartingPieces();
//
//		// RED
//		List<Space> moves = board.getLegalMoves(7);
//
//		assertEquals(0, moves.size());
//	}
//
//	@Test
//	public void getLegalMovesEnemyBlocked() throws IllegalMoveException {
//		board = board.addStartingPieces();
//
//		// RED
//		List<Space> moves = board.getLegalMoves(7);
//
//		assertEquals(0, moves.size());
//	}
//
//	@Test
//	public void getLegalMovesOneJumpForced() throws IllegalMoveException {
//		// Setup - RED
//		board.addPiece(space(22), Piece.get(RED));
//		board.addPiece(space(18), Piece.get(BLACK));
//
//		// Call Method - RED
//		List<Space> moves = board.getLegalMoves(22);
//
//		// Validate - RED
//		// The jump is forced so only show one legal move
//		assertEquals(1, moves.size());
//		assertMoves(moves, 15);
//
//		// Setup - BLACK
//		board.addPiece(space(3), Piece.get(BLACK));
//		board.addPiece(space(7), Piece.get(RED));
//
//		// Call Method - BLACK
//		moves = board.getLegalMoves(3);
//
//		// Validate - BLACK
//		assertEquals(1, moves.size());
//		assertMoves(moves, 10);
//	}
//
//	@Test
//	public void getLegalMovesMultiJump() throws IllegalMoveException {
//		// Setup - RED
//		Piece jumper = Piece.get(RED);
//		Piece dupe1 = Piece.get(BLACK);
//		Piece dupe2 = Piece.get(BLACK);
//		Piece dupe3 = Piece.get(BLACK);
//		board.addPiece(space(29), jumper);
//		board.addPiece(space(25), dupe1);
//		board.addPiece(space(18), dupe2);
//		board.addPiece(space(11), dupe3);
//
//		// Call Method - RED
//		List<Space> moves = board.getLegalMoves(29);
//
//		// Validate - RED
//		// The jump is forced so only show one legal move
//		assertEquals(1, moves.size());
//		assertMoves(moves, 8);
//
//		// Add another piece
//		Piece dupe3b = Piece.get(BLACK);
//		board.addPiece(space(10), dupe3b);
//
//		// Call Method - RED
//		moves = board.getLegalMoves(29);
//
//		// Validate - RED
//		assertEquals(2, moves.size());
//		assertMoves(moves, 8);
//		assertMoves(moves, 6);
//
//		// Add another piece
//		Piece dupe2b = Piece.get(BLACK);
//		board.addPiece(space(17), dupe2b);
//
//		// Call Method - RED
//		moves = board.getLegalMoves(29);
//
//		// Validate - RED
//		assertEquals(3, moves.size());
//		assertMoves(moves, 8);
//		assertMoves(moves, 6);
//		assertMoves(moves, 13);
//	}
//
//	@Test
//	public void getLegalMovesKingNoJump() throws IllegalMoveException {
//		board = board.addStartingPieces();
//
//		// BLACK
//		board.kingPiece(space(10));
//		board.movePiece(space(10), space(18));
//
//		List<Space> moves = board.getLegalMoves(18);
//
//		assertEquals(2, moves.size());
//		assertMoves(moves, 14, 15);
//
//		// RED
//		board.kingPiece(space(23));
//		board.movePiece(space(23), space(15));
//
//		moves = board.getLegalMoves(15);
//
//		assertEquals(2, moves.size());
//		// Piece is blocked by black king, but can take his place
//		assertMoves(moves, 10, 19);
//	}
//
//	@Test
//	public void getLegalMovesNoOpponentsKing() throws IllegalMoveException {
//		Piece king = Piece.get(RED);
//		king.setKing();
//
//		board.addPiece(space(18), king);
//
//		List<Space> moves = board.getLegalMoves(18);
//
//		assertEquals(4, moves.size());
//		assertMoves(moves, 14, 15, 22, 23);
//	}
	
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
	public void isEqualTo() throws IllegalMoveException {
		//Setup
		board.addPiece(space(18), Piece.get(RED));
		BoardState possBoardState1 = new BoardState(RED);
		possBoardState1.addPiece(space(18), Piece.get(RED));
		
		//Test & Validate
		assertTrue(board.isEqualTo(possBoardState1));
	}
	@Test
	public void isEqualTo_FAIL() throws IllegalMoveException {
		//Setup
		board.addPiece(space(16), Piece.get(RED));
		BoardState possBoardState1 = new BoardState(RED);
		possBoardState1.addPiece(space(18), Piece.get(RED));
		
		//Test & Validate
		assertFalse(board.isEqualTo(possBoardState1));
	}
	private boolean hasState(List<BoardState> expectedStates, BoardState actualState) {
		boolean hasState = false;
		for (BoardState exptectedState : expectedStates) {
			if (exptectedState.isEqualTo(actualState)){
				hasState = true;
				break;
			}
		}
		return hasState;
	}
	
	@Test
	public void getPossibleStatesOpenSpaces() throws IllegalMoveException {
	
		//Setup
		Piece piece = Piece.get(RED);
		board.addPiece(space(22), piece);
		BoardState possBoardState1 = new BoardState(board, RED);
		possBoardState1.movePiece(space(22), space(17));
		BoardState possBoardState2 = new BoardState(board, RED);
		possBoardState2.movePiece(space(22), space(18));
		
		//Test
		List<BoardState> actualStates = board.getPossibleStates(space(22), RED);
		
		//Validate
		assertTrue(actualStates.size() == 2);
		assertStates(actualStates, possBoardState1, possBoardState2);
		
	}
	@Test
	public void getPossibleStatesSingleJump() throws IllegalMoveException {
	
		//Setup - ONE OPTION
		Piece rPiece = Piece.get(RED);
		board.addPiece(space(22), rPiece);
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(space(18), bPiece);
		BoardState possBoardState1 = new BoardState(board, RED);
		possBoardState1.movePiece(space(22), space(15));
		possBoardState1.removePiece(space(18));

		//Test
		List<BoardState> actualStates = board.getPossibleStates(space(22), RED);
		
		//Validate
		assertTrue(actualStates.size() == 1);
		assertStates(actualStates, possBoardState1);
	}
	
	@Test
	public void getPossibleStatesSingleJump_Options() throws IllegalMoveException {
		
		//Setup - ONE OPTION
		Piece rPiece = Piece.get(RED);
		board.addPiece(space(22), rPiece);
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(space(18), bPiece);
		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(space(17), bPiece2);
		BoardState possBoardState1 = new BoardState(board, RED);
		possBoardState1.movePiece(space(22), space(15));
		possBoardState1.removePiece(space(18));
		BoardState possBoardState2 = new BoardState(board, RED);
		possBoardState2.movePiece(space(22), space(13));
		possBoardState2.removePiece(space(17));		
		//Test
		List<BoardState> actualStates = board.getPossibleStates(space(22), RED);
		
		//Validate
		assertTrue(actualStates.size() == 2);
		assertStates(actualStates, possBoardState1, possBoardState2);
	}
	
	@Test
	public void getPossibleStatesMultiJump() throws IllegalMoveException {
		
		//Setup - ONE OPTION
		Piece rPiece = Piece.get(RED);
		board.addPiece(space(22), rPiece);
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(space(18), bPiece);
		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(space(11), bPiece2);
		
		BoardState possBoardState1 = new BoardState(board, RED);
		possBoardState1.movePiece(space(22), space(8));
		possBoardState1.removePiece(space(18));		
		possBoardState1.removePiece(space(11));		
		
		//Test
		List<BoardState> actualStates = board.getPossibleStates(space(22), RED);
		
		//Validate
		assertTrue(actualStates.size() == 1);
		assertStates(actualStates, possBoardState1);
	}
	
	@Test
	public void getPossibleStatesMultiJump_Options() throws IllegalMoveException {
		
		//Setup - ONE OPTION
		Piece rPiece = Piece.get(RED);
		board.addPiece(space(22), rPiece);
		
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(space(18), bPiece);

		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(space(11), bPiece2);
		
		Piece bPiece3 = Piece.get(BLACK);
		board.addPiece(space(10), bPiece3);
		
		//TO B1 to B2
		BoardState possBoardState1 = new BoardState(board, RED);
		possBoardState1.movePiece(space(22), space(8));
		possBoardState1.removePiece(space(18));		
		possBoardState1.removePiece(space(11));		
		
		//TO B1 to B3
		BoardState possBoardState2 = new BoardState(board, RED);
		possBoardState2.movePiece(space(22), space(6));
		possBoardState2.removePiece(space(18));		
		possBoardState2.removePiece(space(10));		
		
		//Test
		List<BoardState> actualStates = board.getPossibleStates(space(22), RED);
		
		//Validate
		assertTrue(actualStates.size() == 2);
		assertStates(actualStates, possBoardState1, possBoardState2);
	}
	
	@Test
	public void getAllPossibleStates() throws IllegalMoveException { 
		//Setup
		
		//Test
		board.getAllPossibleStates(RED);
		
		//Validate
		
		
		
	}
	
	private void assertStates(List<BoardState> states, BoardState... boardStates) { 
		for (BoardState state : boardStates) {
			assertTrue("problem with assert state", hasState(states, state));
		}
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
