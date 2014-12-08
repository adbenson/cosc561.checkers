package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PlayerColor;

public class HomeRowEvaluator extends Evaluator {

	public HomeRowEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	@Override
	protected double evaluateInternal(BoardState state, PlayerColor playerColor) {
		double value = 0;
		
		for (Entry e : state.getPieces().iterateSpaces()) {
			if (e.space.inHomeRow(e.piece.color)) {
				if (e.piece.color == playerColor) {
					value += 1;
				}
				else {
					value += -1;
				}
			}
		}

		return value;
	}
	
	protected double getRangeMin() {
		return -4;
	}

	protected double getRangeMax() {
		return 4;
	}

}
