package com.example.andrey.pacman;

import android.graphics.Bitmap;
import android.view.View;
import com.example.andrey.pacman.entity.Ghost;
import com.example.andrey.pacman.entity.Point;

public class Pinky extends Ghost {


    Pinky(Playfield playfield, View view, Point scatterPoint, float x, float y) {
        super(playfield, view, bitmap, scatterPoint, x, y);
    }

    @Override
    protected void choseNextPoint() {

    }
}
