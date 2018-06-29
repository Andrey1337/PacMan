package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;

public class Inky extends Ghost {

    public Inky(Playfield playfield, View view, float x, float y) {
        super(playfield, view, BitmapFactory.decodeResource(view.getResources(), R.drawable.inky_move), new Point(28, 31), x, y);
        inCage = true;
        movementDirection = Direction.DOWN;
        nextDirection = Direction.NONE;
        animate(0);
    }

    @Override
    void choseNextPoint() {

        Pacman pacman = playfield.getPacman();
        Point pacmanPoint = new Point(Math.round(pacman.getX()), Math.round(pacman.getY()));

        Blinky blinky = playfield.getBlinky();
        Point blinkyPoint = new Point(Math.round(blinky.getX()), Math.round(blinky.getY()));

        switch (pacman.lookingDirection) {
            case RIGHT:
                pacmanPoint = new Point(pacmanPoint.x + 2, pacmanPoint.y);
                break;
            case LEFT:
                pacmanPoint = new Point(pacmanPoint.x - 2, pacmanPoint.y);
                break;
            case UP:
                pacmanPoint = new Point(pacmanPoint.x - 2, pacmanPoint.y - 2);
                break;
            case DOWN:
                pacmanPoint = new Point(pacmanPoint.x, pacmanPoint.y + 2);
                break;
            case NONE:
                break;
        }

        Point vector = new Point(pacmanPoint.x - blinkyPoint.x, pacmanPoint.y - blinkyPoint.y);
        destPoint = new Point(pacmanPoint.x + vector.x, pacmanPoint.y + vector.y);
    }

}
