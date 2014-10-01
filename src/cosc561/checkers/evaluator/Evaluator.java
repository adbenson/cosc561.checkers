package cosc561.checkers.evaluator;

import java.util.List;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	protected final PlayerColor playerColor;
	
	public Evaluator(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}

	public abstract int evaluate(BoardState state, PlayerColor currentPlayer);
	
	public BoardState chooseBest(List<BoardState> candidates, PlayerColor forPlayer) {
		int bestScore = 0;

		BoardState bestState = null;
		
		for (BoardState state : candidates) {
			if (bestState == null || evaluate(state, forPlayer) > bestScore) {
				bestState = state;
			}
		}
		
		return bestState;
	}
	
}
