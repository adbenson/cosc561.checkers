package cosc561.checkers.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.Space;

public class BoardGraphics {

	private static final Color BOARD_COLOR = Color.WHITE;
	private static final Color SPACE_COLOR = Color.GRAY;
	private static final Color SPACE_LABEL_COLOR = Color.WHITE;
	private static final Color KING_BORDER_COLOR = Color.YELLOW;
	private static final int SPACE_LABEL_OFFSET_PX = 5;
	private static final double PIECE_TO_SPACE_SIZE_RATIO = 0.8;
	private static final Map<PlayerColor, Color> PIECE_COLORS;
	private static final int PIECE_BORDER_WIDTH = 2;
	
	static {
		PIECE_COLORS = new HashMap<PlayerColor, Color>();
		PIECE_COLORS.put(PlayerColor.RED, new Color(200, 30, 0));
		PIECE_COLORS.put(PlayerColor.BLACK, new Color(50, 50, 50));
	}
	
	private Graphics2D g;
	private Image offScreenBuffer;
	
	private final Container panel;
	
	private final int sideLength;
	
	private final int spaceSize;
	
	private final int pieceOffset;
	private final int pieceSize;
	
	private final int kingBorderSize;
	
	private final Stroke pieceBorderStroke;
	
	private final Grid grid;
	
	public BoardGraphics(Container panel, Grid grid) {
		this.panel = panel;
		this.grid = grid;
		
		sideLength = Math.min(panel.getWidth(), panel.getHeight());
		
		spaceSize = sideLength / Grid.SPACES_PER_SIDE;
		
		pieceSize = (int) (spaceSize * PIECE_TO_SPACE_SIZE_RATIO);
		
		pieceOffset = (int) ((spaceSize - pieceSize) / 2);
		
		kingBorderSize = pieceSize + (2 * PIECE_BORDER_WIDTH);
				
		pieceBorderStroke = new BasicStroke(PIECE_BORDER_WIDTH);
	}
	
	public void init() {
		if (offScreenBuffer == null){
			offScreenBuffer = panel.createImage(panel.getWidth(), panel.getHeight());
			if (offScreenBuffer == null) {
				System.err.println("offScreenBuffer is null");
				return;
			}
			else {
				g = (Graphics2D) offScreenBuffer.getGraphics();
			}
			
			g.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setColor(BOARD_COLOR);
		g.fillRect (0, 0, panel.getWidth(), panel.getHeight());
	}
	
	public void display() {
	    Graphics graphics = null;
		try {
			graphics = panel.getGraphics();
			if ((graphics != null) && (offScreenBuffer != null)) {
				graphics.drawImage(offScreenBuffer, 0, 0, null);
			}
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);  
		} finally {
			if (graphics != null) {
				graphics.dispose();
			}
		}
	}
	
	public void drawGrid() {
		for (Space space : grid.getSpaces()) {
			drawSpace(space);
		}
	}

	private void drawSpace(Space space) {
		int x = spaceSize * space.column;
		int y = spaceSize * space.row;
		
		g.setColor(SPACE_COLOR);
		g.fillRect(x, y, spaceSize, spaceSize);
		
		g.setColor(SPACE_LABEL_COLOR);
		//For now, draw the label in the lower left
		//because Java draws text from the lower left so it's easier to register
		g.drawString(Integer.toString(space.id), x + SPACE_LABEL_OFFSET_PX, y - SPACE_LABEL_OFFSET_PX + spaceSize);
	}

	public void drawBoard(BoardState board) {
		for (Entry piece : board.getPieces().iterateSpaces()) {
			drawPiece(piece);
		}
	}

	private void drawPiece(Entry piece) {
		int x = (spaceSize * piece.space.column) + pieceOffset;
		int y = (spaceSize * piece.space.row) + pieceOffset;
		
		drawPiece(piece.piece, x, y);
	}
	

	private void drawPiece(Piece piece, int x, int y) {
		Color color = PIECE_COLORS.get(piece.color);
		g.setColor(color);
		g.fillOval(x, y, pieceSize, pieceSize);
		
		g.setStroke(pieceBorderStroke);
		
		Color border = color.brighter();
		g.setColor(border);
		g.drawOval(x, y, pieceSize, pieceSize);
		
		
		if (piece.isKing()) {
			g.setColor(KING_BORDER_COLOR);
			g.drawOval(x - PIECE_BORDER_WIDTH, y - PIECE_BORDER_WIDTH, kingBorderSize, kingBorderSize);
		}
	}
	
}