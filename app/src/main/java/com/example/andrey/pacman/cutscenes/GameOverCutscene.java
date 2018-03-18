package com.example.andrey.pacman.cutscenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.GameView;
import com.example.andrey.pacman.PacmanGame;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Ghost;
import com.example.andrey.pacman.entity.Point;

public class GameOverCutscene extends Cutscene {

    private Bitmap gameOver;
    private int X_OFFSET;
    private int Y_OFFSET;

    private Point drawPoint;

    private PacmanGame pacmanGame;


    public GameOverCutscene(View view, PacmanGame game, Playfield playfield) {
        super(playfield, 2 * 1000);

        pacmanGame = game;

        X_OFFSET = (int)(39 / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
        Y_OFFSET = (int)(3 / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());

        drawPoint = new Point(13.65f,16f);
        gameOver = BitmapFactory.decodeResource(view.getResources(), R.mipmap.game_over);
        gameOver = Bitmap.createScaledBitmap(gameOver, (int) (gameOver.getWidth() * playfield.scale),
                (int)(gameOver.getHeight() * playfield.scale), false);

    }

    @Override
    public void onDraw(Canvas canvas) {
        float left = playfield.X_OFFSET + drawPoint.floatX * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - X_OFFSET;

        float top = playfield.Y_OFFSET + drawPoint.floatY * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - Y_OFFSET  + playfield.STARTPOS_Y;

        canvas.drawBitmap(gameOver, left, top, null);
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
}
