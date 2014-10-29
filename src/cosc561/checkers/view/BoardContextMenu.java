package cosc561.checkers.view;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PieceMap.IllegalMoveException;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.Space;

@SuppressWarnings("serial")
class BoardContextMenu extends JPopupMenu {
	
	private final BoardWindow window;
	
	public BoardContextMenu(Space space, BoardWindow window) {
		super();
		
		this.window = window;
		
		if (window.getPiece(space) != null) {
			addItemsForPiece(space);
		}
		else {
			addItemsForEmptySpace(space);
		}
	}

	private void addItemsForPiece(Space space) {
		JMenuItem kingPiece = new JMenuItem("King piece");
		kingPiece.addActionListener(kingPieceListener(space));
		add(kingPiece);
		
		JMenuItem removePiece = new JMenuItem("Remove piece");
		removePiece.addActionListener(removePieceListener(space));
		add(removePiece);
	}

	private void addItemsForEmptySpace(Space space) {
		PlayerColor[] colors = PlayerColor.values();
		
		for (int i = 0; i < colors.length; i++) {
			PlayerColor color = colors[i];
			
			JMenuItem addPiece = new JMenuItem("Add "+color.name().toLowerCase()+" piece");
			addPiece.addActionListener(addPieceListener(color, space));
			this.add(addPiece);
			
			JMenuItem addKing = new JMenuItem("Add "+color.name().toLowerCase()+" king");
			addKing.addActionListener(addKingListener(color, space));
			this.add(addKing);
			
			if (i < colors.length -1) {
				this.addSeparator();
			}
		}
	}

	private ActionListener removePieceListener(final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					window.getGame().getState().removePiece(space);
				} catch (IllegalMoveException e) {
					System.err.println("Exception removing piece by context menu");
					e.printStackTrace();
				}
				window.render();
			}
		};
	}

	private ActionListener kingPieceListener(final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					window.getGame().getState().kingPiece(space);
				} catch (IllegalMoveException e) {
					System.err.println("Exception kinging piece by context menu");
					e.printStackTrace();
				}
				window.render();
			}
		};
	}
	
	private ActionListener addPieceListener(final PlayerColor color, final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					window.getGame().getState().addPiece(Piece.get(color), space);
				} catch (IllegalMoveException e) {
					System.err.println("Exception adding king piece by context menu");
					e.printStackTrace();
				}
				window.render();
			}
		};
	}

	private ActionListener addKingListener(final PlayerColor color, final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					window.getGame().getState().addPiece(Piece.get(color).getKing(), space);
				} catch (IllegalMoveException e) {
					System.err.println("Exception adding piece by context menu");
					e.printStackTrace();
				}
				window.render();
			}
		};
	}

}
