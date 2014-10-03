package cosc561.checkers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PlayerColor;

public class Checkers {
	
	public static void main(String[] args) throws Exception {
		new Checkers();
	}
	
	public Checkers() throws Exception {
		
		System.out.println(Grid.getInstance());
		
		PlayerColor color = PlayerColor.RED; //inquireColor();
		
		BoardState state = new BoardState(PlayerColor.startingPlayer).addStartingPieces();
		System.out.println(state);
		
		Player player = new Player(color, 6);
		Player opponent = new Player(color.opponent(), 6);
		
		while (true) {
		
			state = player.nextMove(state);
			System.out.println(state);
			
			state = opponent.nextMove(state);
			System.out.println(state);
		}

	}
	
	private PlayerColor inquireColor() {
		
		int n = JOptionPane.showOptionDialog(new JFrame(),
		    "What color are we playing as today?",
		    "Choose our color",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    PlayerColor.values(),
		    null);
		
		if (n < 0) {
			throw new RuntimeException("No color choice made");
		}
		
		return PlayerColor.values()[n];
	}
	
}
