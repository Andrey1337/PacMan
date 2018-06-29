package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;

public class Clyde extends Ghost {

    public Clyde(Playfield playfield, View view, float x, float y) {
        super(playfield, view, BitmapFactory.decodeResource(view.getResources(), R.drawable.clyde_move), new Point(0, 31), x, y);
        inCage = true;
        movementDirection = Direction.DOWN;
        nextDirection = Direction.NONE;
        animate(0);
    }


    @Override
    void choseNextPoint() {
        destPoint = new Point(Math.round(playfield.getPacman().getX()), Math.round(playfield.getPacman().getY()));
    }

    @Override
    void ghostLogic() {
        Point currentPoint = new Point(Math.round(x), Math.round(y));
        Point pacmanPoint = new Point(Math.round(playfield.getPacman().getX()), Math.round(playfield.getPacman().getY()));

        if (currentPoint.distance(pacmanPoint) > 8) {
            choseNextPoint();
        } else {
            destPoint = scatterPoint;
        }

    }

}
