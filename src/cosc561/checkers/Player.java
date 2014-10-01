package cosc561.checkers;

import java.util.ArrayList;
import java.util.List;

import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;

public class Player {
	
	private BoardState currentState;
	private PlayerColor color;
	private int searchDepth;
	
	private Evaluator evaluator;
	
	public Player(BoardState startState, PlayerColor color, int searchDepth) {
		this.color = color;
		this.searchDepth = searchDepth;
	}

	public PlayerTurn nextMove() {
		
		PlayerColor currentPlayer = color;
		
		List<BoardState> candidates = new ArrayList<>();
		candidates.add(currentState);
		
		List<BoardState> nextDepth = null;
		for (int i = 1; i < searchDepth; i++) {
			nextDepth = new ArrayList<>();
			
			for (BoardState candidate : candidates) {
				nextDepth.addAll(candidate.getAllPossibleStates(currentPlayer));
			}
			
			currentPlayer = color.opponent();
			candidates = nextDepth;
		}
		
		BoardState bestState = null;
		int bestScore = 0;
		
		evaluator = EvaluatorManager.getEvaluator(color);
		
		for (BoardState state : candidates) {
			if (bestState == null || evaluator.evaluate(state, currentPlayer) > bestScore) {
				bestState = state;
			}
		}
		
		currentState = bestState;
		currentState.setPlayed();
		
		return bestState.getFirstUnplayed();
	}
	
	public void opponentMove(PlayerTurn move) {
		
	}
}
