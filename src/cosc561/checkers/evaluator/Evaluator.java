package cosc561.checkers.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	protected final PlayerColor playerColor;
	
	private final double normalizationFactor;
	private final double initialWeight;
	private final double weightFactor;
	
	public Evaluator(PlayerColor playerColor, double initialWeight, double weightFactor) {
		this.playerColor = playerColor;
		
		double range = getRangeMax() - getRangeMin();
		//Normalize over a range of two : -1 to +1
		this.normalizationFactor = 2 / range;
		
		this.initialWeight = initialWeight;
		this.weightFactor = weightFactor;
	}
	
	/* Default Ranges */
	
	protected double getRangeMin() {
		return -1;
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
	
	protected double getWeight(int pieceCount) {
		//Ranges from 1 (start of game) to 0 (end of game)
		double progress = pieceCount / 24.0;
		
		double weight = initialWeight * ((1.0 - progress) * weightFactor);
		
		return initialWeight;//weight + progress;
	}
	
	protected double evaluateNormal(BoardState state) {
		return normalize(evaluateInternal(state));
	}

	protected double normalize(double score) {		
		return score * normalizationFactor;
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
