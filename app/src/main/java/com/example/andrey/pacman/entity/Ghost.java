package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.GameMode;
import com.example.andrey.pacman.Playfield;

public abstract class Ghost extends Actor {

    private Bitmap scaryGhost;
    boolean inCage;

    Point destPoint;
    private Point scatterPoint;

    private boolean isNormalPoint;

    private float bottomCagePosition;
    private float topCagePosition;
    private boolean exiting;

    private float speedInCage;

    private boolean touchTheBottom;

    Ghost(Playfield playfield, View view, Bitmap bitmap, Point scatterPoint, float x, float y) {
        super(playfield, bitmap, x, y, 8, 8);

        this.scatterPoint = scatterPoint;
        destPoint = this.scatterPoint;

        movementDirection = Direction.LEFT;
        nextDirection = Direction.NONE;

        topCagePosition = 12.50f;
        bottomCagePosition = 13.50f;

        speedInCage = 0.04f;
        frameLengthInMillisecond = 130;
        setSpeed(0.085f);
    }

    public void startExit()
    {
        exiting = true;
    }

    private void exitFromCage(long deltaTime)
    {
        float frameSpeed = speedInCage * deltaTime / 17;

        if(movementDirection == Direction.UP)
            nextPositionY = y - frameSpeed;
        if(movementDirection == Direction.DOWN)
            nextPositionY = y + frameSpeed;


        if(movementDirection == Direction.UP)
        {
            if(nextPositionY <= topCagePosition)
            {
                if(touchTheBottom)
                {
                    movementDirection = Direction.LEFT;
                    inCage = false;
                }
                else {
                    movementDirection = movementDirection.getOposite();
                }
                nextPositionY = topCagePosition;
            }
        }

        if(movementDirection == Direction.DOWN)
        {
            if(nextPositionY >= bottomCagePosition)
            {
                nextPositionY = bottomCagePosition;
                touchTheBottom = true;
                topCagePosition = 10f;
                movementDirection = movementDirection.getOposite();
            }
        }

    }

    private void moveInCage(long deltaTime)
    {
        float frameSpeed = speedInCage * deltaTime / 17;

        if(movementDirection == Direction.UP)
            nextPositionY -= frameSpeed;
        if(movementDirection == Direction.DOWN)
            nextPositionY += frameSpeed;

        if(nextPositionY >= bottomCagePosition)
        {
            nextPositionY = bottomCagePosition;
            movementDirection = movementDirection.getOposite();
        }

        if(nextPositionY < topCagePosition)
        {
            nextPositionY = topCagePosition;
            movementDirection = movementDirection.getOposite();
        }

        if(nextPositionY > bottomCagePosition)
        {
            nextPositionY = bottomCagePosition;
            movementDirection = movementDirection.getOposite();
        }
    }

    public void changeMode(GameMode gameMode)
    {
        if(inCage)
            return;
        movementDirection = movementDirection.getOposite();

        switch (gameMode)
        {
            case CHASE:
                choseNextPoint();
                isNormalPoint = !destPoint.isWall(map);
                break;
            case SCATTER:
                destPoint = scatterPoint;
                break;
            case FRIGHTENED:
                break;
        }

        choseDirection(new Point(Math.round(x), Math.round(y)), movementDirection);
    }

    @Override
    public void move(long deltaTime) {

        float frameSpeed = speed * deltaTime / 17;

        if(inCage)
        {
            if(!exiting)
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

            if (currentPoint.isEqual(destPoint) || (!isNormalPoint && playfield.getGameMode() == GameMode.CHASE)) {
                choseNextPoint();
                isNormalPoint = !destPoint.isWall(map);
            }

            choseDirection(currentPoint, movementDirection);

            this.checkNextDirection();
        }

        x = nextPositionX;
        y = nextPositionY;

        animate();
    }

    abstract void choseNextPoint();

    private void choseDirection(Point currentPoint, Direction currentDirection) {
        if (nextDirection != Direction.NONE)
            return;

        Point nextPoint= new Point(0,0);

        switch (currentDirection) {
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

        if(nextPoint.isWall(map))
        {
            findShortestDirection(movementDirection, currentPoint);
        }
        else if(nextPoint.isFork(map, movementDirection))
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

                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
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
                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance) {
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

                if (!minPoint.isWall(map) && minPoint.distance(destPoint) < minDistance ) {
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
                    bestDirection = Direction.DOWN;
                }
                break;
            case NONE:
                break;
        }

        if(bestDirection == Direction.NONE)
            nextDirection = movementDirection.getOposite();
        else nextDirection = bestDirection;
    }
}
