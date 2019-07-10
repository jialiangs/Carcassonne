package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Menu Panel
 */
public class MenuPanel extends JPanel {
  private JButton previewImage;
  private BufferedImage image;
  private GridBagLayout gridbag;
  private GridBagConstraints constraints;
  private JButton newGameButton;
  private JButton addPlayerButton;
  private JButton rotateButton;
  private ScoreBoardPanel scoreBoardPanel;
  private JPanel operationPanel;

  /**
   * Constructor
   */
  public MenuPanel(){
    setBorder(BorderFactory.createLineBorder(Color.black, 1));
    previewImage = new JButton();
    add(previewImage, BorderLayout.NORTH);
    operationPanel = new JPanel();
    //edit operationPanel
    gridbag = new GridBagLayout();
    constraints = new GridBagConstraints();
    operationPanel.setLayout(gridbag);
    constraints.fill = GridBagConstraints.BOTH;
    //button
    constraints.gridheight = 1;
    constraints.gridwidth = 2;
    newGameButton = new JButton("New Game");
    addPlayerButton = new JButton("Add Player");
    rotateButton = new JButton("Rotate Tile");
    addComponent(operationPanel, getRotateButton());
    addComponent(operationPanel, getNewGameButton());
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    addComponent(operationPanel, getAddPlayerButton());
    //score board
    final int tmpHeight = 3;
    constraints.gridheight = tmpHeight;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    scoreBoardPanel = new ScoreBoardPanel();
    addComponent(operationPanel, getScoreBoardPanel());
    add(operationPanel, BorderLayout.CENTER);
  }

  /**
   * Show the image of the current tile in the GUI
   * @param tileImage image of the current tile
   */
  public void setPreviewImage(BufferedImage tileImage) {
    image = tileImage;
    ImageIcon icon = new ImageIcon(getImage());
    previewImage.setIcon(icon);
  }

  /**
   * rotate the image 90 degrees by clockwise
   */
  public void rotateImage() {
    int weight = getImage().getWidth();
    int height = getImage().getHeight();
    AffineTransform at = AffineTransform.getQuadrantRotateInstance(1, weight / 2.0, height / 2.0);
    AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    BufferedImage dest = new BufferedImage(weight, height, getImage().getType());
    op.filter(getImage(), dest);
    setPreviewImage(dest);
  }

  /**
   * Add components into the panel
   * @param panel     parent panel
   * @param component component we want to add to the panel
   */
  private void addComponent(JPanel panel, Component component) {
    gridbag.setConstraints(component, constraints);
    panel.add(component);
  }

  /**
   * It's next player's turn to play the game
   */
  public void nextPlayer(){
    getScoreBoardPanel().nextPlayer();
  }

  /**
   * get the image of the current tile
   * @return image of the current tile
   */
  public BufferedImage getImage() {
    return image;
  }

  /**
   * Get the score board panel
   * @return score board panel
   */
  public ScoreBoardPanel getScoreBoardPanel() {
    return scoreBoardPanel;
  }

  /**
   * class info
   * @return class info
   */
  @Override
  public String toString() {
    return "Menu Panel";
  }

  /**
   * Get new game button
   * @return new game button
   */
  public JButton getNewGameButton() {
    return newGameButton;
  }

  /**
   * Get add player button
   * @return add player button
   */
  public JButton getAddPlayerButton() {
    return addPlayerButton;
  }

  /**
   * Get rotate button
   * @return rotate button
   */
  public JButton getRotateButton() {
    return rotateButton;
  }
}
