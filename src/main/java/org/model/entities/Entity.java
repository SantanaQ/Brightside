package org.model.entities;

import org.model.board.Collision;

public interface Entity
{

	public boolean isShip();

	public boolean isRock();

	public boolean isShip(int playerID);

	public boolean isMine();

	public boolean isObstacle();

	public Collision landOn(Entity other);

	public boolean canBeManeuveredAround();
}
