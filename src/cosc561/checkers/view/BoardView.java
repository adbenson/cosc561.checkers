package cosc561.checkers.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import cosc561.checkers.Checkers;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;

public class BoardView {

	public static final Dimension DIMENSIONS = new Dimension(400, 400);
	
	private JFrame window;
	private Container content;
	
	private BoardPanel boardPanel;
	
	public BoardView(BoardState state) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				initialize(state);
			};
		});
	}

	protected void initialize() {
		window = new JFrame();
		
		content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		boardPanel = new BoardPanel(DIMENSIONS);
		content.add(boardPanel, BorderLayout.CENTER);
		
		controlPanel = createControlPanel();		
		content.add(controlPanel, BorderLayout.SOUTH);

		outputPanel = createOutputPanel();
		content.add(outputPanel, BorderLayout.EAST);

		window.pack();
		window.setVisible(true);
		
		//Note: this has to be initialized AFTER window.pack(), or dimensions will be zero.
		boardPanel.init();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		boardPanel.render();
	}

	private JPanel createOutputPanel() {
		JPanel outputPanel = new JPanel(new BorderLayout());
		
		outputPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH, 0));
		
		JPanel logPanel = new JPanel();
		logPanel.add(new JLabel("Play Log"));
		
		logArea = new JTextArea();
//		logArea.setPreferredSize(new Dimension(OUTPUT_WIDTH, DIMENSIONS.height - PENDING_TURN_HEIGHT));
		logArea.setEditable(false);
		logArea.setWrapStyleWord(true);
		logArea.setLineWrap(true);
		logArea.setBackground(Color.white);
		
		JScrollPane scroll = new JScrollPane(logArea);
		scroll.setPreferredSize(new Dimension(OUTPUT_WIDTH, DIMENSIONS.height - PENDING_TURN_HEIGHT));
		scroll.setBackground(Color.yellow);

		logPanel.add(scroll);
		
		outputPanel.add(logPanel, BorderLayout.CENTER);
		
		JPanel pendingPanel = new JPanel();
		
		pendingPanel.add(new JLabel("Pending Turn"));
		
		pendingTurn = new JTextArea();
		pendingTurn.setPreferredSize(new Dimension(OUTPUT_WIDTH, PENDING_TURN_HEIGHT));
		pendingTurn.setEditable(false);
		pendingTurn.setWrapStyleWord(true);
		pendingTurn.setLineWrap(true);
		pendingTurn.setBackground(Color.LIGHT_GRAY);
		
		pendingPanel.add(pendingTurn);
		
		outputPanel.add(pendingPanel, BorderLayout.SOUTH);
		
		return outputPanel;
	}

	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		JButton start = new JButton("Start New Game");
		start.addActionListener(startButtonAction());
		controlPanel.add(start);
		
		JButton next = new JButton("Complete Turn");
		next.addActionListener(nextTurnButtonAction());
		controlPanel.add(next);
		
		JButton reset = new JButton("Reset Turn");
		reset.addActionListener(resetButtonAction());
		controlPanel.add(reset);
		
		JButton undo = new JButton("Undo Last Turn");
		undo.addActionListener(undoButtonAction());
		controlPanel.add(undo);
		
		return controlPanel;
	}
	
	@SuppressWarnings("serial")
	public final Action startButtonAction() {
		return new AbstractAction("Start New Game") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						if (!game.isPlaying() || confirmStartNewGame()) {
							startNewGame();
						}
					}
				}).start();
			}	
		};
	};

	public void startNewGame() {
		Checkers.StartGameOptions options = getStartGameChoices();
		
		try {
			BoardState state = game.startGame(options);
			
			logArea.setText("");
			logAction("Starting new game as "+ options.player);
			logAction(PlayerColor.startingPlayer + " starts\n");
			
			if (options.player == PlayerColor.startingPlayer) {
				state = game.playerTurn();
				PlayerTurn turn = state.getTurn();
				
				logAction(turn);
				state = game.endTurn(turn);
			}
			
			boardPanel.setState(state);
			
		} catch (IllegalMoveException e) {
			System.err.println("Exception starting new game");
			e.printStackTrace();
		}
		
		boardPanel.render();
	}

	private boolean confirmStartNewGame() {
		
		int n = JOptionPane.showConfirmDialog(
			    window,
			    "This will start a new game and erase all data from the current game.\nAre you sure?",
			    "Confirm Undo Last Turn",
			    JOptionPane.YES_NO_OPTION);

		return n == 0;
	}
	
	@SuppressWarnings("serial")
	private final Action nextTurnButtonAction() {
		return new AbstractAction("Complete Turn") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						completeTurn();
					}
				}).start();
			}	
		};
	};
	
	private void completeTurn() {
		try {
			BoardState state = boardPanel.getState();
			state = applyTurn(state);
			
			state = game.playerTurn();
			state = applyTurn(state);
			
			boardPanel.setState(state);
			
		} catch (IllegalMoveException e) {
			System.err.println("Exception ending turn");
			e.printStackTrace();
		}
	}
	
	public BoardState applyTurn(BoardState state) throws IllegalMoveException {
		PlayerTurn turn = state.getTurn();
		logAction(turn);
		return game.endTurn(turn);
	}
	
	@SuppressWarnings("serial")
	private final Action resetButtonAction() {
		return new AbstractAction("Reset Turn") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						boardPanel.setState(game.newState());
					}
				}).start();
			}	
		};
	}
	
	@SuppressWarnings("serial")
	private final Action undoButtonAction() {
		return new AbstractAction("Undo Last Turn") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						if (confirmUndo()) {
							undoTurn();
						}
					}
				}).start();
			}	
		};
	}
	
	private boolean confirmUndo() {
		
		int n = JOptionPane.showConfirmDialog(
			    window,
			    "This will undo the last opponent player's turn and this player's response.\nAre you sure?",
			    "Confirm Undo Last Turn",
			    JOptionPane.YES_NO_OPTION);

		return n == 0;
	}
	
	private void undoTurn() {
			
		BoardState lastState = game.undoTurn();
		BoardState previousState = game.undoTurn();
		
		int lastTurn = lastState.getTurn().turnNumber;
		int previousTurn = previousState.getTurn().turnNumber;
		logAction("Turn #"+previousTurn+" and #"+lastTurn+" NEVER HAPPENED. Don't speak of it to anyone.");
		
		boardPanel.setState(previousState);
	}
	
	
	private Checkers.StartGameOptions getStartGameChoices() {
		
		JCheckBox addPieces = new JCheckBox("Add starting pieces");
		addPieces.setSelected(true);
		
		Object[] params = {"What color are we playing as today?", addPieces};
		
		int n = JOptionPane.showOptionDialog(window,
		    params,
		    "Choose our color",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    PlayerColor.players(),
		    PlayerColor.startingPlayer);
		
		System.out.println(addPieces.isSelected());
		
		if (n < 0) {
			throw new RuntimeException("No color choice made");
		}
		
		return new Checkers.StartGameOptions (PlayerColor.players()[n], addPieces.isSelected());
	}
	
	public void logAction(PlayerTurn turn) {
		logAction(turn.toString());
	}

	public void logAction(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logArea.append(message + "\n");
			};
		});
	}

}


