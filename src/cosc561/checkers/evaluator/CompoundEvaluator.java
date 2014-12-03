package cosc561.checkers.evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class CompoundEvaluator extends Evaluator {
	
	private static boolean debug = false;
	
	List<Evaluator> evaluators;

	public CompoundEvaluator(PlayerColor playerColor) {
		super(playerColor, 1.0, 1.0);
		evaluators = new ArrayList<>();
		
		//The primary evaluator. Piece count is pretty much always important.
		evaluators.add(new PieceCountEvaluator(playerColor, 5.0, 2.0));
		
		//Winning or losing should eclipse all other evaluations
		//It will normally return 0 to have no impact
		evaluators.add(new EndgameEvaluator(playerColor, 10.0, 10.0));
		
		
		evaluators.add(new KingPieceEvaluator(playerColor, 2.0, 1.0));
		evaluators.add(new HomeRowEvaluator(playerColor, 1.0, 0.25));
		evaluators.add(new CenterBoardEvaluator(playerColor, 1.0, 1.0));
		
		evaluators.add(new RandomEvaluator(playerColor, 0.5, 0.1));
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		
		double cumulativeScore = 0;
		
		if (debug) {
			System.out.println(state);
		}
		
		for (Evaluator eval : evaluators) {
			double score = eval.evaluateNormal(state);
			
			if (debug) {
				System.out.println(eval.getClass().getSimpleName()+": "+score);
			}
			
			if (debug) {
				System.out.println("Weight: "+ eval.getWeight(state.getPieces().getPieceCount()));
			}
			cumulativeScore += score * eval.getWeight(state.getPieces().getPieceCount());
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
