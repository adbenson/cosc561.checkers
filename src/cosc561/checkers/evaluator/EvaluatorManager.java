package cosc561.checkers.evaluator;

import cosc561.checkers.model.PlayerColor;

public class EvaluatorManager {

	public static Evaluator getEvaluator(PlayerColor color) {
		return new NaiveEvaluator(color);
	}
}
