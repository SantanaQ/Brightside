package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;
import org.model.moves.HookMove;

import java.util.ArrayList;
import java.util.List;

public class Hook  implements Card{

    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        possibleTargets.add(new Coordinate(playerPosition.lane() - 1, playerPosition.progress() + 2));//Left1
        possibleTargets.add(new Coordinate(playerPosition.lane() - 1, playerPosition.progress() + 3));//Left2
        possibleTargets.add(new Coordinate(playerPosition.lane() + 1, playerPosition.progress() + 2));//Right1
        possibleTargets.add(new Coordinate(playerPosition.lane() + 1, playerPosition.progress() + 3));//Right2
        List<Coordinate> realPossibleTargets = new ArrayList<>();
        for (Coordinate c: possibleTargets)
        {
            HookMove move = new HookMove(c);
            if (board.canMakeMove(move, player).isValid())
            {
                realPossibleTargets.add(c);
            }
        }
        return realPossibleTargets;
    }
}
