package com.example.andrey.pacman;


import android.graphics.*;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import com.example.andrey.pacman.entity.Point;


public class UserInterfaceDrawManager {

    private View view;
    private PacmanGame game;

    private Bitmap hightScoreLabel;
    private Bitmap pacmanLife;
    private int pacmanLifes;
    private Bitmap numbers;

    int frameWidth, frameHeight;
    int numberWidth, numberHeight;

    private int numberSpace;

    UserInterfaceDrawManager(View view, PacmanGame game)
    {
        this.view = view;
        this.game = game;

        pacmanLifes = game.getPacmanLives();

        pacmanLife = BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_life);
        pacmanLife = Bitmap.createScaledBitmap(pacmanLife, (int) (pacmanLife.getWidth() * game.getPlayfield().scale),
                (int)(pacmanLife.getHeight() *  game.getPlayfield().scale), false);

        hightScoreLabel = BitmapFactory.decodeResource(view.getResources(), R.mipmap.high_score);
        hightScoreLabel = Bitmap.createScaledBitmap(hightScoreLabel, (int) (hightScoreLabel.getWidth() * game.getPlayfield().scale),
                (int)(hightScoreLabel.getHeight() *  game.getPlayfield().scale), false);

        frameWidth = 7;
        frameHeight= 7;


        frameHeight *= 8;
        frameWidth *= 8;

        numbers = BitmapFactory.decodeResource(view.getResources(), R.mipmap.numbers);
        numbers = Bitmap.createScaledBitmap(numbers, (int) (numbers.getWidth() * game.getPlayfield().scale),
                (int)(numbers.getHeight() *  game.getPlayfield().scale), false);

        int numbersCount = 10;
        numberWidth = numbers.getWidth() / numbersCount;
        numberHeight = numbers.getHeight();

        numbers = Bitmap.createScaledBitmap(numbers, frameWidth * numbersCount,
                frameHeight, false);

    }

    private void drawNumber(Canvas canvas, int number) {
        float left = game.getPlayfield().mapTexture.getWidth()/2;
        float top = numbers.getHeight() / 6;

        char[] numberChars = Integer.toString(number).toCharArray();

        for (int i = 0; i < String.valueOf(number).length(); i++) {
            RectF whereToDraw = new RectF(left + numberWidth * i, top, left + numberWidth * (i + 1), top + numberHeight);
            canvas.drawBitmap(numbers, new Rect(Character.getNumericValue(numberChars[i]) * frameWidth, 0, (Character.getNumericValue(numberChars[i]) * frameWidth) + frameWidth,
                    frameHeight), whereToDraw, null);
        }
    }

    public void onDraw(Canvas canvas)
    {

        float left =  hightScoreLabel.getHeight() / 4;
        float top = hightScoreLabel.getHeight() / 4;
        canvas.drawBitmap(hightScoreLabel, left, top, null);

        drawNumber(canvas, game.getScore());

        left = game.getPlayfield().mapTexture.getWidth()/ game.getPlayfield().MAP_WIDTH;
        top = game.getPlayfield().mapTexture.getHeight() + game.getPlayfield().STARTPOS_Y +
                game.getPlayfield().mapTexture.getWidth()/ game.getPlayfield().MAP_WIDTH;
        for (int i = 0; i <  game.getPacmanLives() ;i++) {
            canvas.drawBitmap(pacmanLife, left, top, null);

            left += pacmanLife.getWidth();
        }
    }
}
