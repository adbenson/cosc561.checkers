package cosc561.checkers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Checkers {
	
	public static void main(String[] args) {
		new Checkers();
	}
	
	public Checkers() {
		
		System.out.println(inquireColor());
		
	}
	
	private COLOR inquireColor() {
		
		int n = JOptionPane.showOptionDialog(new JFrame(),
		    "What color are we playing as today?",
		    "Choose our color",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    COLOR.values(),
		    null);
		
		if (n < 0) {
			throw new RuntimeException("No choice made");
		}
		
		return COLOR.values()[n];
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
