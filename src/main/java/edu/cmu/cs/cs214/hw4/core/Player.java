package edu.cmu.cs.cs214.hw4.core;

import java.util.Stack;

/**
 * Player
 */
public class Player {
  private final int numMeeple = 7;
  private Stack<Meeple> meeples;
  private int id;
  private int score;

  /**
   * constructor
   * @param id id of the player
   */
  public Player(int id) {
    this.id = id;
    meeples = new Stack<>();
    for (int i = 0; i < numMeeple; i++) {
      meeples.add(new Meeple(id));
    }
    score = 0;
  }

  /**
   * find the usable number of meeples
   * @return how many meeples are available now
   */
  public int getAvailableMeeple() {
    return meeples.size();
  }

  /**
   * update the score for the player
   * @param delta change of the score
   */
  public void updateScore(int delta) {
    score += delta;
  }

  /**
   * get score for the player
   * @return score
   */
  public int getScore() {
    return score;
  }

  /**
   * send back the meeple to the related player
   * @param meeple meeple
   */
  public void getMeepleBack(Meeple meeple) {
    meeples.push(meeple);
  }

  /**
   * pop the meeple from the available meeple stack
   * @return meeple
   */
  public Meeple placeMeeple() {
    if (meeples.size() < 0) {
      return null;
    }
    return meeples.pop();
  }

  /**
   * get the id of the player
   * @return player id
   */
  public int getId() {
    return id;
  }

  /**
   * class info
   * @return class information
   */
  @Override
  public String toString() {
    return "Player:" + id;
  }
}
