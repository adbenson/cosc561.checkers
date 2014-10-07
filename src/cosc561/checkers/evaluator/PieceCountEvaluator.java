package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PlayerColor;

public class PieceCountEvaluator extends Evaluator {

	public PieceCountEvaluator(PlayerColor playerColor) {
		super(playerColor);
	}

	@Override
	public double evaluateInternal(BoardState state) {
		int value = 0;
		
		for (Piece piece : state.getPieces()) {
			value += (piece.color == playerColor)? 1 : -1;
		}
			
		return value;
	}

	@Override
	protected double getRangeMin() {
		return -12;
	}

	@Override
	protected double getRangeMax() {
		return 12;
	}

}
