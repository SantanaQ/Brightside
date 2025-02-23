package org.model.entities;

import org.model.board.Collision;

public class Rock implements Entity
{

	@Override
	public boolean isShip()
	{
		return false;
	}

	@Override
	public boolean isShip(int playerID) {
		return false;
	}

	@Override
	public boolean isMine()
	{
		return false;
	}

	@Override
	public boolean isRock()
	{
		return true;
	}

	@Override
	public boolean isObstacle()
	{
		return true;
	}

	@Override
	public Collision landOn(Entity other)
	{
		return Collision.ROCK_LANDS_ON_ANYTHING;
	}

	@Override
	public boolean canBeManeuveredAround()
	{
		return false;
	}
}
