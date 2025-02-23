package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;
import org.model.moves.SteerMove;

import java.util.ArrayList;
import java.util.List;

public class Steer  implements Card {

    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        Coordinate right = new Coordinate(playerPosition.lane() + 1, playerPosition.progress() + 1);
        SteerMove moveRight = new SteerMove(right);
        Coordinate left = new Coordinate(playerPosition.lane() - 1, playerPosition.progress() + 1);
        SteerMove moveLeft = new SteerMove(left);
        if(board.canMakeMove(moveRight, player).isValid())
        {
            possibleTargets.add(right);
        }
        if(board.canMakeMove(moveLeft, player).isValid())
        {
            possibleTargets.add(left);
        }
        return possibleTargets;
    }
}
