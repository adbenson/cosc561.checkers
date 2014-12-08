package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn.Change;
import cosc561.checkers.model.PlayerTurn.King;

public class KingPieceEvaluator extends Evaluator {

	public KingPieceEvaluator(double initialWeight, double weightFactor) {
		super(initialWeight, weightFactor);
	}

	/**
	 * Returns 1 if a piece belonging to this player got kinged,
	 * or -1 if an opponent piece was kinged.
	 */
	@Override
	protected double evaluateInternal(BoardState state, PlayerColor playerColor) {
		for (Change change : state.getTurn().getChanges()) {
			if (change instanceof King) {
				if (change.piece.color == playerColor) {
					return 1;
				}
				else {
					return -1;
				}
			}
		}
		
		return 0;
	}

}
