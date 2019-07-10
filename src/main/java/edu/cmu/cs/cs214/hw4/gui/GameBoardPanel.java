package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Direction;
import edu.cmu.cs.cs214.hw4.core.GameController;
import edu.cmu.cs.cs214.hw4.core.Tile;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Game Board Panel
 */
public class GameBoardPanel extends JPanel {
  private final String imagePath = "src/main/resources/Carcassonne.png";
  private MenuPanel menuPanel;
  private BoardPanel boardPanel;
  private JScrollPane scrollPane;
  private BufferedImage[] images;
  private GameController game;
  private Tile currTile;
  private int rotateTimes;
  private boolean isEnd;

  /**
   * Constructor
   */
  public GameBoardPanel() {
    newGame();
  }

  /**
   * Draw a random tile from the tile stack
   * @return Tile
   */
  private boolean getNextTile() {
    Character type = game.randomDrawCard();
    if (type == null) {
      isEnd = true;
      endOfGame();
      return false;
    }
    currTile = new Tile(type);
    rotateTimes = 0;
    menuPanel.setPreviewImage(images[currTile.getType()-'A']);
    return true;
  }

  /**
   * Execute it when the game is end. It will update the scores and announce the winners.
   */
  private void endOfGame(){
    game.endGame();
    List<Integer> scores = game.getScoreList();
    System.out.println(scores.toString());
    int max = 0;
    for (int score : scores) {
      max = Math.max(max, score);
    }
    String str = "Winner is: ";
    for (int i = 0; i < scores.size(); i++) {
      menuPanel.getScoreBoardPanel().setScore(i, String.valueOf(scores.get(i)));
      if (scores.get(i) == max) {
        char ch = (char)(i + 'A');
        str += ch + ",";
      }
    }
    str = str.substring(0, str.length()-1);
    JOptionPane.showMessageDialog(null, str, "End of Game!",JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Initialize all of the variables at the beginning of a new game
   */
  private void newGame(){
    isEnd = false;
    removeAll();
    game = new GameController();
    menuPanel = new MenuPanel();
    boardPanel = new BoardPanel(imageButtonAction());
    readImages();
    getNextTile();
    setLayout(new BorderLayout());
    add(menuPanel, BorderLayout.NORTH);
    scrollPane = new JScrollPane();
    scrollPane.setViewportView(boardPanel);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    add(scrollPane, BorderLayout.CENTER);
    newGameAction();
    addPlayerAction();
    rotateAction();
    updateUI();
  }

  /**
   * Add action listener for "new game" button
   */
  private void newGameAction() {
    menuPanel.getNewGameButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        newGame();
      }
    });
  }

  /**
   * Add action listener for "add player" button
   */
  private void addPlayerAction(){
    menuPanel.getAddPlayerButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menuPanel.getScoreBoardPanel().addPlayer()) {
          JOptionPane.showMessageDialog(null, "Can't add more player!", "Notification",JOptionPane.WARNING_MESSAGE);
          return;
        }
        game.joinNewPlayer();
      }
    });
  }

  /**
   * Add action listener for "rotate" button
   */
  private void rotateAction(){
    menuPanel.getRotateButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        menuPanel.rotateImage();
        rotateTimes++;
      }
    });
  }

  /**
   * Create the action listener for the image button.
   * Process issues including placing the tile and placing the meeple
   * @return Action listener
   */
  private ActionListener imageButtonAction(){
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isEnd) {
          endOfGame();
          return;
        }
        if (!game.canStartGame()) {
          JOptionPane.showMessageDialog(null, "Need at least two players!", "Notification",JOptionPane.WARNING_MESSAGE);
          return;
        }
        JButton button = (JButton) e.getSource();
        int[] pos = boardPanel.getRelativePos(button);
        currTile.place(pos[0], pos[1]);
        for (int i = 0; i < rotateTimes; i++) {
          currTile.rotate();
        }
        boolean res = game.placeTile(currTile);
        if (!res) {
          currTile = new Tile(currTile.getType());
          JOptionPane.showMessageDialog(null, "Invalid Placement!", "Notification",JOptionPane.WARNING_MESSAGE);
          return;
        }
        boardPanel.placeTile(menuPanel.getImage(), button, currTile);
        //handle meeple
        Direction direction = processMeeple();
        if (direction != null) {
          boardPanel.placeMeeple(button, menuPanel.getImage(), menuPanel.getScoreBoardPanel().getPlayerColor(), direction);
        }
        //update score and meeple number
        checkScore();
        checkImageRecovery();

        getNextTile();
        game.nextPlayer();
        menuPanel.nextPlayer();
      }
    };
  }

  /**
   * Check and update the score board
   */
  private void checkScore() {
    game.checkScoreUpdate();
    ScoreBoardPanel board = menuPanel.getScoreBoardPanel();
    for (int i = 0; i < game.getNumOfPlayer(); i++) {
      board.setScore(i, String.valueOf(game.getPlayer(i).getScore()));
      board.setMeeple(i, String.valueOf(game.getPlayer(i).getAvailableMeeple()));
    }
    updateUI();
  }

  /**
   * Choose a direction in the tile to place the meeple
   * @return Direction
   */
  private Direction processMeeple(){
    if (game.getCurrPlayer().getAvailableMeeple() == 0) {
      return null;
    }
    //check if need meeple
    Set<String> features = new HashSet<>();
    features.add("Do not place meeple");
    if (currTile.getSegments().get(Direction.North) != null) {
      features.add("North");
    }
    if (currTile.getSegments().get(Direction.South) != null) {
      features.add("South");
    }
    if (currTile.getSegments().get(Direction.West) != null) {
      features.add("West");
    }
    if (currTile.getSegments().get(Direction.East) != null) {
      features.add("East");
    }
    if (currTile.getSegments().get(Direction.Center) != null) {
      features.add("Center");
    }

    Object[] options = new Object[features.size()];
    int idx = 0;
    for (String option : features) {
      options[idx++] = option;
    }
    String str = (String) JOptionPane.showInputDialog(null,"Where to place meeple?:\n", "Add Meeple?", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    if (str == null) {
      return null;
    }
    if (str.equals("North")) {
      game.placeMeeple(currTile, Direction.North);
      return Direction.North;
    }
    else if (str.equals("South")) {
      game.placeMeeple(currTile, Direction.South);
      return Direction.South;
    }
    else if (str.equals("West")){
      game.placeMeeple(currTile, Direction.West);
      return Direction.West;
    }
    else if (str.equals("East")) {
      game.placeMeeple(currTile, Direction.East);
      return Direction.East;
    }
    else if (str.equals("Center")) {
      game.placeMeeple(currTile, Direction.Center);
      return Direction.Center;
    }
    else
      return null;
  }


  /**
   * Read images from the given resources
   */
  private void readImages() {
    try {
      final int numOfImages = 24;
      images = new BufferedImage[numOfImages];
      BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
      final int numOfRow = 4;
      final int numOfCol = 6;
      final int unit = 90;
      for (int i = 0; i < numOfRow; i++) {
        for (int j = 0; j < numOfCol; j++) {
          images[i*numOfCol+j] = bufferedImage.getSubimage(j*unit, i*unit, unit, unit);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * If a segment is finished, we need to remove the meeple(circle) from the tile.
   * This function is used to replace the current image(with circle) with the old image(without circle)
   */
  private void checkImageRecovery() {
    Map<Integer, Map<Integer, Tile>> tiles = game.getTiles();
    for (int x : tiles.keySet()) {
      for (int y : tiles.get(x).keySet()) {
        Tile tile = tiles.get(x).get(y);
        Direction posOfMeeple = tile.getPosOfMeeple();
        if (posOfMeeple != null && tile.getSegments().get(posOfMeeple).isComplete()) {
          boardPanel.recoverFromOldImage(tile);
        }
      }
    }
  }

  /**
   * class info
   * @return class info
   */
  @Override
  public String toString() {
    return "Game Board Panel";
  }

}
