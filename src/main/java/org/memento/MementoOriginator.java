package org.memento;

/**
 * <p>Teil des <strong>Memento Entwurfsmusters</strong>: Originator stellt Methoden bereit, die der Urheber nutzt,
 * um seinen Zustand in ein Memento Objekt zu überführen oder einen früheren Zustand aus einem Memento Objekt zu überschreiben.
 * Siehe {@link org.model.match.Match}</p>
 */
public interface MementoOriginator {

    IMemento stateInMemento();
    void stateFromMemento(IMemento memento);

}
