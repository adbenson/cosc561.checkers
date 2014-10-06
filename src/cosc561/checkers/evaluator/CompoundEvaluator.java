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
		
		evaluators.put(new PieceCountEvaluator(playerColor), 0.5);
	}

	@Override
	public EvaluatedState evaluate(BoardState state, PlayerColor currentPlayer) {
		double cumulativeScore = 0;
		
		for (Map.Entry<Evaluator, Double> entry : evaluators.entrySet()) {
			cumulativeScore += entry.getKey().evaluate(state, currentPlayer).score * entry.getValue();
		}
		
		return new EvaluatedState(state, cumulativeScore);
	}

}
