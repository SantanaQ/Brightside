package org.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.board.Coordinate;
import org.model.board.Direction;
import org.model.cards.*;
import org.model.moves.*;

import static org.junit.jupiter.api.Assertions.*;

class MoveFactoryTest {

    private MoveFactory moveFactory;

    @BeforeEach
    void setUp() {
        moveFactory = new MoveFactory();
    }

    @Test
    @DisplayName("Create HookMove")
    void testCreateHookMove() {
        Coordinate target = new Coordinate(2, 3);
        Move move = moveFactory.produceMove(new Hook(), new Coordinate[]{target});
        assertInstanceOf(HookMove.class, move, "Move should be an instance of HookMove");
        assertEquals(target, ((HookMove) move).getTarget(), "Target should be the same as provided");
    }

    @Test
    @DisplayName("Create ForwardOneMove")
    void testCreateForwardOneMove() {
        Move move = moveFactory.produceMove(new ForwardOne(), null);
        assertInstanceOf(ForwardMove.class, move, "Move should be an instance of ForwardMove");
        assertEquals(Direction.FORWARD, ((ForwardMove) move).getDirections()[0], "Move distance should be FORWARD_ONE");
        assertEquals(1, ((ForwardMove) move).getDirections().length, "Move should have one direction");
    }

    @Test
    @DisplayName("Create ForwardTwoMove")
    void testCreateForwardTwoMove() {
        Move move = moveFactory.produceMove(new ForwardTwo(), null);
        assertInstanceOf(ForwardMove.class, move, "Move should be an instance of ForwardMove");
        assertEquals(Direction.FORWARD, ((ForwardMove) move).getDirections()[0], "First Move distance should be FORWARD_ONE");
        assertEquals(Direction.FORWARD, ((ForwardMove) move).getDirections()[1], "Second Move distance should be FORWARD_ONE");
        assertEquals(2, ((ForwardMove) move).getDirections().length, "Move should have two directions");
    }

    @Test
    @DisplayName("Create RamMove")
    void testCreateRamMove() {
        Coordinate target = new Coordinate(1, 1);
        Move move = moveFactory.produceMove(new Ram(), new Coordinate[]{target});

        assertInstanceOf(RamMove.class, move, "Move should be an instance of RamMove");
        assertEquals(new Coordinate(1, 1),((RamMove)move).getTarget() , "Direction should be FORWARD_RIGHT");
    }

    @Test
    @DisplayName("Create ManeuverMove")
    void testCreateManeuverMove() {
        Coordinate target = new Coordinate(1, 3);
        Move move = moveFactory.produceMove(new Maneuver(), new Coordinate[]{target});

        assertInstanceOf(ManeuverMove.class, move, "Move should be an instance of ManeuverMove");
        assertEquals(target, ((ManeuverMove) move).getTarget(), "Target should be the same as provided");
    }

    @Test
    @DisplayName("Create SteerMove-Right")
    void testCreateSteerMoveRight() {
        Coordinate target = new Coordinate(1, 1);
        Move move = moveFactory.produceMove(new Steer(), new Coordinate[]{target});
        assertInstanceOf(SteerMove.class, move, "Move should be an instance of SteerMove");
        assertEquals(target, ((SteerMove) move).getTarget(), "Target should be the same as provided");
    }
    @Test
    @DisplayName("Create SteerMove-Left")
    void testCreateSteerMoveLeft() {
        Coordinate target = new Coordinate(0, 1);
        Move move = moveFactory.produceMove(new Steer(), new Coordinate[]{target});
        assertInstanceOf(SteerMove.class, move, "Move should be an instance of SteerMove");
        assertEquals(target, ((SteerMove) move).getTarget(), "Target should be the same as provided");
    }

    @Test
    @DisplayName("Create BroadsideMove-Left")
    void testCreateBroadsideMoveLeft() {
        Move move = moveFactory.produceMove(new BroadsideLeft(), null);
        assertInstanceOf(BroadsideMove.class, move, "Move should be an instance of BroadsideMove");
        assertEquals(Direction.LEFT, ((BroadsideMove) move).getDirection(), "Direction should be LEFT");
    }

    @Test
    @DisplayName("Create BroadsideMove-Right")
    void testCreateBroadsideMoveRight() {
        Move move = moveFactory.produceMove(new BroadsideRight(), null);
        assertInstanceOf(BroadsideMove.class, move, "Move should be an instance of BroadsideMove");
        assertEquals(Direction.RIGHT, ((BroadsideMove) move).getDirection(), "Direction should be RIGHT");
    }

    @Test
    @DisplayName("Create LayMinesMove")
    void testCreateLayMinesMove() {
        Coordinate[] targets = {new Coordinate(1, 4), new Coordinate(2, 4)};
        Move move = moveFactory.produceMove(new LayMines(), targets);

        assertInstanceOf(LayMinesMove.class, move, "Move should be an instance of LayMinesMove");
        assertArrayEquals(targets, ((LayMinesMove) move).getTargets(), "Targets should match the provided array");
    }

    @Test
    @DisplayName("Null as Card should throw exception")
    void testNullCardThrowsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            moveFactory.produceMove(null, null);
        }, "Making a move with no card should throw RuntimeException");

        assertEquals("card is null", exception.getMessage(), "Exception message should indicate unknown card");
    }


}
