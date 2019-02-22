
package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares = new ArrayList<>();
	@JsonProperty private Square captainQuarter;

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
		this.health = length;
		createCaptainQuarter();
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public Square getCaptainQuarter() {
		return this.captainQuarter;
	}

	public void createCaptainQuarter()
	{
		if(kind.equals("BATTLESHIP"))
			captainQuarter = occupiedSquares.get(2);
		else if(kind.equals("DESTROYER"))
			captainQuarter = occupiedSquares.get(1);
		else if(kind.equals("MINESWEEPER"))
			captainQuarter = occupiedSquares.get(0);
	}



	public void setOccupiedSquares(List<Square> newOccupiedSquares) {
		if (!occupiedSquares.isEmpty()) {
			occupiedSquares.clear();
		}
		for(Square square : newOccupiedSquares) {
			occupiedSquares.add(square);
		}

	}
	/*
	public void checkCaptinQ(char col, int row, boolean isVertical)
	{
		for(int i = 0; i<captainQIndex ; i++)
		{
			if(!isVertical)
			{
				occupiedSquares.add(new Square(row,(char)(col+i)));
			}
			else
			{
				occupiedSquares.add(new Square(row+i, col));
			}
		}
		occupiedSquares.get(captainQIndex-2).setCaptainQbl(true);
	}
*/
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
		if(this.health <= 0) {
			return 1;
		} else {
			return 0;
		}
	}
}