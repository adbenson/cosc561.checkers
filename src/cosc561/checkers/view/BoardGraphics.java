package cosc561.checkers.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap.Entry;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.PlayerTurn;
import cosc561.checkers.model.PlayerTurn.Change;
import cosc561.checkers.model.Space;
import cosc561.checkers.utility.Vector;

public class BoardGraphics {
	
	
	private static final Color BOARD_COLOR = new Color(247, 241, 212);
	private static final Color SPACE_COLOR = new Color(119, 106, 107);
	
	private static final Color SPACE_LABEL_COLOR = Color.WHITE;
	private static final Font SPACE_LABEL_FONT = new Font("Serif", Font.ITALIC, 12);

	private static final Color HIGHLIGHT_END_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.0f);
	private static final Color HIGHLIGHT_START_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);
	
	private static final Color CONTOUR_END_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.3f);
	private static final Color CONTOUR_START_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.0f);
	
	private static final Color KING_HIGHLIGHT_COLOR = new Color(1.0f, 0.770f, 0.035f, 0.6f);
	private static final Color KING_HIGHLIGHT_END_COLOR = new Color(1.0f, 0.770f, 0.035f, 0.0f);
	
	private static final Map<PlayerColor, Color> PIECE_COLORS;
	
	private static final int SPACE_LABEL_OFFSET_PX = 5;
	private static final double PIECE_TO_SPACE_SIZE_RATIO = 0.8;

	private static final int PIECE_BORDER_WIDTH = 6;
	
	private static final double ARROW_ANGLE = Math.PI / 5;
	private static final double ARROW_SIZE = 10;
	
	static {
		PIECE_COLORS = new HashMap<PlayerColor, Color>();
		PIECE_COLORS.put(PlayerColor.RED, new Color(200, 30, 0));
		PIECE_COLORS.put(PlayerColor.BLACK, new Color(70, 70, 80));
	}
	
	private Graphics2D g;
	private Image offScreenBuffer;
	
	private final Container panel;
	
	private final int sideLength;
	
	private final int spaceSize;
	private final int halfSpace;
	
	private final int pieceOffset;
	private final int pieceSize;
	private final int halfPiece;
	
	private final int kingBorderSize;
	
	private final Stroke pieceBorderStroke;
	
	private final Grid grid;
	
	private Vector lightSource;
	
	public BoardGraphics(Container panel, Grid grid) {
		this.panel = panel;
		this.grid = grid;
		
		sideLength = Math.min(panel.getWidth(), panel.getHeight());
		
		spaceSize = sideLength / Grid.SPACES_PER_SIDE;
		halfSpace = spaceSize / 2;
		
		pieceSize = (int) (spaceSize * PIECE_TO_SPACE_SIZE_RATIO);
		halfPiece = pieceSize / 2;
		
		pieceOffset = (int) ((spaceSize - pieceSize) / 2);
		
		kingBorderSize = pieceSize + (2 * PIECE_BORDER_WIDTH);
				
		pieceBorderStroke = new BasicStroke(PIECE_BORDER_WIDTH);
		
		lightSource = new Vector(sideLength / 4, sideLength / 5);
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
		Vector origin = getOrigin(space);
		
		g.setColor(SPACE_COLOR);
		g.fillRect(origin.intX(), origin.intY(), spaceSize, spaceSize);
		
		g.setFont(SPACE_LABEL_FONT);
		g.setColor(SPACE_LABEL_COLOR);
		//For now, draw the label in the lower left
		//because Java draws text from the lower left so it's easier to register
		g.drawString(Integer.toString(space.id), origin.intX() + SPACE_LABEL_OFFSET_PX, origin.intY() - SPACE_LABEL_OFFSET_PX + spaceSize);
	}

	public void drawBoard(BoardState board) {
		for (Entry piece : board.getPieces().iterateSpaces()) {
			drawPiece(piece);
		}
	}
	
	private void drawPiece(Entry piece) {
		Vector location = getOrigin(piece.space).add(pieceOffset);
		drawPiece(piece.piece, location);
	}
	
	public void drawDraggedPiece(Piece piece, Vector to) {
		//Later we can add shadow and shit
		drawPiece(piece, to.add(pieceOffset));
	}
	
	private void drawPiece(Piece piece, Vector location) {
		drawPiece(piece, location, false);
	}
	
	private void drawPiece(Piece piece, Vector location, boolean removed) {
		Color color = PIECE_COLORS.get(piece.color);
		if (!removed) {			
			drawPieceInternal(location, color, piece.isKing());
		}
		
		g.setStroke(pieceBorderStroke);
		
		Color border = color;
		g.setColor(border);
		g.drawOval(location.intX(), location.intY(), pieceSize, pieceSize);
		
		if (piece.isKing()) {
			g.setColor(KING_HIGHLIGHT_COLOR);
			g.drawOval(location.intX(), location.intY(), pieceSize, pieceSize);
		}
	}

	private void drawPieceInternal(Vector location, Color color, boolean king) {
		int x = location.intX();
		int y = location.intY();
		
		Vector center = new Vector(x + halfPiece, y + halfPiece);
		
        // Retains the previous state
        Paint oldPaint = g.getPaint();
        Paint p;

        // Fills the circle with solid color
        g.setColor(color);
        g.fillOval(x, y, pieceSize, pieceSize);
        
        Vector distance = lightSource.subtract(center);
        Vector proportional = distance.scale(1.0 / sideLength);
        Vector scaled = proportional.scale(pieceSize * 0.3f).add(halfPiece);
        Vector translated = scaled.add(x, y);
        
        // Adds oval specular highlight at the top left
        p = new RadialGradientPaint(
        	center, 
    		pieceSize * 0.75f,
    		translated,
            new float[] { 0.0f, 0.5f },
            new Color[] { 
        		king? KING_HIGHLIGHT_COLOR : HIGHLIGHT_START_COLOR,
        		king? KING_HIGHLIGHT_END_COLOR : HIGHLIGHT_END_COLOR 
    		},
            RadialGradientPaint.CycleMethod.NO_CYCLE
        );
        g.setPaint(p);
        g.fillOval(x, y, pieceSize, pieceSize);
        
        // Creates dark edges for 3D effect
        p = new RadialGradientPaint(
    		center, 
    		halfPiece,
            new float[] { 0.5f, 1.0f },
            new Color[] { 
    			CONTOUR_START_COLOR,
    			CONTOUR_END_COLOR 
    		}
        );

        g.setPaint(p);
        g.fillOval(x, y, pieceSize, pieceSize);
        
        // Restores the previous state
        g.setPaint(oldPaint);
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

	public Space getSpaceAt(Vector point) {
		return getSpaceAt(point.intX(), point.intY());
	}
	
	private Vector getOrigin(Space space) {
		int x = spaceSize * space.column;
		int y = spaceSize * space.row;
		
		return new Vector(x, y);
	}

	public Vector getOffset(Space from, Vector to) {
		Vector origin = getOrigin(from);
		
		return to.subtract(origin);
	}

	public void drawTurn(PlayerTurn turn) {
		for (PlayerTurn.Change change : turn.getChanges()) {
			drawChange(change, turn.player);
		}
	}

	private void drawChange(Change change, PlayerColor player) {
		if (change instanceof PlayerTurn.King) {
			drawKing(change);
		}
		else {
			
			if (change.from != null) {
				drawRemovedPiece(change.piece, change.from);
			}
			if (change.from != null && change.to != null) {
				drawMoveLine(change.from, change.to, change.piece.color);
			}

			
			if (change.capture != null) {
				drawChange(change.capture, player);
			}
		}
	}
	
	public void drawRemovedPiece(Piece piece, Space from) {
		drawPiece(piece, getOrigin(from).add(pieceOffset), true);
	}

	public void drawMoveLine(Space from, Space to, PlayerColor player) {
		drawMoveLine(getCenterPoint(from), getCenterPoint(to), player);
	}

	public void drawDragLine(Space from, Vector to, PlayerColor player) {
		drawMoveLine(getCenterPoint(from), to.add(halfSpace), player);
	}

	public void drawMoveLine(Vector from, Vector to, PlayerColor player) {
		Color color = PIECE_COLORS.get(player);

		Path2D.Double path = getMoveArc(from, to);
		
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.WHITE);
		g.draw(path);
		
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(color);
		g.draw(path);
	}
	
	private Path2D.Double getMoveArc(Vector from, Vector to) {
		int peakX = Math.max((from.intX() + to.intX()) / 2, spaceSize / 4);
		int peakY = Math.min(from.intY(), to.intY()) - spaceSize / 4;
		
		Path2D.Double path = new Path2D.Double();
		path.moveTo(from.x, from.y);
		path.quadTo(peakX, peakY, to.x, to.y);
		
		Vector endSegment = new Vector(peakX, peakY).subtract(to);
		double angle = endSegment.getAngle();
		
		Vector arrowEnd1 = to.project(angle - ARROW_ANGLE, ARROW_SIZE);
		Vector arrowEnd2 = to.project(angle + ARROW_ANGLE, ARROW_SIZE);
		
		path.lineTo(arrowEnd1.intX(), arrowEnd1.intY());
		path.moveTo(to.x, to.y);
		path.lineTo(arrowEnd2.intX(), arrowEnd2.intY());
		
		return path;
	}

	private Vector getCenterPoint(Space to) {
		return getOrigin(to).add(halfSpace);
	}

	private void drawKing(Change change) {
		//TODO Actually draw king
	}
	
}
