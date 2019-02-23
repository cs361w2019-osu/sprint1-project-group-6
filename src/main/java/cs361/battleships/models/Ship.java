
package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares = new ArrayList<>();
	@JsonProperty private Square captainQ;
	@JsonProperty private int CQhealth;

	private String kind;
	private int length = 0;
	public int health = 0;

	public Ship() {
	}

	public Ship(String kind) {
		this.kind = kind;
		if (kind.equals("MINESWEEPER")) {
			length = 2;
		} else if (kind.equals("DESTROYER")) {
			length = 3;
		} else if (kind.equals("BATTLESHIP")) {
			length = 4;
		}
		captainQ = getCaptainQ();
		this.health = length;

	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public Square getCaptainQ() {
		return this.captainQ;
	}

	public void createCaptainQ() {

		if (kind.equals("BATTLESHIP")) {
			captainQ.setRow(occupiedSquares.get(2).getRow());
			captainQ.setColumn(occupiedSquares.get(2).getColumn());
			CQhealth = 2;
		} else if (kind.equals("DESTROYER")) {
			captainQ.setRow(occupiedSquares.get(1).getRow());
			captainQ.setColumn(occupiedSquares.get(1).getColumn());
			CQhealth = 2;
		} else if (kind.equals("MINESWEEPER")) {
			captainQ.setRow(occupiedSquares.get(0).getRow());
			captainQ.setColumn(occupiedSquares.get(0).getColumn());
			CQhealth = 1;
		}
	}

	public void setOccupiedSquares(List<Square> newOccupiedSquares) {
		if (!occupiedSquares.isEmpty()) {
			occupiedSquares.clear();
		}
		for(Square square : newOccupiedSquares) {
			occupiedSquares.add(square);
		}

	}

	public void hitCaptain(){
		this.CQhealth--;
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
		if(this.CQhealth <= 0)
			return 1;

		if(this.health <= 0) {
			return 1;
		} else {
			return 0;
		}
	}
}