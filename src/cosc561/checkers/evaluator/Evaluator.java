package cosc561.checkers.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	private final double normalizationFactor;
	private final double initialWeight;
	private final double weightFactor;
	
	public Evaluator(double initialWeight, double weightFactor) {
		
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
	
	protected double getWeight(int pieceCount) {
		//Ranges from 1 (start of game) to 0 (end of game)
		double progress = pieceCount / 24.0;
		
		double weight = initialWeight * ((1.0 - progress) * weightFactor);
		
		return initialWeight;//weight + progress;
	}

	protected double normalize(double score) {		
		return score * normalizationFactor;
	}

	public abstract double evaluate(BoardState state, PlayerColor player);
	
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
