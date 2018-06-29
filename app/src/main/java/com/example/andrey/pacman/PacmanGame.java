package com.example.andrey.pacman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.view.MotionEvent;
import com.example.andrey.pacman.entity.Food;
import com.example.andrey.pacman.entity.Fruit;
import com.example.andrey.pacman.entity.Ghost;
import com.example.andrey.pacman.entity.Pacman;

import java.util.Date;

public class PacmanGame {

    private Playfield playfield;

    private GhostManager ghostModeController;
    private CutsceneManager cutsceneManager;
    private UserInterfaceDrawManager userInterfaceDrawManager;
    private FruitManager fruitManager;

    private GameView view;

    private int pacmanLives = 4;

    private long lastTime;

    private float touchDX;
    private float touchDY;
    private float touchStartX;
    private float touchStartY;
    private boolean touchCanceled = true;

    private int countPoints;

    private float tickInterval;

    private boolean pause;

    private int score;

    private int ghostMultiplyer;

    private int levelNum = 1;

    private int highScore;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    PacmanGame(GameView view) {
        this.view = view;

        preferences = view.getContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        editor = preferences.edit();

        highScore = preferences.getInt("highScore", 0);

        playfield = new Playfield(this, view);
        countPoints = playfield.getCountPoints();

        ghostModeController = new GhostManager(this, playfield);
        userInterfaceDrawManager = new UserInterfaceDrawManager(view, this);
        cutsceneManager = new CutsceneManager(view, this, playfield);
        fruitManager = new FruitManager(view, this, playfield);

        tickInterval = 1000 / 90;
        setTimeout();
        lastTime = new Date().getTime();

        cutsceneManager.addStartGameScene();
    }


    public FruitManager getFruitManager() {
        return fruitManager;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public CutsceneManager getCutsceneManager() {
        return cutsceneManager;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getPacmanLives() {
        return pacmanLives;
    }

    public void nextLevel() {
        levelNum++;
        playfield.nextLevel();
        countPoints = playfield.getCountPoints();
        ghostModeController.nextLevel();
        cutsceneManager.addStartGameScene();
    }

    public void onResume() {
        cutsceneManager.addResumeScene();
        pause = false;
    }

    public void onPause() {
        pause = true;
    }

    public void gameOver() {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);
        pause = true;
    }


    public void killPacman() {

        if (pacmanLives <= 0) {
            cutsceneManager.addGameOverScene();
        } else {
            cutsceneManager.addStartGameScene();
            pacmanLives--;
            playfield.initCharacters(view);
            ghostModeController.pacmanDied();
        }
    }

    public void eatGhost(Ghost ghost) {
        getCutsceneManager().addEatingGhostScene(ghost, ghostMultiplyer);
        ghost.beEaten();
        ghostMultiplyer++;
        score += 200 * ghostMultiplyer;
    }

    public void eatFruit(Fruit fruit) {
        score += fruit.getScore();
    }

    public void eatPoint(Food food) {
        countPoints--;
        fruitManager.eatPoint();

        ghostModeController.increaseEatenDots();

        score += food.getPoints();

        if (food == Food.ENERGIZER) {
            ghostModeController.startFrightened();
            ghostMultiplyer = 0;
        }

        if (countPoints <= 0) {
            cutsceneManager.addNextLevelScene();
            cutsceneManager.addPlayfieldPingingScene();
        }
    }

    private void handleHighScore() {
        if (highScore <= score) {
            highScore = score;
        }
    }

    public void saveHighScore() {
        editor.putInt("highScore", highScore);
        editor.commit();
    }

    public void onDraw(Canvas canvas) {
        playfield.onDraw(canvas);
        userInterfaceDrawManager.onDraw(canvas);

        if (cutsceneManager.hasScene())
            cutsceneManager.onDraw(canvas);
    }

    private void setTimeout() {
        view.redrawHandler.sleep(Math.round(tickInterval));
    }


    public void tick() {
        long now = new Date().getTime();

        long deltaTime = now - lastTime;
        if (!pause) {
            if (cutsceneManager.hasScene()) {
                cutsceneManager.playScene(deltaTime);

            } else {
                playfield.update(deltaTime);
                handleHighScore();
                fruitManager.update(deltaTime);
                ghostModeController.update(deltaTime);
            }
        }

        lastTime = now;

        setTimeout();
    }

    public Playfield getPlayfield() {
        return playfield;
    }

    Pacman getPacman() {
        return playfield.getPacman();
    }

    void handleTouchStart(MotionEvent e) {
        touchDX = 0;
        touchDY = 0;
        if (e.getPointerCount() == 1) {
            touchCanceled = false;
            touchStartX = e.getX(0);
            touchStartY = e.getY(0);
        }
    }

    void handleTouchMove(MotionEvent e) {
        if (touchCanceled) {
            return;
        }

        if (e.getPointerCount() > 1) {
            cancelTouch();
        } else {
            touchDX = e.getX(0) - touchStartX;
            touchDY = e.getY(0) - touchStartY;
        }
    }

    void handleTouchEnd(MotionEvent e) {
        if (touchCanceled) {
            return;
        }

        float absDx = Math.abs(touchDX);
        float absDy = Math.abs(touchDY);
        Pacman pacman = getPacman();
        if (absDx > 15 && absDy < absDx) {
            pacman.setRequestDirection(touchDX > 0 ? Direction.RIGHT : Direction.LEFT);
        } else if (absDy > 15 && absDx < absDy) {
            pacman.setRequestDirection(touchDY > 0 ? Direction.DOWN : Direction.UP);
        }
        cancelTouch();
    }

    private void cancelTouch() {
        touchStartX = Float.NaN;
        touchStartY = Float.NaN;
        touchCanceled = true;
    }

}
