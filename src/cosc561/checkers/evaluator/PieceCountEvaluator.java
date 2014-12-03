package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PlayerColor;

public class PieceCountEvaluator extends Evaluator {
	
	public PieceCountEvaluator(PlayerColor playerColor, double initialWeight, double weightFactor) {
		super(playerColor, initialWeight, weightFactor);
	}

	public static final double KING_FACTOR = 1.5;

	@Override
	public double evaluateInternal(BoardState state) {
		int value = 0;
		
		for (Piece piece : state.getPieces()) {
			int pValue = (piece.color == playerColor)? 1 : -1;
			if (piece.isKing()) {
				pValue *= KING_FACTOR;
			}
			value += pValue;
		}
			
		return value;
	}

	@Override
	protected double getRangeMin() {
		return -12 * KING_FACTOR;
	}

	@Override
	protected double getRangeMax() {
		return 12 * KING_FACTOR;
	}

}
