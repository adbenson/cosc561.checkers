package cosc561.checkers.evaluator;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public class CompoundEvaluator extends Evaluator {
	
	List<Evaluator> evaluators;

	public CompoundEvaluator() {
		super(1.0, 1.0);
		evaluators = new ArrayList<>();
		
		//The primary evaluator. Piece count is pretty much always important.
		evaluators.add(new PieceCountEvaluator(10.0, 2.0));
		
		//Winning or losing should eclipse all other evaluations
		//It will normally return 0 to have no impact
		evaluators.add(new EndgameEvaluator(10.0, 10.0));
		
		
		evaluators.add(new KingPieceEvaluator(3.0, 1.0));
		evaluators.add(new HomeRowEvaluator(2.0, 0.0));
		evaluators.add(new CenterBoardEvaluator(1.0, 1.5));
		evaluators.add(new ProximityEvaluator(0.25, 100.0));
		
		evaluators.add(new RandomEvaluator(0.2, 0.0));

	}

	@Override
	public double evaluate(BoardState state, PlayerColor player) {
		
		double cumulativeScore = 0;
		
		for (Evaluator eval : evaluators) {
			double score = eval.normalize(eval.evaluate(state, player));

			cumulativeScore += score * eval.getWeight(state.getPieces().getPieceCount());
		}
		
		return cumulativeScore;
	}

	//This is so parent class doesn't re-normalize our scores
	@Override
	public double normalize(double score) {
		return score;
	}

	public List<Evaluator> getEvaluators() {
		return new ArrayList<>(evaluators);
	}

}
