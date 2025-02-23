package org.model.match;

import java.util.ArrayList;
import java.util.List;

import org.controller.MatchController;
import org.model.board.*;
import org.model.cards.*;

/**
 * <p>Setup wird verwendet, um ein neues {@link Match} Objekt zu erstellen und es in den {@link MatchController} einzubetten.</p>
 *
 * <p>Dafür verwendet diese Klasse das <strong>Builder Entwurfsmuster</strong>.
 * Das ermöglicht zum einen, dass bestimmte Felder des Matches individuell gesetzt werden können und andere auf ihrem Default Wert
 * verbleiben können. Zum Anderen wird durch das Builder Pattern verhindert, dass mit dem Konstruktor des Matches,
 * der viele Parameter enthält, direkt interagiert werden muss.</p>
 *
 * <p>Durch die "with"-Methoden kann der Builder verkettet aufgebaut werden und so gezielte Änderungen an der Standardspezifikation eines
 * Matches vorgenommen werden.</p>
 */
public class Setup
{

    public static final int minimumBoardLane = 5;
    public static final int minimumBoardProgress = 8;

    public Player currentPlayer;
    public Board board = getDefaultBoard();
    public List<Player> players = getDefaultPlayers();
    public List<Card> cardSelection = getDefaultDeck();
    public RockSpawner rockSpawner = new RandomRockSpawner();
    public boolean transcribe;

    /**
     * Setzen von einem Kartensatz.
     * @param cards Liste an Karten, die verwendet werden sollen.
     * @return Setup Build Komponente
     */
    public Setup withCards(List<Card> cards)
    {
        this.cardSelection = cards;
        for (Player player : players)
        {
            Loadout loadout = new Loadout(cards);
            player.setLoadout(loadout);
        }
        return this;
    }

    /**
     * Setzen von dem startenden Spieler
     * @param player Spieler, der als Erstes am Zug ist.
     * @return Setup Build Komponente
     */
    public Setup withStartingPlayer(Player player)
    {
        this.currentPlayer = player;
        return this;
    }

    /**
     * Board mit gewünschter Größe erzeugen.
     * @param maxLane Lane Dimension
     * @param maxProgress Progress Dimension
     * @return Setup Build Komponente
     */
    public Setup withBoard(int maxLane, int maxProgress)
    {
        if(maxLane < minimumBoardLane)
            maxLane = minimumBoardLane;
        if(maxProgress < minimumBoardProgress)
            maxProgress = minimumBoardProgress;

        this.board = new Board(maxLane, maxProgress);
        return this;
    }

    /**
     * Auswahl eine Rock Spawning Strategie
     * @param rockSpawner Strategie fürs Spawnen von Rocks
     * @return Setup Build Komponente
     */
    public Setup withRockSpawner(RockSpawner rockSpawner)
    {
        this.rockSpawner = rockSpawner;
        return this;
    }

    /**
     * Gewünschte Anzahl Mitspieler
     * @param playerCount Anzahl an mitspielenden Spieler
     * @return Setup Build Komponente
     */
    public Setup withPlayers(int playerCount)
    {
        if(playerCount <= 0) {
            playerCount = board.maxLane(); // Illegal Argument = Default Value
        }
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++)
        {
            Loadout loadout = new Loadout(cardSelection);
            Player p = new Player(i, loadout);
            players.add(p);
        }
        this.players = players;
        this.currentPlayer = players.getFirst();
        return this;
    }

    /**
     * Setzt Transkriptionsflag, sodass Spielzüge mit aufgezeichnet werden sollen.
     * @return Setup Build Komponente
     */
    public Setup withTranscription()
    {
        this.transcribe = true;
        return this;
    }

    /**
     * Erstellung eines MatchControllers, der das neu erstellte Match enthält, finaler Aufruf der Build-Verkettung.
     * @return MatchController, Bindeglied zwischen GUI und Match
     */
    public MatchController build() {
        if(currentPlayer == null)
        {
            currentPlayer = players.getFirst();
        }
        if(players.isEmpty() || players.size() > board.maxLane())
        {
            int playerCount = board.maxLane(); // Illegal Argument = Default Value
            withPlayers(playerCount);
        }
        int[] ids = getPlayerIds();
        board = new Board(board.maxLane(), board.maxProgress(), rockSpawner);
        board = board.prepared(ids);
        Match match = new Match(cardSelection, players, board, currentPlayer, transcribe);
        return new MatchController(match);
    }

    private int[] getPlayerIds()
    {
        int[] ids = new int[players.size()];
        for (int i = 0; i < players.size(); i++)
        {
            ids[i] = players.get(i).getPlayerID();
        }
        return ids;
    }

    private List<Card> getDefaultDeck()
    {
        List<Card> cards = new ArrayList<>();
        cards.add(new BroadsideLeft());
        cards.add(new BroadsideRight());
        cards.add(new ForwardOne());
        cards.add(new ForwardTwo());
        cards.add(new ForwardTwo());
        cards.add(new Steer());
        cards.add(new Steer());
        cards.add(new Steer());
        cards.add(new Hook());
        cards.add(new Ram());
        cards.add(new LayMines());
        cards.add(new Maneuver());
        return cards;
    }

    private List<Player> getDefaultPlayers()
    {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, new Loadout(getDefaultDeck())));
        players.add(new Player(2, new Loadout(getDefaultDeck())));
        players.add(new Player(3, new Loadout(getDefaultDeck())));
        players.add(new Player(4, new Loadout(getDefaultDeck())));
        return players;
    }

    private Board getDefaultBoard()
    {
        return new Board(minimumBoardLane,minimumBoardProgress);
    }

}
