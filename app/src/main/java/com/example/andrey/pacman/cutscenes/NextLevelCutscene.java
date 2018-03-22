package com.example.andrey.pacman.cutscenes;

import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Ghost;

public class NextLevelCutscene extends Cutscene{
    public NextLevelCutscene(Playfield playfield) {
        super(playfield, 2000);
    }

    @Override
    public void startOfScene() {
        for(Ghost ghost : playfield.getGhosts())
        {
            ghost.isVisible = false;
        }
        playfield.getPacman().isVisible = true;
        playfield.getPacman().isPacManBall = true;
    }
}
