package org.model.moves;

import org.model.board.Board;
import org.model.board.Coordinate;
import org.model.board.MoveResult;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class HookMove  implements Move {

    Coordinate target;

    public HookMove(Coordinate target) {
        this.target = target;
    }

    @Override
    public Board playOn(Board board, int playerID) {
        return board.hook(playerID, target);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canHook(playerID, target);
    }

    @Override
    public String transcribe()
    {
        return "Haken werfen auf %s".formatted(target.transcribe());
    }



    public Coordinate getTarget() {
        return target;
    }

}
