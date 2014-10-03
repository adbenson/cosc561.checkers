package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	protected final PlayerColor playerColor;
	
	public Evaluator(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}

	public abstract int evaluate(BoardState state, PlayerColor currentPlayer);
	
}
