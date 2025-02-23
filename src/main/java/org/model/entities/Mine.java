package org.model.entities;

import org.model.board.Collision;

/**
 *
 * Description
 *
 * @version 1.0 from 22.10.2024
 * @author 
 */

public class Mine implements Entity
{

	@Override
	public boolean isShip()
	{
		return false;
	}

	@Override
	public boolean isShip(int playerID)
	{
		return false;
	}

	@Override
	public boolean isMine()
	{
		return true;
	}

	@Override
	public boolean isRock()
	{
		return false;
	}

	@Override
	public boolean isObstacle()
	{
		return true;
	}

	@Override
	public Collision landOn(Entity other)
	{
		return switch(other)
		{
			case Mine ignore -> Collision.MINE_LANDS_ON_MINE;
			case Rock ignore -> Collision.MINE_LANDS_ON_ROCK;
			case Ship ignore -> Collision.MINE_LANDS_ON_SHIP;
			default -> throw new RuntimeException("There was a type of entity collision that is unaccounted for.");
		};
	}

	@Override
	public boolean canBeManeuveredAround()
	{
		return true;
	}
}
