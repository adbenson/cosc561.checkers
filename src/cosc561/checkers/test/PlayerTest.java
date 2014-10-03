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
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {
	
	Player player;
	
	@Mock
	BoardState state;
	BoardState next;
	
	BoardState unplayed;
	
	@Before
	public void setUp() throws IllegalMoveException {
		state = mock(BoardState.class);
	}
	
	private void initReturnStates() throws IllegalMoveException {
		List<BoardState> returnStates = new ArrayList<>();
		
		when(state.getAllPossibleStates(PlayerColor.RED)).thenReturn(returnStates);
		when(state.getAllPossibleStates(PlayerColor.BLACK)).thenReturn(returnStates);
		
		unplayed = mock(BoardState.class);
		when(state.getFirstUnplayed()).thenReturn(unplayed);
		
		returnStates.add(state);
		returnStates.add(state);
		returnStates.add(state);
	}

	@Test
	public void testNextMoveDepth1() throws IllegalMoveException {

		initReturnStates();
		
		player = new Player(PlayerColor.RED, 1);
		
		next = player.nextMove(state); 
		
		assertEquals(unplayed, next);
		verify(state, times(1)).getAllPossibleStates(PlayerColor.RED);
		verify(state, times(0)).getAllPossibleStates(PlayerColor.BLACK);
	}
	
	@Test
	public void testNextMoveDepth2() throws IllegalMoveException {
		
		initReturnStates();
		
		player = new Player(PlayerColor.RED, 2);
		
		next = player.nextMove(state); 
		
		assertEquals(unplayed, next);
		verify(state, times(1)).getAllPossibleStates(PlayerColor.RED);
		verify(state, times(3)).getAllPossibleStates(PlayerColor.BLACK);
	}
	
	@Test
	public void testNextMoveDepth3() throws IllegalMoveException {
		
		initReturnStates();
				
		player = new Player(PlayerColor.RED, 3);
		
		next = player.nextMove(state); 
		
		assertEquals(unplayed, next);
		verify(state, times(10)).getAllPossibleStates(PlayerColor.RED);
		verify(state, times(3)).getAllPossibleStates(PlayerColor.BLACK);
	}

}
