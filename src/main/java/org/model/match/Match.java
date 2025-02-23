package org.model.match;
import com.google.gson.JsonParser;
import org.memento.IMemento;
import org.memento.MatchMemento;
import org.memento.MementoOriginator;
import org.model.board.*;
import org.model.moves.Move;
import org.model.cards.*;
import org.saveload.AutoSaving;
import org.saveload.SlotSave;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Match verwaltet die {@link Player} und das {@link Board}. In dem Zusammenhang ist Match dafür verantwortlich,
 * anzusagen, wann welcher Spieler dran ist und hält Schritt über die Runden-, und Zuganzahl.</p>
 *
 * <p>Die Klasse ist Teil des <strong>Memento Entwurfsmusters</strong>: Agiert als Urheber und ist somit dafür verantwortlich
 * neue {@link MatchMemento} Objekte zu erstellen und seinen Zustand an diese zu übergeben sowie den Zustand aus diesen
 * Objekten zu überschreiben. Dafür implementiert Match das {@link MementoOriginator} Interface.</p>
 */
public class Match implements MementoOriginator
{
	private int currentPlayer;
	private MoveResolver board;
	private List<Integer> activePlayers;
	private List<Player> players;
	private List<Card> cardSelection;
	private int numberTurns = 0;
	private int numberRounds = 1;
	private final AutoSaving autoSaving;
	private boolean running;
	private LiveTranscript liveTranscript;

    public Match(List<Card> cardSelection, List<Player> players,
				 MoveResolver board, Player startingPlayer, boolean transcribe)
	{
		this.cardSelection = cardSelection;
		this.players = players;
		this.activePlayers = new ArrayList<>();
		for(Player p : players) {
			activePlayers.add(p.getPlayerID());
		}
		// decorate if transcriptionflag is set
		if(transcribe) {
			TranscribingBoard transcribingBoard = new TranscribingBoard(board);
			this.liveTranscript = transcribingBoard.getTranscript();
			this.board = transcribingBoard;
		} else
			this.board = board;
		this.currentPlayer = startingPlayer.getPlayerID();
		this.running = true;
		this.autoSaving = new AutoSaving();
		autoSaving.autoSave(this);

	}

	public MoveResolver executeMove(Move move)
	{
		if(isRunning())
		{
			board = board.makeMove(move, currentPlayer);
			if(board.winners().contains(currentPlayer)){
				endTurn();
			}
			removeWinners();
		}
		return board;
	}

	public void endTurn()
	{
		if(isRunning())
		{
			findPlayerInActives(currentPlayer).getLoadout().drawCards(2);
			if(removeWinnerCurrentPlayer())
			{
				setCurrentPlayer(activePlayers.get((numberTurns) % activePlayers.size()));
			} else
			{
				setCurrentPlayer(activePlayers.get((numberTurns + 1) % activePlayers.size()));
				numberTurns++;
			}
			if(roundFinished())
			{
				endRound();
			}
			if(activePlayers.size() <= 1)
			{
				setRunning(false);
			} else
				autoSaving.autoSave(this);
		}
	}

	public Player findPlayerInActives(int id) {
		for(Player player : players)
		{
			if(player.getPlayerID() == id)
			{
				return player;
			}
		}
		return null;
	}

	private void endRound()
	{
		numberTurns = 0;
		board = board.driftMines();
		numberRounds++;
	}

	private void removeWinners()
	{
		for(int player : board.winners())
		{
			if(activePlayers.contains(player))
			{
				activePlayers.remove(Integer.valueOf(player));
			}
		}
		if(activePlayers.size() == 1) {
			setRunning(false);
		}
	}

	private boolean removeWinnerCurrentPlayer()
	{
		if (board.winners().contains(currentPlayer))
		{
			activePlayers.remove(numberTurns);
			return true;
		}
		return false;
	}

	public Coordinate getCurrentPlayerPosition()
	{
		return board.findShip(currentPlayer).e1();
	}

	public int getCurrentPlayer()
	{
		return currentPlayer;
	}

	public MoveResolver getBoard()
	{
		return board;
	}

	public String getTranscript()
	{
		if(liveTranscript == null)
		{
			throw new IllegalStateException("Transcript has not been initialized");
		}
		return this.liveTranscript.toString();
	}


	public int getNumberTurns()
	{
		return numberTurns;
	}

	public int getNumberRounds()
	{
		return numberRounds;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public List<Integer> getActivePlayers()
	{
		return activePlayers;
	}

	public void setCurrentPlayer(int currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}

	public void setBoard(MoveResolver board)
	{
		this.board = board;
	}

	public void setPlayers(List<Player> activePlayers)
	{
		this.players = activePlayers;
	}

	public void setActivePlayers(List<Integer> activePlayers)
	{
		this.activePlayers = activePlayers;
	}

	public List<Card> getCardSelection()
	{
		return cardSelection;
	}

	public void setCardSelection(List<Card> cardSelection)
	{
		this.cardSelection = cardSelection;
	}

	public void setNumberTurns(int numberTurns)
	{
		this.numberTurns = numberTurns;
	}

	public void setNumberRounds(int numberRounds)
	{
		this.numberRounds = numberRounds;
	}

	private boolean roundFinished()
	{
		return numberTurns == activePlayers.size();
	}

	private void setRunning(boolean running)
	{
		this.running = running;
	}

	public boolean isRunning()
	{
		return running;
	}

	public AutoSaving getAutoSaving()
	{
		return autoSaving;
	}

	/**
	 * Erstellt ein neues Memento Objekt und speichert seinen aktuellen Match Zustand in diesem.
	 * @return {@link MatchMemento} Objekt.
	 */
	@Override
	public IMemento stateInMemento()
	{
		SlotSave slotSave = new SlotSave();
		String matchJson = slotSave.serialize(this);
		MatchMemento memento = new MatchMemento();
		memento.setState(matchJson);
		return memento;
	}

	/**
	 * Stellt einen früheren Zustand aus einem Memento Objekt wieder her.
	 * @param memento Früherer Match Zustand.
	 */
	@Override
	public void stateFromMemento(IMemento memento)
	{
		MatchMemento matchMemento = (MatchMemento) memento;
		SlotSave slotSave = new SlotSave();
		Match matchState = slotSave.deserialize(JsonParser.parseString(matchMemento.getState()));
		this.setCurrentPlayer(matchState.getCurrentPlayer());
		this.setBoard(matchState.getBoard());
		this.setPlayers(new ArrayList<>(matchState.getPlayers()));
		this.setActivePlayers(new ArrayList<>(matchState.getActivePlayers()));
		this.setCardSelection(new ArrayList<>(matchState.getCardSelection()));
		this.setNumberTurns(matchState.getNumberTurns());
		this.setNumberRounds(matchState.getNumberRounds());
		this.setRunning(matchState.isRunning());
	}



}

