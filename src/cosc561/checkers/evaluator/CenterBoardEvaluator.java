package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PieceMap.Entry;

public class CenterBoardEvaluator extends Evaluator {
	
	private static final double HALF_BOARD = (Grid.SPACES_PER_SIDE - 1) / 2.0;

	public CenterBoardEvaluator(PlayerColor playerColor) {
		super(playerColor);
	}

	@Override
	protected double evaluateInternal(BoardState state) {
		double value = 0;
		
		for (Entry e : state.getPieces().iterateSpaces()) {
			double distance = Math.floor(Math.abs(e.space.row - HALF_BOARD));
			if (e.piece.color == playerColor) {
				value -= distance;
			}
			else {
				value += distance;
			}
		}
		return value;
	}

	protected double getRangeMin() {
		return -28;
	}

	protected double getRangeMax() {
		return 28;
	}
}
