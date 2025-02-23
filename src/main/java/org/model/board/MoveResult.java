package org.model.board;

public enum MoveResult
{
	VALID,
	SPACE_IS_OCCUPIED,
	OUT_OF_BOUNDS,
	WRONG_ALIGNMENT,
	WRONG_NUMBER_OF_TARGETS,
	TARGETS_OVERLAP,
	WRONG_VICTIM;

	public boolean isValid()
	{
		return this.equals(MoveResult.VALID);
	}

	public boolean isInvalid()
	{
		return !this.equals(MoveResult.VALID);
	}
}
