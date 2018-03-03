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

        destPoint = new Point(20, 0);
        movementDirection = Direction.LEFT;
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
        x = nextPositionX;
        y = nextPositionY;
        animate();
    }

    private void pickShortestDirection() {
        if (nextDirection != Direction.NONE)
            return;

        Point currentPoint = new Point(Math.round(x), Math.round(y));
        Point nextPoint= new Point(0,0);

        switch (movementDirection) {
            case RIGHT:
                nextPoint = new Point(currentPoint.x + 1, currentPoint.y);
                break;
            case LEFT:
                nextPoint = new Point(currentPoint.x - 1, currentPoint.y);
                break;
            case UP:
                nextPoint = new Point(currentPoint.x, currentPoint.y - 1);
                break;
            case DOWN:
                nextPoint  = new Point(currentPoint.x, currentPoint.y + 1);
                break;
        }

        if(nextPoint.isFork(map, movementDirection))
        {
            findDirection(movementDirection, nextPoint);
        }
    }

    private void findDirection(Direction currentDirection, Point point) {
        Point minPoint;
        Direction bestDirection = Direction.NONE;
        double minDistance = Double.MAX_VALUE;

        switch (currentDirection) {
            case RIGHT:
                minPoint = new Point(point.x + 1, point.y);
                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.RIGHT;
                }

                minPoint = new Point(point.x, point.y + 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.DOWN;
                }

                minPoint = new Point(point.x, point.y - 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.UP;
                }

                break;
            case LEFT:

                minPoint = new Point(point.x - 1, point.y);
                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.LEFT;
                }

                minPoint = new Point(point.x, point.y + 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.DOWN;
                }

                minPoint = new Point(point.x, point.y - 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.UP;
                }
                break;

            case UP:
                minPoint = new Point(point.x - 1, point.y);
                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.LEFT;
                }

                minPoint = new Point(point.x + 1, point.y );
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.RIGHT;
                }

                minPoint = new Point(point.x, point.y - 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.UP;
                }
                break;
            case DOWN:
                minPoint = new Point(point.x - 1, point.y);
                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.LEFT;
                }

                minPoint = new Point(point.x + 1, point.y );
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.RIGHT;
                }

                minPoint = new Point(point.x, point.y + 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    minDistance = minPoint.distance(destPoint);
                    bestDirection = Direction.DOWN;
                }
                break;
            case NONE:
                break;
        }

        nextDirection = bestDirection;
    }


}
