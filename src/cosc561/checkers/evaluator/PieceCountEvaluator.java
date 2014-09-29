package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.Piece.Color;

public class PieceCountEvaluator extends Evaluator {

	public PieceCountEvaluator(Color playerColor) {
		super(playerColor);
	}

	@Override
	public int evaluate(BoardState state, Piece.Color currentPlayer) {
		int value = 0;
		
		for (Piece piece : state.getPieces()) {
			value += (piece.color == playerColor)? 1 : -1;
		}
			
		return value;
	}

}