package com.example.andrey.pacman;


import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.entity.Actor;
import com.example.andrey.pacman.entity.Fruit;
import com.example.andrey.pacman.entity.UserInterfaceActor;

public class FruitManager {

    private View view;
    private PacmanGame pacmanGame;
    private Playfield playfield;

    private Fruit fruit;

    private long eatFruitTime;
    private long timer;

    private int pointsEated;
    private int pointsToNextFruit;

    private FruitPoints fruitPoints;
    private FruitsLabel fruitsLabel;

    FruitManager(View view, PacmanGame pacmanGame, Playfield playfield)
    {
        this.view = view;
        this.pacmanGame = pacmanGame;
        this.playfield = playfield;
        eatFruitTime = 10 * 1000;
        fruitsLabel = new FruitsLabel(view, playfield, 0,0);
        pointsToNextFruit = 70;
    }

    public void eatFruit() {
        pacmanGame.eatFruit(fruit);
        fruitPoints = new FruitPoints(view, playfield,fruit.getFruitType().getDrawPosition(),13.5f,16);
        fruit = null;
        timer = 0;
        pointsToNextFruit = pointsEated + 100;
    }

    public void killPacman()
    {
        fruit = null;
        pointsToNextFruit = pointsEated + 100;
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

        if(fruit != null && timer >= eatFruitTime) {
            fruit = null;
            pointsToNextFruit = pointsEated + 100;
            timer = 0;
        }

        long showPointsTime = 2 * 1000;
        if(fruitPoints != null && timer >= showPointsTime) {
            fruitPoints = null;
            timer = 0;
        }

    }

    public void eatPoint() {
        pointsEated++;

        if(fruit == null && pointsEated >= pointsToNextFruit)
        {
            fruit = new Fruit(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_food), pacmanGame.getLevelNum());
            timer = 0;
        }
    }

    public void onDraw(Canvas canvas)
    {
        if(fruit != null)
            fruit.onDraw(canvas);

        if(fruitPoints != null)
            fruitPoints.onDraw(canvas);


        fruitsLabel.setX(pacmanGame.getPlayfield().mapTexture.getWidth() - fruitsLabel.getActorWidth() * 1.2f);
        fruitsLabel.setY(pacmanGame.getPlayfield().mapTexture.getHeight() + pacmanGame.getPlayfield().STARTPOS_Y);

        for(int i = 0; i < pacmanGame.getLevelNum() && i < 8; i++)
        {
            fruitsLabel.setCurrentFrame(i);
            fruitsLabel.onDraw(canvas);
            fruitsLabel.setX(fruitsLabel.getX() - fruitsLabel.getActorWidth());
        }
    }


    public class FruitPoints extends Actor {

        FruitPoints(View view, Playfield playfield,  int currentFrame, float x, float y) {
            super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.food_points), 22, 7, 11, 3, 8, 1, x, y);
            this.currentFrame = currentFrame;
        }

    }


    public class FruitsLabel extends UserInterfaceActor {

        FruitsLabel(View view, Playfield playfield, float x, float y) {
            super(playfield,BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_food), 14, 14, 7, 7, 8, 1, x, y);
        }

    }
}
