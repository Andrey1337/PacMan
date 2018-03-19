package com.example.andrey.pacman.cutscenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Ghost;
import com.example.andrey.pacman.entity.Point;

public class EatingGhostScene extends Cutscene {

    private Ghost ghostWasEaten;
    private Bitmap ghostPoints;

    public EatingGhostScene(View view, Playfield playfield, Ghost ghost, int ghostMultiplyer) {
        super(playfield, 1000);

        ghostPoints = BitmapFactory.decodeResource(view.getResources(), R.mipmap.ghost_points);
        ghostPoints = Bitmap.createScaledBitmap(ghostPoints, (int) (ghostPoints.getWidth() * playfield.scale),
                (int)(ghostPoints.getHeight() * playfield.scale), false);


        ghostWasEaten = ghost;
    }

    @Override
    public void onDraw(Canvas canvas) {

        Point ghostPoint = new Point(ghostWasEaten.getX(), ghostWasEaten.getY());



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
