package org.model.moves;

import org.model.board.Board;
import org.model.board.Coordinate;
import org.model.board.MoveResult;

public class ManeuverMove  implements Move
{

    Coordinate target;

    public ManeuverMove(Coordinate target) {
        this.target = target;
    }

    @Override
    public Board playOn(Board board, int playerID) {
        return board.maneuverShip(playerID, target);
    }

    @Override
    public MoveResult canPlayOn(Board board, int playerID)
    {
        return board.canManeuverShip(playerID, target);
    }

    @Override
    public String transcribe()
    {
        return "Manövrieren nach %s".formatted(target.transcribe());
    }



    public Coordinate getTarget() {
        return target;
    }
}
