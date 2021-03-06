package cosc561.checkers;

import cosc561.checkers.evaluator.CompoundEvaluator;
import cosc561.checkers.evaluator.Evaluator;
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
		
		evaluator = new CompoundEvaluator();
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
			
			if (bestState == null || val > bestValue) {
				bestValue = val;
				bestState = child;
			}
		}

		System.out.println("Chosen:");
		System.out.println(bestState);
		
		if (bestState == null) {
			System.out.println("End?");
		}

		return bestState;
	}
	
	public double negamax(BoardState node, int depth, int colorInt) throws IllegalMoveException {
		if (depth == 0 || node.isEndgame()) {
			return colorInt * evaluator.evaluate(node, color);
		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		
		PlayerColor player = (colorInt > 0)? color : color.opponent();
		for (BoardState child : node.getAllPossibleStates(player.opponent())) {
			double val = - negamax(child, depth - 1, -colorInt);
			bestValue = Math.max(bestValue,  val);
		}
		
		return bestValue;
	}

	public void setEvaluator(Evaluator e) {
		this.evaluator = e;
	}
	
	public String toString() {
		return color.toString();
	}
}
