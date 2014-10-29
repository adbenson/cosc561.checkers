package cosc561.checkers.view;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;

import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PlayerColor;
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
		if (dragFrom != null) {
			window.dragPiece(dragFrom, new Point(event.getPoint()));
		}
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
		if (event.getButton() == MouseEvent.BUTTON1) {
			if (event.getClickCount() > 1) {
				window.capturePiece(getSpace(event));
			}
			else {
				window.selectPiece(getSpace(event));
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
		window.hover(getSpace(event));
	}

	@Override
	public void mouseExited(MouseEvent event) {
		hovered = null;
		
		window.selectPiece(null);
		window.hover(null);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		//Helps to clear the context menu
		window.render();
		dragFrom = getSpace(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.isPopupTrigger()) {
			window.createContextMenu(new Point(event.getPoint()));
		}
		
		if (dragFrom != null) {
			completeDrag(event);
		}
	}
	
	private void completeDrag(MouseEvent event) {
		Space dropped = getSpace(event);
		if (dropped == null) {
			System.out.println("Piece dropped on non-space");
		}
		//Don't bother if the piece hasn't actually moved
		else if (!dropped.equals(dragFrom)) {
			window.movePiece(dragFrom, dropped);
			window.selectPiece(dropped);
			
			dragFrom = null;
			window.dragPiece(null, null);
		}
	}

	private Space getSpace(MouseEvent event) {
		return window.getSpaceAt(new Point(event.getPoint()));
	}
	
}
