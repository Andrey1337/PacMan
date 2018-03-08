package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.GameMode;
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
    public void move(long deltaTime) {
        float frameSpeed = speed * deltaTime;

        if(inCage)
        {
            if(!isExiting)
                moveInCage(deltaTime);
            else
                exitFromCage(deltaTime);
        }
        else {
            switch (movementDirection) {
                case RIGHT:
                    nextPositionX += frameSpeed;
                    break;
                case LEFT:
                    nextPositionX -= frameSpeed;
                    break;
                case UP:
                    nextPositionY -= frameSpeed;
                    break;
                case DOWN:
                    nextPositionY += frameSpeed;
                    break;
            }

            Point currentPoint = new Point(Math.round(x), Math.round(y));
            Point pacmanPoint = new Point(Math.round(playfield.getPacman().getX()),Math.round(playfield.getPacman().getY()));

            if(playfield.getGameMode() == GameMode.CHASE) {
                if (currentPoint.distance(pacmanPoint) > 8) {
                    choseNextPoint();
                } else {
                    destPoint = scatterPoint;
                }
            }

            this.choseDirection(currentPoint, movementDirection);

            this.checkNextDirection();
        }

        x = nextPositionX;
        y = nextPositionY;

        animate();
    }

    @Override
    void choseNextPoint() {
        destPoint = new Point(Math.round(playfield.getPacman().getX()),Math.round(playfield.getPacman().getY()));
    }
}
