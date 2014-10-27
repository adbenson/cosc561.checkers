package cosc561.checkers;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;
import cosc561.checkers.view.BoardWindow;

public class Checkers {
	
	public static final int SEARCH_DEPTH = 6;
	
	public static void main(String[] args) throws Exception {
		new Checkers();
	}
	
	private boolean playing = false;

	private BoardState state;
	
	private Player player;
	
	private PlayerColor currentPlayer;
	
	public Checkers() throws Exception {

		BoardWindow window = new BoardWindow(this);

		System.out.println(Grid.getInstance());

		window.render();
		
		window.startNewGame();
	}

	public void startGame(PlayerColor computerPlayer) throws IllegalMoveException {
		player = new Player(computerPlayer, SEARCH_DEPTH);
		currentPlayer = PlayerColor.startingPlayer;
		
		state = new BoardState(null).addStartingPieces();
		state = new BoardState(state, currentPlayer);
		playing = true;
	}

	public void playerTurn() throws IllegalMoveException {
		state = player.nextMove(state);
	}

	public void endTurn() {
		if (state.isEndgame()) {
			playing = false;
		}
		currentPlayer = currentPlayer.opponent();
		
		state.setPlayed();
		state = new BoardState(state, currentPlayer);
	}
	
	private void resetTurn() {
		state = new BoardState(state.getLastPlayed(), currentPlayer);
	}
	
	private void undoTurn() {
		state = state.getPrevious(0);
	}

	public BoardState getState() {
		return state;
	}

	public PlayerColor getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
}
