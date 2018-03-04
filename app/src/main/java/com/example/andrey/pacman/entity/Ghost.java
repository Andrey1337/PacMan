package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.GameMode;
import com.example.andrey.pacman.Playfield;

public abstract class Ghost extends Actor {

    private Bitmap scaryGhost;
    protected boolean inCage;

    protected Direction prevDirection;

    Point destPoint;
    Point scatterPoint;

    Ghost(Playfield playfield, View view, Bitmap bitmap,Point scatterPoint, float x, float y) {
        super(playfield, bitmap, x, y, 8, 8);

        this.scatterPoint = scatterPoint;
        destPoint = this.scatterPoint;

        movementDirection = Direction.LEFT;
        nextDirection = Direction.NONE;
        prevDirection = movementDirection;
        frameLengthInMillisecond = 200;
        setSpeed(0.065f);
    }

    public void changeMode(GameMode gameMode)
    {
        movementDirection = movementDirection.getOposite();

        switch (gameMode)
        {
            case CHASE:
                choseNextPoint();
                break;
            case SCATTER:
                destPoint = scatterPoint;
                break;
            case FRIGHTENED:
                break;
        }
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

        Point currentPoint = new Point(Math.round(x), Math.round(y));

        if(currentPoint.isEqual(destPoint))
        {
            choseNextPoint();
        }

        choseDirection(currentPoint);

        checkNextDirection();

        x = nextPositionX;
        y = nextPositionY;

        animate();
    }

    abstract void choseNextPoint();


    private void choseDirection(Point currentPoint) {
        if (nextDirection != Direction.NONE)
            return;

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
            findShortestDirection(movementDirection, nextPoint);
        }
    }

    private void findShortestDirection(Direction currentDirection, Point point) {
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
