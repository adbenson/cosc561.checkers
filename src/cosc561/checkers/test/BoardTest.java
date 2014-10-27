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
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn.Move;
import cosc561.checkers.model.Space;

public class BoardTest {

	private BoardState board;
	
	static {
		System.out.println(Grid.getInstance());
	}

	@Before
	public void setUp() throws IllegalMoveException {
		board = new BoardState(RED);
	}

	@Test
	public void getLegalMovesEmptySpace() throws IllegalMoveException {
		List<BoardState> moves = board.getPossibleStates(space(18), RED);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesNoOpponents() throws IllegalMoveException {
		board = board.addStartingPieces();
		// RED
		List<BoardState> moves = board.getPossibleStates(space(22), RED);

		assertEquals(2, moves.size());

		assertMoves(moves, Piece.get(RED), 17, 18);

		// BLACK
		moves = board.getPossibleStates(space(10), RED);

		assertEquals(2, moves.size());

		assertMoves(moves, Piece.get(BLACK), 14, 15);
	}

	@Test
	public void getLegalMovesNoOpponentsEdge() throws IllegalMoveException {
		board = board.addStartingPieces();

		// RED
		List<BoardState> moves = board.getPossibleStates(space(21), RED);

		assertEquals(1, moves.size());
		assertMoves(moves, Piece.get(RED), 17);

		// BLACK
		moves = board.getPossibleStates(space(12), RED);

		assertEquals(1, moves.size());
		assertMoves(moves, Piece.get(BLACK), 16);
	}

	@Test
	public void getLegalMovesFriendlyBlocked() throws IllegalMoveException {
		board = board.addStartingPieces();

		// RED
		List<BoardState> moves = board.getPossibleStates(space(7), RED);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesEnemyBlocked() throws IllegalMoveException {
		board = board.addStartingPieces();

		// RED
		List<BoardState> moves = board.getPossibleStates(space(7), RED);

		assertEquals(0, moves.size());
	}

	@Test
	public void getLegalMovesOneJumpForced() throws IllegalMoveException {
		// Setup - RED
		board.addPiece(Piece.get(RED), space(22));
		board.addPiece(Piece.get(BLACK), space(18));

		// Call Method - RED
		List<BoardState> moves = board.getPossibleStates(space(22), RED);

		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(1, moves.size());
		assertMoves(moves, Piece.get(RED), 15);

		// Setup - BLACK
		board.addPiece(Piece.get(BLACK), space(3));
		board.addPiece(Piece.get(RED), space(7));

		// Call Method - BLACK
		moves = board.getPossibleStates(space(3), RED);

		// Validate - BLACK
		assertEquals(1, moves.size());
		assertMoves(moves, Piece.get(BLACK), 10);
	}

	@Test
	public void getLegalMovesMultiJump() throws IllegalMoveException {
		// Setup - RED
		Piece jumper = Piece.get(RED);
		Piece dupe1 = Piece.get(BLACK);
		Piece dupe2 = Piece.get(BLACK);
		Piece dupe3 = Piece.get(BLACK);
		board.addPiece(jumper, space(29));
		board.addPiece(dupe1, space(25));
		board.addPiece(dupe2, space(18));
		board.addPiece(dupe3, space(11));

		// Call Method - RED
		List<BoardState> moves = board.getPossibleStates(space(29), RED);

		// Validate - RED
		// The jump is forced so only show one legal move
		assertEquals(1, moves.size());
		assertMoves(moves, Piece.get(RED), 8);

		// Add another piece
		Piece dupe3b = Piece.get(BLACK);
		board.addPiece(dupe3b, space(10));

		// Call Method - RED
		moves = board.getPossibleStates(space(29), RED);

		// Validate - RED
		assertEquals(2, moves.size());
		assertMoves(moves, Piece.get(RED), 8);
		assertMoves(moves, Piece.get(RED), 6);

		// Add another piece
		Piece dupe2b = Piece.get(BLACK);
		board.addPiece(dupe2b, space(17));

		// Call Method - RED
		moves = board.getPossibleStates(space(29), RED);

		// Validate - RED
		assertEquals(3, moves.size());
		assertMoves(moves, Piece.get(RED), 8);
		assertMoves(moves, Piece.get(RED), 6);
		assertMoves(moves, Piece.get(RED), 13);
	}

	@Test
	public void getLegalMovesKingNoJump() throws IllegalMoveException {
		board = board.addStartingPieces();

		// BLACK
		board.kingPiece(space(10));
		board.movePiece(space(10), space(18));

		List<BoardState> moves = board.getPossibleStates(space(18), RED);

		assertEquals(2, moves.size());
		assertMoves(moves, Piece.get(BLACK).getKing(), 14, 15);

		// RED
		board.kingPiece(space(23));
		board.movePiece(space(23), space(15));

		moves = board.getPossibleStates(space(15), RED);

		assertEquals(2, moves.size());
		// Piece is blocked by black king, but can take his place
		assertMoves(moves, Piece.get(RED).getKing(), 10, 19);
	}

	@Test
	public void getLegalMovesNoOpponentsKing() throws IllegalMoveException {
		Piece king = Piece.get(RED);

		board.addPiece(king, space(18));
		board.kingPiece(space(18));

		List<BoardState> moves = board.getPossibleStates(space(18), RED);

		assertEquals(4, moves.size());
		assertMoves(moves, Piece.get(RED).getKing(), 14, 15, 22, 23);
	}
	
	private Space space(int id) {
		return Grid.getInstance().getSpaceById(id);
	}

	private void assertMoves(List<BoardState> moves, Piece piece, int... spaceIds) {
		for (int id : spaceIds) {
			assertTrue("Move to space " + id + " not found in (" + moves + ")", hasMove(moves, piece, id));
		}
	}

	private boolean hasMove(List<BoardState> list, Piece piece, int id) {
		for (BoardState s : list) {
			if (s.getPiece(Grid.getInstance().getSpaceById(id)) == piece) {
				return true;
			}
		}

		return false;
	}

	private boolean hasState(List<BoardState> expectedStates, BoardState actualState) {
		boolean hasState = false;
		for (BoardState exptectedState : expectedStates) {
			if (exptectedState.equals(actualState)){
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
		board.addPiece(piece, space(22));
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
		board.addPiece(rPiece, space(22));
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(bPiece, space(18));
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
		board.addPiece(rPiece, space(22));
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(bPiece, space(18));
		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(bPiece2, space(17));
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
		board.addPiece(rPiece, space(22));
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(bPiece, space(18));
		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(bPiece2, space(11));
		
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
		board.addPiece(rPiece, space(22));
		
		Piece bPiece = Piece.get(BLACK);
		board.addPiece(bPiece, space(18));

		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(bPiece2, space(11));
		
		Piece bPiece3 = Piece.get(BLACK);
		board.addPiece(bPiece3, space(10));
		
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
	public void getAllPossibleStatesEmptyBoard() throws IllegalMoveException { 
		//Setup
		
		//Test
		List<BoardState> red = board.getAllPossibleStates(RED);
		List<BoardState> black = board.getAllPossibleStates(BLACK);
		
		
		//Validate
		assertEquals(0, red.size());
		assertEquals(0, black.size());
		
	}
	
	@Test
	public void getAllPossibleStates() throws IllegalMoveException { 
		//Setup
		board = board.addStartingPieces();
		
		//Test
		List<BoardState> states = board.getAllPossibleStates(RED);
				
		//Validate
		assertEquals(7, states.size());
		
	}
	
	private void assertStates(List<BoardState> states, BoardState... boardStates) { 
		for (BoardState state : boardStates) {
			assertTrue("problem with assert state", hasState(states, state));
		}
	}
	
	@Test
	public void testEquals() throws IllegalMoveException {
		BoardState b2 = new BoardState(PlayerColor.RED);
		assertTrue(board.equals(b2));
		
		b2 = b2.addStartingPieces();
		assertFalse(board.equals(b2));
		
		board = board.addStartingPieces();
		assertTrue(board.equals(b2));
		
		board.movePiece(space(22), space(18));
		assertFalse(board.equals(b2));
		
		b2.movePiece(space(22), space(18));
		assertTrue(board.equals(b2));
	}
	
	@Test
	public void testIsRepeat() throws IllegalMoveException {

		board.addPiece(Piece.get(RED), space(22));
		
		board.movePiece(space(22), space(18));
		
		board = new BoardState(board, BLACK);
		board = new BoardState(board, RED);
		
		board.movePiece(space(18), space(22));
		
		board = new BoardState(board, BLACK);
		
		Move move = new Move(Piece.get(RED), space(22), space(18));
		assertTrue(board.isRepeat(move));
		
	}
	
	@Test
	public void testForceJumpGlobal() throws IllegalMoveException {
		//Setup - Red has two pieces
		//This one has two open spaces
		Piece rPiece1 = Piece.get(RED);
		board.addPiece(rPiece1, space(14));

		//This one has two jump options
		Piece rPiece2 = Piece.get(RED);
		board.addPiece(rPiece2, space(27));

		//This is the piece to be jumped
		Piece bPiece1 = Piece.get(BLACK);
		board.addPiece(bPiece1, space(23));
		
		//This is a piece to be multi-jumped
		Piece bPiece3 = Piece.get(BLACK);
		board.addPiece(bPiece3, space(15));
		
		//This is another piece to be single jumped
		Piece bPiece2 = Piece.get(BLACK);
		board.addPiece(bPiece2, space(24));
		
		//Jumping 23 & 15
		BoardState jumpOption1 = new BoardState(board, RED);
		jumpOption1.movePiece(space(27), space(11));
		jumpOption1.removePiece(space(23));
		jumpOption1.removePiece(space(15));

		//Jumping 24
		BoardState jumpOption2 = new BoardState(board, RED);
		jumpOption2.movePiece(space(27), space(20));
		jumpOption2.removePiece(space(24));

		// Test
		List<BoardState> actualStates = board.getAllPossibleStates(RED);

		// Validate
		assertTrue(actualStates.size() == 2);
		assertStates(actualStates, jumpOption1, jumpOption2);

	}
	
	

}
