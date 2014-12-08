package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap;
import cosc561.checkers.model.PlayerColor;

public class EndgameEvaluator extends Evaluator {


	public EndgameEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	@Override
	public double evaluate(BoardState state, PlayerColor playerColor) {
		if (state.isEndgame()) {
			PlayerColor winner = state.getWinner();
			if (winner == playerColor) {
				return 1;
			}
			else if (winner == playerColor.opponent()) {
				return -1;
			}
		}
		
		return 0;
	}

}
