package com.example.andrey.pacman;


import android.graphics.*;
import android.view.View;
import com.example.andrey.pacman.entity.Actor;
import com.example.andrey.pacman.entity.UserInterfaceActor;


public class UserInterfaceDrawManager {

    private GameView view;
    private PacmanGame game;


    private PacmanLife pacmanLife;
    private HighScore highScore;
    private ScoreNumber scoreNumber;


    UserInterfaceDrawManager(GameView view, PacmanGame game)
    {
        this.view = view;
        this.game = game;

        pacmanLife = new PacmanLife(view, game.getPlayfield(),0,0);
        highScore = new HighScore(view, game.getPlayfield(),0,0);
        highScore.setX(highScore.getActorHeight() / 4);
        highScore.setY(highScore.getActorHeight() / 4);

        scoreNumber = new ScoreNumber(view, game.getPlayfield() ,0,0,0);

    }

    private void drawHighScore(Canvas canvas, int highScore) {

        int width = Integer.toString(highScore).toCharArray().length * scoreNumber.getActorWidth() / 2;
        float left = game.getPlayfield().mapTexture.getWidth()/2 - width;

        scoreNumber.setY(scoreNumber.getActorHeight() / 6);

        char[] numberChars = Integer.toString(highScore).toCharArray();

        for (int i = 0; i < String.valueOf(highScore).length(); i++) {
            scoreNumber.setX(left + scoreNumber.getActorWidth() * i);
            scoreNumber.setCurrentFrame(Character.getNumericValue(numberChars[i]));
            scoreNumber.onDraw(canvas);
        }
    }

    private void drawScore(Canvas canvas, int score)
    {
        int width = Integer.toString(score).toCharArray().length * scoreNumber.getActorWidth();
        float left = game.getPlayfield().mapTexture.getWidth() - 1 - width;

        scoreNumber.setY(scoreNumber.getActorHeight()/ 6);

        char[] numberChars = Integer.toString(score).toCharArray();

        for (int i = 0; i < String.valueOf(score).length(); i++) {

            scoreNumber.setX(left + scoreNumber.getActorWidth() * i);
            scoreNumber.setCurrentFrame(Character.getNumericValue(numberChars[i]));
            scoreNumber.onDraw(canvas);
        }
    }


    public void onDraw(Canvas canvas)
    {
        highScore.onDraw(canvas);

        drawHighScore(canvas, game.getHighScore());

        drawScore(canvas, game.getScore());

        pacmanLife.setX(game.getPlayfield().mapTexture.getWidth()/ game.getPlayfield().MAP_WIDTH);
        pacmanLife.setY(game.getPlayfield().mapTexture.getHeight() + game.getPlayfield().STARTPOS_Y + 10);
        for (int i = 0; i <  game.getPacmanLives() ;i++) {
            pacmanLife.onDraw(canvas);
            pacmanLife.setX(pacmanLife.getX() + pacmanLife.getActorWidth());
        }
    }

    class ScoreNumber extends UserInterfaceActor{

        ScoreNumber(View view, Playfield playfield, int number, float x, float y) {
            super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.numbers), 9, 7, 0, 0, 10, 1, x, y);
            currentFrame = number;

            actorWidth = actorWidth * 5 / 6;
            actorHeight = actorHeight * 5 /6;
        }

    }

    class PacmanLife extends UserInterfaceActor{
        PacmanLife(View view, Playfield playfield, float x, float y) {
            super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_life), 13, 13, 0, 0, 1, 1, x, y);
        }
    }

    class HighScore extends UserInterfaceActor{

        HighScore(View view, Playfield playfield, float x, float y) {
            super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.high_score), 75, 7, 0, 0, 1, 1, x, y);

            actorWidth = actorWidth * 5 / 6;
            actorHeight = actorHeight * 5 /6;
        }
    }
}
