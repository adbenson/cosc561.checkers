package cosc561.checkers.view;

public class Point {
	
	public final int x;
	public final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(java.awt.Point point) {
		this(point.x, point.y);
	}

	public Point add(Point that) {
		return new Point(x + that.x, y + that.y);
	}
	
	public Point add(int offset) {
		return new Point(x + offset, y + offset);
	}

	public Point subtract(Point that) {
		return new Point(x - that.x, y - that.y);
	}

}
