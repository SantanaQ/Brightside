package org.model.board;

import org.model.entities.Rock;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import static org.model.board.Direction.*;

/**
 * Diese Klasse ist Teil des <strong>Strategy Patterns</strong>: Sie repräsentiert eine konkrete Strategie, ein Feld zum Respawnen
 * eines Felsens auszuwählen. Das gewählte Feld muss diagonal oder direkt vor dem vordersten Spieler liegen
 * (oder einem von ihnen, wenn es mehrere sind). Sollte das nicht möglich sein, gilt: Das Feld muss
 * vor oder auf der Höhe des vordersten Spielers liegen.
 */
public class MeanRockSpawner implements RockSpawner
{
	public transient RandomGenerator random;

	//GSON uses the default constructor, so the transient random generator has to be instantiated here.
	public MeanRockSpawner()
	{
		this.random = new Random();
	}


	/**
	 * <p>Legt einen neuen Stein auf einem zufälligen Feld eines Spielbrettes ab.
	 * Das gewählte Feld muss diagonal oder direkt vor dem vordersten Spieler liegen
	 * (oder einem von ihnen, wenn es mehrere sind). Sollte das nicht möglich sein, gilt: Das Feld muss
	 * vor oder auf der Höhe des vordersten Spielers liegen.</p>
	 *
	 * <p>Diese Methode ist Teil des <strong>Strategy Patterns</strong>: Unterschiedliche Klassen des Interfaces
	 * {@link RockSpawner} wählen die neue Position des Felsens unterschiedlich aus.</p>
	 *
	 * @param board Das Spielbrett, auf dem ein Felsen abzulegen ist
	 * @return Das veränderte Spielbrett
	 */
	public Board spawnRock(Board board)
	{
		var positionsInFrontOfFurthestShips = positionsInFrontOfFurthestShips(board);

		if(positionsInFrontOfFurthestShips.isEmpty())
			{ return spawnRockAhead(board, highestProgress(board), random); }
		else
		{
			var randomIndex = random.nextInt(0, positionsInFrontOfFurthestShips.size());
			var randomPosition = positionsInFrontOfFurthestShips.get(randomIndex);
			return board.placeEntity(randomPosition, new Rock());
		}
	}

	List<Coordinate> positionsInFrontOfFurthestShips(Board board)
	{
		var boardLayout = board.boardLayout();

		var highestProgress
				= boardLayout.entrySet().stream()
				.filter((entry) -> entry.getValue().isShip())
				.map((entry) -> entry.getKey().progress())
				.max(Integer::compare)
				.orElse(1);

		return boardLayout.entrySet().stream()
				.filter((entry) -> entry.getValue().isShip())
				.map(Map.Entry::getKey)
				.filter((position) -> position.progress() == highestProgress)
				.flatMap((position) -> Stream.of(position.plus(FORWARD_LEFT), position.plus(FORWARD), position.plus(FORWARD_RIGHT)))
				.filter(board::positionIsInBounds)
				.filter((position) -> board.at(position).isEmpty())
				.distinct()
				.toList();
	}
}
