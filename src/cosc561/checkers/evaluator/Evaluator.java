package cosc561.checkers.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cosc561.checkers.evaluator.Evaluator.EvaluatedState;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap;
import cosc561.checkers.model.PlayerColor;

public abstract class Evaluator {
	
	protected final PlayerColor playerColor;
	
	public Evaluator(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}
	
	public List<EvaluatedState> evaluate(List<BoardState> states, PlayerColor player) {
		List<EvaluatedState> evaluated = new ArrayList<EvaluatedState>();
		
		for (BoardState state : states) {
			evaluated.add(evaluate(state, player));
		}
		
		Collections.sort(evaluated);
		
		return evaluated;
	}

	public abstract EvaluatedState evaluate(BoardState state, PlayerColor currentPlayer);
	
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

	
}
