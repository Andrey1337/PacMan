package com.example.andrey.pacman;


public class GhostManager {

    private Playfield playfield;

    private int waveNum;

    private long waveTimeCounter;
    private long totalTimeCounter;

    private int pinkyExitTime = 1000 * 3;
    private boolean isPinkyExit;

    private Wave[] waves;


    private GameMode gameMode;

    GhostManager(Playfield playfield)
    {
        this.playfield = playfield;

        waves = new Wave[4];

        waves[0] = new Wave(1000 * 11, 1000 * 20);
        waves[1] = new Wave(1000 * 8, 1000 * 20);
        waves[2] = new Wave(1000 * 11, 1000 * 20);
        waves[3] = new Wave(1000 * 5, Long.MAX_VALUE);

        waveNum = 0;

        gameMode = playfield.getGameMode();

        waveTimeCounter = 0;
        totalTimeCounter = 0;
    }

    public void reset()
    {
        waveTimeCounter = 0;
        totalTimeCounter = 0;
        isPinkyExit = false;
    }


    public void update(long deltaTime) {

        waveTimeCounter += deltaTime;
        totalTimeCounter += deltaTime;

        if(!isPinkyExit && totalTimeCounter > pinkyExitTime)
        {
            playfield.getPinky().startExit();
            isPinkyExit = true;
        }

        if (gameMode == GameMode.SCATTER && waveTimeCounter > waves[waveNum].getScatterTime()) {
            waveTimeCounter = 0;
            changeGameMode();
        }
        if (gameMode == GameMode.CHASE && waveTimeCounter > waves[waveNum].getChaseTime())
        {
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



