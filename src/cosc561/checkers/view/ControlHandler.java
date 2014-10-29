package cosc561.checkers.view;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import cosc561.checkers.model.Space;

public class ControlHandler extends MouseInputAdapter {
	
	private ControlListener listener;

	private Space hovered;
	private Space dragFrom;
	
	public ControlHandler(ControlListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (dragFrom != null) {
			listener.dragPiece(dragFrom, new Point(event.getPoint()));
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		Space hover = getSpace(event);
		
		//Don't bother if the space we're hovering hasn't changed
		if ((hover != null) && ! hover.equals(hovered)) {
			listener.hover(hover);
		}
		
		hovered = hover;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			if (event.getClickCount() > 1) {
				listener.capturePiece(getSpace(event));
			}
			else {
				listener.selectPiece(getSpace(event));
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
		listener.hover(getSpace(event));
	}

	@Override
	public void mouseExited(MouseEvent event) {
		hovered = null;
		
		listener.selectPiece(null);
		listener.hover(null);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		//Helps to clear the context menu
		listener.render();
		dragFrom = getSpace(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.isPopupTrigger()) {
			listener.createContextMenu(new Point(event.getPoint()));
		}
		
		if (event.getButton() == MouseEvent.BUTTON1 && dragFrom != null) {
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
			listener.movePiece(dragFrom, dropped);
			listener.selectPiece(dropped);
		}
		
		dragFrom = null;
		listener.dragPiece(null, null);
	}

	private Space getSpace(MouseEvent event) {
		return listener.getSpaceAt(new Point(event.getPoint()));
	}
	
}
