package com.example.andrey.pacman.cutscenes;

import com.example.andrey.pacman.PacmanGame;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Ghost;

public class KillPacmanCutscene extends Cutscene{

    private PacmanGame pacmanGame;

    public KillPacmanCutscene(PacmanGame game, Playfield playfield) {
        super(playfield, 1600);

        pacmanGame = game;
    }



    @Override
    public void startOfScene() {
        for(Ghost ghost : playfield.getGhosts()) {
            ghost.isVisible = false;
        }
        playfield.getPacman().startDying();
    }

    @Override
    public void endOfScene() {
        if(!(pacmanGame.getPacmanLives() <= 0)) {
            for (Ghost ghost : playfield.getGhosts()) {
                ghost.isVisible = true;
            }
        }
        playfield.getPacman().stopDying();

        pacmanGame.getFruitManager().killPacman();
        pacmanGame.killPacman();
    }

    @Override
    public void play(long deltaTime) {
        playfield.getPacman().animate(deltaTime);
        pacmanGame.getFruitManager().update(deltaTime);
    }
}
