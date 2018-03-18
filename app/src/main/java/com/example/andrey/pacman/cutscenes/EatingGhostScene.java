package com.example.andrey.pacman.cutscenes;

import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Ghost;

public class EatingGhostScene extends Cutscene {

    private Ghost ghostWasEaten;

    public EatingGhostScene(Playfield playfield, Ghost ghost) {
        super(playfield, 1000);

        ghostWasEaten = ghost;
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
