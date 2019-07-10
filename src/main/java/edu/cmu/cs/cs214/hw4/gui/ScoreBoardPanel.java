package edu.cmu.cs.cs214.hw4.gui;


import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * Score Board Panel
 */
public class ScoreBoardPanel extends JPanel {
  private int numOfPlayer = 0;
  private static final int MAX_NUM_PLAYERS = 5;
  private JLabel[] players;
  private JLabel[] meeples;
  private JLabel[] scores;
  private Color[] colors;
  private int currPlayer = 0;

  /**
   * Constructor
   */
  public ScoreBoardPanel() {
    final int numOfRow = 3;
    final int numOfCol = 6;
    setLayout(new GridLayout(numOfRow, numOfCol));
    players = new JLabel[MAX_NUM_PLAYERS];
    meeples = new JLabel[MAX_NUM_PLAYERS];
    scores = new JLabel[MAX_NUM_PLAYERS];
    colors = new Color[]{Color.black, Color.red, Color.ORANGE, Color.blue, Color.magenta};

    JLabel boardLabel = new JLabel("Player", JLabel.CENTER);
    boardLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    add(boardLabel);
    for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
      players[i] = new JLabel("", JLabel.CENTER);
      players[i].setForeground(colors[i]);
      players[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
      add(players[i]);
    }
    JLabel scoreLabel = new JLabel("Score", JLabel.CENTER);
    scoreLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    add(scoreLabel);
    for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
      scores[i] = new JLabel("", JLabel.CENTER);
      scores[i].setForeground(colors[i]);
      scores[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
      add(scores[i]);
    }
    JLabel meepleLabel = new JLabel("Meeple", JLabel.CENTER);
    meepleLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    add(meepleLabel);
    for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
      meeples[i] = new JLabel("", JLabel.CENTER);
      meeples[i].setForeground(colors[i]);
      meeples[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
      add(meeples[i]);
    }
  }

  /**
   * Turn to next player
   */
  public void nextPlayer(){
    players[currPlayer].setBorder(BorderFactory.createLineBorder(Color.black, 1));
    currPlayer = (currPlayer + 1) % numOfPlayer;
    players[currPlayer].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
  }

  /**
   * Add new player. If the number of current players arrives its maximum capacity, it returns false
   * @return the result of adding new player
   */
  public boolean addPlayer() {
    if (numOfPlayer == MAX_NUM_PLAYERS) {
      return false;
    }
    char id = (char)('A'+numOfPlayer);
    setPlayer(numOfPlayer, String.valueOf(id));
    setScore(numOfPlayer, "0");
    setMeeple(numOfPlayer, "7");
    numOfPlayer++;
    if (numOfPlayer == 1) {
      players[currPlayer].setBorder(BorderFactory.createLineBorder(Color.red, 1));
    }
    return true;
  }

  /**
   * Return the color for a given player
   * @return player's color
   */
  public Color getPlayerColor() {
    return colors[currPlayer];
  }

  /**
   * Set the available number of meeple in the score board
   * @param idx index of the player
   * @param num number of the meeple
   */
  public void setMeeple(int idx, String num) {
    meeples[idx].setText(num);
  }

  /**
   * Set the score in the score board
   * @param idx   index of the player
   * @param score score of the player
   */
  public void setScore(int idx, String score) {
    scores[idx].setText(score);
  }

  /**
   * Set the name of the player in the score board
   * @param idx  index of the player
   * @param name name of the player
   */
  public void setPlayer(int idx, String name) {
    players[idx].setText(name);
  }

  /**
   * class info
   * @return class info
   */
  @Override
  public String toString() {
    return "Score Board Panel";
  }

}
