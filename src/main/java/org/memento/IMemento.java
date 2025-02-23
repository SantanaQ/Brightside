package org.memento;

/**
 * <p>Teil des <strong>Memento Entwurfsmusters</strong>.</p>
 * <p>Dieses Interace wird von Memento Objekten wie {@link MatchMemento} implementiert. Dafür erhält es
 * absichtlich keine Methoden, damit aufbewahrende Klassen keine Modifikationen auf den Mementos durchführen können,
 * und somit der gespeicherte Zustand garantiert gleich bleibt.
 * Aus diesem Grund halten Aufbewahrer nur Referenzen auf dieses Interface und keine konkreten Implementierungsklassen.</p>
 */
public interface IMemento {
}
