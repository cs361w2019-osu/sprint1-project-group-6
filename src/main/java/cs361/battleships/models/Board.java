package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Ship> placedShips;
	private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
		this.placedShips = new ArrayList<>();
		this.attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		List<Square> shipLoc = new ArrayList<>();

		for(Ship shipToCheck : placedShips) {
			if(shipToCheck.getKind().equals(ship.getKind())) {
				return false;
			}
		}

		// Calculate the location of the ship based off of type, starting location, and verticality
		if (isVertical) {
			if (x + ship.getLength() < 11 && x > 0) {
				for (int i = 0; i < ship.getLength(); i++) {
					shipLoc.add(new Square(x + i, y));
				}
			} else {
				return false;
			}
		} else {
			if (y + ship.getLength() - 'A' < 11 && y >= 'A') {
				for (int i = 0; i < ship.getLength(); i++) {
					shipLoc.add(new Square(x, (char)(y + i)));
				}
			} else {
				return false;
			}
		}

		// Check if there is already a ship in this location
		for (Square locToCheck : shipLoc) {
			for (Ship shipToCheck : placedShips) {
				for (Square shipCheckLocToCheck : shipToCheck.getOccupiedSquares()) {
					if (locToCheck.getRow() == shipCheckLocToCheck.getRow() && locToCheck.getColumn() == shipCheckLocToCheck.getColumn()) {
						return false;
					}
				}
			}
		}

		Ship newShip = new Ship(ship.getKind());
		newShip.setOccupiedSquares(shipLoc);
		placedShips.add(newShip);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result atkRes = new Result();
		atkRes.setLocation(new Square(x,y));

		if(y < 'A' || y > 'J' || x < 0 || x > 10){
			atkRes.setResult(AtackStatus.INVALID);
			this.attacks.add(atkRes);
			return atkRes;
		}

		for(Result resToCheck : this.attacks) {
			if(atkRes.getLocation().getColumn() == resToCheck.getLocation().getColumn() && atkRes.getLocation().getRow() == resToCheck.getLocation().getRow()) {
				atkRes.setResult(AtackStatus.INVALID);
				this.attacks.add(atkRes);
				return atkRes;
			}
		}

		for(Ship ship : this.placedShips) {
			for(Square shipSquare : ship.getOccupiedSquares()) {
				if(shipSquare.getRow() == atkRes.getLocation().getRow() && shipSquare.getColumn() == atkRes.getLocation().getColumn()) {
					atkRes.setResult(AtackStatus.HIT);
					ship.hitShip();
					if(ship.isDead() == 1) {
						atkRes.setResult(AtackStatus.SUNK);
					}

					if(allShipsSunk()) {
						atkRes.setResult(AtackStatus.SURRENDER);
					}

					this.attacks.add(atkRes);
					return atkRes;
				}
			}
		}

		atkRes.setResult(AtackStatus.MISS);
		this.attacks.add(atkRes);
		return atkRes;
	}

	public boolean allShipsSunk() {
		for(Ship ship : this.placedShips) {
			if(ship.isDead() != 1) {
				return false;
			}
		}
		return true;
	}

	public List<Ship> getShips() {
		//TODO implement
		return placedShips;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
		placedShips = ships;
	}

	public List<Result> getAttacks() {
		//TODO implement
		return attacks;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
		this.attacks = attacks;
	}
}
