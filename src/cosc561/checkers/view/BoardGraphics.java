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
import cosc561.checkers.model.Piece;
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
	private final int halfSpace;
	
	private final int pieceOffset;
	private final int pieceSize;
	private final int pieceRadius;
	
	private final int kingBorderSize;
	
	private final Stroke pieceBorderStroke;
	
	private final Grid grid;
	
	public BoardGraphics(Container panel, Grid grid) {
		this.panel = panel;
		this.grid = grid;
		
		sideLength = Math.min(panel.getWidth(), panel.getHeight());
		
		spaceSize = sideLength / Grid.SPACES_PER_SIDE;
		halfSpace = spaceSize / 2;
		
		pieceSize = (int) (spaceSize * PIECE_TO_SPACE_SIZE_RATIO);
		pieceRadius = pieceSize / 2;
		
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
		Point origin = getOrigin(space);
		
		g.setColor(SPACE_COLOR);
		g.fillRect(origin.x, origin.y, spaceSize, spaceSize);
		
		g.setColor(SPACE_LABEL_COLOR);
		//For now, draw the label in the lower left
		//because Java draws text from the lower left so it's easier to register
		g.drawString(Integer.toString(space.id), origin.x + SPACE_LABEL_OFFSET_PX, origin.y - SPACE_LABEL_OFFSET_PX + spaceSize);
	}

	public void drawBoard(BoardState board) {
		for (Entry piece : board.getPieces().iterateSpaces()) {
			drawPiece(piece);
		}
	}

	private void drawPiece(Entry piece) {
		Point location = getOrigin(piece.space).add(pieceOffset);
		drawPiece(piece.piece, location);
	}
	

	public void drawDraggedPiece(Piece piece, Point to, Point dragOffset) {
		//Later we can add shadow and shit
		drawPiece(piece, to.subtract(dragOffset));
	}
	
	private void drawPiece(Piece piece, Point location) {
		Color color = PIECE_COLORS.get(piece.color);
		g.setColor(color);
		g.fillOval(location.x, location.y, pieceSize, pieceSize);
		
		g.setStroke(pieceBorderStroke);
		
		Color border = color.brighter();
		g.setColor(border);
		g.drawOval(location.x, location.y, pieceSize, pieceSize);
		
		
		if (piece.isKing()) {
			g.setColor(KING_BORDER_COLOR);
			g.drawOval(location.x - PIECE_BORDER_WIDTH, location.y - PIECE_BORDER_WIDTH, kingBorderSize, kingBorderSize);
		}
	}

	public Space getSpaceAt(int x, int y) {
		if (spaceSize < 1) {
			return null;
		}
		
		int column = x / spaceSize;
		int row = y / spaceSize;
		
		try {
			return grid.getSpaceByCoordinates(row, column);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public Space getSpaceAt(Point point) {
		return getSpaceAt(point.x, point.y);
	}
	
	private Point getOrigin(Space space) {
		int x = spaceSize * space.column;
		int y = spaceSize * space.row;
		
		return new Point(x, y);
	}

	public Point getOffset(Space from, Point to) {
		Point origin = getOrigin(from);
		
		return to.subtract(origin);
	}
	
}
