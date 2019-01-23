package cs361.battleships.models;

@SuppressWarnings("unused")
public class Result {

	private Square location;
	private Ship ship;
	private AtackStatus result;

	public Result () {
	}

	public Result (Square square, Ship ship, AtackStatus result) {
		this.setLocation(square);
		this.setShip(ship);
		this.setResult(result);
	}

	public AtackStatus getResult() {
		return this.result;
	}

	public void setResult(AtackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		return this.ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return this.location;
	}

	public void setLocation(Square square) {
		this.location = square;
	}
}
