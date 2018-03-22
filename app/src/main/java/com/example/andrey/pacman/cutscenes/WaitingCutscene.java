package com.example.andrey.pacman.cutscenes;

import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Ghost;

public class WaitingCutscene extends Cutscene{

    public WaitingCutscene(Playfield playfield) {
        super(playfield, 1200);
    }
    
    @Override
    public void play(long deltaTime) {

        for (Ghost ghost : playfield.getGhosts()) {
            ghost.animate(deltaTime);
        }
    }
}
