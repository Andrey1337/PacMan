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
    Point scatterPoint;

    private boolean isNormalPoint;

    private float bottomCagePosition;
    private float topCagePosition;
    private float middleCagePositionX;
    private float middleCagePositionY;
    boolean isExiting;

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
        middleCagePositionX = 13.5f;
        middleCagePositionY = (bottomCagePosition + topCagePosition)/2;

        speedInCage = 0.002f;
        frameLengthInMillisecond = 130;
        setSpeed(0.005f);
    }

    public void startExit()
    {
        isExiting = true;
    }

    void exitFromCage(long deltaTime)
    {
        float frameSpeed = speedInCage * deltaTime;

        if(movementDirection == Direction.RIGHT)
            nextPositionX = x + frameSpeed;
        if(movementDirection == Direction.LEFT)
            nextPositionX = x - frameSpeed;
        if(movementDirection == Direction.UP)
            nextPositionY = y - frameSpeed;
        if(movementDirection == Direction.DOWN)
            nextPositionY = y + frameSpeed;

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

        if(movementDirection == Direction.UP && touchTheBottom)
        {
            if(x < middleCagePositionX && nextPositionY <= middleCagePositionY)
            {
                nextPositionY = middleCagePositionY;
                movementDirection = Direction.RIGHT;
            }
            if(x > middleCagePositionX && nextPositionY <= middleCagePositionY)
            {
                nextPositionY = middleCagePositionY;
                movementDirection = Direction.LEFT;
            }
        }

        if(movementDirection == Direction.LEFT && nextPositionX <= middleCagePositionX
                || movementDirection == Direction.RIGHT && nextPositionX >= middleCagePositionX)
        {
            nextPositionX = middleCagePositionX;
            movementDirection = Direction.UP;
        }

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
    }

    void moveInCage(long deltaTime)
    {
        float frameSpeed = speedInCage * deltaTime;

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

    public boolean isInCage() {
        return inCage;
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
                    nextPositionX = nextPositionX + frameSpeed;
                    break;
                case LEFT:
                    nextPositionX = nextPositionX - frameSpeed;
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

    void choseDirection(Point currentPoint, Direction currentDirection) {
        if (nextDirection != Direction.NONE)
            return;

        Point nextPoint = new Point(0,0);

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

        if(nextPoint.isFork(map, currentDirection))
        {
            findShortestDirection(currentDirection, nextPoint);
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
