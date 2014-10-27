package cosc561.checkers.view;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import cosc561.checkers.model.Space;

public class ControlHandler extends MouseInputAdapter {
	
	private BoardWindow window;

	private Space hovered;
	private Space dragFrom;
	
	public ControlHandler(BoardWindow window) {
		super();
		this.window = window;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (dragFrom == null) {
			dragFrom = getSpace(event);
		}
		window.dragPiece(dragFrom, event.getPoint());
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		Space hover = getSpace(event);
		
		//Don't bother if the space we're hovering hasn't changed
		if ((hover != null) && ! hover.equals(hovered)) {
			window.hover(hover);
		}
		
		hovered = hover;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		window.selectPiece(getSpace(event));
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		window.hover(getSpace(event));
	}

	@Override
	public void mouseExited(MouseEvent event) {
		dragFrom = null;
		hovered = null;
		
		window.dragPiece(null, null);
		window.selectPiece(null);
		window.hover(null);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		dragFrom = getSpace(event);
		window.dragPiece(dragFrom, event.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		Space dropped = getSpace(event);
		if (dropped == null) {
			System.out.println("Piece dropped on non-space");
		}
		//Don't bother if the piece hasn't actually moved
		else if (!dropped.equals(dragFrom)) {
			window.movePiece(dragFrom, dropped);
			window.selectPiece(dropped);
		}
		
		dragFrom = null;
		window.dragPiece(null, null);
	}
	
	private Space getSpace(MouseEvent event) {
		return window.getSpaceAt(event.getPoint());
	}
}
