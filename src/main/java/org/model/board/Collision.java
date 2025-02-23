package org.model.board;

public enum Collision
{
	MINE_LANDS_ON_MINE,
	MINE_LANDS_ON_ROCK,
	MINE_LANDS_ON_SHIP,
	ROCK_LANDS_ON_ANYTHING,
	SHIP_LANDS_ON_MINE,
	SHIP_LANDS_ON_ROCK,
	SHIP_LANDS_ON_SHIP;

	public boolean isIllegal()
	{
		return switch(this)
		{
			case ROCK_LANDS_ON_ANYTHING -> true;
			case SHIP_LANDS_ON_SHIP -> true;
			default -> false;
		};
	}
}
