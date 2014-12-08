package cosc561.checkers.evaluator;

import java.util.Random;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class RandomEvaluator extends Evaluator {
	
	public RandomEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
		random = new Random(System.currentTimeMillis());
	}

	private Random random;

	@Override
	protected double evaluateInternal(BoardState state, PlayerColor playerColor) {
		return random.nextDouble();
	}
	
}
