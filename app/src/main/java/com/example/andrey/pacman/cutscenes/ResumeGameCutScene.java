package com.example.andrey.pacman.cutscenes;

import android.view.View;
import com.example.andrey.pacman.Playfield;


public class ResumeGameCutScene extends StartGameCutScene{

    private long pingTime;
    private long pingingTimer;

    public ResumeGameCutScene(View view, Playfield playfield) {
        super(view, playfield);
         pingTime = getSceneTime() / 9;
    }


    @Override
    public void play(long deltaTime) {

        pingingTimer += deltaTime;
        if(pingingTimer >= pingTime)
        {
            pingingTimer = 0;
            isVisible = !isVisible;
        }

    }

    @Override
    public void startOfScene() { }

    @Override
    public void endOfScene() { }
}
