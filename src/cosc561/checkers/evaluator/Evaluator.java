package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Color;

public abstract class Evaluator {
	
	protected final Color playerColor;
	
	public Evaluator(Color playerColor) {
		this.playerColor = playerColor;
	}

	public abstract int evaluate(BoardState state, Color currentPlayer);
	
}
