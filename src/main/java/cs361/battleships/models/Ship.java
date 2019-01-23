package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	private String kind;
	private int health;

	public Ship() {
		occupiedSquares = new ArrayList<>();
		this.health = 0;
	}

	public Ship(String kind) {
		//TODO implement
		this.kind = kind;
		this.health = getSize();
	}

	public String getKind() {
		return this.kind;
	}

	public List<Square> getOccupiedSquares() {
		//TODO implement
		return this.occupiedSquares;
	}

	public void setOccupiedSquares(List<Square> occupiedSquares) {
		this.occupiedSquares = occupiedSquares;
	}

	public int getSize() {
		switch (kind) {
			case "MINESWEEPER":
				return 2;

			case "DESTROYER":
				return 3;

			case "BATTLESHIP":
				return 4;
		}

		return -1;
	}

	public void hitShip() {
		this.health -= 1;
	}

	public boolean isDead() {
		return this.health <= 0;
	}
}