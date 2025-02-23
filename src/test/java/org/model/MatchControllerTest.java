package org.model;

import org.controller.MatchController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.board.Coordinate;
import org.model.cards.*;
import org.model.entities.Mine;
import org.model.entities.Ship;
import org.model.match.Setup;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {

    private MatchController matchController;

    @BeforeEach
    void setUp() {
        matchController = new Setup().build();
    }

    @AfterEach
    void tearDown() {
        matchController = null;
    }


    @Test
    @DisplayName("Variables should be null on setup")
    void variables_should_be_null_on_setup() {
        assertNull(matchController.getLockedCard(), "LockedCard should initially be null");
        assertNull(matchController.getTargets(), "Targets should initially be null");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should initially be 0");
    }

    @Test
    @DisplayName("Target coordinate amount should be based on selected card")
    void target_coordinate_amount_should_be_based_on_selected_card() {
        matchController.chooseCard(new LayMines());
        int targetCount = matchController.getRequiredFields();
        assertEquals(2, targetCount, "LAY_MINES should require 2 target coordinates");
        assertEquals(LayMines.class, matchController.getLockedCard().getClass(), "LockedCard should be set to LAY_MINES");

        matchController.chooseCard(new Steer());
        targetCount = matchController.getRequiredFields();
        assertEquals(1, targetCount, "RAM should require 1 target coordinate");
        assertEquals(Steer.class, matchController.getLockedCard().getClass(), "LockedCard should be set to RAM");

        matchController.chooseCard(new ForwardOne());
        targetCount = matchController.getRequiredFields();
        assertEquals(0, targetCount, "FORWARD_ONE should require 0 target coordinates");
        assertNull(matchController.getLockedCard(), "LockedCard should be reset after executing move");
    }

    @Test
    @DisplayName("choosing target after playing BroadsideLeft should execute move and restore GUI params to default")
    void choosing_target_after_playing_broadside_left_should_execute_move_and_restore_gui_params_to_default() {
        matchController.chooseCard(new BroadsideLeft());
        matchController.setTarget(new Coordinate(2, 2)); //TODO: Koordinaten anpassen wenn Board Schiffe hat

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing BroadsideRight should execute move and restore GUI params to default")
    void choosing_target_after_playing_broadside_right_should_execute_move_and_restore_gui_params_to_default() {
        matchController.chooseCard(new BroadsideRight());
        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing ForwardOne should execute move and restore GUI params to default")
    void playing_forward_one_should_execute_move_and_restore_gui_params_to_default() {
        matchController.chooseCard(new ForwardOne());

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing ForwardTwo should execute move and restore GUI params to default")
    void playing_forward_two_should_execute_move_and_restore_gui_params_to_default() {
        matchController.chooseCard(new ForwardTwo());

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing Hook should execute move and restore GUI params to default")
    void choosing_target_after_playing_hook_should_execute_move_and_restore_gui_params_to_default() {
        Coordinate currentPlayerPosition = matchController.getMatch().getCurrentPlayerPosition();
        matchController.getMatch().getBoard().placeEntity(new Coordinate(currentPlayerPosition.lane() + 1, currentPlayerPosition.progress() + 2), new Ship(67));
        matchController.chooseCard(new Hook());
        matchController.setTarget(new Coordinate(currentPlayerPosition.lane() + 1, currentPlayerPosition.progress() + 2));

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing LayMines should execute move and restore GUI params to default")
    void choosing_two_targets_after_playing_lay_mines_should_execute_move_and_restore_gui_params_to_default() {
        matchController.chooseCard(new LayMines());
        Coordinate currentPlayerPosition = matchController.getMatch().getCurrentPlayerPosition();
        matchController.setTarget(new Coordinate(1, currentPlayerPosition.progress() + 4));
        matchController.setTarget(new Coordinate(2, currentPlayerPosition.progress() + 4));

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target (Ship) after playing Maneuver should execute move and restore GUI params to default")
    void choosing_target_that_is_a_ship_after_playing_maneuver_should_execute_move_and_restore_gui_params_to_default() {
        Coordinate currentPlayerPosition = matchController.getMatch().getCurrentPlayerPosition();
        matchController.getMatch().getBoard().placeEntity(new Coordinate(currentPlayerPosition.lane(), currentPlayerPosition.progress() + 1), new Ship(98));
        matchController.chooseCard(new Maneuver());
        matchController.setTarget(new Coordinate(currentPlayerPosition.lane(), currentPlayerPosition.progress() + 2));

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target (Mine) after playing Maneuver should execute move and restore GUI params to default")
    void choosing_target_that_is_a_mine_after_playing_maneuver_should_execute_move_and_restore_gui_params_to_default() {
        Coordinate currentPlayerPosition = matchController.getMatch().getCurrentPlayerPosition();
        matchController.getMatch().getBoard().placeEntity(new Coordinate(currentPlayerPosition.lane(), currentPlayerPosition.progress() + 1), new Mine());
        matchController.chooseCard(new Maneuver());
        matchController.setTarget(new Coordinate(currentPlayerPosition.lane(), currentPlayerPosition.progress() + 2));

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing Ram should execute move and restore GUI params to default")
    void choosing_target_after_playing_ram_should_execute_move_and_restore_gui_params_to_default() {
        Coordinate currentPlayerPosition = matchController.getMatch().getCurrentPlayerPosition();
        matchController.getMatch().getBoard().placeEntity(new Coordinate(currentPlayerPosition.lane(), currentPlayerPosition.progress() + 1), new Ship(46));
        matchController.chooseCard(new Ram());
        matchController.setTarget(new Coordinate(currentPlayerPosition.lane(), currentPlayerPosition.progress() + 1));

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }

    @Test
    @DisplayName("choosing target after playing Steer should execute move and restore GUI params to default")
    void choosing_target_after_playing_steer_should_execute_move_and_restore_gui_params_to_default() {
        matchController.chooseCard(new Steer());
        matchController.setTarget(new Coordinate(2, 2)); //TODO: Koordinaten anpassen wenn Board Schiffe hat

        assertNull(matchController.getLockedCard(), "LockedCard should be null after move execution");
        assertNull(matchController.getTargets(), "Targets should be null after move execution");
        assertEquals(0, matchController.getTargetIndex(), "TargetIndex should be reset to 0 after move execution");
    }


    @Test
    @DisplayName("Choosing the same coordinate for LayMines twice should remove it from targets")
    void choosing_the_same_Coordinate_for_laymines_twice_should_remove_it_from_targets() {
        matchController.chooseCard(new LayMines());
        Coordinate currentPlayerPosition = matchController.getMatch().getCurrentPlayerPosition();
        matchController.setTarget(new Coordinate(0, currentPlayerPosition.progress() + 4));
        // Remove the first target
        matchController.setTarget(new Coordinate(0, currentPlayerPosition.progress() + 4));
        matchController.setTarget(new Coordinate(1, currentPlayerPosition.progress() + 4));

        assertEquals(new Coordinate(1, currentPlayerPosition.progress() + 4), matchController.getTargets()[0], "Remaining target should be (1,currentPlayerposition.progress() + 4)");
        assertNull(matchController.getTargets()[1], "There should only be one target in targets array");
    }

    @Test
    void choosing_card_that_is_not_currently_playable_should_set_requiredFields_to_minus_one() {
        matchController.chooseCard(new Hook());
        assertEquals(-1, matchController.getRequiredFields());
    }

    @Test
    void ending_all_turns_should_start_a_new_round() {
        for(int i = 0; i < matchController.getMatch().getActivePlayers().size(); i++) {
            matchController.endTurn();
        }
        assertEquals(2, matchController.rounds());
    }

}
