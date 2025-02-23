package org.model.board;

import org.model.entities.Entity;
import org.model.entities.Ship;
import org.model.moves.Move;
import org.util.T2;

import java.util.HashMap;
import java.util.List;

public interface MoveResolver
{
    int maxLane();
    int maxProgress();
    HashMap<Coordinate, Entity> boardLayout();
    T2<Coordinate, Ship> findShip(int playerID);

    MoveResolver makeMove(Move move, int playerID);
    MoveResult canMakeMove(Move move, int playerID);
    MoveResolver placeEntity(Coordinate position, Entity placedEntity);
    MoveResolver driftMines();

    List<Integer> winners();
}
