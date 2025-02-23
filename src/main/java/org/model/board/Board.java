package org.model.board;

import org.model.entities.*;
import org.model.moves.Move;
import org.util.T2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Board repräsentiert das Spielbrett, das heißt die Anordnung aller Entitäten einschließlich der spielbaren Schiffe.</p>
 *
 * <p>Diese Klasse ist Teil des <strong>Command Patterns</strong>: Das 'Command' ist hier der {@link Move} (Spielzug eines Spielers),
 * der dem Spielbrett übergeben und ausgewertet und dürchgeführt wird.</p>
 *
 * <p>Diese Klasse ist Teil des <strong>Strategy Patterns</strong>: Es gibt verschiedene Möglichkeiten, zu bestimmen,
 * wo ein Felsen respawnt wird.
 * Diese Entscheidung trifft das Spielbrett nicht selbst, sondern delegiert dies an einen internen {@link RockSpawner}.
 * Wie die neue Position eines Felsens bestimmt wird hängt davon ab, welche Klasse hinter diesem Interface steht.</p>
 */
public class Board implements MoveResolver
{

	private int maxProgress;
	private int maxLane;

	private HashMap<Coordinate, Entity> boardLayout = new HashMap<>();

	private List<Integer> winners = new ArrayList<>();

	private RockSpawner rockSpawner = new RandomRockSpawner();

	public Board(int maxLane, int maxProgress, RockSpawner rockSpawningStrategy)
	{
		this.rockSpawner = rockSpawningStrategy;
		this.maxProgress = maxProgress;
		this.maxLane = maxLane;
	}

	public Board(int maxLane, int maxProgress)
	{
		this.maxProgress = maxProgress;
		this.maxLane = maxLane;
	}

	Board()
	{
		this(5, 10);
	}

	@Override
	public int maxLane()
	{
		return maxLane;
	}

	@Override
	public int maxProgress()
	{
		return maxProgress;
	}

	public RockSpawner rockSpawner() { return rockSpawner; }

	@Override
	public HashMap<Coordinate, Entity> boardLayout()
	{
		return this.boardLayout;
	}

	boolean isEmpty()
	{
		return boardLayout.isEmpty();
	}

	Board prepared()
	{
		int numberOfPlayers = maxLane - 2;
		return this.prepared(numberOfPlayers);
	}

	Board prepared(int numberOfPlayers)
	{
		int[] shipIDs = new int[numberOfPlayers];

		for(int i=0; i<numberOfPlayers; i++)
			{ shipIDs[i] = i; }

		return this.prepared(shipIDs);
	}


	public Board prepared(int[] players)
	{
		int numberOfPlayers = players.length;
		if(numberOfPlayers > maxLane)
			{ throw new RuntimeException("Not enough space for all players."); }

		int unusedLanes = maxLane - numberOfPlayers;
		int unusedLanesLeftSide = unusedLanes / 2;
		int firstLaneWithShip = unusedLanesLeftSide + 1;

		for(int i=0; i<numberOfPlayers; i++)
		{
			int lane = firstLaneWithShip + i;
			int idOfPlacedShip = players[i];
			placeEntity(new Coordinate(lane, 1), new Ship(idOfPlacedShip));
		}

		rockSpawner.prepareRocks(this);

		return this;
	}

	@Override
	public List<Integer> winners()
	{
		return this.winners;
	}

	/**
	 * <p>Führt einen Spielzug aus, wenn dieser regelkonform ist.</p>
	 *
	 * <p>Diese Methode ist Teil des <strong>Command Patterns</strong>: Der Spielzug ({@link Move}) ist hier das "Command",
	 * das die Entscheidung repräsentiert, wie genau der Zug aussehen soll, das heißt welche weitere Methode
	 * auf dem Spielbrett aufzurufen ist und mit welchen Parametern.</p>
	 *
	 * @param move Der durchzuführende Spielzug
	 * @param playerID Die ID des Spielers, der den Zug macht
	 * @return Das ggf. veränderte Spielbrett
	 */
	public Board makeMove(Move move, int playerID)
	{
		return move.playOn(this, playerID);
	}

	@Override
	public MoveResult canMakeMove(Move move, int playerID)
	{
		return move.canPlayOn(this, playerID);
	}

	private MoveResult canMoveShip(int playerID, Direction direction)
	{
		Coordinate currentPosition = findShip(playerID).e1();
		Coordinate targetPosition = direction.appliedTo(currentPosition);

		if(shipAt(targetPosition).isPresent())
			{ return MoveResult.SPACE_IS_OCCUPIED; }
		else if(!positionIsInBounds(targetPosition))
			{ return MoveResult.OUT_OF_BOUNDS; }
		else
			{ return MoveResult.VALID; }
	}

	public MoveResult canMoveShip(int playerID, Direction[] movements)
	{
		return canMoveShip(playerID, movements[0]);
	}

	public Board moveShip(int playerID, Direction[] movements)
	{
		var origin_ship = findShip(playerID);
		var origin = origin_ship.e1();
		var movedShip = origin_ship.e2();

		var result = this;

		for (Direction direction : movements)
		{
			var target = direction.appliedTo(origin);

			if(!isValidPositionForShip(target))
				{ return result; }
			else if(obstacleAt(target).isPresent())
			{
				return result
					.clear(origin)
					.placeEntity(target, movedShip);
			}
			else
			{
				result = result
					.clear(origin)
					.placeEntity(target, movedShip);

				origin = direction.appliedTo(origin);
			}

		}
		return result;
	}

	public Board moveShip(int playerID, Direction direction)
	{
		var origin_ship = findShip(playerID);
		var origin = origin_ship.e1();
		var movedShip = origin_ship.e2();
		var target = direction.appliedTo(origin);

		var failure = this;

		if(!isValidPositionForShip(target))
			{ return failure; }
		else
		{
			return this
				.clear(origin)
				.placeEntity(target, movedShip);
		}
	}

	public MoveResult canSteerShip(int id, Coordinate target)
	{
		var origin_ship = findShip(id);
		var origin = origin_ship.e1();

		if(shipAt(target).isPresent())
			{ return MoveResult.SPACE_IS_OCCUPIED; }
		else if(!positionIsInBounds(target))
			{ return MoveResult.OUT_OF_BOUNDS; }
		else if(!(origin.plus(Direction.FORWARD_LEFT).equals(target)
			|| origin.plus(Direction.FORWARD_RIGHT).equals(target)))
		{
			return MoveResult.WRONG_ALIGNMENT;
		}
		else
			{ return MoveResult.VALID; }
	}

	public Board steerShip(int id, Coordinate target)
	{
		if(canSteerShip(id, target).isValid())
		{
			var origin_ship = findShip(id);
			var origin = origin_ship.e1();
			var movedShip = origin_ship.e2();

			clear(origin);
			placeEntity(target, movedShip);
		}
		return this;
	}

	public MoveResult canShoot(int id, Direction direction)
	{
		return MoveResult.VALID;
	}

	public Board shoot(int id, Direction direction)
	{

		var origin_ship = findShip(id);
		var origin = origin_ship.e1();

		Optional<Integer> hitShipId = Optional.empty();

		for(var hitSpace=origin.plus(direction); positionIsInBounds(hitSpace); hitSpace=hitSpace.plus(direction))
		{
			var hitShip = shipAt(hitSpace);
			if(hitShip.isPresent())
			{
				hitShipId = Optional.of(hitShip.get().playerID());
				break;
			}
		}

		hitShipId.ifPresent((hit) -> setBack(hit));

		moveShip(id, direction.opposite());

		return this;
	}

	public MoveResult canLayMine(int id, Coordinate target)
	{
		if(!positionIsInBounds(target))
			{ return MoveResult.OUT_OF_BOUNDS; }

		var origin = findShip(id).e1();

		if(target.progress() != origin.progress() + 4)
			{ return MoveResult.WRONG_ALIGNMENT; }

		return MoveResult.VALID;
	}

	public MoveResult canLayMines(int id, Coordinate[] targets)
	{
		if(targets.length != 2)
			{ return MoveResult.WRONG_NUMBER_OF_TARGETS; }

		if(targets[0].equals(targets[1]))
			{ return MoveResult.TARGETS_OVERLAP; }

		var canLayFirstMine = canLayMine(id, targets[0]);

		if(canLayFirstMine.isInvalid())
			{ return canLayFirstMine; }

		var canLaySecondMine = canLayMine(id, targets[1]);

		if(canLaySecondMine.isInvalid())
			{ return canLaySecondMine; }

		return MoveResult.VALID;
	}

	public Board layMines(int id, Coordinate[] targets)
	{
		if(canLayMines(id, targets).isValid())
		{
			for(var target : targets)
				{ placeEntity(target, new Mine()); }
		}

		return this;
	}

	public MoveResult canManeuverShip(int id, Coordinate target)
	{
		if(!positionIsInBounds(target))
			{ return MoveResult.OUT_OF_BOUNDS; }

		if(shipAt(target).isPresent())
			{ return MoveResult.SPACE_IS_OCCUPIED; }

		var origin = findShip(id).e1();

		var alignedDirection = Direction.streamAll()
				.filter((dir) -> origin.plus(dir).plus(dir).equals(target))
				.findAny();

		if(alignedDirection.isEmpty())
			{ return MoveResult.WRONG_ALIGNMENT; }

		var hoppedPosition = origin.plus(alignedDirection.get());

		if(at(hoppedPosition).filter(Entity::canBeManeuveredAround).isEmpty())
			{ return MoveResult.WRONG_VICTIM; }

		return MoveResult.VALID;

	}

	public Board maneuverShip(int id, Coordinate target)
	{
		var origin_ship = findShip(id);
		var origin = origin_ship.e1();
		var movingShip = origin_ship.e2();

		if(canManeuverShip(id, target).isValid())
		{
			clear(origin);
			placeEntity(target, movingShip);
		}

		return this;
	}

	public MoveResult canRam(int id, Coordinate target)
	{

		if(!positionIsInBounds(target))
			{ return MoveResult.OUT_OF_BOUNDS; }

		var origin = findShip(id).e1();

		if(!origin.isNeighborsWith(target))
			{ return MoveResult.WRONG_ALIGNMENT; }

		if(shipAt(target).isEmpty())
			{ return MoveResult.WRONG_VICTIM; }

		return MoveResult.VALID;
	}

	public Board ram(int id, Coordinate target)
	{
		if(canRam(id, target).isValid())
		{
			var origin = findShip(id).e1();
			var targetID = shipAt(target).get().playerID();
			var rammingDirection
					= Direction.streamAll()
					.filter((dir) -> origin.plus(dir).equals(target))
					.findAny()
					.get();

			moveShip(targetID, rammingDirection);
			moveShip(id, rammingDirection.opposite());
		}

		return this;
	}

	public MoveResult canHook(int id, Coordinate target)
	{
		if(!positionIsInBounds(target))
			{ return MoveResult.OUT_OF_BOUNDS; }

		var origin = findShip(id).e1();
		var targetLaneIsNextToOriginLane
				= origin.lane() + 1 == target.lane()
				|| origin.lane() - 1 == target.lane();
		var targetProgressIsThreeOrFourAhead
				= origin.progress() + 2 == target.progress()
				|| origin.progress() + 3 == target.progress();

		if(!(targetLaneIsNextToOriginLane && targetProgressIsThreeOrFourAhead))
			{ return MoveResult.WRONG_ALIGNMENT; }

		if(shipAt(target).isEmpty())
			{ return MoveResult.WRONG_VICTIM; }

		return MoveResult.VALID;

	}

	public Board hook(int id, Coordinate target)
	{
		if(canHook(id, target).isValid())
		{
			var targetID = shipAt(target).get().playerID();
			var ownMovement = new Direction[]{Direction.FORWARD, Direction.FORWARD};
			var victimMovement = Direction.BACKWARD;
			moveShip(id, ownMovement);
			moveShip(targetID, victimMovement);
		}

		return this;
	}

	private boolean isValidPositionForShip(Coordinate target)
	{
		return shipAt(target).isEmpty()&& positionIsInBounds(target);
	}

	@Override
	public Board placeEntity(Coordinate position, Entity placedEntity)
	{

		if(!positionIsInBounds(position))
		{
			return this;
		}
		else if(at(position).isEmpty())
		{
			boardLayout.put(position, placedEntity);
		}
		else
		{
			var preexistingEntity = at(position).get();

			var collision = placedEntity.landOn(preexistingEntity);

			switch (collision) {
				case ROCK_LANDS_ON_ANYTHING -> {
					throw new RuntimeException("Cannot put Rock on non-empty space. This case needs checking.");
				}

				case SHIP_LANDS_ON_SHIP -> {
					throw new RuntimeException("Cannot put ship on other ship. This case needs checking.");
				}

				case MINE_LANDS_ON_MINE -> {
					boardLayout.remove(position);
				}

				case MINE_LANDS_ON_ROCK -> {
					boardLayout.remove(position);
					spawnRock();
				}

				case MINE_LANDS_ON_SHIP -> {
					setBack(((Ship) preexistingEntity).playerID());
				}

				case SHIP_LANDS_ON_MINE -> {
					boardLayout.put(position, placedEntity);
					setBack(((Ship) placedEntity).playerID());
				}

				case SHIP_LANDS_ON_ROCK -> {
					boardLayout.put(position, placedEntity);
					setBack(((Ship) placedEntity).playerID());
					spawnRock();
				}

			}
		}

		if (placedEntity.isShip())
		{
			var placedShipID = ((Ship) placedEntity).playerID();

			if (isOnLastProgress(placedShipID) && !winners.contains(placedShipID))
			{
				winners.add(placedShipID);
				var shipPosition = findShip(placedShipID).e1();
				this.clear(shipPosition);
			}
		}

		return this;
	}


	private boolean isOnLastProgress(int playerID)
	{
		return findShip(playerID).e1().progress() == this.maxProgress;
	}


	/**
	 * <p>Erzeugt einen neuen Felsen auf einem zufällig ausgewählten Feld.</p>
	 *
	 * <p>Diese Methode ist Teil des <strong>Strategy Patterns</strong>: Der Aufruf wird an den internen {@link RockSpawner} delegiert.
	 * Je nachdem, welche Klasse dort hinterlegt ist, wird unterschiedlich entschieden, wo ein zerstörter Felsen
	 * wieder auftauchen soll.</p>
	 *
	 * @return Das veränderte Spielbrett
	 */
	private Board spawnRock()
	{
		return rockSpawner.spawnRock(this);
	}


	Board setBack(int playerID)
	{
		var movements = new Direction[]{Direction.BACKWARD, Direction.BACKWARD};

		return moveShip(playerID, movements);
	}

	@Override
	public Board driftMines()
	{
		var minePositions = boardLayout.entrySet().stream()
			.filter((entry) -> entry.getValue().isMine())
			.map((entry) -> entry.getKey())
			.collect(Collectors.toList());

		minePositions.sort(Comparator.comparingInt(Coordinate::progress));

		minePositions.stream()
			.forEach( (coord) -> {clear(coord);} );

		minePositions.stream()
			.map((coord) -> Direction.BACKWARD.appliedTo(coord))
			.forEach( (coord) -> placeEntity(coord, new Mine()));

		return this;
	}


	boolean positionIsInBounds(Coordinate coordinate)
	{
		return coordinate.lane() >= 1
				&& coordinate.lane() <= maxLane
				&& coordinate.progress() >= 1
				&& coordinate.progress() <= maxProgress;
	}

	Optional<Entity> at(Coordinate coordinate)
	{
		return Optional.ofNullable(boardLayout.get(coordinate));
	}

	private Board clear(Coordinate coordinate)
	{
		boardLayout.remove(coordinate);
		return this;
	}

	@Override
	public T2<Coordinate, Ship> findShip(int playerID)
	{
		for(var entry : boardLayout.entrySet())
		{
			if(entry.getValue().isShip(playerID))
				{ return new T2<>(entry.getKey(), (Ship) entry.getValue()); }
		}

		throw new RuntimeException("Asked for ship that is not on board.");
	}

	private Optional<Ship> shipAt(Coordinate coordinate)
	{
		return this
				.at(coordinate)
				.filter(Entity::isShip)
				.map((ent) -> (Ship) ent);
	}

	private Optional<Entity> obstacleAt(Coordinate coordinate)
	{
		return this
				.at(coordinate)
				.filter(Entity::isObstacle);
	}

}
