package org.model.board;

import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Diese Klasse ist Teil des <strong>Strategy Patterns</strong>: Sie repräsentiert eine konkrete Strategie,
 * ein Feld zum Respawnen eines Felsens auszuwählen.
 * Das gewählte Feld muss vor oder auf der Höhe des hintersten Spielers liegen.
 * Bevorzugt werden allerdings Felder, die vor oder auf der Höhe des vordersten Spielers liegen.
 */
public class BalancedRockSpawner implements RockSpawner
{
	public transient RandomGenerator random;

	//GSON uses the default constructor, so the transient random generator has to be instantiated here.
	public BalancedRockSpawner()
	{
		this.random = new Random();
	}

	/**
	 * <p>Legt einen neuen Stein auf einem zufälligen Feld eines Spielbrettes ab.
	 * Das gewählte Feld muss vor oder auf der Höhe des hintersten Spielers liegen.
	 * Bevorzugt werden allerdings Felder, die vor oder auf der Höhe des vordersten Spielers liegen.</p>
	 *
	 * <p>Diese Methode ist Teil des <strong>Strategy Patterns</strong>: Unterschiedliche Klassen des Interfaces
	 * {@link RockSpawner} wählen die neue Position des Felsens unterschiedlich aus.</p>
	 *
	 * @param board Das Spielbrett, auf dem ein Felsen abzulegen ist
	 * @return Das veränderte Spielbrett
	 */
	@Override
	public Board spawnRock(Board board)
	{
		var shouldMessWithFirstPlayer = random.nextBoolean();

		int minProgressToSpawnOn = shouldMessWithFirstPlayer
				? highestProgress(board)
				: lowestProgress(board);

		return spawnRockAhead(board, minProgressToSpawnOn, random);
	}
}
