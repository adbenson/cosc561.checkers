package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	private final double normalizationFactor;
	private final double initialWeight;
	private final double weightFactor;
	
	public Evaluator(double initialWeight, double weightFactor) {
		
		double range = getRangeMax() - getRangeMin();
		//Normalize over a range of two : -1 to +1
		this.normalizationFactor = 2.0 / range;
		
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
	
	public double getWeight(int pieceCount) {
		//Ranges from 1 (start of game) to 0 (end of game)
		//TODO: MAGIC NUMBER! We're actually not set up to easily know the total pieces on the board!
		double progress = pieceCount / 24.0;
		
		//Scale will range from 1 (start of game) to weightFactor (end of game)
		double scale = progress + ((1 - progress) * weightFactor);
		
		return initialWeight * scale;
	}

	public double normalize(double score) {		
		return score * normalizationFactor;
	}

	public abstract double evaluate(BoardState state, PlayerColor player);
	
}
