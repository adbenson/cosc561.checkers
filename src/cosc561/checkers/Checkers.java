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
		//We'll be immediately ending a turn, so start with the starting player's opponent
		currentPlayer = PlayerColor.startingPlayer.opponent();
		
		state = new BoardState(null).addStartingPieces();
		playing = true;
		
		endTurn(true);
	}

	public void playerTurn() throws IllegalMoveException {
		state = player.nextMove(state);
	}

	public synchronized void endTurn(boolean newState) {
		if (state.isEndgame()) {
			playing = false;
		}
		currentPlayer = currentPlayer.opponent();
		
		state.setPlayed();
		
		if (newState) {
			state = new BoardState(state, currentPlayer);
		}
	}
	
	public void resetTurn() {
		state = new BoardState(state.getLastPlayed(), currentPlayer);
	}
	
	public void undoTurn() {
		//Get the state before the last one; we don't need to undo our AI's turns
		state = state.getPrevious(1);
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
