 package org.model;

import org.junit.jupiter.api.*;
import org.model.match.Match;
import org.model.match.Setup;
import org.model.moves.ForwardMove;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    Match match;

    @BeforeEach
    void setUp()
    {
        match = new Setup().build().getMatch();
    }

    @AfterEach
    void tearDown()
    {
        this.match = null;
    }

    @Test
    @DisplayName("End turn increases numberTurns ")
    void end_Turn_Increases_NumberTurns()
    {
        match.endTurn();
        assertEquals(1,match.getNumberTurns());
    }

    @Test
    @DisplayName("End turn goes to next player")
    void end_Turn_Should_Go_To_Next_Player()
    {
        int player1 = match.getCurrentPlayer();
        match.endTurn();
        int player2 = match.getCurrentPlayer();
        assertNotEquals(player1, player2, "Players should have changed after ending turn");
        assertEquals(match.getActivePlayers().get(1), player2, "Player after ending turn should be second player in list.");
    }


    @Test
    @DisplayName("End last turn increases numberRounds")
    void end_Last_Turn_Increases_NumberRounds()
    {
        match.endTurn();
        match.endTurn();
        match.endTurn();
        match.endTurn();
        assertEquals(2,match.getNumberRounds());
    }

    @Test
    @DisplayName("Transcription, when enabled, should not be empty after move")
    public void transcription_when_enabled_should_be_available_after_move()
    {
        match = new Setup().withTranscription().build().getMatch();
        match.executeMove(new ForwardMove(2));
        assertNotEquals("", match.getTranscript());
    }






}
