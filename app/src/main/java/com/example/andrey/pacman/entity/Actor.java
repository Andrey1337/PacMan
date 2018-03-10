package com.example.andrey.pacman.entity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.TileSpecification;


public abstract class Actor extends Entity{

	int currentFrame = 0;
	int frameLengthInMillisecond;

	int frameWidth, frameHeight;

	int frameCount = 2;
    private int framesMovesCount = 4;

    Rect frameToDraw;

	Playfield playfield;

	Direction movementDirection;
	Direction nextDirection;
	Direction lookingDirection;

	Point currentPoint;

	TileSpecification[][] map;
	float speed;
	float nextPositionX;
	float nextPositionY;

	final int ACTOR_X_OFFSET;
	final int ACTOR_Y_OFFSET;

	int actorWidth,actorHeight;


	Actor(Playfield playfield, Bitmap bitmap, float x, float y, float actorXOffset, float actorYOffset) {
		super(x, y);

		this.playfield = playfield;

        frameWidth = 18;
        frameHeight = 18;

		frameWidth *= 6;
		frameHeight *= 6;

        this.bitmap  = Bitmap.createScaledBitmap(bitmap, frameWidth * frameCount, frameHeight * framesMovesCount,false);

		actorWidth = (int)((float)bitmap.getWidth() * playfield.scale / (float)frameCount);
		actorHeight = (int)((float)bitmap.getHeight() * playfield.scale / (float)framesMovesCount);

		nextPositionX = x;
		nextPositionY = y;
		nextDirection = Direction.NONE;


		frameToDraw = new Rect(0, 0, frameWidth, frameHeight);

		ACTOR_X_OFFSET = (int)(actorXOffset / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
		ACTOR_Y_OFFSET = (int)(actorYOffset / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());

		map = playfield.getMap();
	}


	public void setRequestDirection(Direction direction)
	{
		nextDirection = direction;
	}

	void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public abstract void onDraw(Canvas canvas);

	public abstract void move(long deltaTime);

	boolean isInTonel()
	{
		if(y == 13 && (x < 4 || x > map.length - 5))
		{
			return true;
		}
		return false;
	}


	long animationTime;
	void animate(long deltaTime)
	{
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

		frameToDraw.left = currentFrame * frameWidth;
		frameToDraw.right = frameToDraw.left + frameWidth;

		frameToDraw.top = movementDirection.getValue() * frameHeight;
		frameToDraw.bottom = frameToDraw.top + frameHeight;
    }

	void checkTunnel()
	{
		if(!isInTonel())
			return;

		if(x < - 1.5)
		{
			x = map.length + 1.5f;
		}

		if(x > map.length + 1.5f)
		{
			x = -1.5f;
		}


	}

	float turnCutSpeed;
	void checkNextDirection()
	{
		float nextPositionXWithCut = nextPositionX;
		float nextPositionYWithCut = nextPositionY;
		switch (movementDirection)
		{
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

	    switch (nextDirection)
        {
            case NONE:
                return;
            case UP:
                if(!new Point(currentPoint.x, currentPoint.y - 1).isWall(map)
                        && (x <= currentPoint.x && nextPositionXWithCut >= currentPoint.x
						|| x >= currentPoint.x && nextPositionXWithCut <= currentPoint.x))
				{
					nextPositionX = currentPoint.x;
					movementDirection = Direction.UP;
					nextDirection = Direction.NONE;
				}
                break;
            case DOWN:
				if(!new Point(currentPoint.x, currentPoint.y + 1).isWall(map)
						&& (x <= currentPoint.x && nextPositionXWithCut >= currentPoint.x
						|| x >= currentPoint.x && nextPositionXWithCut <= currentPoint.x))
				{
					nextPositionX = currentPoint.x;
					movementDirection = Direction.DOWN;
					nextDirection = Direction.NONE;
				}
                break;
            case RIGHT:
				if(!new Point(currentPoint.x + 1, currentPoint.y).isWall(map)
						&& (y <= currentPoint.y && nextPositionYWithCut >= currentPoint.y
						|| y >= currentPoint.y && nextPositionYWithCut <= currentPoint.y))
				{
					nextPositionY = currentPoint.y;
					movementDirection = Direction.RIGHT;
					nextDirection = Direction.NONE;
				}
                break;
            case LEFT:
				if(!new Point(currentPoint.x - 1, currentPoint.y).isWall(map)
						&& (y <= currentPoint.y && nextPositionYWithCut >= currentPoint.y
						|| y >= currentPoint.y && nextPositionYWithCut <= currentPoint.y))
				{
					nextPositionY = currentPoint.y;
					movementDirection = Direction.LEFT;
					nextDirection = Direction.NONE;
				}
                break;

        }

		lookingDirection = movementDirection;
	}

}
