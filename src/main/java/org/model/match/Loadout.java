package org.model.match;

import org.model.cards.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Loadout
{

    private List<Card> drawPile;
    private List<Card> hand;
    private List<Card> discardPile;

    public Loadout(List<Card> cardSelection)
    {
        this.hand = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.drawPile = new ArrayList<>(cardSelection);
        this.shuffleDrawPile();
        this.drawCards(Math.min(cardSelection.size(), 3));
    }

    public Card chooseCard(int cardNumber)
    {
        if(hand.isEmpty() ||cardNumber < 0 || cardNumber >= hand.size())
        {
            return null;
        }
        return hand.get(cardNumber);
    }

    public Loadout shuffleDrawPile()
    {
        Collections.shuffle(drawPile);
        return this;
    }

    public Loadout drawCards(int howMany)
    {
        if(howMany <= 0 || (drawPile.isEmpty() && discardPile.isEmpty()))
            return this;
        if(drawPile.isEmpty())
            resetPiles();
        Card draw = drawPile.getLast();
        hand.add(draw);
        drawPile.remove(draw);
        howMany--;
        return drawCards(howMany);
    }

    private void resetPiles()
    {
        drawPile = discardPile;
        discardPile = new ArrayList<>();
        shuffleDrawPile();
    }

    public Loadout playCard(Card card)
    {
        discardPile.add(card);
        hand.remove(card);
        return this;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setDrawPile(List<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

}
