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

	int frameCount;

    Rect frameToDraw;

	Playfield playfield;

	Direction movementDirection;
	Direction nextDirection;
	Direction lookingDirection;

	Point currentPoint;

	public boolean isVisible = true;

	TileSpecification[][] map;
	float speed;
	float nextPositionX;
	float nextPositionY;

	final int ACTOR_X_OFFSET;
	final int ACTOR_Y_OFFSET;

	int actorWidth,actorHeight;


	Actor(Playfield playfield, Bitmap bitmap, float x, float y, float actorXOffset, float actorYOffset, int frameCount, int frameMovesCount) {
		super(x, y);

		this.playfield = playfield;

        frameWidth = 18;
        frameHeight = 18;

		frameWidth *= 6;
		frameHeight *= 6;

		this.frameCount = frameCount;

        this.bitmap = Bitmap.createScaledBitmap(bitmap, frameWidth * frameCount,
				frameHeight * frameMovesCount,false);

		actorWidth = (int)((float)bitmap.getWidth() * playfield.scale / (float)frameCount);
		actorHeight = (int)((float)bitmap.getHeight() * playfield.scale / (float) frameMovesCount);

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

	public void move(long deltaTime){}

	boolean isInTonel()
	{
		return y == 13 && (x < 4 || x > map.length - 5);
	}


	long animationTime;
	public void animate(long deltaTime) {}

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


}
