package org.model.board;

import org.model.entities.Entity;
import org.model.entities.Ship;
import org.model.moves.Move;
import org.util.T2;

import java.util.HashMap;
import java.util.List;

public class TranscribingBoard implements MoveResolver
{

	MoveResolver delegate;
	LiveTranscript transcript;

	public TranscribingBoard(MoveResolver toDecorate)
	{
		this.delegate = toDecorate;
		this.transcript = new LiveTranscript();
	}

	public LiveTranscript getTranscript()
	{
		return this.transcript;
	}

	@Override
	public MoveResolver makeMove(Move move, int playerID)
	{
		if(delegate.canMakeMove(move, playerID).isValid())
		{
			transcript.appendLine("Player %d: %s".formatted(playerID, move.transcribe()));
			delegate.makeMove(move, playerID);
		}

		return this;
	}

	@Override
	public MoveResult canMakeMove(Move move, int playerID)
	{
		return delegate.canMakeMove(move, playerID);
	}

	@Override
	public MoveResolver placeEntity(Coordinate position, Entity placedEntity)
	{
		delegate.placeEntity(position, placedEntity);
		return this;
	}

	@Override
	public List<Integer> winners()
	{
		return delegate.winners();
	}

	@Override
	public T2<Coordinate, Ship> findShip(int playerID)
	{
		return delegate.findShip(playerID);
	}

	@Override
	public MoveResolver driftMines()
	{
		delegate.driftMines();
		return this;
	}

	@Override
	public int maxLane()
	{
		return delegate.maxLane();
	}

	@Override
	public int maxProgress()
	{
		return delegate.maxProgress();
	}

	@Override
	public HashMap<Coordinate, Entity> boardLayout()
	{
		return delegate.boardLayout();
	}

}
