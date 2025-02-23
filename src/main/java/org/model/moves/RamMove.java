package org.model.moves;

import org.model.board.Board;
import org.model.board.Coordinate;
import org.model.board.MoveResult;

public class RamMove implements Move {

    Coordinate target;

    public RamMove(Coordinate target) {
        this.target = target;
    }

    @Override
    public Board playOn(Board board, int playerID) {
        return board.ram(playerID, target);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canRam(playerID, target);
    }

    @Override
    public String transcribe()
    {
        return "Rammen gegen Ziel auf %s".formatted(target.transcribe());
    }



    public Coordinate getTarget() {
        return target;
    }
}
