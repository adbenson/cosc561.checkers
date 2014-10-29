package cosc561.checkers.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import cosc561.checkers.model.Piece;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.Space;

@SuppressWarnings("serial")
class BoardContextMenu extends JPopupMenu {
	
	private final BoardPanel board;
	
	public BoardContextMenu(Space space, BoardPanel boardPanel, boolean hasPiece) {
		super();
		
		this.board = boardPanel;
		
		if (hasPiece) {
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
				board.removePiece(space);
			}
		};
	}

	private ActionListener kingPieceListener(final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				board.kingPiece(space);
			}
		};
	}
	
	private ActionListener addPieceListener(final PlayerColor color, final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				board.addPiece(Piece.get(color), space);
			}
		};
	}

	private ActionListener addKingListener(final PlayerColor color, final Space space) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				board.addPiece(Piece.get(color).getKing(), space);
			}
		};
	}

}
