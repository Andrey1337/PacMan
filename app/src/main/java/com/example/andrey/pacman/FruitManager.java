package com.example.andrey.pacman;


import android.graphics.*;
import android.view.View;
import com.example.andrey.pacman.entity.FRUIT_TYPE;
import com.example.andrey.pacman.entity.Fruit;
import com.example.andrey.pacman.entity.Point;

public class FruitManager {

    private PacmanGame pacmanGame;
    private Playfield playfield;

    private Fruit fruit;

    private long eatFruitTime;
    private long timer;


    private boolean isFruitEated;
    private long showNumberTime;


    private Bitmap points;
    private Point pointsDrawPos;

    private int POINTS_OFFSET_X;
    private int POINTS_OFFSET_Y;

    private int frameWidth, frameHeight;
    private int numbersWidth, numbersHeight;


    FruitManager(View view, PacmanGame pacmanGame, Playfield playfield)
    {
        this.pacmanGame = pacmanGame;
        this.playfield = playfield;
        eatFruitTime = 8 * 1000;
        pointsDrawPos = new Point(13.5f, 16);
        frameWidth = 22 * 8;
        frameHeight = 7 * 8;
        points  = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.point),frameWidth * 8,
                frameHeight,false);

        numbersWidth = (int)((float)points.getWidth() * playfield.scale / (float) 8);
        numbersHeight = (int)((float)points.getHeight() * playfield.scale);

        POINTS_OFFSET_X = (int)(11 / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
        POINTS_OFFSET_Y = (int)(3 / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());


        fruit = new Fruit(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_food),
                FRUIT_TYPE.KEY);
    }

    public void eatFruit()
    {
        pacmanGame.eatFruit(fruit);
        fruit = null;
    }

    public boolean canEatFruit()
    {
        return !isFruitEated || fruit != null;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public void update(long deltaTime)
    {
        timer += deltaTime;

        if(isFruitEated)
        {
            if(timer >= showNumberTime)
            {
                isFruitEated = false;

            }
        }
    }

    public void onDraw(Canvas canvas)
    {
        /*float left = playfield.X_OFFSET + pointsDrawPos.floatX * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                - POINTS_OFFSET_X;
        float top = playfield.Y_OFFSET + pointsDrawPos.floatY * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                - POINTS_OFFSET_Y +  playfield.STARTPOS_Y;

        canvas.drawBitmap(points, new Rect(frameWidth * fruit.getDrawPos(),
                        0, frameWidth * fruit.getDrawPos() + frameWidth, frameHeight)
                , new RectF(left, top, left + numbersWidth, top + numbersHeight),
                null);*/

        fruit.onDraw(canvas);


    }
}
