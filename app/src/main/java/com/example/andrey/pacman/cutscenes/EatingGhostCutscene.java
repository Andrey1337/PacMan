package com.example.andrey.pacman.cutscenes;

import android.graphics.*;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Ghost;
import com.example.andrey.pacman.entity.Point;

public class EatingGhostCutscene extends Cutscene {

    private Ghost ghostWasEaten;
    private Bitmap ghostPoints;

    private int pointsWidth, pointsHeight;
    private int X_OFFSET, Y_OFFSET;

    private int frameWidth, frameHeight;

    private int ghostMultiplyer;
    public EatingGhostCutscene(View view, Playfield playfield, Ghost ghost, int ghostMultiplyer) {
        super(playfield, 1000);

        this.ghostMultiplyer = ghostMultiplyer;
        int framesCount = 4;

        frameWidth = 18 * 8;
        frameHeight = 9 * 8;

        ghostPoints = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.ghost_points),
                frameWidth * framesCount,
                frameHeight,false);

        X_OFFSET = (int)(8 / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
        Y_OFFSET = (int)(4 / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());

        pointsWidth = (int)(BitmapFactory.decodeResource(view.getResources(), R.mipmap.ghost_points).getWidth() * playfield.scale) / framesCount;
        pointsHeight = (int)(BitmapFactory.decodeResource(view.getResources(), R.mipmap.ghost_points).getHeight() * playfield.scale);

        ghostWasEaten = ghost;
    }


    @Override
    public void onDraw(Canvas canvas) {

        Point ghostPoint = new Point(ghostWasEaten.getX(), ghostWasEaten.getY());

        float left = playfield.X_OFFSET + ghostPoint.floatX * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - X_OFFSET;
        float top = playfield.Y_OFFSET + ghostPoint.floatY * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - Y_OFFSET  + playfield.STARTPOS_Y;

        RectF whereToDraw = new RectF(left , top, left + pointsWidth, top + pointsHeight);
        canvas.drawBitmap(ghostPoints, new Rect(frameWidth * ghostMultiplyer,0,frameWidth * (ghostMultiplyer + 1),frameHeight), whereToDraw, null);

    }

    @Override
    public void startOfScene() {
        playfield.getPacman().isVisible = false;
        ghostWasEaten.isVisible = false;
    }

    @Override
    public void endOfScene() {
        playfield.getPacman().isVisible = true;
        ghostWasEaten.isVisible = true;
    }
}
