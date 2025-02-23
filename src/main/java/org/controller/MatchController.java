package org.controller;

import org.memento.IMemento;
import org.memento.MementoOriginator;
import org.model.board.Coordinate;
import org.model.entities.Entity;
import org.model.match.Match;
import org.model.match.Player;
import org.model.moves.MoveFactory;
import org.model.cards.*;
import org.saveload.Persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>MatchController nimmt Player Inputs entgegen, steuert das {@link Match} und liefert interne Informationen an die grafische Oberfläche.</p>
 *
 * <p>Diese Klasse ist Teil des <strong>Memento Entwurfsmusters</strong>: Agiert als Aufbewahrer des {@link IMemento} Interfaces. Der Aufbewahrer
 * hat die Aufgabe das aktuelle Memento Objekt zu halten. Dabei ist wichtig dass hier lediglich das Interface gehalten wird. Somit ist ausgeschlossen, dass
 * diese Klasse den internen Speicherstand des Mementos manipulieren kann. Zudem bedient sich der Aufbewahrer an die Methoden des {@link MementoOriginator},
 * die hier durch das {@link Match} implementiert sind, um das Sichern und Wiederherstellen des internen Match Zustandes zu steuern.</p>
 */
public class MatchController {

    private Match match;
    Card lockedCard = null;
    Coordinate[] targets = null;
    int targetIndex = 0;
    List<Coordinate> possibleTargets = new ArrayList<>();
    List<Coordinate> hoverTargets = new ArrayList<>();
    int requiredFields;
    IMemento memento;


    public MatchController(Match match)
    {
        this.match = match;
        memento = match.stateInMemento();
    }


    public void executeMove()
    {
        Card locked = lockedCard;
        Coordinate[] t = targets;
        resetGUIParameters();
        MoveFactory moveFactory = new MoveFactory();
        match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().playCard(locked);
        int numberPlayersBefore = match.getActivePlayers().size();
        match.executeMove(moveFactory.produceMove(locked, t));
        if(wasWinningMove(numberPlayersBefore))
        {
            memento = match.stateInMemento();
        }
    }

    private boolean wasWinningMove(int numberPlayersBefore)
    {
        return match.getActivePlayers().size() != numberPlayersBefore;
    }

    public void chooseCard(Card chosenCard)
    {
        if(match.isRunning())
        {
            resetGUIParameters();
            this.lockedCard = chosenCard;
            possibleTargets = chosenCard.possibleTargets(
                    match.getBoard(),
                    match.getCurrentPlayerPosition(),
                    match.getCurrentPlayer()
            );

            if (possibleTargets.isEmpty())
            {
                requiredFields = -1; //Fehler: "Kein possible Target"
            } else
            {
                switch (chosenCard)
                {
                    case LayMines l -> setAmountOfTargets(2);
                    case Ram r -> setAmountOfTargets(1);
                    case Steer s -> setAmountOfTargets(1);
                    case Hook h-> setAmountOfTargets(1);
                    case Maneuver m -> setAmountOfTargets(1);
                    default ->
                    { // F, F2, BL, BR
                        targets = new Coordinate[0];
                        executeMove();
                        requiredFields = 0;
                    }
                }
            }
        }
    }

    private void setAmountOfTargets(int amount)
    {
        targets = new Coordinate[amount];
        requiredFields = targets.length;
    }

    public int setTarget(Coordinate target)
    {
        if(lockedCard == null)
        {
            return -1;
        }
        if (!possibleTargets.contains(target))
        {
            return possibleTargets.size();
        }
        for (int i = 0; i < targets.length && targets.length > 1; i++)
        {
            // Remove target, if changed mind (e.g taking mine back)
            if (targets[i] != null && targets[i].equals(target))
            {
                targets[i] = null;
                targets = removeEmptyElements();
                targetIndex--;
                return possibleTargets.size();
            }
        }
        this.targets[targetIndex] = target;
        if (targetIndex == targets.length - 1)
        {
            executeMove();
        } else
            targetIndex++;
        return possibleTargets.size();
    }

    private Coordinate[] removeEmptyElements()
    {
        Coordinate[] targetsCpy = new Coordinate[targets.length];
        int indexCpy = 0;
        for (Coordinate target : targets)
        {
            if (target != null)
            {
                targetsCpy[indexCpy++] = target;
            }
        }
        return targetsCpy;
    }

    public Card getLockedCard() {
        return lockedCard;
    }

    public Coordinate[] getTargets() {
        return targets;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    private void resetGUIParameters()
    {
        lockedCard = null;
        targets = null;
        targetIndex = 0;
        requiredFields = -1;
        possibleTargets.clear();
        hoverTargets.clear();
    }

    public boolean newRoundStarted() {
        return match.getNumberTurns() == 0;
    }

    public int rounds() {
        return match.getNumberRounds();
    }

    public void endTurn()
    {
        match.endTurn();
        resetGUIParameters();
        memento = match.stateInMemento();
    }

    public void resetTurn()
    {
        match.stateFromMemento(memento);
        resetGUIParameters();
    }

    public boolean saveMatch(Persistence repo, String matchIdentification)
    {
        return repo.save(matchIdentification, match);
    }


    public boolean loadMatch(Persistence repo, String matchIdentification)
    {
        try
        {
            match = repo.load(matchIdentification);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public HashMap<Coordinate, Entity> getBoard()
    {
        return match.getBoard().boardLayout();
    }

    public Player getCurrentPlayer()
    {
        return match.findPlayerInActives(match.getCurrentPlayer());
    }

    public List<Card> getCurrentHand(){ return match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getHand(); }

    public Match getMatch() {
        return match;
    }

    public List<Coordinate> getPossibleTargets() {
        return possibleTargets;
    }

    public int getRequiredFields() {
        return requiredFields;
    }

    public boolean isGameOver() {
        return !match.isRunning();
    }

    public List<Integer> getWinners() {
        return match.getBoard().winners();
    }

    public void setHoverTargets(Card card)
    {

        if(lockedCard != null && card != null && !lockedCard.equals(card))
        {
            resetGUIParameters();
        }

        if(card != null)
        {
            this.hoverTargets = card.possibleTargets(match.getBoard(), match.getCurrentPlayerPosition(), match.getCurrentPlayer());
        } else
        {
            this.hoverTargets.clear();
        }
    }

    public List<Coordinate> getHoverTargets()
    {
        return hoverTargets;
    }




}

