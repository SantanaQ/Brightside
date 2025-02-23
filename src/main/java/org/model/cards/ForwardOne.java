package org.model.cards;


import org.model.board.Coordinate;
import org.model.board.Direction;
import org.model.board.MoveResolver;
import org.model.moves.ForwardMove;

import java.util.ArrayList;
import java.util.List;

public class ForwardOne implements Card{

    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        Direction[] dirs = new Direction[]{Direction.FORWARD};
        ForwardMove move = new ForwardMove(1);
        if(board.canMakeMove(move ,player).isValid())
            possibleTargets.add(new Coordinate(playerPosition.lane(), playerPosition.progress() + 1));
        return possibleTargets;
    }
}

