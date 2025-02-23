package org.model.cards;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;

import java.util.List;

public interface Card {

    List<Coordinate> possibleTargets(MoveResolver board, Coordinate playerPosition, int player);

}
