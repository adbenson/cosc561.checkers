package cosc561.checkers.test;

import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class MockEvaluator extends Evaluator {

	public MockEvaluator(PlayerColor playerColor) {
		super(playerColor);
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		return ((MockBoard)state).score;
	}
	
	@Override
	protected double normalize(double score) {
		return score;
	}

}
