package cosc561.checkers;

import java.util.List;

import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.evaluator.Evaluator.EvaluatedState;
import cosc561.checkers.evaluator.EvaluatorManager;
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
		play.setPlayed();
		
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
			bestState = EvaluatedState.worst(bestState, childResult);
		}
		
		return bestState;
	}
	
	public String toString() {
		return color.toString();
	}
}
