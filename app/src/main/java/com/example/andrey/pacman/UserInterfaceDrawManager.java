package com.example.andrey.pacman;


import android.graphics.*;


public class UserInterfaceDrawManager {

    private GameView view;
    private PacmanGame game;

    private Bitmap highScoreLabel;
    private Bitmap pacmanLife;
    private Bitmap numbers;

    private int frameWidth, frameHeight;
    private int numberWidth, numberHeight;

    UserInterfaceDrawManager(GameView view, PacmanGame game)
    {
        this.view = view;
        this.game = game;

        pacmanLife = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_life),
                (int) (BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_life).getWidth() * game.getPlayfield().scale),
                (int)(BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_life).getHeight() *  game.getPlayfield().scale), false);

        highScoreLabel = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.high_score),
                (int) (BitmapFactory.decodeResource(view.getResources(), R.mipmap.high_score).getWidth() * 5/6 * game.getPlayfield().scale),
                (int)(BitmapFactory.decodeResource(view.getResources(), R.mipmap.high_score).getHeight() * 5/6 *  game.getPlayfield().scale), false);

        frameWidth = 7;
        frameHeight= 7;

        frameHeight *= 8;
        frameWidth *= 8;

        numbers = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.numbers),
                (int) (BitmapFactory.decodeResource(view.getResources(), R.mipmap.numbers).getWidth() * 5/6 * game.getPlayfield().scale),
                (int)(BitmapFactory.decodeResource(view.getResources(), R.mipmap.numbers).getHeight() * 5/6 * game.getPlayfield().scale), false);

        int numbersCount = 10;
        numberWidth = numbers.getWidth() / numbersCount;
        numberHeight = numbers.getHeight();

        numbers = Bitmap.createScaledBitmap(numbers, frameWidth * numbersCount,
                frameHeight, false);

    }

    private void drawHighScore(Canvas canvas, int highScore) {

        int width = Integer.toString(highScore).toCharArray().length * numberWidth / 2;
        float left = game.getPlayfield().mapTexture.getWidth()/2 - width;
        float top = numbers.getHeight() / 6;

        char[] numberChars = Integer.toString(highScore).toCharArray();

        for (int i = 0; i < String.valueOf(highScore).length(); i++) {
            RectF whereToDraw = new RectF(left + numberWidth * i, top, left + numberWidth * (i + 1), top + numberHeight);
            canvas.drawBitmap(numbers, new Rect(Character.getNumericValue(numberChars[i]) * frameWidth, 0, (Character.getNumericValue(numberChars[i]) * frameWidth) + frameWidth,
                    frameHeight), whereToDraw, null);
        }
    }

    private void drawScore(Canvas canvas, int score)
    {
        int width = Integer.toString(score).toCharArray().length * numberWidth ;
        float left = game.getPlayfield().mapTexture.getWidth() - 1 - width;

        float top = numbers.getHeight() / 6;

        char[] numberChars = Integer.toString(score).toCharArray();

        for (int i = 0; i < String.valueOf(score).length(); i++) {
            RectF whereToDraw = new RectF(left + numberWidth * i, top, left + numberWidth * (i + 1), top + numberHeight);
            canvas.drawBitmap(numbers, new Rect(Character.getNumericValue(numberChars[i]) * frameWidth, 0, (Character.getNumericValue(numberChars[i]) * frameWidth) + frameWidth,
                    frameHeight), whereToDraw, null);
        }
    }


    public void onDraw(Canvas canvas)
    {
        float left =  highScoreLabel.getHeight() / 4;
        float top = highScoreLabel.getHeight() / 4;
        canvas.drawBitmap(highScoreLabel, left, top, null);

        drawHighScore(canvas, game.getHighScore());

        drawScore(canvas, game.getScore());

        left = game.getPlayfield().mapTexture.getWidth()/ game.getPlayfield().MAP_WIDTH;
        top = game.getPlayfield().mapTexture.getHeight() + game.getPlayfield().STARTPOS_Y +
                game.getPlayfield().mapTexture.getWidth()/ game.getPlayfield().MAP_WIDTH;
        for (int i = 0; i <  game.getPacmanLives() ;i++) {
            canvas.drawBitmap(pacmanLife, left, top, null);

            left += pacmanLife.getWidth();
        }
    }
}
