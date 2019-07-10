package edu.cmu.cs.cs214.hw4.core.Feature;

import edu.cmu.cs.cs214.hw4.core.Segment;

/**
 * Feature: Monastery
 */
public class Monastery implements Feature{
  /** if the given segment is complete or not
   * @param  segment segment we want to check
   * @return result of complete check
   */
  @Override
  public boolean isComplete(Segment segment) {
    final int capacity = 9;
    if (segment.getTiles().size() < capacity)
      return false;
    return true;
  }
  /** if the given segment is complete or not
   * @param  segment segment we want to check
   * @return result of complete check
   */
  @Override
  public int calculateScore(Segment segment) {
    if (segment.getMeeples().size() == 0) {
      return 0;
    }
    return segment.getTiles().size();
  }
  /**
   * @return class name
   */
  @Override
  public String toString() {
    return "Monastery";
  }
}
