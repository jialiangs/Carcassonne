package edu.cmu.cs.cs214.hw4.core.Feature;

import edu.cmu.cs.cs214.hw4.core.Segment;

/**
 * Feature is used to represent different type of segments
 */
public interface Feature {
  /** if the given segment is complete or not
   * @param  segment segment we want to check
   * @return result of complete check
   */
  boolean isComplete(Segment segment);

  /** if the given segment is complete or not
   * @param  segment segment we want to check
   * @return result of complete check
   */
  int calculateScore(Segment segment);

  /**
   * @return class name
   */
  String toString();
}
