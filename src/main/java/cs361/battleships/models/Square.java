package cs361.battleships.models;

@SuppressWarnings("unused")
public class Square {

	private int captainQ;
	private boolean captainQbl;
	private int row;
	private char column;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
		this.captainQbl = false;
		this.captainQ = 0;
	}

	public void setCaptainQ(int hits)
	{
		this.captainQ = hits;
	}
	public int getCaptainQ()
	{
		return this.captainQ;
	}
	public  void setCaptainQbl(boolean bool)
	{
		this.captainQbl = bool;
	}
	public boolean getCaptainQbl() {
		return  this.captainQbl;
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
