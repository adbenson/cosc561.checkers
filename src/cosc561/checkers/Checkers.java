package cosc561.checkers;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
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

	public void startGame(StartGameOptions options) throws IllegalMoveException {
		player = new Player(options.player, SEARCH_DEPTH);
		//We'll be immediately ending a turn, so start with the starting player's opponent
		currentPlayer = PlayerColor.startingPlayer.opponent();
		
		state = new BoardState(null);
		
		if (options.addStartingPieces) {
			state.addStartingPieces();
		}
		
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
		//We want to go back to before the opponent's last turn.
		//Previous 1 gets us our last turn, 2 gets us their last turn, 3 gets us before their last turn
		state = state.getPrevious(3);
		state = new BoardState(state, currentPlayer);
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
	
	
	public static class StartGameOptions {
		public final PlayerColor player;
		public final boolean addStartingPieces;
		
		public StartGameOptions(PlayerColor player, boolean addStartingPieces) {
			this.player = player;
			this.addStartingPieces = addStartingPieces;
		}
	}
	
}
