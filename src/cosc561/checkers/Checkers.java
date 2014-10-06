package cosc561.checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
		boolean playing = true;
		while (playing) {
		
			state = player.nextMove(state);
			
			System.out.println(state);
			System.out.println("--------------------------");

			if (state.isEndgame()) {
				playing = false;
			}
			else {
				state = getInput(state, color.opponent());//opponent.nextMove(state);
				System.out.println(state);
				System.out.println("--------------------------");

				if (state.isEndgame()) {
					playing = false;
				}
			}

		}

	}

	private BoardState getInput(BoardState currentState, PlayerColor player)
			throws IllegalMoveException, IOException {

		BoardState newState = new BoardState(currentState, player);

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print("Enter from: ");
			int from = Integer.parseInt(br.readLine());

			ArrayList<Integer> toCoord = new ArrayList<Integer>();
			int to = 0;
			while (to != -1) {
				System.out.print("Enter \"to\": (-1 to quit) ");
				to = Integer.parseInt(br.readLine());
				if (to != -1) {
					toCoord.add(to);
				}
			}

			ArrayList<Integer> removes = new ArrayList<Integer>();
			int remove = 0;
			try {
				while (remove != -1) {
					System.out.print("Remove Piece: (-1 to quit)");
					remove = Integer.parseInt(br.readLine());
					if (remove != -1) {
						removes.add(remove);
					}
				}
			} catch (NumberFormatException nfe) {
				System.err.println("Invalid Format!");
			}

			System.out.println("From is: " + from);
			for (Integer num : toCoord) {
				System.out.println("to: " + num);
			}
			for (Integer num : removes) {
				System.out.println("remove: " + num);
			}

			newState.removePiece(removes);
			newState.movePiece(from, toCoord);
			newState.setPlayed();
		} catch (NumberFormatException nfe) {
			System.err.println("Invalid Format!");
			newState = getInput(currentState, player);
		}

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
