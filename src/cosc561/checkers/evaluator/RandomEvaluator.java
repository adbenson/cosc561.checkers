package cosc561.checkers.evaluator;

import java.util.Random;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class RandomEvaluator extends Evaluator {
	
	private Random random;

	public RandomEvaluator(PlayerColor playerColor) {
		super(playerColor);
		random = new Random(System.currentTimeMillis());
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		return random.nextDouble();
	}
	
}
