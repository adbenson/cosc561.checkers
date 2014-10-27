package cosc561.checkers.model;

import java.util.HashMap;
import java.util.Map;


public class Piece {
	
	private static final Map<PlayerColor, Piece> singletons;
	
	static {
		singletons = new HashMap<>();
		
		for (PlayerColor color : PlayerColor.values()) {
			singletons.put(color, new Piece(color));
		}
	}
	
	public static Piece get(PlayerColor color) {
		return singletons.get(color);
	}
		
	public final PlayerColor color;
	
	private KingPiece king;
	
	private Piece(PlayerColor color) {
		this.color = color;
	}

	public boolean isKing() {
		return false;
	}

	public KingPiece getKing() {
		if (king == null) {
			king = new KingPiece(color);
		}
		
		return king;
	}

	public Direction[] getDirections() {
		return color.directions;
	}

	public boolean isOpponent(Piece that) {
		return that.color != this.color;
	}
	
	public String toString() {
		return color.name().toLowerCase();
	}
	
	public String toShortString() {
		return color.shortName + " ";
	}
	
	public static class KingPiece extends Piece {
		public KingPiece(PlayerColor color) {
			super(color);
		}
		
		public boolean isKing() {
			return true;
		}
		
		public KingPiece getKing() {
			return this;
		}
		
		public Direction[] getDirections() {
			return Direction.values();
		}
		
		public String toString() {
			return color.name().toLowerCase() + " king";
		}
		
		public String toShortString() {
			return color.shortName + color.shortName;
		}
	}
}
