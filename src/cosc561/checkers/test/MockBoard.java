package cosc561.checkers.test;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class MockBoard extends BoardState {
	
	public List<BoardState> children = new ArrayList<BoardState>();
	
	public double score = 0;

	public MockBoard(double score) {
		super(PlayerColor.RED);
		this.score = score;
	}
	
	@Override
	public List<BoardState> getAllPossibleStates(PlayerColor color) {
		return children;
	}

	public String toString() {
		return "MockBoard #"+score;
	}
}
