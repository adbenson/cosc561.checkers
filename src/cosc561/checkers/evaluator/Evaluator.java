package cosc561.checkers.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	protected final PlayerColor playerColor;
	
	private double factor;
	
	public Evaluator(PlayerColor playerColor) {
		this.playerColor = playerColor;
		
		this.factor = 1 / (getRangeMax() - getRangeMin());
	}
	
	/* Default Ranges */
	
	protected double getRangeMin() {
		return 0;
	}

	protected double getRangeMax() {
		return 1;
	}

	public List<EvaluatedState> evaluate(List<BoardState> states, PlayerColor player) {
		List<EvaluatedState> evaluated = new ArrayList<EvaluatedState>();
		
		for (BoardState state : states) {
			evaluated.add(evaluate(state, player));
		}
		
		Collections.sort(evaluated);
		
		return evaluated;
	}
	
	public EvaluatedState evaluate(BoardState state, PlayerColor currentPlayer) {
		double score = normalize(evaluateInternal(state));
		
		if (currentPlayer != playerColor) {
			score = -score;
		}
		
		return new EvaluatedState(state, score);
	}

	protected double normalize(double score) {		
		return (score + getRangeMin()) * factor;
	}

	protected abstract double evaluateInternal(BoardState state);
	
	public static class EvaluatedState implements Comparable<EvaluatedState> {
		public final BoardState state;
		public final double score;
				
		public EvaluatedState(BoardState state, double cumulativeScore) {
			this.state = state;
			this.score = cumulativeScore;
		}

		@Override
		public int compareTo(EvaluatedState that) {
			return (int) (this.score - that.score);
		}
		
		public String toString() {
			return "Board #"+state.uid+", Score: "+score;
		}

	}

	
}
