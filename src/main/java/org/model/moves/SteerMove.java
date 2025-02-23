package org.model.moves;

import org.model.board.Board;
import org.model.board.Coordinate;
import org.model.board.MoveResult;

public class SteerMove implements Move {

    Coordinate target;

    public SteerMove(Coordinate target) {
        this.target = target;
    }

    @Override
    public Board playOn(Board board, int playerID) {
        return board.steerShip(playerID, target);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canSteerShip(playerID, target);
    }

    @Override
    public String transcribe()
    {
        return "Steuern nach %s".formatted(target.transcribe());
    }

    public Coordinate getTarget() {
        return target;
    }
}
