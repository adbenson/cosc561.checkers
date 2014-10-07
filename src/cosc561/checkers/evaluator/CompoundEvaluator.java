package cosc561.checkers.evaluator;

import java.util.HashMap;
import java.util.Map;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class CompoundEvaluator extends Evaluator {
	
	Map<Evaluator, Double> evaluators;

	public CompoundEvaluator(PlayerColor playerColor) {
		super(playerColor);
		evaluators = new HashMap<>();
		
		evaluators.put(new PieceCountEvaluator(playerColor), 1.0);
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		double cumulativeScore = 0;
		
		for (Map.Entry<Evaluator, Double> entry : evaluators.entrySet()) {
			double score = (entry.getKey()).evaluateInternal(state);
			//Weight the value appropriately
			cumulativeScore += super.normalize(score) * entry.getValue();
		}
		
		return cumulativeScore;
	}

	//This is so parent class doesn't re-normalize our scores
	protected double normalize(double score) {
		return score;
	}

}
