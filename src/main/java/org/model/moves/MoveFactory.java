package org.model.moves;

import org.model.board.Coordinate;
import org.model.board.Direction;
import org.model.cards.*;

/**
 * <p>MoveFactory erstellt auf Grundlage von ausgespielten Karten einen dazugehörigen Spielzug ({@link Move}).</p>
 *
 * <p>Dazu verwendet die Klasse das <strong>Factory Entwurfsmuster</strong>. Dabei wird eine Karte an die Factory übergeben,
 * wodurch die Factory einen konkreten Move produziert.</p>
 *
 * <p>In dieser Variante werden Felder, die vom Spieler als Bewegungs-, oder Angriffsziel ausgewählt wurden an den passenden Move
 * übergeben oder eine Richtung mitgegeben, die von der Spielfigur angesteuert werden soll.</p>
 */
public class MoveFactory
{

    /**
     * Produziert die Spielzüge.
     * @param card Ausgespielte Karte.
     * @param targets Felder, auf die mit den Moves Aktionen ausgeführt werden sollen.
     * @return Zu einer Karte zugeöriger Spielzug.
     */
    public Move produceMove(Card card, Coordinate[] targets)
    {
        if (card == null)
        {
            throw new NullPointerException("card is null");
        }

        switch(card)
        {
            case Hook h ->
            {
                return new HookMove(targets[0]);
            }
            case ForwardOne fo ->
            {
                return  new ForwardMove(1);
            }
            case ForwardTwo ft ->
            {
                return new ForwardMove(2);
            }
            case Ram r ->
            {
                return new RamMove(targets[0]);
            }
            case Maneuver m ->
            {
                return new ManeuverMove(targets[0]);
            }
            case Steer s ->
            {
                return new SteerMove(targets[0]);
            }
            case BroadsideRight br ->
            {
                return new BroadsideMove(Direction.RIGHT);
            }
            case BroadsideLeft bl ->
            {
                return new BroadsideMove(Direction.LEFT);
            }
            case LayMines l ->
            {
                return new LayMinesMove(targets);
            }
            default -> throw new RuntimeException("Unknown Card.");
        }
    }

}
