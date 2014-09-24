package cosc561.checkers.model;


public class Piece {
		
	public final COLOR color;
	
	private boolean king;
	
	public Piece(COLOR color) {
		this.color = color;
	}

	public boolean isKing() {
		return king;
	}

	public void setKing(boolean king) {
		this.king = king;
	}
	
	public static enum COLOR {
		RED (true), 
		BLACK (false);
		
		public final boolean first;
		
		private COLOR(boolean first) {
			this.first = first;
		}
	}
}
