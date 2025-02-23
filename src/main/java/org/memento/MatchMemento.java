package org.memento;

import org.model.match.Match;

/**
 * MatchMemento speichert einen früheren Match Zustand im JSON Format.
 *
 * <p>Diese Klasse ist Teil des <strong>Memento Entwurfsmusters</strong>: Memento Komponente, die vom Urheber erstellt wird
 * und einen internen Zustands des Urhebers speichert.</p>
 *
 * <p>In diesem Fall wird der {@link Match} Zustand einmalig am Anfang eines jeden Spielzugs gespeichert. Die Wiederherstellung dieses
 * Zustandes kann innerhalb des dazugehörigen Spielzugs beliebig zu jedem Zeitpunkt erfolgen und ermöglicht es dem Spieler seinen
 * Zug von vorne zu beginnen.</p>
 */
public class MatchMemento implements IMemento{

    private String stateJSON;

    public void setState(String stateJSON) {
        this.stateJSON = stateJSON;
    }

    public String getState() {
        return stateJSON;
    }


}
