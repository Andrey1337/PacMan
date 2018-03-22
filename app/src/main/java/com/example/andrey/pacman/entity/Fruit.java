package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import com.example.andrey.pacman.Playfield;

public class Fruit extends Actor {

    private FRUIT_TYPE fruit_type;

    public Fruit(Playfield playfield, Bitmap bitmap, FRUIT_TYPE fruit_type) {
        super(playfield, bitmap, 14, 14, 7 , 7 , 8,1,13.5f,16);

        this.fruit_type = fruit_type;
        currentFrame = fruit_type.getDrawPosition();
    }

    public int getScore()
    {
        return fruit_type.getPoints();
    }
}
