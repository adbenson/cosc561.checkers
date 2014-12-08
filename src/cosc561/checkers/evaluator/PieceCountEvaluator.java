package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class PieceCountEvaluator extends Evaluator {
	
	public PieceCountEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	public static final double KING_FACTOR = 1.5;

	@Override
	public double evaluate(BoardState state, PlayerColor playerColor) {
		int playerCount = state.getPieces().getPieceCount(playerColor);
		int opponentCount = state.getPieces().getPieceCount(playerColor.opponent());
		
		return playerCount - opponentCount;		
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
