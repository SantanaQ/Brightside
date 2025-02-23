package org.model.board;

import org.junit.jupiter.api.Assertions;
import org.model.entities.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest
{

	@Test void
	generating_a_new_board_instantiates_all_its_fields_to_be_empty()
	{
		var board = new Board();
		assertTrue(board.isEmpty());
	}

	@Test void
	preparing_a_board_for_play_puts_the_given_number_of_players_on_the_lowest_progress_position()
	{
		var boardWith3Ships = new Board().prepared(new int[]{0,1,2});
		assertTrue(boardWith3Ships.at(new Coordinate(2,1)).get().isShip());
		assertTrue(boardWith3Ships.at(new Coordinate(3,1)).get().isShip());
		assertTrue(boardWith3Ships.at(new Coordinate(4,1)).get().isShip());

		var boardWith5Ships = new Board().prepared(new int[]{0,1,2,3,4});
		assertTrue(boardWith5Ships.at(new Coordinate(1,1)).get().isShip());
		assertTrue(boardWith5Ships.at(new Coordinate(2,1)).get().isShip());
		assertTrue(boardWith5Ships.at(new Coordinate(3,1)).get().isShip());
		assertTrue(boardWith5Ships.at(new Coordinate(4,1)).get().isShip());
		assertTrue(boardWith5Ships.at(new Coordinate(5,1)).get().isShip());
	}

	@Test void
	moving_forward_moves_the_ship_of_the_given_playerID_by_1_unit_of_progress()
	{
		var movementOrigin = new Coordinate(1,2);
		var movementTarget = new Coordinate(1,3);

		var boardAfterMoving = new Board()
				.placeEntity(movementOrigin, new Ship(1))
				.moveShip(1, Direction.FORWARD);


		assertTrue(boardAfterMoving.at(movementOrigin).isEmpty());
		assertTrue(boardAfterMoving.at(movementTarget).get().isShip(1));
	}

	@Test void
	trying_to_move_a_ship_to_the_position_of_another_ship_fails_and_does_not_change_the_board()
	{
		var ship1Position = new Coordinate(3,4);
		var ship2Position = new Coordinate(4,4);

		var boardAfterAttempt = new Board()
				.placeEntity(ship1Position, new Ship(1))
				.placeEntity(ship2Position, new Ship(2))
				.moveShip(1, Direction.RIGHT);


		assertTrue(boardAfterAttempt.at(ship1Position).get().isShip(1));
		assertTrue(boardAfterAttempt.at(ship2Position).get().isShip(2));
	}

	@Test void
	trying_to_move_a_ship_off_of_a_board_fails_and_does_not_change_the_board()
	{
		var boardWithShipsInCorners =
				new Board()
						.placeEntity(new Coordinate(1,1), new Ship(1))
						.placeEntity(new Coordinate(5,1), new Ship(2));

		var couldMoveThroughLowerLeftCorner = boardWithShipsInCorners
				.canMoveShip(1, new Direction[]{Direction.BACKWARD_LEFT}).isValid();
		var couldMoveThroughLowerRightCorner = boardWithShipsInCorners
				.canMoveShip(2, new Direction[]{Direction.BACKWARD_RIGHT}).isValid();

		assertFalse(couldMoveThroughLowerLeftCorner);
		assertFalse(couldMoveThroughLowerRightCorner);

	}

	@Test void
	a_ship_colliding_with_an_obstacle_is_set_back_by_two_units_if_the_way_is_not_obstructed()
	{
		var boardAfterCollision = new Board()
				.placeEntity(new Coordinate(4,5), new Ship(1))
				.placeEntity(new Coordinate(5,6), new Mine())
				.moveShip(1, Direction.FORWARD_RIGHT);

		assertTrue(boardAfterCollision.at(new Coordinate(4,5)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(5,6)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(5,5)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(5,4)).get().isShip(1));
	}

	@Test void
	a_ship_colliding_with_an_obstacle_stops_the_setback_if_there_is_a_ship_in_the_way()
	{
		var boardAfterCollision = new Board()
				.placeEntity(new Coordinate(4,5), new Ship(1))
				.placeEntity(new Coordinate(5,6), new Mine())
				.placeEntity(new Coordinate(5,4), new Ship(2))
				.moveShip(1, Direction.FORWARD_RIGHT);

		assertTrue(boardAfterCollision.at(new Coordinate(4,5)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(5,6)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(5,5)).get().isShip(1));
		assertTrue(boardAfterCollision.at(new Coordinate(5,4)).get().isShip(2));
	}

	@Test void
	a_ship_colliding_with_an_obstacle_stops_the_setback_if_it_would_move_out_of_bounds()
	{
		var boardAfterCollision = new Board()
				.placeEntity(new Coordinate(3,1), new Ship(1))
				.placeEntity(new Coordinate(4,2), new Mine())
				.moveShip(1, Direction.FORWARD_RIGHT);

		assertTrue(boardAfterCollision.at(new Coordinate(3,1)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,2)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,1)).get().isShip(1));
	}

	@Test void
	a_ship_colliding_with_an_obstacle_will_start_a_new_setback_if_it_hits_more_obstacles()
	{
		var boardAfterCollision = new Board()
				.placeEntity(new Coordinate(3,7), new Ship(1))
				.placeEntity(new Coordinate(4,8), new Mine())
				.placeEntity(new Coordinate(4,6), new Mine())
				.moveShip(1, Direction.FORWARD_RIGHT);

		assertTrue(boardAfterCollision.at(new Coordinate(3,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,8)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,6)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,5)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,4)).get().isShip(1));
	}

	@Test void
	setbacks_from_obstacles_do_not_stack()
	{
		var boardAfterCollision = new Board()
			.placeEntity(new Coordinate(3,7), new Ship(1))
			.placeEntity(new Coordinate(4,8), new Mine())
			.placeEntity(new Coordinate(4,7), new Mine())
			.moveShip(1, Direction.FORWARD_RIGHT);

		assertTrue(boardAfterCollision.at(new Coordinate(3,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,8)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,6)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,5)).get().isShip(1));
	}

	@Test void
	chained_movements_will_fail_if_the_first_movement_fails()
	{
		var movements = new Direction[] {Direction.FORWARD_RIGHT, Direction.FORWARD};
		var boardAfterCollision = new Board()
			.placeEntity(new Coordinate(3,7), new Ship(1))
			.placeEntity(new Coordinate(4,8), new Ship(2))
			.moveShip(1, movements);

		assertTrue(boardAfterCollision.at(new Coordinate(3,7)).get().isShip(1));
		assertTrue(boardAfterCollision.at(new Coordinate(4,8)).get().isShip(2));
	}

	@Test void
	all_valid_chained_movements_will_be_made_if_the_first_movement_is_valid()
	{
		var movements = new Direction[] {Direction.FORWARD, Direction.RIGHT};
		var boardAfterCollision = new Board()
			.placeEntity(new Coordinate(3,7), new Ship(1))
			.placeEntity(new Coordinate(4,8), new Ship(2))
			.moveShip(1, movements);

		assertTrue(boardAfterCollision.at(new Coordinate(3,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(3,8)).get().isShip(1));
		assertTrue(boardAfterCollision.at(new Coordinate(4,8)).get().isShip(2));
	}

	@Test void
	chained_movements_into_an_obstacle_will_result_in_a_setback_and_stop_there()
	{
		var movements = new Direction[] {Direction.FORWARD_RIGHT, Direction.LEFT};
		var boardAfterCollision = new Board()
			.placeEntity(new Coordinate(3,7), new Ship(1))
			.placeEntity(new Coordinate(4,8), new Mine())
			.moveShip(1, movements);

		assertTrue(boardAfterCollision.at(new Coordinate(3,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,8)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,7)).isEmpty());
		assertTrue(boardAfterCollision.at(new Coordinate(4,6)).get().isShip(1));
		assertTrue(boardAfterCollision.at(new Coordinate(3,6)).isEmpty());
	}

	@Test void
	the_current_moves_all_mines_backward_by_one_progress()
	{
		var boardAfterCurrent = new Board()
			.placeEntity(new Coordinate(3,3), new Mine())
			.placeEntity(new Coordinate(4,3), new Mine())
			.driftMines();

		assertTrue(boardAfterCurrent.at(new Coordinate(3,3)).isEmpty());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,2)).get().isMine());
		assertTrue(boardAfterCurrent.at(new Coordinate(4,3)).isEmpty());
		assertTrue(boardAfterCurrent.at(new Coordinate(4,2)).get().isMine());
	}

	@Test void
	mines_will_be_drifted_starting_from_the_lowest_progress()
	{
		var boardAfterCurrent = new Board()
			.placeEntity(new Coordinate(3,3), new Mine())
			.placeEntity(new Coordinate(3,4), new Mine())
			.driftMines();

		assertTrue(boardAfterCurrent.at(new Coordinate(3,2)).get().isMine());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,4)).isEmpty());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,3)).get().isMine());
	}

	@Test void
	mines_can_drift_off_of_the_Board_due_to_the_current()
	{
		var boardAfterCurrent = new Board()
			.placeEntity(new Coordinate(3,1), new Mine())
			.driftMines();

		assertTrue(boardAfterCurrent.at(new Coordinate(3,1)).isEmpty());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,0)).isEmpty());
	}

	@Test void
	if_a_mine_drifts_into_a_ship_and_that_ship_lands_on_a_lower_mine_that_lower_mines_drift_is_resolved_before_the_collision()
	{
		var boardAfterCurrent = new Board()
			.placeEntity(new Coordinate(3,6), new Mine())
			.placeEntity(new Coordinate(3,3), new Mine())
			.placeEntity(new Coordinate(3,5), new Ship(1))
			.driftMines();

		assertTrue(boardAfterCurrent.at(new Coordinate(3,6)).isEmpty());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,5)).isEmpty());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,3)).get().isShip());
		assertTrue(boardAfterCurrent.at(new Coordinate(3,2)).get().isMine());
	}

	@Test void
	the_number_of_rocks_does_not_change_after_a_collision_between_a_rock_and_a_ship()
	{
		var boardAfterCollision = new Board(5,10)
			.placeEntity(new Coordinate(3,4), new Rock())
			.placeEntity(new Coordinate(3,3), new Ship(1))
			.moveShip(1, Direction.FORWARD);

		assertTrue(boardAfterCollision.at(new Coordinate(3,2)).get().isShip(1));
		assertEquals(1,
			boardAfterCollision.boardLayout()
				.entrySet()
				.stream()
				.filter(ent -> ent.getValue().isRock())
				.count());
	}

	@Test void
	a_ship_shooting_in_a_direction_sets_back_the_first_other_ship_in_that_direction_and_moves_the_shooting_ship_in_the_opposite_direction()
	{
		var boardAfterShooting = new Board()
				.placeEntity(new Coordinate(2,4), new Ship(0))
				.placeEntity(new Coordinate(4,4), new Ship(1))
				.shoot(0, Direction.RIGHT);

		assertTrue(boardAfterShooting.at(new Coordinate(2,4)).isEmpty());
		assertTrue(boardAfterShooting.at(new Coordinate(1,4)).get().isShip(0));
		assertTrue(boardAfterShooting.at(new Coordinate(4,4)).isEmpty());
		assertTrue(boardAfterShooting.at(new Coordinate(4,2)).get().isShip(1));
	}

	@Test void
	a_ship_laying_mines_out_of_bounds_is_not_allowed()
	{
		var result = new Board(10,5)
				.placeEntity(new Coordinate(3,8), new Ship(0))
				.canLayMines(0, new Coordinate[]{new Coordinate(2,12), new Coordinate(4,12)});

		Assertions.assertEquals(MoveResult.OUT_OF_BOUNDS, result);
	}

	@Test void
	a_ship_can_only_lay_mines_on_positions_that_are_four_units_of_progress_ahead()
	{
		var shipPosition = new Coordinate(3,3);
		var correctTarget = new Coordinate(2,3+4);
		var wrongTarget = new Coordinate(4,3+5);
		var result = new Board(5,10)
				.placeEntity(shipPosition, new Ship(0))
				.canLayMines(0, new Coordinate[]{correctTarget, wrongTarget});

		assertEquals(MoveResult.WRONG_ALIGNMENT, result);
	}

	@Test void
	a_ship_cannot_lay_mines_on_two_equal_spaces_at_once()
	{
		var shipPosition = new Coordinate(3,3);
		var correctTarget = new Coordinate(2,3+4);
		var sameTarget = new Coordinate(2,3+4);

		var result = new Board(5,10)
				.placeEntity(shipPosition, new Ship(0))
				.canLayMines(0, new Coordinate[]{correctTarget, sameTarget});

		assertEquals(MoveResult.TARGETS_OVERLAP, result);
	}
	
	@Test void
	a_ship_can_lay_mines_on_two_distinct_positions_that_are_four_units_of_progress_ahead_and_in_bounds()
	{
		var shipPosition = new Coordinate(3,3);
		var target1 = new Coordinate(2,3+4);
		var target2 = new Coordinate(4,3+4);

		var boardAfterLayingMines = new Board(5,10)
				.placeEntity(shipPosition, new Ship(0))
				.layMines(0, new Coordinate[]{target1, target2});

		assertTrue(boardAfterLayingMines.at(target1).get().isMine());
		assertTrue(boardAfterLayingMines.at(target2).get().isMine());
	}

	@Test void
	a_ship_cannot_maneuver_out_of_bounds()
	{
		var shipPosition = new Coordinate(4,6);
		var hoppedShipPosition = new Coordinate(5,7);

		var canManeuver = new Board(5,10)
				.placeEntity(shipPosition, new Ship(0))
				.placeEntity(hoppedShipPosition, new Ship(1))
				.canManeuverShip(0, new Coordinate(6,8));

		assertEquals(MoveResult.OUT_OF_BOUNDS, canManeuver);
	}

	@Test void
	a_ship_cannot_maneuver_around_a_ship_that_is_not_adjacent()
	{
		var shipPosition = new Coordinate(4,3);
		var hoppedShipPosition = new Coordinate(2,3);

		var canManeuver = new Board(5,10)
				.placeEntity(shipPosition, new Ship(0))
				.placeEntity(hoppedShipPosition, new Ship(1))
				.canManeuverShip(0, new Coordinate(1,3));

		assertEquals(MoveResult.WRONG_ALIGNMENT, canManeuver);
	}

	@Test void
	a_ship_can_maneuver_around_an_adjacent_ship()
	{
		var shipPosition = new Coordinate(4,3);
		var hoppedShipPosition = shipPosition.plus(Direction.FORWARD_LEFT);
		var targetPosition = hoppedShipPosition.plus(Direction.FORWARD_LEFT);

		var boardAfterManeuver = new Board(5,10)
				.placeEntity(shipPosition, new Ship(0))
				.placeEntity(hoppedShipPosition, new Ship(1))
				.maneuverShip(0, targetPosition);

		assertTrue(boardAfterManeuver.at(shipPosition).isEmpty());
		assertTrue(boardAfterManeuver.at(targetPosition).get().isShip(0));
	}

	@Test void
	a_ship_ramming_an_adjacent_ship_results_in_both_ships_moving_away_from_each_other_by_1()
	{
		var rammingShipPosition = new Coordinate(3,3);
		var rammingShipMovesTo = rammingShipPosition.plus(Direction.LEFT);
		var rammedShipPosition = rammingShipPosition.plus(Direction.RIGHT);
		var rammedShipMovesTo = rammedShipPosition.plus(Direction.RIGHT);

		var boardAfterRam = new Board(5,10)
				.placeEntity(rammingShipPosition, new Ship(0))
				.placeEntity(rammedShipPosition, new Ship(1))
				.ram(0, rammedShipPosition);

		assertTrue(boardAfterRam.at(rammingShipPosition).isEmpty());
		assertTrue(boardAfterRam.at(rammedShipPosition).isEmpty());

		assertTrue(boardAfterRam.at(rammingShipMovesTo).get().isShip(0));
		assertTrue(boardAfterRam.at(rammedShipMovesTo).get().isShip(1));
	}

	@Test void
	a_ship_hooking_another_ship_is_moved_forward_by_two_units_of_progress_while_the_victim_is_moved_back_by_one_unit_of_progress()
	{
		var attackerPosition = new Coordinate(2,2);
		var victimPosition = new Coordinate(3,5);
		var attackerGoesTo = attackerPosition.plus(Direction.FORWARD).plus(Direction.FORWARD);
		var victimGoesTo = victimPosition.plus(Direction.BACKWARD);

		var boardAfterHook = new Board(5,10)
				.placeEntity(attackerPosition, new Ship(0))
				.placeEntity(victimPosition, new Ship(1))
				.hook(0, victimPosition);

		assertTrue(boardAfterHook.at(attackerPosition).isEmpty());
		assertTrue(boardAfterHook.at(victimPosition).isEmpty());
		assertTrue(boardAfterHook.at(attackerGoesTo).get().isShip(0));
		assertTrue(boardAfterHook.at(victimGoesTo).get().isShip(1));
	}

	@Test void
	winning_players_are_ranked_by_the_order_in_which_they_reach_the_highest_possible_progress()
	{
		var board = new Board(5,10)
				.placeEntity(new Coordinate(2,9), new Ship(0))
				.placeEntity(new Coordinate(3,9), new Ship(1))
				.placeEntity(new Coordinate(4,9), new Ship(2))
				.moveShip(1, Direction.FORWARD)
				.moveShip(0, Direction.FORWARD);

		var expectedWinners = Arrays.asList(1,0);
		assertEquals(expectedWinners, board.winners());

		var boardWithExtraWinner = board.moveShip(2, Direction.FORWARD);
		var newExpectedWinners = Arrays.asList(1,0,2);
		assertEquals(newExpectedWinners, boardWithExtraWinner.winners());
	}

	@Test void
	winning_ships_are_removed_from_the_board()
	{
		var board = new Board(5,10)
				.placeEntity(new Coordinate(3,9), new Ship(0))
				.moveShip(0, Direction.FORWARD);

		assertTrue(board.at(new Coordinate(3,10)).isEmpty());
	}

	@Test void
	momentarily_reaching_the_last_progress_but_being_set_back_does_not_count_as_winning()
	{
		var board = new Board(5,10)
				.placeEntity(new Coordinate(3,9), new Ship(0))
				.placeEntity(new Coordinate(3,10), new Mine())
				.moveShip(0, Direction.FORWARD);

		assertTrue(board.winners().isEmpty());
	}

	@Test void
	mean_rock_respawning_tries_to_select_the_positions_diagonally_or_cardinally_in_front_of_the_ships_with_the_highest_achieved_progress()
	{
		var board = new Board(5,10)
				.placeEntity(new Coordinate(1,7), new Ship(0))
				.placeEntity(new Coordinate(2,7), new Ship(1))
				.placeEntity(new Coordinate(3,5), new Ship(2));

		var spawner = new MeanRockSpawner();
		var selectedPositions = new HashSet<>(spawner.positionsInFrontOfFurthestShips(board));

		var expectedPositions = Stream.of(
				new Coordinate(1,8),
				new Coordinate(2,8),
				new Coordinate(3,8)
		).collect(Collectors.toSet());

		assertEquals(expectedPositions, selectedPositions);
	}

}