package com.example.andrey.pacman;


import android.util.Log;
import com.example.andrey.pacman.entity.Clyde;
import com.example.andrey.pacman.entity.Ghost;
import com.example.andrey.pacman.entity.Inky;
import com.example.andrey.pacman.entity.Pinky;

public class GhostManager {

    private Playfield playfield;

    private int waveNum;

    private long waveTimeCounter;
    private long afkTimer;

    private int inkyPointsExit = 30;
    private int clydePointsExit = 60;
    private int inkyExitTime = 1000 * 4;
    private int clydeExitTime = 1000 * 7;

    private Pinky pinky;
    private Inky inky;
    private Clyde clyde;

    private Wave[] waves;

    private GameMode safePrevMode;
    private GameMode prevMode;
    private GameMode gameMode;

    private int levelNum;

    private int eatedDots;

    private long frightenedTimer;
    private long frightenedTime = 8 * 1000;
    private long startPingingTime;
    private long pingingTime;
    private long pingingTimer = 0;

    GhostManager(Playfield playfield) {
        this.playfield = playfield;
        pingingTime = 300;
        startPingingTime = frightenedTime - pingingTime * 10;

        pinky = playfield.getPinky();
        inky = playfield.getInky();
        clyde = playfield.getClyde();

        waves = new Wave[4];
        waves[0] = new Wave(1000 * 11, 1000 * 20);
        waves[1] = new Wave(1000 * 11, 1000 * 20);
        waves[2] = new Wave(1000 * 8, 1000 * 20);
        waves[3] = new Wave(1000 * 8, Long.MAX_VALUE);

        waveNum = 0;
        levelNum = 1;

        gameMode = playfield.getGameMode();
        safePrevMode = gameMode;
        prevMode = gameMode;
        waveTimeCounter = 0;
        afkTimer = 0;
    }

    public void pacmanDied()
    {
        afkTimer = 0;
        inkyPointsExit -= inkyPointsExit / 4;
        clydePointsExit -= clydePointsExit / 4;
        pinky = playfield.getPinky();
        inky = playfield.getInky();
        clyde = playfield.getClyde();
    }

    public void nextLevel()
    {
        eatedDots = 0;
        waveNum = 0;
        frightenedTimer = 0;
        waveTimeCounter = 0;
        afkTimer = 0;

        changeGameMode(GameMode.CHASE);
    }

    public void increaseEatenDots()
    {
        afkTimer = 0;
        eatedDots++;
    }

    private void ghostPing() {
        for (Ghost ghost : playfield.getGhosts())
        {
            ghost.ping();
        }
    }

    public void update(long deltaTime) {

        if(gameMode != GameMode.FRIGHTENED) {
            waveTimeCounter += deltaTime;
            afkTimer += deltaTime;
        }
        else {
            frightenedTimer += deltaTime;
            if(frightenedTimer >= startPingingTime)
            {
                pingingTimer += deltaTime;
            }

            if(pingingTimer >= pingingTime)
            {
                ghostPing();
                pingingTimer = 0;
            }

            if(frightenedTimer >= frightenedTime)
                changeGameMode(safePrevMode);
        }

        if(pinky.isInCage()) {
            playfield.getPinky().startExit();
        }

        if(!pinky.isInCage() && inky.isInCage() && (eatedDots >= inkyPointsExit || afkTimer > inkyExitTime)) {
            afkTimer = 0;
            playfield.getInky().startExit();
        }

        if(!inky.isInCage() && clyde.isInCage() && (eatedDots >= clydePointsExit || afkTimer > clydeExitTime)) {
            afkTimer = 0;
            playfield.getClyde().startExit();
        }

        if (gameMode == GameMode.SCATTER && waveTimeCounter > waves[waveNum].getScatterTime()) {
            waveTimeCounter = 0;
            safePrevMode = GameMode.SCATTER;
            changeGameMode(GameMode.CHASE);
        }

        if(gameMode == GameMode.CHASE && waveTimeCounter > waves[waveNum].getChaseTime()) {
            waveTimeCounter = 0;
            safePrevMode = GameMode.CHASE;
            changeGameMode(GameMode.SCATTER);
        }
    }

    public void startFrightened() {
        frightenedTimer = 0;
        pingingTimer = 0;
        changeGameMode(GameMode.FRIGHTENED);
    }

    private void changeGameMode(GameMode newGameMode)
    {
        prevMode = gameMode;
        switch (newGameMode)
        {
            case CHASE:
                break;
            case SCATTER:
                waveNum++;
                break;
            case FRIGHTENED:
                frightenedTimer = 0;
                break;
        }
        gameMode = newGameMode;

        changeGameModeToGhosts(prevMode, gameMode);

        playfield.setGameMode(newGameMode);
    }

    private void changeGameModeToGhosts(GameMode prevMode,GameMode newGameMode)
    {
        for(Ghost ghost : playfield.getGhosts())
        {
            ghost.changeMode(prevMode, newGameMode);
        }
    }


    private class Wave
    {
        private long scatterTime;
        private long chaseTime;
        Wave(long scatterTime, long chaseTime)
        {
            this.scatterTime = scatterTime;
            this.chaseTime = chaseTime;
        }

        private long getChaseTime() {
            return chaseTime;
        }

        long getScatterTime() {
            return scatterTime;
        }
    }
}



