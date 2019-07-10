package edu.cmu.cs.cs214.hw4.core;

/**
 * Meeple
 */
public class Meeple {
  private int id;

  /**
   * constructor
   * @param id id of the player
   */
  public Meeple(int id) {
    this.id = id;
  }

  /**
   * which player the meeple belongs to
   * @return id of the player
   */
  public int getId() {
    return id;
  }

  /**
   * @return class name
   */
  @Override
  public String toString() {
    return "Meeple:" + id;
  }
}
