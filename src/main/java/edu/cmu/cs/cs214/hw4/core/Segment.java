package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.core.Feature.Feature;

import java.util.HashSet;
import java.util.Set;

/**
 * Segment class
 */
public class Segment {
  private boolean status;
  private Set<Tile> tiles;
  private Feature feature;
  private Set<Meeple> meeples;
  private Set<String> requiredConnection;

  /**
   * constructor
   * @param feature segment's feature
   * @param tile    tile
   */
  public Segment(Feature feature, Tile tile) {
    setStatus(false);
    tiles = new HashSet<>();
    tiles.add(tile);
    this.feature = feature;
    meeples = new HashSet<>();
    requiredConnection = new HashSet<>();
  }

  /**
   * merge segment
   * @param segment segment that we need to merge
   */
  public void merge(Segment segment) {
    meeples.addAll(segment.getMeeples());
    tiles.addAll(segment.getTiles());
    requiredConnection.addAll(segment.getRequiredConnection());
  }

  /**
   * is segment complete
   * @return status of the segment
   */
  public boolean isComplete() {
    return status;
  }


  /**
   * use feature to check if the segment is complete
   * @return status of the segment
   */
  public boolean checkForComplete() {
    status = feature.isComplete(this);
    return status;
  }

  /**
   * if two segment can match
   * @param segment segment we want to match
   * @return        result of match check
   */
  public boolean isMatch(Segment segment) {
    return feature.toString().equals(segment.feature.toString());
  }

  /**
   * calculate score for the segment
   * @return score
   */
  public int calculateScore() {
    return getFeature().calculateScore(this);
  }

  /**
   * tiles belong to the segment
   * @return tiles
   */
  public Set<Tile> getTiles() {
    return tiles;
  }

  /**
   * meeples belong to the segment
   * @return meeples
   */
  public Set<Meeple> getMeeples() {
    return meeples;
  }

  /**
   * get the required tile sets to complete the segment
   * @return required tile location to finish the segment
   */
  public Set<String> getRequiredConnection() {
    return requiredConnection;
  }

  /**
   * set the complete status for the segment
   * @param status status
   */
  public void setStatus(boolean status) {
    this.status = status;
  }

  /**
   * segment's feature
   * @return feature
   */
  public Feature getFeature() {
    return feature;
  }

  /**
   * class info
   * @return class info
   */
  @Override
  public String toString() {
    return "Tiles:"+tiles.size()+",Meeples:" + meeples.size()+",status:"+status + ",Feature:" + feature.toString();
  }
}
