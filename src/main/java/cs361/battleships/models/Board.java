package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

	@JsonProperty public List<Result> pulsed;
	private List<Ship> placedShips;
	private List<Result> attacks;
	private List<Result> pulsedCenters;
	private int pulseDistFromCenter = 2;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
		this.placedShips = new ArrayList<>();
		this.attacks = new ArrayList<>();
		this.pulsed = new ArrayList<>();
		this.pulsedCenters = new ArrayList<>();
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
            int i = 0;
			for(Square shipSquare : ship.getOccupiedSquares()) {
				if(shipSquare.getRow() == atkRes.getLocation().getRow() && shipSquare.getColumn() == atkRes.getLocation().getColumn()) {
				    if(i == ship.captainQLocation) {
				        ship.hitCaptain();
				        atkRes.setResult(AtackStatus.BANG);
                    } else {
                        atkRes.setResult(AtackStatus.HIT);
                        ship.hitShip();
                    }
					if(ship.isDead() == 1) {
						atkRes.setResult(AtackStatus.SUNK);
					}

					if(allShipsSunk()) {
						atkRes.setResult(AtackStatus.SURRENDER);
					}

					this.attacks.add(atkRes);
					return atkRes;
				}
				i++;
			}
		}

		atkRes.setResult(AtackStatus.MISS);
		this.attacks.add(atkRes);
		return atkRes;
	}

	/*
	Used for sonar pulse attack.
	 */
	public Result pulse(int x, char y) {
		// Check if square was already a pulse center
		Result pulseCenterRes = new Result();
		pulseCenterRes.setLocation(new Square(x, y));
		for (Result resToCheck : this.pulsedCenters) {
			if (resToCheck.getLocation().getColumn() == y && resToCheck.getLocation().getRow() == x && (y < 'A' || y > 'J' || x < 0 || x > 10)) {
				pulseCenterRes.setResult(AtackStatus.INVALID);
				this.pulsedCenters.add(pulseCenterRes);
				return pulseCenterRes;
			}
		}

		// Iterate through pulse left to right
		for (int i = -(getPulseDistFromCenter()); i <= getPulseDistFromCenter(); i++) {
			for (int j = -(getPulseDistFromCenter() - Math.abs(i)); j <= getPulseDistFromCenter() - Math.abs(i); j++) {
				Result pulseRes = new Result();
				// Set all locations to be pulsed.
				pulseRes.setLocation(new Square(x + i, (char)(y + j)));
				pulseRes.setResult(AtackStatus.SHOWNOSHIP);
				if(!(pulseRes.getLocation().getColumn() < 'A' || pulseRes.getLocation().getColumn() > 'J' || pulseRes.getLocation().getRow() < 1 || pulseRes.getLocation().getRow() > 10)) {
					for (Ship ship : this.placedShips) {
						for (Square shipSquare : ship.getOccupiedSquares()) {
							if (shipSquare.getRow() == pulseRes.getLocation().getRow() && shipSquare.getColumn() == pulseRes.getLocation().getColumn()) {
								pulseRes.setResult(AtackStatus.SHOWSHIP);
							}
						}
					}
					this.pulsed.add(pulseRes);
				}
			}
		}

		return pulseCenterRes;
	}

	// Check if a pulsed squares is out of bounds
	public boolean pulsedOutOfBounds(Result res) {
		if (res.getLocation().getColumn() < 'A' || res.getLocation().getColumn() > 'J' || res.getLocation().getRow() < 1 || res.getLocation().getRow() > 10) {
			return true;
		}
		return false;
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

	public int getPulseDistFromCenter() {
		return pulseDistFromCenter;
	}
}
