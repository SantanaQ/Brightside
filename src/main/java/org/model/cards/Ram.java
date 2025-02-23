package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;
import org.model.moves.RamMove;

import java.util.ArrayList;
import java.util.List;

public class Ram  implements Card{

    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        possibleTargets.add(new Coordinate(playerPosition.lane(), playerPosition.progress() + 1));//Up
        possibleTargets.add(new Coordinate(playerPosition.lane() + 1, playerPosition.progress() + 1));//RightUp
        possibleTargets.add(new Coordinate(playerPosition.lane() - 1, playerPosition.progress() + 1));//LeftUp
        possibleTargets.add(new Coordinate(playerPosition.lane() - 1, playerPosition.progress()));//Left
        possibleTargets.add(new Coordinate(playerPosition.lane() + 1, playerPosition.progress()));//Right
        possibleTargets.add(new Coordinate(playerPosition.lane() + 1, playerPosition.progress() - 1));//RightDown
        possibleTargets.add(new Coordinate(playerPosition.lane() - 1, playerPosition.progress() - 1));//LeftDown
        possibleTargets.add(new Coordinate(playerPosition.lane(), playerPosition.progress() - 1));//Down
        List<Coordinate> realPossibleTargets = new ArrayList<>();
        for (Coordinate c: possibleTargets)
        {
            RamMove move = new RamMove(c);
            if (board.canMakeMove(move, player).isValid())
            {
                realPossibleTargets.add(c);
            }
        }
        return realPossibleTargets;
    }
}
