package com.example.andrey.pacman.cutscenes;

import com.example.andrey.pacman.PacmanGame;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Ghost;

public class PlayfieldPingingCutscene extends Cutscene {

    private PacmanGame game;

    private long timeCounter;
    private long pingTime;

    public PlayfieldPingingCutscene(PacmanGame game, Playfield playfield) {
        super(playfield, 1500);
        this.game = game;

        pingTime = getSceneTime() / 8;
    }

    @Override
    public void startOfScene() {
        for (Ghost ghost : playfield.getGhosts()) {
            ghost.isVisible = false;
        }
        playfield.getPacman().isVisible = false;

        playfield.isPing = true;
    }

    @Override
    public void play(long deltaTime) {

        timeCounter += deltaTime;
        if (timeCounter >= pingTime) {
            timeCounter = 0;
            playfield.isPing = !playfield.isPing;
        }
    }

    @Override
    public void endOfScene() {
        for (Ghost ghost : playfield.getGhosts()) {
            ghost.isVisible = true;
        }
        playfield.getPacman().isVisible = true;
        playfield.isPing = false;

        game.nextLevel();
    }
}
