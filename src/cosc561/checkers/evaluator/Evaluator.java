package cosc561.checkers.evaluator;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	protected final PlayerColor playerColor;
	
	public Evaluator(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}
	public static class EvaluatedState implements Comparable<EvaluatedState> {
		public final BoardState state;
		private final int score;
				
		public EvaluatedState(BoardState state, int score) {
			this.state = state;
			this.score = score;
		}

		@Override
		public int compareTo(EvaluatedState that) {
			return this.score - that.score;
		}
		
		public static EvaluatedState best(EvaluatedState s1, EvaluatedState s2) {
			if (s1 == null) {
				return s2;
			}
			if (s2 == null) {
				return s1;
			}
			return (s1.score > s2.score)? s1 : s2;
		}
		
		public static EvaluatedState worst(EvaluatedState s1, EvaluatedState s2) {
			if (s1 == null) {
				return s2;
			}
			if (s2 == null) {
				return s1;
			}
			return (s1.score < s2.score)? s1 : s2;
		}
	}

	public abstract int evaluate(BoardState state, PlayerColor currentPlayer);
	
}
