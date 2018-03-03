package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Actor;

public abstract class Ghost extends Actor {

    private Bitmap scaryGhost;
    protected boolean inCage;

    protected Direction prevDirection;

    Point destPoint;

    Ghost(Playfield playfield, View view, Bitmap bitmap, float x, float y) {
        super(playfield, bitmap, x, y, 8, 8);

        destPoint = new Point(20, 5);
        movementDirection = Direction.RIGHT;
        prevDirection = movementDirection;
        frameLengthInMillisecond = 200;
        setSpeed(0.06f);
    }

    @Override
    public void move() {

        switch (movementDirection) {
            case RIGHT:
                nextPositionX += speed;
                break;
            case LEFT:
                nextPositionX -= speed;
                break;
            case UP:
                nextPositionY -= speed;
                break;
            case DOWN:
                nextPositionY += speed;
                break;
        }


        pickShortestDirection();

        checkNextDirection();
    }

    private void pickShortestDirection() {
        if (nextDirection != Direction.NONE)
            return;

        Point currentPoint = new Point(Math.round(x), Math.round(y));
        Point nextPoint;

        switch (movementDirection) {
            case RIGHT:
                nextPoint = new Point(currentPoint.x + 1, currentPoint.y);
                if (!nextPoint.isWall(map)) {
                    if (!new Point(nextPoint.x, nextPoint.y + 1).isWall(map)) {

                    }
                }

                break;
            case LEFT:
                break;
            case UP:
                break;
            case DOWN:
                break;
        }
    }

    private void findDirection(Direction currentDirection, Point point) {
        Direction bestDirection = Direction.NONE;
        switch (currentDirection) {
            case RIGHT:
                double minDistance = Double.MAX_VALUE;

                Point minPoint = new Point(point.x + 1, point.y);
                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
                    bestDirection = Direction.RIGHT;
                }
                break;
            case LEFT:
                break;
            case UP:
                break;
            case DOWN:
                break;
            case NONE:
                break;
        }

    }
}
