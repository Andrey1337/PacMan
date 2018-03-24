package com.example.andrey.pacman.cutscenes;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.PacmanGame;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Actor;
import com.example.andrey.pacman.entity.Ghost;

public class GameOverCutscene extends Cutscene {

    private PacmanGame pacmanGame;

    private GameOverLabel gameOverLabel;

    public GameOverCutscene(View view, PacmanGame game, Playfield playfield) {
        super(playfield, 2 * 1000);

        pacmanGame = game;

        gameOverLabel = new GameOverLabel(view, playfield,13.65f,16f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        gameOverLabel.onDraw(canvas);
    }

    @Override
    public void startOfScene() {
        for(Ghost ghost : playfield.getGhosts()) {
            ghost.isVisible = false;
        }
        playfield.getPacman().isVisible = false;
    }

    @Override
    public void endOfScene() {
        pacmanGame.gameOver();
    }

    class GameOverLabel extends Actor {

        GameOverLabel(View view,Playfield playfield, float x, float y) {
            super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.game_over), 79, 7, 39, 3, 1, 1, x, y);
        }
    }
}
