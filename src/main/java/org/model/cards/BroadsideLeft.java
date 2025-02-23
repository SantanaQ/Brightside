package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.Direction;
import org.model.board.MoveResolver;
import org.model.moves.BroadsideMove;

import java.util.ArrayList;
import java.util.List;

public class BroadsideLeft implements Card{


    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        Direction dir = Direction.LEFT;
        BroadsideMove move = new BroadsideMove(dir);

        if(board.canMakeMove(move, player).isValid())
            possibleTargets.add(new Coordinate(playerPosition.lane() - 1, playerPosition.progress()));
        return possibleTargets;
    }


}
