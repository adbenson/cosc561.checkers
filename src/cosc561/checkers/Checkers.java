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
		
		window.startNewGame();
	}

	public BoardState startGame(StartGameOptions options) throws IllegalMoveException {
		player = new Player(options.player, SEARCH_DEPTH);
		//We'll be immediately ending a turn, so start with the starting player's opponent
		currentPlayer = PlayerColor.startingPlayer.opponent();
		
		state = new BoardState();
		
		if (options.addStartingPieces) {
			state.addStartingPieces();
		} 
		
		playing = true;

		return newState();
	}

	public BoardState newState() {
		return new BoardState(state, currentPlayer.opponent());
	}

	public BoardState playerTurn() throws IllegalMoveException {
		return player.nextMove(state);
	}

	public synchronized BoardState endTurn(PlayerTurn turn) throws IllegalMoveException {
		state = new BoardState(state, currentPlayer);
		state.apply(turn);
		state.setPlayed();
		
		if (state.isEndgame()) {
			playing = false;
		}
		
		currentPlayer = currentPlayer.opponent();
		
		return newState();
	}
	
	public BoardState undoTurn() {
		state = state.getPrevious(1);
		return newState();
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
