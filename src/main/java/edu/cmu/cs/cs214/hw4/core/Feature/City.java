package edu.cmu.cs.cs214.hw4.core.Feature;

import edu.cmu.cs.cs214.hw4.core.Segment;
import edu.cmu.cs.cs214.hw4.core.Tile;

/**
 * Feature: City
 */
public class City implements Feature{
  /** if the given segment is complete or not
   * @param  segment segment we want to check
   * @return result of complete check
   */
  @Override
  public boolean isComplete(Segment segment) {
    return segment.getRequiredConnection().isEmpty();
  }
  /** if the given segment is complete or not
   * @param  segment segment we want to check
   * @return result of complete check
   */
  @Override
  public int calculateScore(Segment segment) {
    if (segment.getMeeples().isEmpty()) {
      return 0;
    }
    int sum = 0;
    for (Tile tile : segment.getTiles()) {
      sum += 1;
      if (tile.isShield()) {
        sum += 1;
      }
    }
    if (isComplete(segment)) {
      sum *= 2;
    }
    return sum;
  }
  /**
   * @return class name
   */
  @Override
  public String toString() {
    return "City";
  }

}
