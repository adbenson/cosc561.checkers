package cosc561.checkers.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerTurn;
import cosc561.checkers.model.Space;
import cosc561.checkers.utility.Vector;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements ControlListener, ComponentListener {
	
	public static final int EVALUATOR_WIDTH = 300;	
	
	private BoardState state;
	
	private static Grid grid = Grid.getInstance();
	
	private ControlHandler controlHandler;
	
	private EvaluationPanel evaluation;
	private JPanel board;
	
	private BoardGraphics graphics;
	
	private Space hovered;
	private Space selected;
	private Space dragFrom;
	private Vector dragTo;
	private Vector dragOffset;

	public BoardPanel(Dimension dimensions) {
		super(new BorderLayout());
		
		board = new JPanel();
		board.setPreferredSize(dimensions);
		
		controlHandler = new ControlHandler(this);

		board.addMouseListener(controlHandler);
		board.addMouseMotionListener(controlHandler);
		board.addComponentListener(this);
		
		this.add(board, BorderLayout.CENTER);
		
		
		evaluation = new EvaluationPanel();
		evaluation.setPreferredSize(new Dimension(EVALUATOR_WIDTH, 0));
		
		this.add(evaluation, BorderLayout.EAST);
		
		//Create empty starting state so it's never null
		state = new BoardState();
	}

	public void init() {
		
		graphics = new BoardGraphics(board, grid);
		
		hovered = null;
		selected = null;
		dragFrom = null;
		dragTo = null;
		
		render();
	}
	
	public BoardState getState() {
		return state;
	}
	
	public void setState(BoardState state) {
		this.state = state;
		render();
	}
	
	@Override
	public void render() {
		render(state);
	}
		
	public void render(final BoardState board) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				graphics.init();

				graphics.drawGrid();
				
				if (board != null) {
					renderBoard(board);
					//TODO figure out how to show pending turn after refactor
				}
				
				graphics.display();
				
				evaluation.showEvaluation(board);
			};
		});
	}

	protected boolean dragging() {
		return (dragFrom != null && dragTo != null);
	}

	private void renderBoard(BoardState board) {
		BoardState temp = new BoardState(board, null);
		
		Piece piece = null;
		
		if (dragging()) {
			piece = removeDraggedPiece(temp);
		}
		
		graphics.drawBoard(temp);
		
		PlayerTurn turn = state.getTurn();
		if (! turn.hasChange() && state.getPrevious(1) != null) {
			turn = state.getPrevious(1).getTurn();
		}
		
		graphics.drawTurn(turn);

		if (piece != null) {
			Vector to = dragTo.subtract(dragOffset);
			graphics.drawRemovedPiece(piece, dragFrom);
			graphics.drawDraggedPiece(piece, to);
			graphics.drawDragLine(dragFrom, to, piece.color);
		}
		
		if (hovered != null) {
			//TODO: indicate previous moves of hovered piece
		}
		
		if (selected != null) {
			//TODO: indicate available moves to selected piece
		}
	}

	protected Piece removeDraggedPiece(BoardState temp) {
		
		Piece piece = state.getPiece(dragFrom);
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
	
	/*
	 * ControlListener methods
	 */
	
	@Override
	public void movePiece(Space from, Space to) {
		Piece piece = state.getPiece(from);
		
		if (piece == null) {
			System.out.println("Could not find piece to move");
			return;
		}
		
		try {
			state.movePiece(from, to);
		} catch (IllegalMoveException e) {
			System.err.println("Exception applying drag move to board state");
			e.printStackTrace();
		}
		
		render();
	}
	
	@Override
	public void dragPiece(Space from, Vector to) {
		//Check if this is a new dragging action
		if (dragFrom == null && from != null) {
			dragOffset = graphics.getOffset(from, to);
		}
		
		//Allow null 'from' to reset dragging, otherwise check if there's a piece there.
		if (from == null || state.getPiece(from) != null) {
			dragFrom = from;
			dragTo = to;
		}
		render();
	}
	
	@Override
	public void hover(Space space) {
		hovered = space;
	}
	
	@Override
	public void selectPiece(Space space) {
		selected = space;
	}
	
	@Override
	public void capturePiece(Space space) {
		if (state.getPiece(space) != null) {
			try {
				state.removePiece(space);
			} catch (IllegalMoveException e) {
				System.err.println("Exception removing capture piece");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Space getSpaceAt(Vector point) {
		return graphics.getSpaceAt(point);
	}
	
	@Override
	public void createContextMenu(Vector point) {
		Space space = getSpaceAt(point);
		boolean hasPiece = state.getPiece(space) != null;
		
		if (space != null) {
			JPopupMenu menu = new BoardContextMenu(space, this, hasPiece);
			menu.show(this, point.intX(), point.intY());
		}
	}
	
	/*
	 *  ComponentListener methods 
	 */
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		//Do nothing
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		render();
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		init();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		render();
	}

	/* 
	 * Context Menu methods 
	 */
	
	public void addPiece(Piece piece, Space space) {
		try {
			state.addPiece(piece, space);
		} catch (IllegalMoveException e) {
			System.err.println("Exception adding king piece by context menu");
			e.printStackTrace();
		}
		render();
	}

	public void kingPiece(Space space) {
		try {
			state.kingPiece(space);
		} catch (IllegalMoveException e) {
			System.err.println("Exception kinging piece by context menu");
			e.printStackTrace();
		}
		render();
	}

	public void removePiece(Space space) {
		try {
			state.removePiece(space);
		} catch (IllegalMoveException e) {
			System.err.println("Exception removing piece by context menu");
			e.printStackTrace();
		}
		render();
	}
}
