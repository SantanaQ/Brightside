package org.model.moves;

import org.model.board.Board;
import org.model.board.Coordinate;
import org.model.board.Direction;
import org.model.board.MoveResult;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class LayMinesMove  implements Move {

    Coordinate[] targets;

    public LayMinesMove(Coordinate[] targets) {
        this.targets = targets;
    }


    @Override
    public Board playOn(Board board, int playerID)
    {
        return board.layMines(playerID, targets);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canLayMines(playerID, targets);
    }

    @Override
    public String transcribe()
    {
        return Arrays.stream(targets)
                .map(Coordinate::transcribe)
                .collect(Collectors.joining(",","Minen legen auf [","]"));
    }



    public Coordinate[] getTargets() {
        return targets;
    }
}
