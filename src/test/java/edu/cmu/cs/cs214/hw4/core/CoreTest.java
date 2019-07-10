package edu.cmu.cs.cs214.hw4.core;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit Test
 */
public class CoreTest {
  private GameController game;

  /**
   * Initialize verifiers and test function joinNewPlayer
   */
  @Before
  public void initialize() {
    game = new GameController();
    int num = 5;
    for (int i = 0; i < num; i++) {
      Player player = game.joinNewPlayer();
      assertEquals(player.getId(), i);
      assertEquals(player.getAvailableMeeple(), 7);
      assertEquals(player.getScore(), 0);
    }
  }

  @Test
  public void testRandomDispatch() {
    Character type = game.randomDrawCard();
    Map<Character, Integer> map = new HashMap<>();
    assertTrue(type == 'D');
    map.put(type, 1);
    while (game.getNumOfTile() > 0) {
      type = game.randomDrawCard();
      assertNotNull(type);
      assertNotNull(new Tile(type));
      if (!map.containsKey(type)) {
        map.put(type, 1);
      } else {
        map.put(type, map.get(type)+1);
      }
    }
    char[] types = new char[]{'C', 'G', 'Q', 'T', 'X'};
    for (char type1 : types) {
      assertTrue(map.get(type1) == 1);
    }
    types = new char[]{'A', 'F', 'I', 'M', 'O', 'S'};
    for (char type1 : types) {
      assertTrue(map.get(type1) == 2);
    }
    types = new char[]{'H', 'J', 'K', 'L', 'N', 'P', 'R'};
    for (char type1 : types) {
      assertTrue(map.get(type1) == 3);
    }
    types = new char[]{'B', 'D', 'W'};
    for (char type1 : types) {
      assertTrue(map.get(type1) == 4);
    }
    assertTrue(map.get('E') == 5);
    assertTrue(map.get('U') == 8);
    assertTrue(map.get('V') == 9);
  }

  /**
   * Test the scenario when we finish a road
   */
  @Test
  public void testToFinishARoad() {
    char type;
    Tile tile;
    //first tile
    type = game.randomDrawCard();
    tile = new Tile(type);
    tile.place(0,0);
    assertNotNull(game.placeTile(tile));
    //second Tile
    game.nextPlayer();
    type = 'W';
    tile = new Tile(type);
    tile.place( -1,0);
    assertFalse(game.placeTile(tile));
    tile = new Tile(type);
    tile.place(0,1);
    assertTrue(game.placeTile(tile));

    //third tile
    game.nextPlayer();
    type = 'L';
    tile = new Tile(type);
    tile.place(0,-1);
    assertTrue(game.placeTile(tile));
    assertTrue(game.placeMeeple(tile, Direction.North));
    assertEquals(game.getPlayer(2).getAvailableMeeple(), 6);

    //check score
    game.checkScoreUpdate();
    assertEquals(game.getPlayer(2).getAvailableMeeple(), 7);
    assertTrue(game.getScoreList().get(2) == 3);
  }

  /**
   * Test the scenario when we finish a City
   */
  @Test
  public void testToFinishACity() {
    char type;
    Tile tile;
    //first tile
    type = game.randomDrawCard();
    tile = new Tile(type);
    tile.place(0,0);
    assertTrue(game.placeTile(tile));
    assertTrue(game.placeMeeple(tile, Direction.East));
    //second Tile
    game.nextPlayer();
    type = 'E';
    tile = new Tile(type);
    tile.place(1, 0);
    assertFalse(game.placeTile(tile));
    tile.rotate();
    tile.rotate();
    tile.rotate();
    assertTrue(game.placeTile(tile));
    assertEquals(game.getPlayer(0).getAvailableMeeple(), 6);

    //check score
    game.checkScoreUpdate();
    assertEquals(game.getPlayer(0).getAvailableMeeple(), 7);
    assertTrue(game.getScoreList().get(0) == 4);
  }

  /**
   * Test the scenario when we finish a Monastery
   */
  @Test
  public void testToFinishAMonastery() {
    char type;
    Tile tile;
    //first tile
    type = game.randomDrawCard();
    tile = new Tile(type);
    tile.place(0,0);
    assertTrue(game.placeTile(tile));
    //second
    game.nextPlayer();
    type = 'B';
    tile = new Tile(type);
    tile.place(-1,0);
    assertTrue(game.placeTile(tile));
    game.placeMeeple(tile, Direction.Center);
    //third
    game.nextPlayer();
    type = 'V';
    tile = new Tile(type);
    tile.place( 0, 1);
    assertTrue(game.placeTile(tile));
    //forth
    game.nextPlayer();
    type = 'U';
    tile = new Tile(type);
    tile.place(-1, 1);
    tile.rotate();
    assertTrue(game.placeTile(tile));
    //fifth
    game.nextPlayer();
    type = 'V';
    tile = new Tile(type);
    tile.place(-2, 1);
    tile.rotate();
    tile.rotate();
    tile.rotate();
    assertTrue(game.placeTile(tile));
    //sixth
    game.nextPlayer();
    type = 'U';
    tile = new Tile(type);
    tile.place(-2, 0);
    assertTrue(game.placeTile(tile));
    //seventh
    game.nextPlayer();
    type = 'V';
    tile = new Tile(type);
    tile.place( -2, -1);
    tile.rotate();
    tile.rotate();
    assertTrue(game.placeTile(tile));
    //eighth
    game.nextPlayer();
    type = 'U';
    tile = new Tile(type);
    tile.place(-1, -1);
    tile.rotate();
    assertTrue(game.placeTile(tile));
    //ninth
    game.nextPlayer();
    type = 'V';
    tile = new Tile(type);
    tile.place(0, -1);
    tile.rotate();
    assertTrue(game.placeTile(tile));

    //check score
    game.checkScoreUpdate();
    assertEquals(game.getPlayer(0).getAvailableMeeple(), 7);
    assertTrue(game.getScoreList().get(1) == 9);
  }
}
