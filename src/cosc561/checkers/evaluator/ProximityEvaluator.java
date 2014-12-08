package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap;
import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PlayerColor;

public class ProximityEvaluator extends Evaluator {
	
	public ProximityEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	private static final double HALF_BOARD = (Grid.SPACES_PER_SIDE - 1) / 2.0;


	@Override
	public double evaluate(BoardState state, PlayerColor playerColor) {
		PieceMap pieces = state.getPieces();
		
		double totalDistance = 0;
		
		for (Entry player : pieces.iterateSpaces(playerColor)) {
			for (Entry opponent : pieces.iterateSpaces(playerColor.opponent())) {
				double distance = player.space.distance(opponent.space);
				
				totalDistance += distance;
			}
		}
		
		return -totalDistance;
	}
	
	public double normalize(double score) {
		return - (score / getRangeMin());
	}

	protected double getRangeMin() {
		return -1100;
	}

	protected double getRangeMax() {
		return 0;
	}
}
