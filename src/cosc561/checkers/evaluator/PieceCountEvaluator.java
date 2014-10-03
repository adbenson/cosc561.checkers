package cosc561.checkers.evaluator;

import java.util.Map;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.Space;

public class PieceCountEvaluator extends Evaluator {

	public PieceCountEvaluator(PlayerColor playerColor) {
		super(playerColor);
	}

	@Override
	public int evaluate(BoardState state, PlayerColor currentPlayer) {
		int value = 0;
		
		for (Map.Entry<Space, Piece> entry : state.getPieces()) {
			value += (entry.getValue().color == playerColor)? 1 : -1;
		}
			
		return value;
	}

}
