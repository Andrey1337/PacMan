package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;

public class Pinky extends Ghost {

    public Pinky(Playfield playfield, View view, float x, float y) {
        super(playfield, view, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pinky_move), new Point(3, -3), x, y);
        inCage = true;
        movementDirection = Direction.UP;
        nextDirection = Direction.NONE;
    }

    @Override
    protected void choseNextPoint() {
        Pacman pacman = playfield.getPacman();
        Point pacmanPoint = new Point(Math.round(pacman.getX()), Math.round(pacman.getY()));

        switch (pacman.lookingDirection)
        {
            case RIGHT:
                destPoint = new Point(pacmanPoint.x + 4, pacmanPoint.y);
                break;
            case LEFT:
                destPoint = new Point(pacmanPoint.x - 4, pacmanPoint.y);
                break;
            case UP:
                destPoint = new Point(pacmanPoint.x - 4, pacmanPoint.y - 4);
                break;
            case DOWN:
                destPoint = new Point(pacmanPoint.x, pacmanPoint.y + 4);
                break;
            case NONE:
                break;
        }
    }
}
