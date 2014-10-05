package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class NaiveEvaluator extends Evaluator {
	
	
	public NaiveEvaluator(PlayerColor color) {
		super(color);
	}

	@Override
	public EvaluatedState evaluate(BoardState state, PlayerColor currentPlayer) {
		return new EvaluatedState(state, 0);
	}

}
