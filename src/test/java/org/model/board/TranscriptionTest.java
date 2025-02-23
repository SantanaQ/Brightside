package org.model.board;

import org.junit.jupiter.api.Test;
import org.model.entities.Ship;
import org.model.moves.*;

import static org.junit.jupiter.api.Assertions.*;

public class TranscriptionTest
{
	@Test void
	transcribing_a_broadside_move_names_the_move_and_the_direction_of_firing()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(2,2), new Ship(0))
				.makeMove(new BroadsideMove(Direction.LEFT), 0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Breitseite") && transcriptString.contains("links"));
	}

	@Test void
	transcribing_a_forward_move_lists_the_individual_steps()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(3,4), new Ship(0))
				.makeMove(new ForwardMove(2), 0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Bewegen") && transcriptString.contains("vorne,vorne"));
	}

	@Test void
	transcribing_a_hook_move_names_the_move_and_target_position()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(2,4), new Ship(0))
				.placeEntity(new Coordinate(3,6), new Ship(1))
				.makeMove(new HookMove(new Coordinate(3,6)), 0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Haken") && transcriptString.contains("C6"));
	}

	@Test void
	transcribing_a_mine_laying_move_names_the_move_and_lists_target_positions()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		var targets = new Coordinate[]{
				new Coordinate(1,8),
				new Coordinate(4,8)};

		decoratedBoard.placeEntity(new Coordinate(2,4), new Ship(0))
				.makeMove(new LayMinesMove(targets), 0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Minen")
				&& transcriptString.contains("A8")
				&& transcriptString.contains("D8"));
	}

	@Test void
	transcribing_a_maneuver_move_names_the_move_and_target_position()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(2,4), new Ship(0))
				.placeEntity(new Coordinate(2,5), new Ship(1))
				.makeMove(new ManeuverMove(new Coordinate(2,6)),0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Manövrieren")
				&& transcriptString.contains("B6"));
	}

	@Test void
	transcribing_a_ramming_move_names_the_move_and_the_target_position()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(2,4), new Ship(0))
				.placeEntity(new Coordinate(2,5), new Ship(1))
				.makeMove(new RamMove(new Coordinate(2,5)),0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Ramme")
				&& transcriptString.contains("B5"));
	}

	@Test void
	transcribing_a_steering_move_names_the_move_and_the_target_position()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(2,4), new Ship(0))
				.makeMove(new SteerMove(new Coordinate(3,5)),0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Steuern")
				&& transcriptString.contains("C5"));

	}

	@Test void
	every_move_done_on_a_transcribing_board_is_transcribed()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(3,3), new Ship(0))
				.makeMove(new SteerMove(new Coordinate(4,4)), 0)
				.makeMove(new ForwardMove(1), 0)
				.makeMove(new BroadsideMove(Direction.LEFT), 0);

		var transcriptString = liveTranscript.toString();

		assertTrue(transcriptString.contains("Steuern")
				&& transcriptString.contains("Bewegen")
				&& transcriptString.contains("Breitseite"));
	}


	@Test void
	invalid_moves_are_not_transcribed()
	{
		TranscribingBoard board = new TranscribingBoard(new Board(5,10));
		LiveTranscript liveTranscript = board.getTranscript();
		MoveResolver decoratedBoard = (MoveResolver) board;

		decoratedBoard.placeEntity(new Coordinate(4,4), new Ship(0))
				.placeEntity(new Coordinate(4,5), new Ship(1))
				.makeMove(new ForwardMove(1), 0);

		assertTrue(liveTranscript.isEmpty());
	}

}
