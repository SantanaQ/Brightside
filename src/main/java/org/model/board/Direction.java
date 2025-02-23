package org.model.board;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Direction
{
	FORWARD(0, 1),
	FORWARD_RIGHT(1, 1),
	RIGHT(1, 0),
	BACKWARD_RIGHT(1, -1),
	BACKWARD(0, -1),
	BACKWARD_LEFT(-1, -1),
	LEFT(-1, 0),
	FORWARD_LEFT(-1, 1);

	private final int laneDelta;
	private final int progressDelta;

	private Direction(int laneDelta, int progressDelta)
	{
		this.laneDelta = laneDelta;
		this.progressDelta = progressDelta;
	}

	Coordinate appliedTo(Coordinate coordinate)
	{
		var newLane = coordinate.lane() + laneDelta;
		var newProgress = coordinate.progress() + progressDelta;
		return new Coordinate(newLane, newProgress);
	}

	Direction opposite()
	{
		return switch(this)
		{
			case FORWARD -> BACKWARD;
			case FORWARD_RIGHT -> BACKWARD_LEFT;
			case RIGHT -> LEFT;
			case BACKWARD_RIGHT -> FORWARD_LEFT;
			case BACKWARD -> FORWARD;
			case BACKWARD_LEFT -> FORWARD_RIGHT;
			case LEFT -> RIGHT;
			case FORWARD_LEFT -> BACKWARD_RIGHT;
		};
	}

	static Stream<Direction> streamAll()
	{
		return Arrays.stream(new Direction[]
		{
			FORWARD,
			FORWARD_RIGHT,
			RIGHT,
			BACKWARD_RIGHT,
			BACKWARD,
			BACKWARD_LEFT,
			LEFT,
			FORWARD_LEFT
		});
	}

	public String transcribe()
	{
		return
				switch(this)
				{
					case FORWARD -> "vorne";
					case FORWARD_RIGHT -> "vorne rechts";
					case RIGHT -> "rechts";
					case BACKWARD_RIGHT -> "hinten rechts";
					case BACKWARD -> "hinten";
					case BACKWARD_LEFT -> "hinten links";
					case LEFT -> "links";
					case FORWARD_LEFT -> "vorne links";
				};
	}

}