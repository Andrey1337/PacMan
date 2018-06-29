package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import com.example.andrey.pacman.Playfield;

public class Fruit extends Actor {

    private FruitType fruitType;

    public Fruit(Playfield playfield, Bitmap bitmap, int levelNum) {
        super(playfield, bitmap, 14, 14, 7, 7, 8, 1, 13.5f, 16);

        switch (levelNum) {
            case 1:
                fruitType = FruitType.CHERRY;
                break;
            case 2:
                fruitType = FruitType.STRAWBERRY;
                break;
            case 3:
                fruitType = FruitType.ORANGE;
                break;
            case 4:
                fruitType = FruitType.APPLE;
                break;
            case 5:
                fruitType = FruitType.MELON;
                break;
            case 6:
                fruitType = FruitType.GALAXIAN_BOSS;
                break;
            case 7:
                fruitType = FruitType.BELL;
                break;
            case 8:
                fruitType = FruitType.KEY;
                break;
            default:
                fruitType = FruitType.KEY;
                break;
        }

        currentFrame = fruitType.getDrawPosition();
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public int getScore() {
        return fruitType.getPoints();
    }
}
