
package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares = new ArrayList<>();

	private String kind;
	private int length = 0;
	public int health = 0;
	public int captainQHealth = 0;
	public int captainQLocation = 0;
	public boolean submerged = false;

	public Ship() {
	}

	public Ship(String kind) {
		this.kind = kind;
		if (kind.equals("MINESWEEPER")) {
			captainQHealth = 1;
			captainQLocation = 0;
			length = 2;
		} else if (kind.equals("DESTROYER")) {
			captainQHealth = 2;
			captainQLocation = 1;
			length = 3;
		} else if (kind.equals("BATTLESHIP")) {
			captainQHealth = 2;
			captainQLocation = 2;
			length = 4;
		} else if (kind.equals("SUBMARINE")) {
			captainQHealth = 2;
			captainQLocation = 1;
			length = 5;
		}
		this.health = length;
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public void setOccupiedSquares(List<Square> newOccupiedSquares) {
		if (!occupiedSquares.isEmpty()) {
			occupiedSquares.clear();
		}
		for(Square square : newOccupiedSquares) {
			occupiedSquares.add(square);
		}
	}

	public int hitCaptain()
	{
		captainQHealth--;
		return  captainQHealth;
	}

	public int getLength() {
		return length;
	}

	public String getKind() {
		return kind;
	}

	public void hitShip() {
		this.health -= 1;
	}

	public int isDead() {
		if(this.health <= 0 || this.captainQHealth <= 0) {
			return 1;
		} else {
			return 0;
		}
	}
}