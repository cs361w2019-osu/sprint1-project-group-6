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
	private boolean sub_submerged = false;

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
		boolean isSubmarine = ship.getKind().equals("SUBMARINE");
		// Check if already placed
		for(Ship shipToCheck : placedShips) {
			if(shipToCheck.getKind().equals(ship.getKind())) {
				return false;
			}
		}

		// Calculate the location of the ship based off of type, starting location, and verticality
		if (isVertical) {
			if (x + ship.getLength() < 11 && x > 0 && !isSubmarine) {
				for (int i = 0; i < ship.getLength(); i++) {
					shipLoc.add(new Square(x + i, y));
				}
			} else if (isSubmarine && x > 0 && x + ship.getLength()-1 <= 11 && y + 2 - 'A' < 11) {
				for (int i = 0; i < ship.getLength() - 1; i++) {
					shipLoc.add(new Square(x + i, y));
				}
				shipLoc.add(new Square(x + 2, (char)(y + 1)));
			} else {
				return false;
			}
		} else {
			if (y + ship.getLength() - 'A' < 11 && y >= 'A' && !isSubmarine) {
				for (int i = 0; i < ship.getLength(); i++) {
					shipLoc.add(new Square(x, (char)(y + i)));
				}
			} else if (isSubmarine && x > 1 && y + ship.getLength()-1 - 'A' < 11) {
				for (int i = 0; i < ship.getLength() - 1; i++) {
					shipLoc.add(new Square(x, (char)(y + i)));
				}
				shipLoc.add(new Square(x - 1, (char)(y + 2)));
			} else {
				return false;
			}
		}

		// If sub, then check and mark as submerged
		if (isSubmarine) {
			for (Square locToCheck : shipLoc) {
				for (Ship shipToCheck : placedShips) {
					for (Square shipCheckLocToCheck : shipToCheck.getOccupiedSquares()) {
						if (locToCheck.getRow() == shipCheckLocToCheck.getRow() && locToCheck.getColumn() == shipCheckLocToCheck.getColumn()) {
							sub_submerged = true;
							break;
						}
					}
				}
			}
		} else {
			// Check if there is already a ship in this location
			// Check for submarine, if submarine is in square, then mark it as submerged
			for (Square locToCheck : shipLoc) {
				for (Ship shipToCheck : placedShips) {
					if (!shipToCheck.getKind().equals("SUBMARINE")) {
						for (Square shipCheckLocToCheck : shipToCheck.getOccupiedSquares()) {
							if (locToCheck.getRow() == shipCheckLocToCheck.getRow() && locToCheck.getColumn() == shipCheckLocToCheck.getColumn()) {
								return false;
							}
						}
					} else {
						for (Square shipCheckLocToCheck : shipToCheck.getOccupiedSquares()) {
							if (locToCheck.getRow() == shipCheckLocToCheck.getRow() && locToCheck.getColumn() == shipCheckLocToCheck.getColumn()) {
								sub_submerged = true;
							}
						}
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


		// Where we check the results to see if we can attack there again
//		for(Result resToCheck : this.attacks) {
//			if(atkRes.getLocation().getColumn() == resToCheck.getLocation().getColumn() && atkRes.getLocation().getRow() == resToCheck.getLocation().getRow()) {
//				atkRes.setResult(AtackStatus.INVALID);
//				this.attacks.add(atkRes);
//				return atkRes;
//			}
//		}

		// Where we check to ships to see if it is hit
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

					if(atkRes.getResult() != AtackStatus.BANG) {
						this.attacks.add(atkRes);
					}
					return atkRes;
				}
				i++;
			}
		}

		atkRes.setResult(AtackStatus.MISS);
		this.attacks.add(atkRes);
		return atkRes;
	}


	// Direction to move is 0 - 3 and maps to N, W, S, E
	public void moveShip(int direction) {
		List<Ship> newShipList = new ArrayList<Ship>(this.placedShips);
		List<Ship> finalShipList = new ArrayList<Ship>();

		while(newShipList.size() > 0) {

			// Get the directionest ship
			int dist = 10;
			int closestShipId = 0;
			int currShipId = 0;
			for (Ship ship : newShipList) {
				for (Square square : ship.getOccupiedSquares()) {
					// Direction is north/south so we need to check row position
					if (direction == 0 || direction == 2) {
						if (Math.abs(square.getRow() - (direction * 5)) < dist) {
							dist = Math.abs(square.getRow() - (direction * 5));
							if(direction == 2) {
								dist++;
							}
							closestShipId = currShipId;
						}
					} else { // Direction is east/west so we need to check col pos
						if (Math.abs((square.getColumn() - 65) - ((direction - 1) * 5)) < dist) {
							dist = Math.abs((square.getColumn() - 65) - ((direction - 1) * 5));
							closestShipId = currShipId;
							if(direction == 1) {
								dist++;
							}
						}
					}
				}
				currShipId++;
			}

			if(dist <= 1) {
				finalShipList.add(newShipList.get(closestShipId));
				newShipList.remove(closestShipId);
				continue;
			}

			List<Square> occSquares = new ArrayList<Square>();
			for(Square sq : newShipList.get(closestShipId).getOccupiedSquares()) {
				Square newSq = new Square();
				newSq.setColumn(sq.getColumn());
				newSq.setRow(sq.getRow());
				occSquares.add(newSq);
			}

			boolean noOverlap = true;
			int currIndex = 0;
			for (Square square : occSquares) {
				if (direction == 0 || direction == 2) {
					int newRow = (square.getRow() + (direction - 1));
					if(newRow > 0 && newRow < 11) {
					    square.setRow(newRow);
                        occSquares.set(currIndex, square);

                        for(Ship ship : finalShipList) {
                        	for(Square sq : ship.getOccupiedSquares()) {
                        		if(sq.getColumn() == square.getColumn() && sq.getRow() == square.getRow()) {
                        			noOverlap = false;
								}
							}
						}
                    }
				} else { // Direction is east/west so we need to check col pos
					// subtract to to get the either 1 or 3 to be -1 or 1
					char newCol = ((char) (square.getColumn() + (direction - 2)));
					if(newCol <= 'J' && newCol >= 'A') {
					    square.setColumn(newCol);
                        occSquares.set(currIndex, square);

						for(Ship ship : finalShipList) {
							for(Square sq : ship.getOccupiedSquares()) {
								if(sq.getColumn() == square.getColumn() && sq.getRow() == square.getRow()) {
									noOverlap = false;
								}
							}
						}
                    }
				}
				currIndex++;
			}

			if(noOverlap) {
				newShipList.get(closestShipId).setOccupiedSquares(occSquares);
			}

			finalShipList.add(newShipList.get(closestShipId));

			newShipList.remove(closestShipId);
		}

		this.placedShips = finalShipList;
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
