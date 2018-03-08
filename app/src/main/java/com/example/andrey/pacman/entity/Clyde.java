package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;

public class Clyde extends Ghost {

    public Clyde(Playfield playfield, View view, float x, float y) {
        super(playfield, view, BitmapFactory.decodeResource(view.getResources(), R.mipmap.clyde_move), new Point(-1, 31), x, y);
        inCage = true;
        movementDirection = Direction.DOWN;
        nextDirection = Direction.NONE;
    }

    @Override
    void choseNextPoint() {

    }
}
