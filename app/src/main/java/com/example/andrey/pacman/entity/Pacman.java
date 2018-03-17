package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;


public class Pacman extends Actor{

    private Bitmap pacmanStartGame;

    private Bitmap pacmanDying;

    public boolean isPacManBall;

    private boolean isDying;
    private long dyingTime;

	public Pacman(Playfield playfield, View view, float x, float y) {
		super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_move) ,x, y, 8,8);
		currentPoint = new Point(x,y);
		movementDirection = Direction.LEFT;
		lookingDirection = Direction.LEFT;

		pacmanStartGame = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_startgame), frameWidth, frameHeight ,false);

		pacmanDying =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_dying), frameWidth * 12, frameHeight ,false);

		dyingTime = 900;

		frameLengthInMillisecond = 60;
        turnCutSpeed = 0.02f;
		setSpeed(0.006f);
	}

    public void startDying()
    {
        animationTime = 0;
        currentFrame = 0;
        isDying = true;
    }

    public void stopDying()
    {
        animationTime = 0;
        currentFrame = 0;
        isDying = false;
    }

    @Override
    public void move(long deltaTime)
    {
        float frameSpeed = speed * deltaTime;

        currentPoint = new Point(Math.round(x), Math.round(y));

        switch (movementDirection) {
            case NONE:
                break;
            case UP:
                if(new Point(currentPoint.x, currentPoint.y - 1).isWall(map) && y - frameSpeed <= currentPoint.y)
                {
                    movementDirection = Direction.NONE;
                    nextPositionY = currentPoint.y;
                }
                else {
                    nextPositionY = y - frameSpeed;
                }
                break;
            case DOWN:
                if(new Point(currentPoint.x, currentPoint.y + 1).isWall(map) && y + frameSpeed >= currentPoint.y)
                {
                    movementDirection = Direction.NONE;
                    nextPositionY = currentPoint.y;
                }
                else {
                    nextPositionY = y + frameSpeed;
                }
                break;
            case LEFT:
                if(new Point(currentPoint.x - 1, currentPoint.y).isWall(map) && x - frameSpeed <= currentPoint.x && !isInTonel())
                {
                    movementDirection = Direction.NONE;
                    nextPositionX = currentPoint.x;
                }
                else {
                    nextPositionX = x - frameSpeed;
                }
                break;
            case RIGHT:
                if(new Point(currentPoint.x + 1, currentPoint.y).isWall(map) && x + frameSpeed >= currentPoint.x && !isInTonel())
                {
                    movementDirection = Direction.NONE;
                    nextPositionX = currentPoint.x;
                }
                else {
                    nextPositionX = x + frameSpeed;
                }
                break;
        }

        checkNextDirection();

        x = nextPositionX;
        y = nextPositionY;

        checkTunnel();
        animate(deltaTime);
    }

    @Override
    public void animate(long deltaTime)
    {
        if(movementDirection == Direction.NONE)
            return;

        animationTime += deltaTime;
        if(isDying)
        {
            if (animationTime > dyingTime / 12) {
                currentFrame++;
                animationTime = 0;
            }
        }
        else if (animationTime > frameLengthInMillisecond) {
            currentFrame++;
            animationTime = 0;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
        }

        if(isDying)
        {
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;

            frameToDraw.top = 0;
            frameToDraw.bottom = frameToDraw.top + frameHeight;
        }
        else {
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;

            frameToDraw.top = movementDirection.getValue() * frameHeight;
            frameToDraw.bottom = frameToDraw.top + frameHeight;
        }
    }



    @Override
    public void onDraw(Canvas canvas)
    {
        if(!isVisible)
            return;

        float left = playfield.X_OFFSET + x * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_X_OFFSET;

        float top = playfield.Y_OFFSET + y * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_Y_OFFSET;

        RectF whereToDraw = new RectF(left, top, left + actorWidth, top + actorHeight);

        if(isDying)
        {
            canvas.drawBitmap(pacmanDying, frameToDraw, whereToDraw, null);
        }
        else canvas.drawBitmap(isPacManBall ? pacmanStartGame : bitmap, frameToDraw, whereToDraw, null);
    }
}

