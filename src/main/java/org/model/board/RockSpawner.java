package org.model.board;

import org.model.entities.Rock;

import java.util.Random;
import java.util.random.RandomGenerator;


/**
 * Dieses Interface ist Teil des <strong>Strategy Patterns</strong>: Es repräsentiert die Strategie,
 * die ein Spielbrett verwendet, um einen Felsen an einer neuen Stelle zu respawnen.
 * Je nachdem, welche Klasse sich dahinter verbirgt, wird diese Entscheidung auf unterschiedliche Weise getroffen.
 */
public interface RockSpawner
{
	/**
	 * Diese Methode ist Teil des <strong>Strategy Patterns</strong>: Je nach implementierender Klasse
	 * werden Felsen auf unterschiedliche Weise verteilt.
	 *
	 * @param board Das Spielbrett, auf dem ein Felsen abzulegen ist
	 * @return Das veränderte Spielbrett
	 */
	Board spawnRock(Board board);

	/**
	 * <p>Dient dazu, zu Spielbeginn Felsen auf dem Spielbrett zu verteilen.</p>
	 *
	 * <p>Diese Methode ist Teil des <strong>Strategy Patterns</strong>: Der Aufruf wird vom Spielbrett delegiert.
	 * Allerdings wird hier davon ausgegangen, dass zu Spielbeginn ohnehin eine reine Zufallsverteilung
	 * gewünscht ist. Deshalb wird hier eine Standardimplementierung vorgegeben, die auch in keiner Klasse
	 * überschrieben wird.</p>
	 *
	 * @param board Das Spielbrett, auf dem Felsen zu verteilen sind.
	 * @return Das veränderte Spielbrett
	 */
	default Board prepareRocks(Board board)
	{
		var random = new Random();
		var numberOfRocks = (board.maxLane() * board.maxProgress()) / 18;
		for(int i=0; i<numberOfRocks; i++)
			{ board = spawnRockAhead(board, 1, random); }

		return board;
	}


	default Board spawnRockAhead(Board board, int minProgress, RandomGenerator random)
	{
		var maxLane = board.maxLane();
		var maxProgress = board.maxProgress();

		Coordinate targetCoordinate;

		do
		{
			var targetLane = random.nextInt(maxLane) + 1;
			var targetProgress = random.nextInt(maxProgress - minProgress) + minProgress + 1;

			targetCoordinate = new Coordinate(targetLane, targetProgress);
		}
		while(board.at(targetCoordinate).isPresent());

		return board.placeEntity(targetCoordinate, new Rock());
	}

	default int highestProgress(Board board)
	{
		return board.boardLayout().entrySet().stream()
				.filter((entry) -> entry.getValue().isShip())
				.map((entry) -> entry.getKey().progress())
				.max(Integer::compare)
				.orElse(1);
	}

	default int lowestProgress(Board board)
	{
		return board.boardLayout().entrySet().stream()
				.filter((entry) -> entry.getValue().isShip())
				.map((entry) -> entry.getKey().progress())
				.min(Integer::compare)
				.orElse(1);
	}
}
