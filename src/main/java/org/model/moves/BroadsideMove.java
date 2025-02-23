package org.model.moves;

import org.model.board.Board;
import org.model.board.Direction;
import org.model.board.MoveResolver;
import org.model.board.MoveResult;

public class BroadsideMove implements Move {

    Direction direction;

    public BroadsideMove(Direction direction) {
        this.direction = direction;
    }

    @Override
    public  Board playOn(Board board, int playerID)
    {
        return board.shoot(playerID, direction);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canShoot(playerID, direction);
    }

    @Override
    public String transcribe()
    {
        return "Breitseite nach %s".formatted(direction.transcribe());
    }

    public Direction getDirection() {
        return direction;
    }
}
