package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.GameMode;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;

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
    private boolean isExiting;

    private float speedInCage;
    private float speedInTonel;
    private float speedInFrightened;

    private boolean touchTheBottom;

    private boolean isFrightened;

    private boolean isEyes;

    Ghost(Playfield playfield, View view, Bitmap bitmap, Point scatterPoint, float x, float y) {
        super(playfield, bitmap, x, y, 8, 8);


        scaryGhost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.frightened), frameWidth * 2, frameHeight* 2,false);

        this.scatterPoint = scatterPoint;
        destPoint = this.scatterPoint;

        movementDirection = Direction.LEFT;
        nextDirection = Direction.NONE;

        topCagePosition = 12.50f;
        bottomCagePosition = 13.50f;
        middleCagePositionX = 13.5f;
        middleCagePositionY = (bottomCagePosition + topCagePosition)/2;

        speedInTonel = 0.003f;
        speedInCage = 0.002f;
        speedInFrightened = 0.003f;
        frameLengthInMillisecond = 110;
        setSpeed(0.005f);
    }

    public void startExit()
    {
        isExiting = true;
    }

    private void exitFromCage(long deltaTime)
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

    private void moveInCage(long deltaTime)
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

    public boolean isEyes() {
        return isEyes;
    }

    public void changeMode(GameMode newGameMode)
    {
        isFrightened = newGameMode == GameMode.FRIGHTENED;

        if(inCage)
            return;

        /*if(prevGameMode != GameMode.FRIGHTENED) {
            movementDirection = movementDirection.getOposite();
            nextDirection = Direction.NONE;
        }*/
        switch (newGameMode)
        {
            case CHASE:
                choseNextPoint();
                isNormalPoint = !destPoint.isWall(map);
                isFrightened = false;
                break;
            case SCATTER:
                destPoint = scatterPoint;
                isFrightened = false;
                break;

        }

        choseDirection(movementDirection);
    }

    @Override
    void animate(long deltaTime) {

        if(movementDirection == Direction.NONE)
            return;

        animationTime += deltaTime;
        if (animationTime > frameLengthInMillisecond) {
            currentFrame++;
            animationTime = 0;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
        }

        if(!isFrightened) {
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;

            frameToDraw.top = movementDirection.getValue() * frameHeight;
            frameToDraw.bottom = frameToDraw.top + frameHeight;
        }
        else {
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;
            frameToDraw.top = 0;
            frameToDraw.bottom = frameToDraw.top + frameHeight;
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        float left = playfield.X_OFFSET + x * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_X_OFFSET;

        float top = playfield.Y_OFFSET + y * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_Y_OFFSET;

        RectF whereToDraw = new RectF(left, top, left + actorWidth, top + actorHeight);

        canvas.drawBitmap(isFrightened ? scaryGhost: bitmap, frameToDraw, whereToDraw, null);
    }

    public boolean isInCage() {
        return inCage;
    }

    @Override
    public void move(long deltaTime) {

        float frameSpeed = deltaTime;
        if(isFrightened)
            frameSpeed *= speedInFrightened;
        else if(isInTonel())
            frameSpeed *= speedInTonel;
        else
            frameSpeed *= speed;

        if(inCage)
        {
            if(!isExiting)
                moveInCage(deltaTime);
            else
                exitFromCage(deltaTime);
        }
        else
        {
            currentPoint = new Point(Math.round(x), Math.round(y));
            switch (movementDirection) {
                case RIGHT:
                    nextPositionX = x + frameSpeed;
                    break;
                case LEFT:
                    nextPositionX = x - frameSpeed;
                    break;
                case UP:
                    nextPositionY = y - frameSpeed;
                    break;
                case DOWN:
                    nextPositionY = y + frameSpeed;
                    break;
            }

            ghostLogic();

            choseDirection( movementDirection);
            this.checkNextDirection();
        }

        x = nextPositionX;
        y = nextPositionY;

        checkTunnel();
        animate(deltaTime);
    }

    void ghostLogic()
    {
        Point currentPoint = new Point(Math.round(x), Math.round(y));
        if (currentPoint.isEqual(destPoint) || (!isNormalPoint && playfield.getGameMode() == GameMode.CHASE)) {
            choseNextPoint();
            isNormalPoint = !destPoint.isWall(map);
        }

    }

    public void beEaten()
    {
        isEyes = true;
        isFrightened = false;
        destPoint = new Point(13, 10);
        movementDirection = Direction.NONE;
        nextDirection = Direction.NONE;
    }

    abstract void choseNextPoint();

    private void choseDirection(Direction currentDirection) {
        if (nextDirection != Direction.NONE || isInTonel())
            return;

        Point nextPoint = new Point(0, 0);

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
                nextPoint = new Point(currentPoint.x, currentPoint.y + 1);
                break;
            case NONE:
                nextPoint = new Point(currentPoint.x, currentPoint.y);
                break;
        }

        if(nextPoint.isWall(map))
        {
            findShortestDirection(currentDirection, currentPoint);
        }
        else if(nextPoint.isFork(map, currentDirection))
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

                minPoint = new Point(point.x, point.y - 1);
                if(!minPoint.isWall(map) &&  minPoint.distance(destPoint) < minDistance)
                {
                    bestDirection = Direction.UP;
                }

                break;
        }

       nextDirection = bestDirection;
    }
}
