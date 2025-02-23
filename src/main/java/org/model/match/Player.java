package org.model.match;

public class Player {

  private final int playerID;
  private Loadout loadout;


  public Player(int playerID, Loadout loadout)
  {
    this.loadout = loadout;
    this.playerID = playerID;
  }

  public int getPlayerID() {
    return playerID;
  }

  public Loadout getLoadout() {
    return loadout;
  }

  public void setLoadout(Loadout loadout) {
    this.loadout = loadout;
  }

}