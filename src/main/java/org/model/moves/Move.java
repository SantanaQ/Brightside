package org.model.moves;

import org.model.board.Board;
import org.model.board.MoveResolver;
import org.model.board.MoveResult;

/**
 *<p>Dieses Interface wird verwendet, um einen Spielzug aus einer gespielten Karte zu erstellen.</p>
 *
 * <p>Teil des <strong>Command Entwurfsmusters</strong>: Produkte sind Commands an das {@link Board}</p>
 */
public interface Move
{

    /**
     * Ruft eine passende Routine auf dem {@link Board} auf, die den übergebenen Command (Spielzug) bedient.
     * @param board Brett, auf dem der Spielzug stattfindet.
     * @param playerID ID des Spielers, der den Spielzug ausführt.
     * @return Aktualisiertes Board nach Durchführung des Spielzugs.
     */
    Board playOn(Board board, int playerID);

    String transcribe();

	MoveResult canPlayOn(Board board, int playerID);
}
