package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Color;

public class NaiveEvaluator extends Evaluator {
	
	
	public NaiveEvaluator(Color color) {
		super(color);
	}

	@Override
	public int evaluate(BoardState state, Color currentPlayer) {
		return 0;
	}

}
