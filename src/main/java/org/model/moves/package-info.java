/**
 * Dieses Package enthält alle Klassen, die aus einer ausgespielten Karte ein Spielzug konstruieren, der wiederum als
 * <strong>Command</strong>  an das {@link org.model.board.Board} ausgeliefert wird.<br>
 *
 * Die {@link org.model.moves.Move} Objekte sind daher für das <strong>Command Design Pattern</strong>
 * unerlässlich.
 *
 * <p>Weiterhin von von besonderer Bedeutung in Bezug auf Design Patterns: {@link org.model.moves.MoveFactory} (Factory Design Pattern).</p>
 */
package org.model.moves;