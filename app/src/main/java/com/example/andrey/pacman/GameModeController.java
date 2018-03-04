package com.example.andrey.pacman;

import com.example.andrey.pacman.entity.Ghost;

import java.util.ArrayList;

public class GameModeController {

    ArrayList<Ghost> ghosts;

    int waveNum = 1;

    protected long lastTimeChanged;

    int wave1ScatterTime = 1000 * 8;
    int wave1ChaseTime = 1000 * 20;

    int wave2ScatterTime = 1000 * 8;
    int wave2ChaseTime = 1000 * 20;

    int wave3ScatterTime = 1000 * 5;
    int wave3ChaseTime = 1000 * 20;

    int wave4ScatterTime = 1000 * 5;



    GameMode gameMode;

    public GameModeController(Playfield playfield)
    {
        ghosts = playfield.getGhosts();
        gameMode = GameMode.SCATTER;
        lastTimeChanged = System.currentTimeMillis();
    }


    public void update()
    {
        if(gameMode == GameMode.FRIGHTENED || waveNum == 4 && gameMode == GameMode.CHASE)
            return;

        long time = System.currentTimeMillis();

        if(waveNum == 1 || waveNum == 2)
        {
            if(gameMode == GameMode.SCATTER) {
                if (time > lastTimeChanged + wave1ScatterTime) {
                    lastTimeChanged = time;
                    changeGameMode();
                }
            }
            if(gameMode == GameMode.CHASE)
            {
                if (time > lastTimeChanged + wave1ChaseTime) {
                    lastTimeChanged = time;
                    changeGameMode();
                }
            }
        }

        if(waveNum == 3 || waveNum == 4)
        {
            if(gameMode == GameMode.SCATTER) {
                if (time > lastTimeChanged + wave3ScatterTime) {
                    lastTimeChanged = time;
                    changeGameMode();
                }
            }
            if(gameMode == GameMode.CHASE)
            {
                if (time > lastTimeChanged + wave3ChaseTime) {
                    lastTimeChanged = time;
                    changeGameMode();
                }
            }
        }
    }

    private void changeGameMode()
    {
        if(gameMode == GameMode.SCATTER)
        {
            gameMode = GameMode.CHASE;
        }
        else
        {
             gameMode = GameMode.SCATTER;
             waveNum++;
        }

        for(Ghost ghost : ghosts)
        {
            ghost.changeMode(gameMode);
        }
    }
}
