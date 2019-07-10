package edu.cmu.cs.cs214.hw4;

import edu.cmu.cs.cs214.hw4.gui.GameBoardPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Main class
 */
public class Main {
  private static final int WIDTH = 1200;
  private static final int HEIGHT = 800;
  /**
   * Main function
   * @param args parameters
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(Main::createAndShowGameBoard);
  }

  /**
   * Show game board
   */
  private static void createAndShowGameBoard() {
    // Create and set-up the window.
    JFrame frame = new JFrame("Carcassonne");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    GameBoardPanel gamePanel = new GameBoardPanel();
    frame.setContentPane(gamePanel);

    // Display the window.
    frame.pack();
    frame.setSize(WIDTH, HEIGHT);
    frame.setVisible(true);
  }
}
