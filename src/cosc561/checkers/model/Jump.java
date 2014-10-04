package cosc561.checkers.model;

public class Jump {

	Space origin;
	Space capture;
	Space landing;

	public Jump() {
	}

	public Jump(Space o, Space j, Space l) {
		this.origin = o;
		this.capture = j;
		this.landing = l;
	}

}
