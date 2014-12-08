package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap;
import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PlayerColor;

public class CenterBoardEvaluator extends Evaluator {
	
	public CenterBoardEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	private static final double HALF_BOARD = (Grid.SPACES_PER_SIDE - 1) / 2.0;


	@Override
	public double evaluate(BoardState state, PlayerColor playerColor) {
		PieceMap pieces = state.getPieces();
		
		double playerValue = 0;
		double opponentValue = 0;
		
		for (Entry e : pieces.iterateSpaces()) {
			double distance = Math.floor(Math.abs(e.space.row - HALF_BOARD));
			
			if (e.piece.color == playerColor) {
				playerValue -= distance;
			}
			else {
				opponentValue -= distance;
			}
		}
		
		playerValue = average(playerValue, pieces, playerColor);
		opponentValue = average(opponentValue, pieces, playerColor.opponent());
		
		return playerValue - opponentValue;
	}

	private double average(double value, PieceMap pieces, PlayerColor color) {
		 int count = pieces.getPieceCount(color);
		 
		 if (count > 0) {
			 return value / count;
		 }
		 
		 return 0;
	}

	protected double getRangeMin() {
		return -20;
	}

	protected double getRangeMax() {
		return 20;
	}
}
