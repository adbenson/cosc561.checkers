package cosc561.checkers;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.evaluator.EvaluatorManager;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.BoardState.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;
import cosc561.checkers.model.Space;

public class Player {
	
	private PlayerColor color;
	private int searchDepth;
	
	private Evaluator evaluator;
	
	public Player(PlayerColor color, int searchDepth) {
		this.color = color;
		this.searchDepth = searchDepth;
	}

	public BoardState nextMove(BoardState currentState) throws IllegalMoveException {

		return currentState.getAllPossibleStates(color).get(0);
	}

}
