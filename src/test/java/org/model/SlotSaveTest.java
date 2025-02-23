package org.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.board.Coordinate;
import org.model.match.Match;
import org.model.match.Player;
import org.model.match.Setup;
import org.saveload.SlotSave;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlotSaveTest {
    SlotSave fileServe;
    Match match;
    Match loaded;

    @BeforeEach
    void setUp() {
        match = new Setup().withTranscription().build().getMatch();
        fileServe = new SlotSave();
        fileServe.save(SlotSave.SAVE_SLOT_DEV, match);
        loaded = fileServe.load(SlotSave.SAVE_SLOT_DEV);
    }

    @AfterEach
    void tearDown() {
        fileServe.delete(SlotSave.SAVE_SLOT_DEV);
        match = null;
        loaded = null;
    }

    @Test
    void current_player_should_stay_the_same_after_saving_and_loading() {
        assertEquals(match.getCurrentPlayer(), loaded.getCurrentPlayer());
    }

    @Test
    void player_list_should_stay_the_same_after_saving_and_loading() {

        List<Integer> playerIDsBefore = match.getPlayers()
                .stream()
                .map(Player::getPlayerID)
                .collect(Collectors.toList());
        List<Integer> playerIDsAfter = loaded.getPlayers()
                .stream()
                .map(Player::getPlayerID)
                .collect(Collectors.toList());
        assertEquals(playerIDsBefore, playerIDsAfter);
    }

    @Test
    void numberTurns_should_stay_the_same_after_saving_and_loading() {

        assertEquals(match.getNumberTurns(), loaded.getNumberTurns());
    }

    @Test
    void numberRounds_should_stay_the_same_after_saving_and_loading() {
        assertEquals(match.getNumberRounds(), loaded.getNumberRounds());
    }

    @Test
    void board_entities_should_stay_the_same_after_saving_and_loading() {

        HashMap<Coordinate, String> entitiesBefore = new HashMap<>();
        for (Coordinate c : match.getBoard().boardLayout().keySet()) {
            entitiesBefore.put(c, match.getBoard().boardLayout().get(c).getClass().getSimpleName());
        }
        HashMap<Coordinate, String> entitiesAfter = new HashMap<>();
        for (Coordinate c : loaded.getBoard().boardLayout().keySet()) {
            entitiesAfter.put(c, loaded.getBoard().boardLayout().get(c).getClass().getSimpleName());
        }
        assertEquals(entitiesBefore, entitiesAfter);
    }

    @Test
    void board_maxLanes_and_maxProgress_should_stay_the_same_after_saving_and_loading() {

        assertEquals(match.getBoard().maxLane(), loaded.getBoard().maxLane());
        assertEquals(match.getBoard().maxProgress(), loaded.getBoard().maxProgress());
    }

    @Test
    void current_player_loadouts_should_stay_the_same_after_saving_and_loading() {

        List<String> handBefore = match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getHand()
                .stream()
                .map(card -> card.getClass().getSimpleName())
                .toList();
        List<String> drawPileBefore = match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getDrawPile()
                .stream()
                .map(card -> card.getClass().getSimpleName())
                .toList();
        List<String> discardPileBefore = match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getDiscardPile()
                .stream()
                .map(card -> card.getClass().getSimpleName())
                .toList();
        List<String> handAfter = match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getHand()
                .stream()
                .map(card -> card.getClass().getSimpleName())
                .toList();
        List<String> drawPileAfter = match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getDrawPile()
                .stream()
                .map(card -> card.getClass().getSimpleName())
                .toList();
        List<String> discardPileAfter  = match.findPlayerInActives(match.getCurrentPlayer()).getLoadout().getDiscardPile()
                .stream()
                .map(card -> card.getClass().getSimpleName())
                .toList();
        assertEquals(handBefore, handAfter);
        assertEquals(drawPileBefore, drawPileAfter);
        assertEquals(discardPileBefore, discardPileAfter);
    }

    @Test
    void saving_a_file_and_deleting_it_afterwards_should_return_true_in_both_scenarios(){
        assertTrue(fileServe.save(SlotSave.SAVE_SLOT_DEV, match));
        assertTrue(fileServe.delete(SlotSave.SAVE_SLOT_DEV));
    }


    @Test
    void saving_a_file_then_deleting_it_and_then_saving_it_again_should_work() {
        fileServe.save(SlotSave.SAVE_SLOT_DEV, match);
        fileServe.delete(SlotSave.SAVE_SLOT_DEV);
        assertTrue(fileServe.save(SlotSave.SAVE_SLOT_DEV, match));
    }


}
