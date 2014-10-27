package cosc561.checkers;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;
import cosc561.checkers.view.BoardWindow;

public class Checkers {
	
	public static void main(String[] args) throws Exception {
		new Checkers();
	}

	private BoardState state;
	
	public Checkers() throws Exception {

		BoardWindow window = new BoardWindow(this);

		System.out.println(Grid.getInstance());
		
		startGame(PlayerColor.RED);
		
		window.render(state);
	}

	public BoardState startGame(PlayerColor computerPlayer) throws IllegalMoveException {
		state = new BoardState(computerPlayer).addStartingPieces();
		
		return state;
	}

	public void nextTurn() {
		
	}
	
	public void applyTurn(PlayerTurn turn) throws IllegalMoveException {
		state = new BoardState(state, turn.player);
		
		state.apply(turn);
	}

	public BoardState getState() {
		return state;
	}

	public PlayerColor getCurrentPlayer() {
		// TODO Track current player color
		return PlayerColor.RED;
	}
	
}
