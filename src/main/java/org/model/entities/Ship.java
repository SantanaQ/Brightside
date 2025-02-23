package org.model.entities;

import org.model.board.Collision;

/**
 *
 * Description
 *
 * @version 1.0 from 22.10.2024
 * @author
 */

public class Ship implements Entity
{

	final private int playerID;

	public Ship(int playerID)
	{
		this.playerID = playerID;
	}


	@Override
	public boolean isShip()
	{
		return true;
	}

	@Override
	public boolean isShip(int playerID) {
		return playerID == this.playerID;
	}

	public int playerID()
		{ return this.playerID; }

	@Override
	public boolean isMine()
	{
		return false;
	}

	@Override
	public boolean isRock()
	{
		return false;
	}

	@Override
	public boolean isObstacle()
	{
		return false;
	}

	@Override
	public Collision landOn(Entity other)
	{
		return switch(other)
		{
			case Mine ignore -> Collision.SHIP_LANDS_ON_MINE;
			case Rock ignore -> Collision.SHIP_LANDS_ON_ROCK;
			case Ship ignore -> Collision.SHIP_LANDS_ON_SHIP;
			default -> throw new RuntimeException("There was a type of entity collision that is unaccounted for.");
		};
	}

	@Override
	public boolean canBeManeuveredAround()
	{
		return true;
	}
}
