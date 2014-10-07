package cosc561.checkers.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {
	
	Player player;
	
	@Mock
	BoardState state;
	BoardState next;
	
	EvaluatedState evaluated;
	EvaluatedState chosen;
	Evaluator eval;
	
	BoardState unplayed;
	
	@Before
	public void setUp() throws IllegalMoveException {
		state = mock(BoardState.class);
		unplayed = mock(BoardState.class);
		eval = mock(Evaluator.class);
		evaluated = new EvaluatedState(state, 1);
		
		
		
		player = new Player(RED, 0);
	}
	
	@Test
	public void testNextMoveZeroDepth() throws IllegalMoveException {
				
		player = new Player(RED, 0);
		player.setEvaluator(eval);
		
		when(state.getFirstUnplayed()).thenReturn(unplayed);
		
		EvaluatedState evalulated = new EvaluatedState(state, 1);
		when(eval.evaluate(state, RED)).thenReturn(evalulated);
		
		next = player.nextMove(state); 
		
		assertEquals(unplayed, next);
	}
	
	@Test
	public void testNextMoveEndgame() throws IllegalMoveException {
				
		player = new Player(RED, 6);
		player.setEvaluator(eval);
		
		when(state.getFirstUnplayed()).thenReturn(unplayed);
		when(state.isEndgame()).thenReturn(true);
		
		EvaluatedState evalulated = new EvaluatedState(state, 1);
		when(eval.evaluate(state, RED)).thenReturn(evalulated);
		
		next = player.nextMove(state); 
		
		assertEquals(state, next);
	}

}
