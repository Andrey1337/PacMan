package com.example.andrey.pacman.cutscenes;

import com.example.andrey.pacman.PacmanGame;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Ghost;

public class WaitingCutscene extends Cutscene{

    private PacmanGame pacmanGame;
    public WaitingCutscene(PacmanGame game, Playfield playfield) {
        super(playfield, 1200);

        pacmanGame = game;
    }
    
    @Override
    public void play(long deltaTime) {
        pacmanGame.getFruitManager().update(deltaTime);
        for (Ghost ghost : playfield.getGhosts()) {
            ghost.animate(deltaTime);
        }
    }
}
