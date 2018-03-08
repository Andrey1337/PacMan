package com.example.andrey.pacman;


import com.example.andrey.pacman.entity.Clyde;
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

    private GameMode gameMode;

    private int eatedDots;

    GhostManager(Playfield playfield) {
        this.playfield = playfield;

        pinky = playfield.getPinky();
        inky = playfield.getInky();
        clyde = playfield.getClyde();

        waves = new Wave[4];

        waves[0] = new Wave(1000 * 11, 1000 * 20);
        waves[1] = new Wave(1000 * 11, 1000 * 20);
        waves[2] = new Wave(1000 * 8, 1000 * 20);
        waves[3] = new Wave(1000 * 8, Long.MAX_VALUE);

        waveNum = 0;

        gameMode = playfield.getGameMode();

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

    public void increaseEatenDots()
    {
        afkTimer = 0;
        eatedDots++;
    }

    public void update(long deltaTime) {

        waveTimeCounter += deltaTime;
        afkTimer += deltaTime;

        if(pinky.isInCage()) {
            playfield.getPinky().startExit();
        }

        if(!pinky.isInCage() && inky.isInCage() && (eatedDots >= inkyPointsExit || afkTimer > inkyExitTime)) {
            playfield.getInky().startExit();
        }

        if(!inky.isInCage() && clyde.isInCage() && eatedDots >= clydePointsExit) {
            playfield.getClyde().startExit();
        }

        if (gameMode == GameMode.SCATTER && waveTimeCounter > waves[waveNum].getScatterTime() || (gameMode == GameMode.CHASE && waveTimeCounter > waves[waveNum].getChaseTime())) {
            waveTimeCounter = 0;
            changeGameMode();
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

        playfield.changeGameMode(gameMode);
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



