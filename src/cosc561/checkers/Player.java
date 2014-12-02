package cosc561.checkers;

import java.util.List;

import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.evaluator.Evaluator.EvaluatedState;
import cosc561.checkers.evaluator.PieceCountEvaluator;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;

public class Player {
	
	private static final boolean debug = false;
	
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
		
		BoardState bestState = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		
		for (BoardState child : currentState.getAllPossibleStates(color)) {
			double val = negamax(child, searchDepth, 1);
			if (debug) {
				System.out.println(child);
				System.out.println("score " + val);
			}
			
			if (val > bestValue) {
				bestValue = val;
				bestState = child;
			}
		}

		System.out.println("Chosen:");
		System.out.println(bestState);

		return bestState;
	}
	
	public double negamax(BoardState node, int depth, int colorInt) throws IllegalMoveException {
		if (depth == 0 || node.isEndgame()) {
			return colorInt * evaluator.evaluate(node, color).score;
		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		
		PlayerColor player = (colorInt > 0)? color : color.opponent();
		for (BoardState child : node.getAllPossibleStates(player)) {
			double val = - negamax(child, depth - 1, -colorInt);
			bestValue = Math.max(bestValue,  val);
		}
		
		return bestValue;
	}
	
	public EvaluatedState negamax(BoardState state, int depth, PlayerColor player) throws IllegalMoveException {
		if (depth == 0) {
			if (player == color) {
				return evaluator.evaluate(state, player);
			}
			else {
				return evaluator.evaluate(state, player.opponent());
			}
		}
				
		List<BoardState> childStates = state.getAllPossibleStates(player);
		
		if (state.isEndgame()) {
			return new EvaluatedState(state, 0);
		}
		
		EvaluatedState bestState = null;
		for (BoardState child : childStates) {
			EvaluatedState childResult = negamax(child, depth - 1, player.opponent());
			if (bestState == null || -childResult.score > bestState.score) {
				bestState = childResult;
			}
		}
		
		return bestState;
	}
	
	public void setEvaluator(Evaluator e) {
		this.evaluator = e;
	}
	
	public String toString() {
		return color.toString();
	}
}
