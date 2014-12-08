package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class NaiveEvaluator extends Evaluator {

	public NaiveEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	@Override
	public double evaluate(BoardState state, PlayerColor playerColor) {
		return 1;
	}

}
