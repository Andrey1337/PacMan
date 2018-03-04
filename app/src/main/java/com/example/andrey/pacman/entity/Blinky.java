package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;

public class Blinky extends Ghost {

    public Blinky(Playfield playfield, View view,float x, float y) {
        super(playfield, view, BitmapFactory.decodeResource(view.getResources(), R.mipmap.blinky_move),new Point(25, -3),x,y);

        inCage = false;
    }

    @Override
    void choseNextPoint() {
        destPoint = new Point(Math.round(playfield.getPacman().getX()), Math.round(playfield.getPacman().getY()));
    }
}
