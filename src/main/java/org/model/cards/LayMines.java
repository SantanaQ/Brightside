package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;
import org.model.moves.LayMinesMove;

import java.util.ArrayList;
import java.util.List;

public class LayMines  implements Card{

    @Override
    public List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player)
    {
        List<Coordinate> possibleTargets = new ArrayList<>();
        for(int i = 1; i < board.maxLane(); i++)
        {
            Coordinate possibleOne = new Coordinate(i, playerPosition.progress() + 4);
            for(int j = 2; j <= board.maxLane(); j++)
            {
                Coordinate possibleTwo = new Coordinate(j, playerPosition.progress() + 4);
                LayMinesMove move = new LayMinesMove(new Coordinate[] {possibleOne, possibleTwo});
                if(board.canMakeMove(move, player).isValid())
                {
                    possibleTargets.add(possibleOne);
                    possibleTargets.add(possibleTwo);
                }
            }
        }
        return possibleTargets;
    }
}
