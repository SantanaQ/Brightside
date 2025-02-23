package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;
import org.model.moves.ManeuverMove;

import java.util.ArrayList;
import java.util.List;

public class Maneuver  implements Card{

    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        possibleTargets.add(new Coordinate(playerPosition.lane(), playerPosition.progress() + 2));//Up
        possibleTargets.add(new Coordinate(playerPosition.lane() + 2, playerPosition.progress() + 2));//RightUp
        possibleTargets.add(new Coordinate(playerPosition.lane() - 2, playerPosition.progress() + 2));//LeftUp
        possibleTargets.add(new Coordinate(playerPosition.lane() - 2, playerPosition.progress()));//Left
        possibleTargets.add(new Coordinate(playerPosition.lane() + 2, playerPosition.progress()));//Right
        possibleTargets.add(new Coordinate(playerPosition.lane() + 2, playerPosition.progress() - 2));//RightDown
        possibleTargets.add(new Coordinate(playerPosition.lane() - 2, playerPosition.progress() - 2));//LeftDown
        possibleTargets.add(new Coordinate(playerPosition.lane(), playerPosition.progress() - 2));//Down
        List<Coordinate> realPossibleTargets = new ArrayList<>();
        for (Coordinate c: possibleTargets)
        {
            ManeuverMove move = new ManeuverMove(c);
            if (board.canMakeMove(move, player).isValid())
            {
                realPossibleTargets.add(c);
            }
        }
        return realPossibleTargets;
    }
}
