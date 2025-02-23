package org.model.moves;

import org.model.board.Board;
import org.model.board.Direction;
import org.model.board.MoveResult;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForwardMove  implements Move {

    Direction[] directions;

    public ForwardMove(int howMany)
    {
        directions = new Direction[howMany];
        Arrays.fill(directions, Direction.FORWARD);
    }

    @Override
    public Board playOn(Board board, int playerID)
    {
        return board.moveShip(playerID, directions);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canMoveShip(playerID, directions);
    }

    @Override
    public String transcribe()
    {
        return Arrays.stream(directions)
                .map(Direction::transcribe)
                .collect(Collectors.joining(",","Bewegen nach [","]"));
    }

    public Direction[] getDirections() {
        return directions;
    }
}
