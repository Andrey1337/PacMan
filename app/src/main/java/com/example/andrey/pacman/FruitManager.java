package com.example.andrey.pacman;


import android.graphics.*;
import android.view.View;
import com.example.andrey.pacman.entity.Actor;
import com.example.andrey.pacman.entity.FRUIT_TYPE;
import com.example.andrey.pacman.entity.Fruit;

public class FruitManager {

    private PacmanGame pacmanGame;
    private Playfield playfield;

    private Fruit fruit;

    private long eatFruitTime;
    private long timer;

    private boolean showPoints;
    private FruitPoints fruitPoints;

    FruitManager(View view, PacmanGame pacmanGame, Playfield playfield)
    {
        this.pacmanGame = pacmanGame;
        this.playfield = playfield;
        eatFruitTime = 8 * 1000;

        fruitPoints = new FruitPoints(playfield,BitmapFactory.decodeResource(view.getResources(), R.mipmap.fruit_points), 13.5f,16);
        fruit = new Fruit(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_food),
                FRUIT_TYPE.CHERRY);
    }

    public void eatFruit()
    {
        pacmanGame.eatFruit(fruit);
        fruit = null;
        showPoints = true;
        timer = 0;
    }

    public boolean canEatFruit()
    {
        return fruit != null;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public void update(long deltaTime)
    {
        timer += deltaTime;

        long showPointsTime = 2 * 1000;
        if(showPoints && timer >= showPointsTime)
        {
            showPoints = false;
            timer = 0;
        }

    }

    public void onDraw(Canvas canvas)
    {
        if(fruit != null)
            fruit.onDraw(canvas);

        if(showPoints)
            fruitPoints.onDraw(canvas);
    }


    public class FruitPoints extends Actor {

        FruitPoints(Playfield playfield, Bitmap bitmap,  float x, float y) {
            super(playfield, bitmap, 22, 7, 11, 3, 8, 1, x, y);
        }
    }
}
