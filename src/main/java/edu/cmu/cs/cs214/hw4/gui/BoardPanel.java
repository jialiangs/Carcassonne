package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Direction;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Board Panel
 */
public class BoardPanel extends JPanel {
  private static final int INIT_WIDTH = 1200;
  private static final int INIT_HEIGHT = 600;
  private static final int DELTA = 200;
  private static final int PIXEL = 100;
  private int width = INIT_WIDTH;
  private int height = INIT_HEIGHT;
  private Map<JButton, Tile> buttonTileMap;
  private Map<Integer, Map<Integer, JButton>> buttonMap;
  private Map<JButton, BufferedImage> oldImageMap;
  private JButton centerButton;
  private ActionListener actionListener;

  /**
   * constructor
   * @param actionListener listener for the tile button
   */
  public BoardPanel(ActionListener actionListener){
    setPreferredSize(new Dimension(width, height));
    updateUI();
    buttonTileMap = new HashMap<>();
    buttonMap = new HashMap<>();
    oldImageMap = new HashMap<>();
    this.actionListener = actionListener;
    int centerX = width /2;
    int centerY = height /2;
    setLayout(null);
    centerButton = createButton(centerX, centerY);
  }

  /**
   * After placing a tile, add adjacent available button into the game,
   * so that the user can place tile in these buttons in the future.
   * @param x coordinate x
   * @param y coordinate y
   */
  private void addNewAvailablePos(int x, int y) {
    if (getButton(x- PIXEL, y) == null) {
      createButton(x- PIXEL, y);
    }
    if (getButton(x+ PIXEL, y) == null) {
      createButton(x+ PIXEL, y);
    }
    if (getButton(x, y+ PIXEL) == null) {
      createButton(x, y+ PIXEL);
    }
    if (getButton(x, y- PIXEL) == null) {
      createButton(x, y- PIXEL);
    }
    if (x- PIXEL == 0) {
      enlargeBoard("WEST");
    }
    if (y- PIXEL == 0) {
      enlargeBoard("North");
    }
    if (x + 2 * PIXEL == width) {
      enlargeBoard("EAST");
    }
    if (y + 2 * PIXEL == height) {
      enlargeBoard("SOUTH");
    }
    updateUI();
  }

  /**
   * enlarge the board when the current board is not large enough
   * @param direction enlarge board in which direction
   */
  private void enlargeBoard(String direction){
    direction = direction.toUpperCase();
    int dX = 0;
    int dY = 0;
    if (direction.equals("NORTH")){
      height += DELTA;
      dY = DELTA;
    } else if (direction.equals("SOUTH")) {
      height += DELTA;
    } else if (direction.equals("WEST")) {
      width += DELTA;
      dX += DELTA;
    } else if (direction.equals("EAST")) {
      width += DELTA;
    }
    setPreferredSize(new Dimension(width, height));
    if (dX != 0 || dY != 0) {
      Map<Integer, Map<Integer, JButton>> newMap = new HashMap<>();
      for (int x : buttonMap.keySet()){
        for (int y : buttonMap.get(x).keySet()) {
          JButton button = buttonMap.get(x).get(y);
          button.setBounds(button.getX()+dX, button.getY()+dY, PIXEL, PIXEL);
          insertButton(button.getX(), button.getY(), button, newMap);
        }
      }
      buttonMap = newMap;
    }
    updateUI();
  }

  /**
   * Given the coordinate and return the button in that position
   * @param x coordinate x
   * @param y coordinate y
   * @return  button
   */
  private JButton getButton(int x, int y) {
    if (!buttonMap.containsKey(x)) {
      return null;
    }
    if (!buttonMap.get(x).containsKey(y)) {
      return null;
    }
    return buttonMap.get(x).get(y);
  }

  /**
   * Insert the button into the index map
   * @param x         coordinate
   * @param y         coordinate
   * @param button    button needs to be inserted
   * @param buttonMap map
   */
  private void insertButton(int x, int y, JButton button, Map<Integer, Map<Integer, JButton>> buttonMap) {
    //add button to the map
    if (!buttonMap.containsKey(x)) {
      buttonMap.put(x, new HashMap<>());
    }
    buttonMap.get(x).put(y, button);
  }

  /**
   * create a new button in the given place
   * @param x coordinate
   * @param y coordinate
   * @return  button
   */
  private JButton createButton(int x, int y) {
    JButton button = new JButton("Place Tile");
    button.setBounds(new Rectangle(x, y, PIXEL, PIXEL));
    button.addActionListener(actionListener);
    add(button);
    insertButton(x, y, button, buttonMap);
    return button;
  }

  /**
   * Get the relative position for a given button
   * @param button button
   * @return       relative position of the button
   */
  public int[] getRelativePos(JButton button) {
    int x = (button.getX() - centerButton.getX()) / PIXEL;
    int y = -1 * (button.getY() - centerButton.getY()) / PIXEL;
    return new int[]{x, y};
  }

  /**
   * Place the tile into the chosen button and set the image.
   * @param image   image of the tile
   * @param button  button
   * @param tile    tile we want to place
   */
  public void placeTile(BufferedImage image, JButton button, Tile tile){
    buttonTileMap.put(button, tile);
    button.setText("");
    button.setHorizontalTextPosition(SwingConstants.CENTER);
    ImageIcon icon = new ImageIcon(image);
    button.setIcon(icon);
    addNewAvailablePos(button.getX(), button.getY());
  }

  /**
   * Place meeple into the tile
   * @param button     button
   * @param src        image
   * @param color      color of the player
   * @param direction  in which direction we want to place the meeple
   */
  public void placeMeeple(JButton button, BufferedImage src, Color color, Direction direction) {
    final int radius = 10;
    final int dis = PIXEL / 3;
    int x = PIXEL /2;
    int y = PIXEL /2;
    if (direction == Direction.North) {
      y -= dis;
    }
    if (direction == Direction.South) {
      y += dis;
    }
    if (direction == Direction.West) {
      x -= dis;
    }
    if (direction == Direction.East) {
      x += dis;
    }

    oldImageMap.put(button, src);
    BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

    Graphics2D g = (Graphics2D) dest.getGraphics();
    g.drawImage(src, 0, 0, null);
    g.setColor(color);
    g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    g.dispose();
    ImageIcon icon = new ImageIcon(dest);
    button.setIcon(icon);
  }

  /**
   * When the meeples return to the players, we need to remove the circle of the image.
   * This function implements this by replacing the current image with the previous image without circle.
   * @param tile tile
   */
  public void recoverFromOldImage(Tile tile) {
    int x = centerButton.getX();
    int y = centerButton.getY();
    x += PIXEL * tile.getX();
    y -= PIXEL * tile.getY();
    JButton button = getButton(x, y);
    if (oldImageMap.containsKey(button)) {
      ImageIcon icon = new ImageIcon(oldImageMap.get(button));
      button.setIcon(icon);
    }
  }

  /**
   * class info
   * @return class info
   */
  @Override
  public String toString() {
    return "Board Panel";
  }

}
