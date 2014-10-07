package cosc561.checkers.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.same;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cosc561.checkers.Player;
import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.evaluator.Evaluator.EvaluatedState;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import static cosc561.checkers.model.PlayerColor.*;

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
		evaluated.add(scored.get(3));
		evaluated.add(scored.get(6));
		evaluated.add(scored.get(9));
		evaluated.add(scored.get(7));
		
		when(currentState.getAllPossibleStates(RED)).thenReturn(states);
		
		when(eval.evaluate(states, RED)).thenReturn(evaluated);

		next = player.negamax(current, 1, RED);

		assertEquals(scored.get(3), next);
	}
	
	@Test
	public void testNextMoveDepth2() throws IllegalMoveException {
		
		evaluated.add(scored.get(5)); // set 1
		evaluated.add(scored.get(6)); // set 2
		evaluated.add(scored.get(7)); // set 3
		
		when(currentState.getAllPossibleStates(RED)).thenReturn(states);
		when(eval.evaluate(states, RED)).thenReturn(evaluated);
		
		List<BoardState> states1 = new ArrayList<>();
		when(scored.get(5).state.getAllPossibleStates(BLACK)).thenReturn(states1);
		List<EvaluatedState> evaluated1 = new ArrayList<>();
		when(eval.evaluate(same(states1), same(BLACK))).thenReturn(evaluated1);
		
		evaluated1.add(scored.get(2));
		evaluated1.add(scored.get(8)); 
		evaluated1.add(scored.get(4));
		
		List<BoardState> states2 = new ArrayList<>();
		when(scored.get(6).state.getAllPossibleStates(BLACK)).thenReturn(states2);
		List<EvaluatedState> evaluated2 = new ArrayList<>();
		when(eval.evaluate(same(states2), same(BLACK))).thenReturn(evaluated2);
		
		evaluated2.add(scored.get(1)); //Worst of set 3
		evaluated2.add(scored.get(9));
		evaluated2.add(scored.get(11)); 
		
		List<BoardState> states3 = new ArrayList<>();
		when(scored.get(7).state.getAllPossibleStates(BLACK)).thenReturn(states3);
		List<EvaluatedState> evaluated3 = new ArrayList<>();
		when(eval.evaluate(same(states3), same(BLACK))).thenReturn(evaluated3);
		
		evaluated3.add(scored.get(13)); 
		evaluated3.add(scored.get(3)); //Worst of set 3
		evaluated3.add(scored.get(12)); 

		next = player.negamax(current, 2, RED);

		assertEquals(scored.get(8), next);
	}
	
	@Test
	public void testNextMoveDepth3() throws IllegalMoveException {
		
//		evaluated.add(score5);
//		evaluated.add(score3);
//		evaluated.add(score6);
//		evaluated.add(score9);
//		evaluated.add(score7);
//		
//		when(currentState.getAllPossibleStates(RED)).thenReturn(states);
//		
//		when(eval.evaluate(states, RED)).thenReturn(evaluated);
//
//		next = player.negamax(current, 1, RED);
//
//		assertEquals(score3, next);
	}

}
