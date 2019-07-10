package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.core.Feature.City;
import edu.cmu.cs.cs214.hw4.core.Feature.Monastery;
import edu.cmu.cs.cs214.hw4.core.Feature.Road;

import java.util.HashMap;
import java.util.Map;

/**
 * Tile
 */
public class Tile {
  private Map<Direction, Segment> segments;
  private boolean isShield;
  private int x;
  private int y;
  private char type;
  private Direction posOfMeeple;

  /**
   * constructor
   * @param type type of the tile
   */
  public Tile(char type){
    this.type = type;
    isShield = false;
    segments = new HashMap<>();
  }

  /**
   * place the tile into the board
   * @param x relative coordinate x
   * @param y relative coordinate y
   */
  public void place(int x, int y) {
    this.x = x;
    this.y = y;
    initTile();
  }

  /**
   * place the meeple into the tile
   * @param direction in which direction of the tile we want to place the meeple
   * @param meeple    meeple we want to place
   */
  public void placeMeeple(Direction direction, Meeple meeple) {
    posOfMeeple = direction;
    segments.get(direction).getMeeples().add(meeple);
  }

  /**
   * clockwise 90 degree
   */
  public void rotate() {
    for (Direction d : segments.keySet()) {
      segments.get(d).getRequiredConnection().clear();
    }

    Segment tmp1 = segments.get(Direction.East);
    if (segments.containsKey(Direction.North)) {
      segments.put(Direction.East, segments.get(Direction.North));
      Segment segment = segments.get(Direction.East);
      segment.getRequiredConnection().add(combine(x+1,y));
    } else {
      segments.remove(Direction.East);
    }
    Segment tmp2 = segments.get(Direction.South);
    if (tmp1 != null) {
      segments.put(Direction.South, tmp1);
      Segment segment = segments.get(Direction.South);
      segment.getRequiredConnection().add(combine(x,y-1));
    } else {
      segments.remove(Direction.South);
    }
    Segment tmp3 = segments.get(Direction.West);
    if (tmp2 != null) {
      segments.put(Direction.West, tmp2);
      Segment segment = segments.get(Direction.West);
      segment.getRequiredConnection().add(combine(x-1,y));
    } else {
      segments.remove(Direction.West);
    }
    if (tmp3 != null) {
      segments.put(Direction.North, tmp3);
      Segment segment = segments.get(Direction.North);
      segment.getRequiredConnection().add(combine(x,y+1));
    } else {
      segments.remove(Direction.North);
    }
  }

  /**
   * convert the coordinates into the string
   * @param x coordinate axis x
   * @param y coordinate axis y
   * @return  combined string
   */
  private String combine(int x, int y) {
    return x+","+y;
  }

  /**
   * create new segments for the tile
   * @param d       direction of the segment
   * @param segment segment
   */
  private void createNewSegment(Direction d, Segment segment) {
    getSegments().put(d, segment);
    if (d == Direction.North) {
      segment.getRequiredConnection().add(combine(x, y+1));
    }
    if (d == Direction.South) {
      segment.getRequiredConnection().add(combine(x, y-1));
    }
    if (d == Direction.West) {
      segment.getRequiredConnection().add(combine(x-1, y));
    }
    if (d == Direction.East) {
      segment.getRequiredConnection().add(combine(x+1, y));
    }
  }

  /**
   * initialize the tile
   */
  private void initTile() {
    Segment segment;
    switch (getType()) {
      case 'A':
        createNewSegment(Direction.Center, new Segment(new Monastery(), this));
        createNewSegment(Direction.South, new Segment(new Road(), this));
        break;
      case 'B':
        getSegments().put(Direction.Center, new Segment(new Monastery(), this));
        break;
      case 'C':
        isShield = true;
        segment = new Segment(new City(), this);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.South, segment);
        createNewSegment(Direction.East, segment);
        break;
      case 'D':
        segment = new Segment(new Road(), this);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.South, segment);
        createNewSegment(Direction.East, new Segment(new City(), this));
        break;
      case 'E':
        createNewSegment(Direction.North, new Segment(new City(), this));
        break;
      case 'F':
        isShield = true;
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.East, segment);
        break;
      case 'G':
        segment = new Segment(new City(), this);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.South, segment);
        break;
      case 'H':
        createNewSegment(Direction.West, new Segment(new City(), this));
        createNewSegment(Direction.East, new Segment(new City(), this));
        break;
      case 'I':
        createNewSegment(Direction.South, new Segment(new City(), this));
        createNewSegment(Direction.East, new Segment(new City(), this));
        break;
      case 'J':
        createNewSegment(Direction.North, new Segment(new City(), this));
        segment = new Segment(new Road(), this);
        createNewSegment(Direction.East, segment);
        createNewSegment(Direction.South, segment);
        break;
      case 'K':
        segment = new Segment(new Road(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.East, new Segment(new City(), this));
        break;
      case 'L':
        createNewSegment(Direction.North, new Segment(new Road(), this));
        createNewSegment(Direction.West, new Segment(new Road(), this));
        createNewSegment(Direction.South, new Segment(new Road(), this));
        createNewSegment(Direction.East, new Segment(new City(), this));
        break;
      case 'M':
        isShield = true;
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        break;
      case 'N':
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        break;
      case 'O':
        isShield = true;
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        segment  =new Segment(new Road(), this);
        createNewSegment(Direction.South, segment);
        createNewSegment(Direction.East, segment);
        break;
      case 'P':
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        segment  =new Segment(new Road(), this);
        createNewSegment(Direction.South, segment);
        createNewSegment(Direction.East, segment);
        break;
      case 'Q':
        isShield = true;
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.East, segment);
        break;
      case 'R':
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.East, segment);
        break;
      case 'S':
        isShield = true;
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.East, segment);
        createNewSegment(Direction.South, new Segment(new Road(), this));
        break;
      case 'T':
        segment = new Segment(new City(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.East, segment);
        createNewSegment(Direction.South, new Segment(new Road(), this));
        break;
      case 'U':
        segment = new Segment(new Road(), this);
        createNewSegment(Direction.North, segment);
        createNewSegment(Direction.South, segment);
        break;
      case 'V':
        segment = new Segment(new Road(), this);
        createNewSegment(Direction.West, segment);
        createNewSegment(Direction.South, segment);
        break;
      case 'W':
        createNewSegment(Direction.West, new Segment(new Road(), this));
        createNewSegment(Direction.South, new Segment(new Road(), this));
        createNewSegment(Direction.East, new Segment(new Road(), this));
        break;
      case 'X':
        createNewSegment(Direction.North, new Segment(new Road(), this));
        createNewSegment(Direction.West, new Segment(new Road(), this));
        createNewSegment(Direction.South, new Segment(new Road(), this));
        createNewSegment(Direction.East, new Segment(new Road(), this));
        break;
      default:
        break;
    }
  }

  /**
   * check if tile contains shield
   * @return if tile contains shield
   */
  public boolean isShield() {
    return isShield;
  }

  /**
   * get all of the segments this tile belongs to
   * @return segment map
   */
  public Map<Direction, Segment> getSegments() {
    return segments;
  }

  /**
   * get x
   * @return coordinate axis x
   */
  public int getX() {
    return x;
  }

  /**
   * get y
   * @return coordinate axis y
   */
  public int getY() {
    return y;
  }

  /**
   * get the coordinate
   * @return coordinate
   */
  public String getCoordinate() {
    return x+","+y;
  }

  /**
   * class info
   * @return class info
   */
  @Override
  public String toString() {
    return "("+x+","+y+"):"+ getType();
  }

  /**
   * get the type
   * @return type of the tile
   */
  public char getType() {
    return type;
  }

  /**
   * get the relative position of the meeple in this tile
   * @return position of the meeple
   */
  public Direction getPosOfMeeple() {
    return posOfMeeple;
  }
}
