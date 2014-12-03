package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class NaiveEvaluator extends Evaluator {

	public NaiveEvaluator(PlayerColor playerColor, double initialWeight, double weightFactor) {
		super(playerColor, initialWeight, weightFactor);
	}

	@Override
	public double evaluateInternal(BoardState state) {
		return 1;
	}

}
