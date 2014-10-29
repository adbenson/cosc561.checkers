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
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;
import cosc561.checkers.model.Space;

public class BoardWindow {

	public static final int OUTPUT_WIDTH = 300;	
	public static final int PENDING_TURN_HEIGHT = 150;
	public static final Dimension DIMENSIONS = new Dimension(750, 750);
	
	private JFrame window;
	private Container content;
	
	private JPanel boardPanel;
	private JPanel controlPanel;
	private JPanel outputPanel;
	
	private BoardGraphics graphics;
	
	private ControlHandler controlHandler;
	
	private Checkers game;
	
	private Space hovered;
	private Space selected;
	private Space dragFrom;
	private Point dragTo;
	private Point dragOffset;
	
	private Grid grid = Grid.getInstance();
	
	private JTextArea logArea;
	private JTextArea pendingTurn;
	
	public BoardWindow(Checkers game) throws InvocationTargetException, InterruptedException {
		
		this.game = game;
		
		hovered = null;
		selected = null;
		dragFrom = null;
		dragTo = null;
		
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				initialize();
			};
		});
	}

	protected void initialize() {
		window = new JFrame();
		
		controlHandler = new ControlHandler(this);
		
		content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		boardPanel = new JPanel();
		boardPanel.setPreferredSize(DIMENSIONS);
		boardPanel.addMouseListener(controlHandler);
		boardPanel.addMouseMotionListener(controlHandler);
		content.add(boardPanel, BorderLayout.CENTER);
		
		controlPanel = createControlPanel();		
		content.add(controlPanel, BorderLayout.SOUTH);

		outputPanel = createOutputPanel();
		content.add(outputPanel, BorderLayout.EAST);

		window.pack();
		window.setVisible(true);
		
		//Note: this has to be initialized AFTER window.pack(), or dimensions will be zero.
		graphics = new BoardGraphics(boardPanel, grid);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
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
		PlayerColor color = inquireColor();
		try {
			game.startGame(color);
			
			logArea.setText("");
			logAction("Starting new game as "+ color);
			logAction(PlayerColor.startingPlayer + " starts\n");
			
			if (color == PlayerColor.startingPlayer) {
				game.playerTurn();
				logAction(game.getState().getTurn());
				game.endTurn(true);
			}
			
		} catch (IllegalMoveException e) {
			System.err.println("Exception starting new game");
			e.printStackTrace();
		}
		
		render();
	}
	
	private boolean confirmStartNewGame() {
		
		int n = JOptionPane.showConfirmDialog(
			    window,
			    "This will start a new game and erase all data from the current game.\nAre you sure?",
			    "Confirm Undo Last Turn",
			    JOptionPane.YES_NO_OPTION);

		return n == 0;
	}
	
	private final Action nextTurnButtonAction() {
		return new AbstractAction("Complete Turn") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						try {
							logAction(game.getState().getTurn());
							game.endTurn(false);
							
							game.playerTurn();
							logAction(game.getState().getTurn());
							game.endTurn(true);
						} catch (IllegalMoveException e) {
							System.err.println("Exception starting new game");
							e.printStackTrace();
						}
						render();
					}
				}).start();
			}	
		};
	};
	
	private final Action resetButtonAction() {
		return new AbstractAction("Reset Turn") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						game.resetTurn();
						render();
					}
				}).start();
			}	
		};
	}
	
	private final Action undoButtonAction() {
		return new AbstractAction("Undo Last Turn") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						undoTurn();
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
		if (confirmUndo()) {
			BoardState lastState = game.getState().getPrevious(1);
			int lastTurn = lastState.getTurn().turnNumber;
			int previousTurn = lastState.getPrevious(1).getTurn().turnNumber;
			logAction("Turn #"+previousTurn+" and #"+lastTurn+" NEVER HAPPENED. Don't speak of it to anyone.");
			game.undoTurn();
			render();
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
	
	public void render() {
		render(game.getState());
	}
	
	public void render(final BoardState board) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				graphics.init();

				graphics.drawGrid();
				
				if (board != null) {
					renderBoard(board);
					pendingTurn.setText(board.getTurn().toString());
				}
				
				graphics.display();
			};
		});
	}
	
	private void renderBoard(BoardState board) {
		BoardState temp = new BoardState(board, game.getCurrentPlayer());
		
		Piece piece = null;
		
		if (dragging()) {
			piece = removeDraggedPiece(temp);
		}
		
		graphics.drawBoard(temp);
		
		if (piece != null) {
			graphics.drawDraggedPiece(piece, dragTo, dragOffset);
		}
	}
	
	protected Piece removeDraggedPiece(BoardState temp) {
		BoardState current = game.getState();
		
		Piece piece = current.getPiece(dragFrom);
		if (piece != null) {
			try {
				temp.removePiece(dragFrom);
			} catch (IllegalMoveException e) {
				System.err.println("Exception removing piece for drag");
				e.printStackTrace();
				return null;
			}
		}
		else {
			System.out.println("Piece to drag not found");
		}
				
		return piece;
	}

	protected boolean dragging() {
		return (dragFrom != null && dragTo != null);
	}

	public void movePiece(Space from, Space to) {
		Piece piece = getPiece(from);
		
		if (piece == null) {
			System.out.println("Could not find piece to move");
			return;
		}
		
		try {
			game.getState().movePiece(from, to);
		} catch (IllegalMoveException e) {
			System.err.println("Exception applying drag move to board state");
			e.printStackTrace();
		}
		
		render();
	}
	
	public void dragPiece(Space from, Point to) {
		//Check if this is a new dragging action
		if (dragFrom == null && from != null) {
			dragOffset = graphics.getOffset(from, to);
		}
		
		//Allow null 'from' to reset dragging, otherwise check if there's a piece there.
		if (from == null || game.getState().getPiece(from) != null) {
			dragFrom = from;
			dragTo = to;
		}
		render();
	}
	
	public void hover(Space space) {
		
	}
	
	public void selectPiece(Space space) {
		
	}
	
	public void capturePiece(Space space) {
		if (getPiece(space) != null) {
			try {
				game.getState().removePiece(space);
			} catch (IllegalMoveException e) {
				System.err.println("Exception removing capture piece");
				e.printStackTrace();
			}
		}
	}

	public Space getSpaceAt(Point point) {
		return graphics.getSpaceAt(point);
	}
	
	public Piece getPiece(Space space) {
		return game.getState().getPiece(space);
	}

}


