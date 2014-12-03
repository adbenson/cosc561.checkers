package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class EndgameEvaluator extends Evaluator {


	public EndgameEvaluator(PlayerColor playerColor, double initialWeight,
			double weightFactor) {
		super(playerColor, initialWeight, weightFactor);
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		if (state.isEndgame()) {
			if (state.winner() == playerColor) {
				return 1;
			}
			else {
				return -1;
			}
		}
		
		return 0;
	}

}
