package cosc561.checkers.test;

import static cosc561.checkers.model.PlayerColor.BLACK;
import static cosc561.checkers.model.PlayerColor.RED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import cosc561.checkers.Player;
import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.IllegalMoveException;

public class SearchTest {
	
	Player player;
	
	@Mock
	BoardState currentState;
	BoardState nextState;
	
	EvaluatedState current;
	EvaluatedState next;

	Evaluator eval;
	
	List<BoardState> states;
	List<EvaluatedState> evaluated;
	
	ArrayList<EvaluatedState> scored;
	
	@Before
	public void setUp() throws IllegalMoveException {
		currentState = mock(BoardState.class);
		nextState = mock(BoardState.class);
		
		eval = mock(Evaluator.class);
		
		current = new EvaluatedState(currentState, 1);
		next = new EvaluatedState(nextState, 1);
		
		player = new Player(RED, 0);
		player.setEvaluator(eval);
		
		states = new ArrayList<BoardState>();
		evaluated = new ArrayList<EvaluatedState>();
		
		scored = new ArrayList<>();
		
		for (int i=0; i<15; i++) {
			scored.add(new EvaluatedState(mock(BoardState.class), i));
		}
	
	}

	@Test
	public void testNegamaxDepth0() throws IllegalMoveException {

		next = player.negamax(current, 0, RED);

		assertEquals(current, next);
	}
	
	@Test
	public void testNegamaxEndgame() throws IllegalMoveException {
		
		when(currentState.isEndgame()).thenReturn(true);

		next = player.negamax(current, 1, RED);

		assertEquals(current, next);
	}
	
	@Test
	public void testNegamaxDepth1() throws IllegalMoveException {
		
		evaluated.add(scored.get(5));
		evaluated.add(scored.get(3)); //worst
		evaluated.add(scored.get(6));
		evaluated.add(scored.get(9)); //best
		evaluated.add(scored.get(7));
		
		when(currentState.getAllPossibleStates(RED)).thenReturn(states);
		
		when(eval.evaluate(states, RED)).thenReturn(evaluated);

		next = player.negamax(current, 1, RED);

		assertEquals(scored.get(9), next);
	}
	
	@Test
	public void testNextMoveDepth2() throws IllegalMoveException {
		List<MockBoard> all = generateBoards(15);
		
		MockBoard init = all.get(0);
		init.children.add(all.get(5));
		init.children.add(all.get(6));
		init.children.add(all.get(7));
		
		all.get(5).children.add(all.get(2)); //worst
		all.get(5).children.add(all.get(8)); //best worst
		all.get(5).children.add(all.get(4));
		
		all.get(6).children.add(all.get(1)); //worst
		all.get(6).children.add(all.get(9));
		all.get(6).children.add(all.get(11)); //best
		
		all.get(7).children.add(all.get(13)); //best
		all.get(7).children.add(all.get(3)); //worst best
		all.get(7).children.add(all.get(12)); 
		
		player.setEvaluator(new MockEvaluator(RED));
		next = player.negamax(new EvaluatedState(init, 0), 2, RED);
		
		assertSame(all.get(8), next.state);
		
		player.setEvaluator(new MockEvaluator(BLACK));
		next = player.negamax(new EvaluatedState(init, 0), 2, BLACK);

		assertSame(all.get(3), next.state);
	}
	
	@Test
	public void testNextMoveDepth3() throws IllegalMoveException {
		
		List<MockBoard> all = generateBoards(40);
		
		MockBoard init = all.get(0);
		init.children.add(all.get(5));
		init.children.add(all.get(6));
		init.children.add(all.get(7));
		
			all.get(5).children.add(all.get(2)); //worst
			all.get(5).children.add(all.get(8)); //best worst
			all.get(5).children.add(all.get(4));
			
				all.get(2).children.add(all.get(25)); 
				all.get(2).children.add(all.get(20)); //worst best worst ***
				all.get(2).children.add(all.get(37)); //best
				
				all.get(8).children.add(all.get(31)); //best
				all.get(8).children.add(all.get(27)); 
				all.get(8).children.add(all.get(15)); //worst
				
				all.get(4).children.add(all.get(18)); //worst
				all.get(4).children.add(all.get(19));
				all.get(4).children.add(all.get(22)); //best worst
			
			all.get(6).children.add(all.get(1)); //worst
			all.get(6).children.add(all.get(9));
			all.get(6).children.add(all.get(11)); //best
			
				all.get(1).children.add(all.get(23)); //worst best
				all.get(1).children.add(all.get(35)); //best
				all.get(1).children.add(all.get(29)); 
				
				all.get(9).children.add(all.get(28));  
				all.get(9).children.add(all.get(38)); //best
				all.get(9).children.add(all.get(16)); //worst
				
				all.get(11).children.add(all.get(30)); //best worst
				all.get(11).children.add(all.get(33));
				all.get(11).children.add(all.get(14)); //worst
				
			all.get(7).children.add(all.get(13)); //best
			all.get(7).children.add(all.get(3));  //worst best
			all.get(7).children.add(all.get(12)); 
			
				all.get(13).children.add(all.get(34)); //best worst best ***
				all.get(13).children.add(all.get(26)); 
				all.get(13).children.add(all.get(21)); //worst best
				
				all.get(3).children.add(all.get(17)); //worst
				all.get(3).children.add(all.get(36)); //best
				all.get(3).children.add(all.get(24)); 
				
				all.get(12).children.add(all.get(15)); //worst
				all.get(12).children.add(all.get(39)); //best
				all.get(12).children.add(all.get(32)); 
		
		player.setEvaluator(new MockEvaluator(RED));
		next = player.negamax(new EvaluatedState(init, 0), 3, RED);

		assertSame(all.get(34), next.state);
		
		player.setEvaluator(new MockEvaluator(BLACK));
		next = player.negamax(new EvaluatedState(init, 0), 3, BLACK);
		
		assertSame(all.get(20), next.state);
	}

	
	private List<MockBoard> generateBoards(int n) {
		List<MockBoard> boards = new ArrayList<>();
		
		for (int i=0; i<n; i++) {
			boards.add(new MockBoard(i));
		}
		
		return boards;
	}
}
