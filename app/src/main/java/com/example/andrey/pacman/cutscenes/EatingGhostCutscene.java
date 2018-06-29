package com.example.andrey.pacman.cutscenes;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Actor;
import com.example.andrey.pacman.entity.Ghost;

public class EatingGhostCutscene extends Cutscene {

    private Ghost ghostWasEaten;

    private GhostPoints ghostPoints;

    public EatingGhostCutscene(View view, Playfield playfield, Ghost ghost, int ghostMultiplyer) {
        super(playfield, 1000);

        ghostWasEaten = ghost;
        ghostPoints = new GhostPoints(view, playfield, ghostMultiplyer, ghostWasEaten.getX(), ghostWasEaten.getY());

    }

    @Override
    public void onDraw(Canvas canvas) {
        ghostPoints.onDraw(canvas);
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

    class GhostPoints extends Actor {
        GhostPoints(View view, Playfield playfield, int ghostMultiplyer, float x, float y) {
            super(playfield, BitmapFactory.decodeResource(view.getResources(), R.drawable.ghost_points), 18, 9, 8, 4, 4, 1, x, y);
            currentFrame = ghostMultiplyer;
        }
    }
}

