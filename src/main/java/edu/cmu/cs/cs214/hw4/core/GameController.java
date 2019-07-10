package edu.cmu.cs.cs214.hw4.core;


import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Controller of the game, responsible for controlling all of the process of the game
 */
public class GameController {
  private static final int MAX_PLAYERS = 5;
  private static final String CONFIG_PATH = "src/main/resources/config.json";
  private List<Player> players;
  private int idxOfPlayer;
  private Map<Character, Integer> cardStack;
  private int numOfTile;
  private int numOfCard;
  private Map<Integer, Map<Integer, Tile>> tiles;

  /**
   * Start the game, initialize all of the tiles
   */
  private void startNewGame() {
    if (players == null) {
      players = new ArrayList<>();
    }
    tiles = new HashMap<>();
    cardStack = new HashMap<>();
    numOfTile = 0;
    numOfCard = 0;
    idxOfPlayer = 0;
    parseFromConfig();
  }

  /**
   * if there are more than 2 players, then we can start the game
   * @return if we can start the game
   */
  public boolean canStartGame() {
    return players.size() >= 2;
  }

  /**
   * parse the config file and get the tile information
   */
  private void parseFromConfig(){
    Gson gson = new Gson();
    try {
      Reader reader = new FileReader(new File(CONFIG_PATH));
      JSONConfigReader.JSONGroceryStore result = gson.fromJson(reader, JSONConfigReader.JSONGroceryStore.class);
      for (JSONConfigReader.JSONItem item : result.inventory) {
        cardStack.put(item.item, item.quantity);
        numOfTile += item.quantity;
        numOfCard += item.quantity;
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Error when reading file: " + CONFIG_PATH, e);
    }
  }

  /**
   * Constructor
   */
  public GameController() {
    startNewGame();
  }

  /**
   * create a new player and join the game
   * @return new player
   */
  public Player joinNewPlayer() {
    if (players.size() == MAX_PLAYERS) {
      return null;
    }
    Player player = new Player(players.size());
    players.add(player);
    return player;
  }

  /**
   * place the tile and check its validation
   * @param newTile new tile
   * @return        validate the placement of the tile
   */
  public boolean placeTile(Tile newTile) {
    int x = newTile.getX();
    int y = newTile.getY();
    boolean isAlone = true;
    int[] dxs = new int[]{0, 0, 1, -1};
    int[] dys = new int[]{1, -1, 0, 0};
    Direction[] dirs = new Direction[] {Direction.South, Direction.North, Direction.West, Direction.East};
    for (int i = 0; i < dxs.length; i++) {
      int dx = dxs[i];
      int dy = dys[i];
      Tile base = getTile(x+dx, y+dy);
      if (base == null) {
        continue;
      }
      isAlone = false;
      if (!isTileMatch(base, dirs[i], newTile)) {
        return false;
      }

    }
    if (numOfTile+1 != numOfCard && isAlone) {
      return false;
    }
    insertTile(x, y, newTile);
    // merge same segments
    mergeSegment(newTile);
    return true;
  }

  /**
   * place the meeple into the tile
   * @param tile      tile
   * @param direction where the meeple is placed in the tile
   * @return if this meeple can be successfully placed
   */
  public boolean placeMeeple(Tile tile, Direction direction) {
    Player player = players.get(idxOfPlayer);
    if (tile.getSegments().get(direction) == null) {
      return false;
    }
    Meeple meeple = player.placeMeeple();
    if (meeple == null) {
      return false;
    }
    tile.placeMeeple(direction, meeple);
    return true;
  }

  /**
   * update the score of the segment
   * @param segment segment we want to update
   */
  public void updateScore(Segment segment) {
    Map<Integer, Integer> count = new HashMap<>();
    int maxCount = 0;
    for (Meeple meeple : segment.getMeeples()) {
      int id = meeple.getId();
      count.put(id, count.getOrDefault(id, 0)+1);
      maxCount = Math.max(maxCount, count.get(id));
    }
    int score = segment.calculateScore();
    for (int id : count.keySet()) {
      if (count.get(id) == maxCount) {
        players.get(id).updateScore(score);
      }
    }
    for (Meeple meeple : segment.getMeeples()) {
      players.get(meeple.getId()).getMeepleBack(meeple);
    }
  }

  /**
   * End of the game, final scoring
   */
  public void endGame() {
    for (int x : getTiles().keySet()) {
      for (int y : getTiles().get(x).keySet()) {
        Tile base = getTile(x, y);
        for (Direction d : base.getSegments().keySet()) {
          Segment segment = base.getSegments().get(d);
          if (segment.isComplete()) {
            continue;
          }
          if (segment.getMeeples().isEmpty()) {
            continue;
          }
          updateScore(segment);
          segment.setStatus(true);
        }
      }
    }
  }

  /**
   * check if the segment is complete; if it is complete, update its score
   */
  public void checkScoreUpdate(){
    Set<Segment> visited = new HashSet<>();
    for (int x : getTiles().keySet()) {
      for (int y : getTiles().get(x).keySet()) {
        Tile base = getTile(x, y);
        for (Direction d : base.getSegments().keySet()) {
          Segment segment = base.getSegments().get(d);
          if (segment.isComplete()) {
            continue;
          }
          if (visited.contains(segment)) {
            continue;
          }
          if (segment.checkForComplete()) {
            updateScore(segment);
          }
        }
      }
    }
  }

  /**
   * merge the segments of the adjacent tile
   * @param tile adjacent tile
   */
  public void mergeSegment(Tile tile) {
    int x = tile.getX();
    int y = tile.getY();
    Tile target;
    //Center
    for (int i = -1; i <= 1; i+=1) {
      for (int j = -1; j <= 1; j+=1) {
        if (i == 0 && j == 0)
          continue;
        target = getTile(x + i, y + j);
        if (target == null) {
          continue;
        }
        // if this tile is monastery
        if (tile.getSegments().get(Direction.Center) != null) {
          tile.getSegments().get(Direction.Center).getTiles().add(target);
        }
        // if there are monasteries aroun
        if (target.getSegments().get(Direction.Center) != null) {
          target.getSegments().get(Direction.Center).getTiles().add(tile);
        }
      }
    }

    //North
    target = getTile(x, y+1);
    if (target != null && target.getSegments().get(Direction.South) != null) {
      Segment base = tile.getSegments().get(Direction.North);
      Segment targetSeg = target.getSegments().get(Direction.South);
      //remove in requiredConnection
      base.getRequiredConnection().remove(target.getCoordinate());
      targetSeg.getRequiredConnection().remove(tile.getCoordinate());
      base.merge(targetSeg);
      updateAllSegment(target, Direction.South, base);
    }
    //East
    target = getTile(x+1, y);
    if (target != null && target.getSegments().get(Direction.West) != null) {
      Segment base = tile.getSegments().get(Direction.East);
      Segment targetSeg = target.getSegments().get(Direction.West);
      //remove in requiredConnection
      base.getRequiredConnection().remove(target.getCoordinate());
      targetSeg.getRequiredConnection().remove(tile.getCoordinate());
      base.merge(targetSeg);
      updateAllSegment(target, Direction.West, base);
    }

    //South
    target = getTile(x, y-1);
    if (target != null && target.getSegments().get(Direction.North) != null) {
      Segment base = tile.getSegments().get(Direction.South);
      Segment targetSeg = target.getSegments().get(Direction.North);
      //remove in requiredConnection
      base.getRequiredConnection().remove(target.getCoordinate());
      targetSeg.getRequiredConnection().remove(tile.getCoordinate());
      base.merge(targetSeg);
      updateAllSegment(target, Direction.North, base);
    }

    //West
    target = getTile(x-1, y);
    if (target != null && target.getSegments().get(Direction.East) != null) {
      Segment base = tile.getSegments().get(Direction.West);
      Segment targetSeg = target.getSegments().get(Direction.East);
      //remove in requiredConnection
      base.getRequiredConnection().remove(target.getCoordinate());
      targetSeg.getRequiredConnection().remove(tile.getCoordinate());
      base.merge(targetSeg);
      updateAllSegment(target, Direction.East, base);
    }
  }

  /**
   * Use "target" and "direction" to find the old segment we want to replace.
   * Traverse all of the tiles, find the old segment and replace them with new segment("alter")
   * @param target    target tile
   * @param direction used to find the segment we want to replace
   * @param alter     new segment we want to use
   */
  public void updateAllSegment(Tile target, Direction direction, Segment alter) {
    Segment old = target.getSegments().get(direction);
    for (int x : getTiles().keySet()) {
      for (int y : getTiles().get(x).keySet()) {
        Map<Direction, Segment> segments = getTile(x, y).getSegments();
        for (Direction dir : segments.keySet()) {
          if (segments.get(dir) == old) {
            segments.put(dir, alter);
          }
        }
      }
    }
  }

  /**
   * is two adjacent tiles match
   * @param base    base tile
   * @param d1      which part of the base tile is related to the adjacent tile
   * @param newTile adjacent tile
   * @return
   */
  private boolean isTileMatch(Tile base, Direction d1, Tile newTile) {
    Segment s1, s2;
    Direction d2 = null;
    if (d1 == Direction.North) d2 = Direction.South;
    if (d1 == Direction.South) d2 = Direction.North;
    if (d1 == Direction.West) d2 = Direction.East;
    if (d1 == Direction.East) d2 = Direction.West;

    s1 = base.getSegments().get(d1);
    s2 = newTile.getSegments().get(d2);
    if (s1 == null && s2 == null) {
      return true;
    }
    if (s1 == null || s2 == null) {
      return false;
    }
    if (s1.isMatch(s2)) {
      return true;
    }
    return false;
  }

  /**
   * insert tile into the game
   * @param x    location of x
   * @param y    location of y
   * @param tile tile we want to place
   */
  private void insertTile(int x, int y, Tile tile) {
    if (!getTiles().containsKey(x)) {
      getTiles().put(x, new HashMap<>());
    }
    getTiles().get(x).put(y, tile);
  }

  /**
   * find the tile
   * @param x location of x
   * @param y location of y
   * @return  get the tile for a given location
   */
  private Tile getTile(int x, int y) {
    if (!getTiles().containsKey(x) || !getTiles().get(x).containsKey(y)) {
      return null;
    }
    return getTiles().get(x).get(y);
  }

  /**
   * randomly draw the card from the tile stack
   * @return type of the card
   */
  public Character randomDrawCard() {
    // start tile must be Type-D
    if (numOfTile == numOfCard) {
      cardStack.put('D', cardStack.get('D')-1);
      numOfTile--;
      return 'D';
    }
    int idx = (int)(Math.random()* numOfTile);
    for (char key : cardStack.keySet()) {
      if (idx >= cardStack.get(key)) {
        idx -= cardStack.get(key);
      } else {
        cardStack.put(key, cardStack.get(key)-1);
        if (cardStack.get(key) == 0) {
          cardStack.remove(key);
        }
        numOfTile--;
        return key;
      }
    }
    return null;
  }


  /**
   * how many cards are in the tile stack
   * @return num of tile
   */
  public int getNumOfTile() {
    return numOfTile;
  }

  /**
   * It is next player's turn to play the game
   */
  public void nextPlayer() {
    idxOfPlayer = (idxOfPlayer + 1) % players.size();
  }

  /**
   * Get current player
   * @return current player
   */
  public Player getCurrPlayer() {
    return players.get(idxOfPlayer);
  }

  /**
   * find the player for a given index
   * @param idx index of the player
   * @return    player
   */
  public Player getPlayer(int idx) {
    return players.get(idx);
  }

  /**
   * get the number of players in the game
   * @return number of players
   */
  public int getNumOfPlayer() {
    return players.size();
  }

  /**
   * get the score list
   * @return scores for all of the players
   */
  public List<Integer> getScoreList() {
    List<Integer> res = new ArrayList<>();
    for (Player player : players) {
      res.add(player.getScore());
    }
    return res;
  }

  /**
   * @return class name
   */
  @Override
  public String toString() {
    return "Game Controller";
  }

  /**
   * get all of the placed tiles
   * @return tiles
   */
  public Map<Integer, Map<Integer, Tile>> getTiles() {
    return tiles;
  }
}
