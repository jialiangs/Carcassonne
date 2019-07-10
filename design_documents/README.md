## Guidance
### Button Introduction
- **Rotate Tile**: After clicking this button, you can see that the preview image in the leftside will turn 90 degree clockwise
- **New Game**: After clicking this button, it will clear all of the game data, clean the game board, and start a new game.
- **Add Player**: After clicking this button, you can see that there is a new player in the score board. But when the number of player arrives its limitation which is 5, it will notified you that you cannot add more player.

### Score Board Introduction
- Different players have different colors.
- The current player who are placing the tile is marked by red border.
- The score board shows scores and available meeples for every player. 

### Game Board Introduction
- The game board will only shows placed tiles and their adjacent blocks since only these blocks are available for players to place the new tile
- If the number of current player is less than 2, the game will notify you that, a game needs at least 2 players, so you have to add new players.
- After you press the button and place a tile, if this placementt is invalid, it will notify you, and the player needs to find a new position to place. If this placement is valid, the game board will expand available blocks and update scores if there are newly complete segments.
- After you finish a valid placement, you can see a dialog box which ask you if you want to place a meeple, and in which direction of the image you want to place this meeple. After you place the meeple, you can see a circle appears in the image which represent your meeple. And this circle will disappear if its related segment is complete. If player's meeples are used up, then the system will not ask you to choose where to place the meeple.

### End of Game
- If all of the tiles are used up, the game will pop up a dialog box and shows the winner (one or more). Then if you close the dialog box, you can still see the score board, and all of the game data will not removed until you click the "new game" button.


### Design Changes
- I re-designed all of the design documents in Milestone A, and hope that you can re-evaluate them.