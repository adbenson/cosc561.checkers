package cosc561.checkers;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
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
		Player opponent = new Player(color.opponent(), 1);
		
		boolean playing = true;
		while (playing) {
		
			state = player.nextMove(state);
			System.out.println(state);
			
			if (state.isEndgame()) {
				playing = false;
			}
			else {
				state = getInput(state, color.opponent());//opponent.nextMove(state);
				System.out.println(state);
				
				if (state.isEndgame()) {
					playing = false;
				}
			}

		}

	}

	private BoardState getInput(BoardState currentState, PlayerColor player) throws IllegalMoveException {
		BoardState newState = new BoardState(currentState, player);
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter from: ");
		int from = 0;
		while (!scanner.hasNextInt()) {
			scanner.next();
		}
		from = scanner.nextInt();

		ArrayList<Integer> toCoord = new ArrayList<Integer>();

		System.out.println("Enter to: ");
		while (scanner.hasNextInt()) {

			int num = 0;
			num = scanner.nextInt();
			if (num != -1) {
				toCoord.add(num);
			} else {
				break;
			}
		}
		for (Integer num : toCoord) {
			System.out.println("to: " + num);
		}

		System.out.println("Remove piece?: ");
		ArrayList<Integer> removes = new ArrayList<Integer>();
		while (scanner.hasNextInt()) {

			int num = 0;
			num = scanner.nextInt();
			if (num != -1) {
				removes.add(num);
			} else {
				break;
			}
		}
		newState.removePiece(removes);
		newState.movePiece(from, toCoord);

		scanner.close();
		return newState;
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
