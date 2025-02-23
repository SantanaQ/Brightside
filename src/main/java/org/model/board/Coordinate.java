package org.model.board;

public record Coordinate (int lane, int progress)
{
    public Coordinate plus(Direction direction)
    {
        return direction.appliedTo(this);
    }

    boolean isNeighborsWith(Coordinate other)
    {
        return Math.abs(this.lane() - other.lane()) <= 1 && Math.abs(this.progress() - other.progress()) <= 1;
    }

	//Here's hoping that no board will ever be wider than 26 spaces.
	private char laneAsLetter()
	{
		return (char)(lane + 64);
	}

	public String transcribe()
	{
		return "%c%d".formatted(laneAsLetter(), progress);
	}
}
