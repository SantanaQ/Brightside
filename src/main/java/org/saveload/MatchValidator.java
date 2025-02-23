package org.saveload;

import org.model.board.Coordinate;
import org.model.board.MoveResolver;
import org.model.cards.Card;
import org.model.entities.Entity;
import org.model.match.Match;
import org.model.match.Player;
import org.model.match.Setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchValidator {

    public boolean validate(Match match) {
        if(!currentPlayerIsValid(match))
        {
            System.out.println("Current Player is not valid");
            return false;
        }
        if(!boardIsValid(match))
        {
            System.out.println("Board is not valid");
            return false;
        }
        if(!activePlayersAreValid(match))
        {
            System.out.println("Active players are not valid");
            return false;
        }
        if(!playersAreValid(match))
        {
            System.out.println("ALL Players are not valid");
            return false;
        }
        if(!cardsAreValid(match))
        {
            System.out.println("Cards are not valid");
            return false;
        }
        if(!numberTurnsIsValid(match))
        {
            System.out.println("Number Turns are not valid");
            return false;
        }
        if(!numberRoundsIsValid(match))
        {
            System.out.println("Number Rounds are not valid");
            return false;
        }
        if(!autoSavingIsValid(match))
        {
            System.out.println("Auto saving is not valid");
            return false;
        }
        if(!matchIsRunning(match))
        {
            System.out.println("Match isn't running");
            return false;
        }
        return true;
    }


    private boolean currentPlayerIsValid(Match match) {
        int currentPlayer = match.getCurrentPlayer();
        if(!match.getActivePlayers().contains(currentPlayer))
        {
            return false;
        }
        for(Player player : match.getPlayers())
        {
            if(player.getPlayerID() == currentPlayer)
            {
                return true;
            }
        }
        return false;
    }

    private boolean boardIsValid(Match match) {
        MoveResolver board = match.getBoard();
        if(board == null)
        {
            System.out.println("Board is null");
            return false;
        }
        if(board.maxLane() < Setup.minimumBoardLane || board.maxProgress() < Setup.minimumBoardProgress)
        {
            System.out.println("Board is too small");
            return false;
        }
        HashMap<Coordinate, Entity> boardLayout = match.getBoard().boardLayout();
        for (Coordinate coordinate : boardLayout.keySet()) {
            if(coordinate == null
                    || coordinate.lane() > board.maxLane()
                    || coordinate.lane() < 1
                    || coordinate.progress() > board.maxProgress()
                    || coordinate.progress() < 1)
            {
                System.out.println("Invalid coordinate" + coordinate);
                return false;
            }
            if (boardLayout.get(coordinate) == null) {
                System.out.println("Invalid entity");
                return false;
            }
            if(boardLayout.get(coordinate).isShip())
            {
                if(coordinate.progress() > board.maxProgress()-1)
                {
                    return false;
                }
            }

        }

        for (int player : board.winners()) {
            try
            {
                System.out.println("Player " + player + " has won");
                board.findShip(player);
                return false;

            }catch(RuntimeException _)
            {
            }
        }
/*
        if(board.rockSpawner() == null)
        {
            System.out.println("Rock Spawner");
            return false;
        }*/
        return true;
    }

    private boolean activePlayersAreValid(Match match)
    {
        MoveResolver board = match.getBoard();

        for (int player : match.getActivePlayers()) {
            try
            {
                board.findShip(player);

            }catch(RuntimeException e)
            {
                return false;
            }
        }
        return true;
    }


    private boolean playersAreValid(Match match)
    {
        List<Player> players = match.getPlayers();
        List<Integer> winners = match.getBoard().winners();
        List<Integer> activePlayers = match.getActivePlayers();
        for(Player player : players)
        {
            if((!activePlayers.contains(player.getPlayerID()) && !winners.contains(player.getPlayerID()))
                    || (activePlayers.contains(player.getPlayerID()) && winners.contains(player.getPlayerID())))
            {
                return false;
            }
        }
        return true;
    }

    private boolean cardsAreValid(Match match)
    {

        for (Player player : match.getPlayers()) {
            List<String> cards = new ArrayList<>();
            for (Card c : match.getCardSelection())
            {
                cards.add(c.getClass().getSimpleName());
            }
            List<Card> loadout = new ArrayList<>();
            loadout.addAll(player.getLoadout().getHand());
            loadout.addAll(player.getLoadout().getDrawPile());
            loadout.addAll(player.getLoadout().getDiscardPile());

            if(cards.size() != loadout.size())
            {
                return false;
            }
            loadout.forEach(card -> cards.remove(card.getClass().getSimpleName()));
            if(!cards.isEmpty())
            {
                return false;
            }

        }
        return true;
    }

    private boolean numberTurnsIsValid(Match match)
    {
        int numberTurns = match.getNumberTurns();
        int currentPlayerIndex = match.getActivePlayers().indexOf(match.getCurrentPlayer());
        return numberTurns >= 0
                && numberTurns < match.getActivePlayers().size()
                && numberTurns == currentPlayerIndex;
    }

    private boolean numberRoundsIsValid(Match match)
    {
        int numberRounds = match.getNumberRounds();
        return numberRounds > 0;
    }

    private boolean autoSavingIsValid(Match match)
    {
        return match.getAutoSaving() != null;
    }

    private boolean matchIsRunning(Match match)
    {
        return match.isRunning();
    }

}
