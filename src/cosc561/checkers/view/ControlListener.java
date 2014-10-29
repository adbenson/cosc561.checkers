package cosc561.checkers.view;

import cosc561.checkers.model.Space;

public interface ControlListener {

	void dragPiece(Space dragFrom, Point point);

	void hover(Space hover);

	void capturePiece(Space space);

	void selectPiece(Space space);

	void render();

	void createContextMenu(Point point);

	void movePiece(Space dragFrom, Space dropped);

	Space getSpaceAt(Point point);

}
