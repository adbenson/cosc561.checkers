package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.Piece.Color;

public abstract class Evaluator {
	
	protected final Piece.Color playerColor;
	
	public Evaluator(Piece.Color playerColor) {
		this.playerColor = playerColor;
	}

	public abstract int evaluate(BoardState state, Color currentPlayer);
	
}
