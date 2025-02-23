package org.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.model.cards.*;
import org.model.match.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SetupTest {

    Setup setup;
    List<Card> cards;

    @BeforeEach
    public void setUp() {
        setup = new Setup();
        cards = new ArrayList<>();
        cards.add(new BroadsideLeft());
        cards.add(new Ram());
        cards.add(new ForwardOne());
        cards.add(new Maneuver());
        cards.add(new Hook());

    }


    @Test
    @DisplayName("withBoard should return Board size")
    public void withBoard_should_return_correct_board_size(){
        setup.withBoard(5,8);
        assertEquals(8, setup.board.maxProgress(), "Board width should be 8");
        assertEquals(5, setup.board.maxLane(), "Board length should be 5");
    }

    @Test
    @DisplayName("withCurrentPlayer should return correct player")
    public void withCurrentPlayer_should_return_correct_player(){
        setup.withStartingPlayer(setup.players.get(1));
        assertEquals(setup.currentPlayer, setup.players.get(1), "Match should start with first player in playerList");
    }

    @Test
    @DisplayName("withPlayers should return correct player amount")
    public void withPlayers_should_return_correct_player_amount (){
        setup.withPlayers(3).build();
        assertEquals(setup.players.size(),3, "withPlayers should return 10 players");
    }

    @Test
    @DisplayName("withCards should return correct card set")
    public void withCards_should_return_correct_card_set(){
        setup.withCards(cards);
        assertEquals(5, setup.players.getFirst().getLoadout().getDiscardPile().size()
                + setup.players.getFirst().getLoadout().getHand().size()
                + setup.players.getFirst().getLoadout().getDrawPile().size(), "Loadout should contain 5 cards total");
        assertEquals(BroadsideLeft.class, cards.get(0).getClass(), "first card in cardSelection should be " + cards.get(0).getClass().getSimpleName());
        assertEquals(Ram.class, cards.get(1).getClass(), "secont card in cardSelection should be " + cards.get(1).getClass().getSimpleName());
        assertEquals(ForwardOne.class, cards.get(2).getClass(), "third card in cardSelection should be " + cards.get(2).getClass().getSimpleName());
        assertEquals(Maneuver.class, cards.get(3).getClass(), "fourth card in cardSelection should be " + cards.get(3).getClass().getSimpleName());
        assertEquals(Hook.class, cards.get(4).getClass(), "fifth card in cardSelection should be " + cards.get(4).getClass().getSimpleName());
    }

    @Test
    @DisplayName("When negative playercount value is given, players list should be default")
    public void given_negative_playercount_should_create_default_playerlist(){
        setup.withPlayers(-1).build();
        assertEquals(setup.board.maxLane(), setup.players.size(), "Players list should contain 4 players");
    }

    @Test
    @DisplayName("Not explicitly calling loadout build creates default loadouts")
    public void not_explicitly_calling_loadout_build(){
        setup.build();
        int loadoutSize = setup.currentPlayer.getLoadout().getDiscardPile().size()
                + setup.currentPlayer.getLoadout().getDrawPile().size()
                + setup.currentPlayer.getLoadout().getDiscardPile().size()
                + setup.currentPlayer.getLoadout().getHand().size();
        assertEquals(12, loadoutSize);
    }

    @Test
    @DisplayName("More players than board lanes should set player amount equal to board lanes")
    public void building_with_more_players_than_board_lanes_should_set_player_amount_equal_to_board_lanes(){
        setup.withPlayers(100).withBoard(5,10).build().getMatch();
        assertEquals(5, setup.players.size());
    }

    @Test
    @DisplayName("Building with transcription should initialize transcriber")
    public void building_with_transcription_should_initialize_transcription(){
        Match match = setup.withTranscription().build().getMatch();
        assertNotNull(match.getTranscript());
    }


}
