package org.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.cards.*;
import org.model.match.Loadout;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadoutTest {

    Loadout loadoutInit;
    List<Card> cards;

    @BeforeEach
    void setUp() {

        cards = new ArrayList<>();
        cards.add(new Ram());
        cards.add(new Hook());
        cards.add(new Maneuver());
        cards.add(new BroadsideLeft());
        cards.add(new BroadsideRight());
        cards.add(new Steer());
        cards.add(new ForwardOne());

        loadoutInit = new Loadout(cards);
        //BasicLoadout - also beim Starten
        //Drawpile ist nur noch 1
        //Drawpile ist 0
    }

    @Test
    @DisplayName("Loadout Init")
    void test_init_loadout() {
        assertNotNull(loadoutInit.getDrawPile(), "DrawPile should not be null");
        assertNotNull(loadoutInit.getHand(), "Hand should not be null");
        assertNotNull(loadoutInit.getDiscardPile(), "DiscardPile should not be null");
    }

    @Test
    @DisplayName("Hand should contain 3 cards after init, Drawpile should contain 4 cards")
    void cards_should_go_to_hand_after_init() {
        assertEquals(3, loadoutInit.getHand().size(), "Hand should contain 3 cards");
        assertEquals(4, loadoutInit.getDrawPile().size(), "Drawpile should contain 4 cards");
        assertEquals(0, loadoutInit.getDiscardPile().size(), "Discard Pile should contain 0 cards");
    }

    @Test
    @DisplayName("Drawing cards with negative value should not change hand size")
    void drawCards_with_neg_values_should_not_change_hand_size() {
        loadoutInit.drawCards(-1);
        assertEquals(3, loadoutInit.getHand().size(), "Hand should contain 3 cards");
    }

    @Test
    @DisplayName("Drawing cards should increase hand size")
    void drawCards_should_increase_hand_size(){
        loadoutInit.drawCards(2);
        assertEquals(5, loadoutInit.getHand().size(), "Hand should contain 5 cards");
        assertEquals(2, loadoutInit.getDrawPile().size(), "Drawpile should contain 2 cards");
        assertEquals(0, loadoutInit.getDiscardPile().size(), "Discardpile should be empty");
    }


    @Test
    @DisplayName("When drawpile is empty, discard pile should become the new drawpile")
    void when_drawpile_is_empty_discard_pile_should_become_the_new_drawpile() {
        loadoutInit.setDrawPile(new ArrayList<>());
        loadoutInit.setDiscardPile(cards);
        loadoutInit.setHand(new ArrayList<>());
        loadoutInit.drawCards(2);
        assertEquals(5, loadoutInit.getDrawPile().size(), "Draw Pile should contain 5 cards");
        assertEquals(0, loadoutInit.getDiscardPile().size(), "Discard Pile should contain 0 cards");
        assertEquals(2, loadoutInit.getHand().size(), "Hand should contain 2 cards");

    }

    @Test
    @DisplayName("When card is being played, discard pile should get played card")
    void played_card_should_be_given_to_discard_pile() {
        Card c = loadoutInit.chooseCard(0);
        loadoutInit.playCard(c);
        assertEquals(c, loadoutInit.getDiscardPile().get(loadoutInit.getDiscardPile().size()-1), "Top card of discard pile should be " + c);
        assertEquals(2, loadoutInit.getHand().size(), "Hand should contain 2 cards");
        assertEquals(1, loadoutInit.getDiscardPile().size(), "Discard Pile should contain 1 card");
        assertEquals(4, loadoutInit.getDrawPile().size(), "Draw Pile should contain 4 cards");
        assertFalse(loadoutInit.getHand().remove(c), "Hand should not contain specific card");
    }

    @Test
    @DisplayName("Choosing a card out of bounds of hand size should return null")
    void choosing_card_out_of_bounds_return_null() {
        assertNull(loadoutInit.chooseCard(loadoutInit.getHand().size()), "Card should be null");
        assertNull(loadoutInit.chooseCard(-1), "Card should be null, when negative number");
        loadoutInit.setHand(new ArrayList<>());
        assertNull(loadoutInit.chooseCard(1), "Card should be null, when hand is empty");
    }

}