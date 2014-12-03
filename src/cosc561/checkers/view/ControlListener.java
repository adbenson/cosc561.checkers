package cosc561.checkers.view;

import cosc561.checkers.model.Space;
import cosc561.checkers.utility.Vector;

public interface ControlListener {

	void dragPiece(Space dragFrom, Vector point);

	void hover(Space hover);

	void capturePiece(Space space);

	void selectPiece(Space space);

	void render();

	void createContextMenu(Vector point);

	void movePiece(Space dragFrom, Space dropped);

	Space getSpaceAt(Vector point);

}
