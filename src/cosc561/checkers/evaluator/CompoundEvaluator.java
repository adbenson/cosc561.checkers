package cosc561.checkers.evaluator;

import java.util.HashMap;
import java.util.Map;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class CompoundEvaluator extends Evaluator {
	
	private static boolean debug = false;
	
	Map<Evaluator, Double> evaluators;

	public CompoundEvaluator(PlayerColor playerColor) {
		super(playerColor);
		evaluators = new HashMap<>();
		
		evaluators.put(new PieceCountEvaluator(playerColor), 1.0);
		//Winning or losing should eclipse all other evaluations
		//It will normally return 0 to have no impact
		evaluators.put(new EndgameEvaluator(playerColor), 10.0);
		evaluators.put(new KingPieceEvaluator(playerColor), 5.0);
		evaluators.put(new HomeRowEvaluator(playerColor), 0.25);
		evaluators.put(new CenterBoardEvaluator(playerColor), 1.0);
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		double cumulativeScore = 0;
		
		if (debug) {
			System.out.println(state);
		}
		
		for (Map.Entry<Evaluator, Double> entry : evaluators.entrySet()) {
			double score = (entry.getKey()).evaluateInternal(state);
			//Weight the value appropriately
			double normalized = entry.getKey().normalize(score) * entry.getValue();
			
			if (debug) {
				System.out.println(entry.getKey().getClass().getSimpleName()+": "+normalized);
			}
			
			cumulativeScore += normalized * entry.getValue();
		}
		
		if (debug) {
			System.out.println("Cumulative: "+cumulativeScore);
		}
		
		return cumulativeScore;
	}

	//This is so parent class doesn't re-normalize our scores
	protected double normalize(double score) {
		return score;
	}

}
