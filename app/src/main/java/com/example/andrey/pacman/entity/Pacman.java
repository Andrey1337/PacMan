package com.example.andrey.pacman.entity;

import android.graphics.*;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;


public class Pacman extends PlayfieldActor {

    private Bitmap pacmanStartGame;
    private Bitmap pacmanDying;

    public boolean isPacManBall;

    private boolean isDying;
    private long dyingTime;

    public Pacman(Playfield playfield, View view, float x, float y) {
        super(playfield, BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_move),
                x, y, 18, 18, 8, 8, 2, 4);

        currentPoint = new Point(x, y);
        movementDirection = Direction.LEFT;
        lookingDirection = Direction.LEFT;

        isPacManBall = true;

        pacmanStartGame = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_startgame), frameWidth, frameHeight, false);
        pacmanDying = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_dying), frameWidth * 12, frameHeight, false);

        dyingTime = 1000;

        frameLengthInMillisecond = 55;
        turnCutSpeed = 0.15f;
        setSpeed(0.006f);
    }

    public void startDying() {
        animationTime = 0;
        currentFrame = 0;
        isDying = true;
    }

    public void stopDying() {
        animationTime = 0;
        currentFrame = 0;
        isDying = false;
    }

    @Override
    public void move(long deltaTime) {
        float frameSpeed = speed * deltaTime;

        currentPoint = new Point(Math.round(x), Math.round(y));

        switch (movementDirection) {
            case NONE:
                break;
            case UP:
                if (new Point(currentPoint.x, currentPoint.y - 1).isWall(map) && y - frameSpeed <= currentPoint.y) {
                    movementDirection = Direction.NONE;
                    nextPositionY = currentPoint.y;
                } else {
                    nextPositionY = y - frameSpeed;
                    nextPositionX = currentPoint.x;
                }
                break;
            case DOWN:
                if (new Point(currentPoint.x, currentPoint.y + 1).isWall(map) && y + frameSpeed >= currentPoint.y) {
                    movementDirection = Direction.NONE;
                    nextPositionY = currentPoint.y;
                } else {
                    nextPositionY = y + frameSpeed;
                    nextPositionX = currentPoint.x;
                }
                break;
            case LEFT:
                if (new Point(currentPoint.x - 1, currentPoint.y).isWall(map) && x - frameSpeed <= currentPoint.x && !isInTonel()) {
                    movementDirection = Direction.NONE;
                    nextPositionX = currentPoint.x;
                } else {
                    nextPositionX = x - frameSpeed;
                    nextPositionY = currentPoint.y;
                }
                break;
            case RIGHT:
                if (new Point(currentPoint.x + 1, currentPoint.y).isWall(map) && x + frameSpeed >= currentPoint.x && !isInTonel()) {
                    movementDirection = Direction.NONE;
                    nextPositionX = currentPoint.x;
                } else {
                    nextPositionX = x + frameSpeed;
                    nextPositionY = currentPoint.y;
                }
                break;
        }

        checkNextDirection(frameSpeed);

        x = nextPositionX;
        y = nextPositionY;

        checkTunnel();
        animate(deltaTime);
    }


    private float turnCutSpeed;

    private void checkNextDirection(float speed) {
        float nextPositionXWithCut = nextPositionX;
        float nextPositionYWithCut = nextPositionY;
        switch (movementDirection) {
            case RIGHT:
                nextPositionXWithCut += turnCutSpeed;
                break;
            case LEFT:
                nextPositionXWithCut -= turnCutSpeed;
                break;
            case UP:
                nextPositionYWithCut -= turnCutSpeed;
                break;
            case DOWN:
                nextPositionYWithCut += turnCutSpeed;
                break;

        }

        switch (nextDirection) {
            case NONE:
                return;
            case UP:
                if (!new Point(currentPoint.x, currentPoint.y - 1).isWall(map)
                        && (x <= currentPoint.x && nextPositionXWithCut >= currentPoint.x
                        || x >= currentPoint.x && nextPositionXWithCut <= currentPoint.x)) {
                    nextPositionY = y - speed / 2;
                    movementDirection = Direction.UP;
                    nextDirection = Direction.NONE;
                }
                break;
            case DOWN:
                if (!new Point(currentPoint.x, currentPoint.y + 1).isWall(map)
                        && (x <= currentPoint.x && nextPositionXWithCut >= currentPoint.x
                        || x >= currentPoint.x && nextPositionXWithCut <= currentPoint.x)) {
                    nextPositionY = y + speed / 2;
                    movementDirection = Direction.DOWN;
                    nextDirection = Direction.NONE;
                }
                break;
            case RIGHT:
                if (!new Point(currentPoint.x + 1, currentPoint.y).isWall(map)
                        && (y <= currentPoint.y && nextPositionYWithCut >= currentPoint.y
                        || y >= currentPoint.y && nextPositionYWithCut <= currentPoint.y)) {
                    nextPositionX = x + speed / 2;
                    movementDirection = Direction.RIGHT;
                    nextDirection = Direction.NONE;
                }
                break;
            case LEFT:
                if (!new Point(currentPoint.x - 1, currentPoint.y).isWall(map)
                        && (y <= currentPoint.y && nextPositionYWithCut >= currentPoint.y
                        || y >= currentPoint.y && nextPositionYWithCut <= currentPoint.y)) {
                    nextPositionX = x - speed / 2;
                    movementDirection = Direction.LEFT;
                    nextDirection = Direction.NONE;
                }
                break;

        }

        lookingDirection = movementDirection;
    }

    @Override
    public void animate(long deltaTime) {
        if (movementDirection == Direction.NONE && !isDying)
            return;

        animationTime += deltaTime;
        if (isDying) {
            if (animationTime > dyingTime / 12) {
                currentFrame++;
                animationTime = 0;
            }
        } else if (animationTime > frameLengthInMillisecond) {
            currentFrame++;
            animationTime = 0;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
        }

        if (isDying) {
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;

            frameToDraw.top = 0;
            frameToDraw.bottom = frameToDraw.top + frameHeight;
        } else {
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;

            frameToDraw.top = movementDirection.getValue() * frameHeight;
            frameToDraw.bottom = frameToDraw.top + frameHeight;
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (!isVisible)
            return;

        float left = playfield.X_OFFSET + x * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_X_OFFSET;

        float top = playfield.Y_OFFSET + y * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_Y_OFFSET + playfield.STARTPOS_Y;

        RectF whereToDraw = new RectF(left, top, left + actorWidth, top + actorHeight);

        if (isDying)
            canvas.drawBitmap(pacmanDying, frameToDraw, whereToDraw, null);
        else if (isPacManBall)
            canvas.drawBitmap(pacmanStartGame, new Rect(0, 0, frameWidth, frameHeight), whereToDraw, null);
        else canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
    }
}

