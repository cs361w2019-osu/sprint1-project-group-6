package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() != INVALID);

        return true;
    }

    public char randCol() {
        //Column = 10
        Random rand = new Random();
//        int MaxShipLength = 4; //Maxium ship length is 4
        char randChar = (char) (65 + rand.nextInt(9)); ; // Select from 1 to 10 for Column(y-axis).
        return randChar;
    }

    public int randRow() {
        Random rand = new Random();
        int x = rand.nextInt(9) + 1; // Select from 1 to 10 for Row(x-axis).
        return x;
    }

    public boolean randVertical() {
        Random randBoolean = new Random(); //Create random obj
        boolean randDirect = randBoolean.nextBoolean(); //Get next boolean value
//        int MaxShipLength = 4; //The max of ship length is 4.
//        int Shipsize = randBoolean.nextInt(MaxShipLength) + 1; //Randomize the size of ship, max length is 4.
        return randDirect;
    }
}
