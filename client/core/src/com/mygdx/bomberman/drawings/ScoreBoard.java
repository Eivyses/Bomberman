package com.mygdx.bomberman.drawings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.entities.Position;
import com.mygdx.bomberman.entities.mapobject.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** @author Satalia team. */
public class ScoreBoard extends TextDrawable {
  private List<Player> players;

  public ScoreBoard(final SpriteBatch batch) {
    super(batch, "", new Position(3, Gdx.graphics.getHeight() - 30));
    players = new ArrayList<>();
  }

  public void setPlayers(final List<Player> players) {
    this.players = players;
  }

  @Override
  public void update() {
    if (players.isEmpty()) {
      super.setText("Initializing...");
      return;
    }

    final String text =
        players.stream()
            .map(
                $player ->
                    "Player: "
                        + $player.getId().substring(0, 5)
                        + " kills: "
                        + $player.getKillCount()
                        + "\n")
            .collect(Collectors.joining());
    super.setText(text);
  }
}
