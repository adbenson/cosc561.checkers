package cosc561.checkers.model;


public class Piece {
		
	public final Color color;
	
	private boolean king;
	
	public Piece(Color color) {
		this.color = color;
	}

	public boolean isKing() {
		return king;
	}

	public void setKing(boolean king) {
		this.king = king;
	}
	
	public static enum Color {
		RED (true), 
		BLACK (false);
		
		public final boolean first;
		
		private Color(boolean first) {
			this.first = first;
		}
	}
}
