package edu.cmu.cs.cs214.hw4.core.Feature;

import edu.cmu.cs.cs214.hw4.core.Segment;

/**
 * Feature: Road
 */
public class Road implements Feature {
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
    return segment.getTiles().size();
  }
  /**
   * @return class name
   */
  @Override
  public String toString() {
    return "Road";
  }
}
