package cosc561.checkers;

import java.util.List;

import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.evaluator.Evaluator.EvaluatedState;
import cosc561.checkers.evaluator.PieceCountEvaluator;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;

public class Player {
	
	private PlayerColor color;
	private int searchDepth;
	
	private Evaluator evaluator;
	
	public Player(PlayerColor color, int searchDepth) {
		this.color = color;
		this.searchDepth = searchDepth;
		
		evaluator = new PieceCountEvaluator(color);
	}

	public BoardState nextMove(BoardState currentState) throws IllegalMoveException {
		if (currentState.isEndgame()) {
			System.err.println("nextMove called on completed game");
			return currentState;
		}
		
		EvaluatedState evaluated = evaluator.evaluate(currentState, color);
		
		EvaluatedState chosenEvaluated = negamax(evaluated, searchDepth, color);
		if (chosenEvaluated == null) {
			System.out.println();
		}
		BoardState chosen = chosenEvaluated.state;
		
		BoardState play = chosen.getFirstUnplayed();
		
		System.out.println(play.getTurn());
		
		return play;
	}
	
	public EvaluatedState negamax(EvaluatedState state, int depth, PlayerColor player) throws IllegalMoveException {
		if (depth == 0) {
			return state;
		}
				
		List<BoardState> childStates = state.state.getAllPossibleStates(player);
		
		if (state.state.isEndgame()) {
			return state;
		}
		
		List<EvaluatedState> evaluated = evaluator.evaluate(childStates, player);
		
		EvaluatedState bestState = null;
		for (EvaluatedState child : evaluated) {
			EvaluatedState childResult = negamax(child, depth - 1, player.opponent());
			bestState = optimum(bestState, childResult, player);
		}
		
		return bestState;
	}
	
	public EvaluatedState optimum(EvaluatedState s1, EvaluatedState s2, PlayerColor currentPlayer) {
		if (s1 == null) {
			return s2;
		}
		if (s2 == null) {
			return s1;
		}
		
		
		if (s1.score > s2.score ^ color == currentPlayer) {
			return s2;
		}
		else {
			return s1;
		}
	}
	
	public void setEvaluator(Evaluator e) {
		this.evaluator = e;
	}
	
	public String toString() {
		return color.toString();
	}
}
