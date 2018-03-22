package com.example.andrey.pacman.entity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.TileSpecification;


public abstract class PlayfieldActor extends Actor{


	Direction movementDirection;
	Direction nextDirection;
	Direction lookingDirection;
	Point currentPoint;



	TileSpecification[][] map;

	float speed;
	float nextPositionX;
	float nextPositionY;

	PlayfieldActor(Playfield playfield, Bitmap bitmap, float x, float y, int frameWidth,int frameHeight, float actorXOffset, float actorYOffset, int frameCount, int frameMovesCount) {
		super(playfield, bitmap,frameWidth,frameHeight, actorXOffset,  actorYOffset, frameCount,  frameMovesCount,x, y);

		nextPositionX = x;
		nextPositionY = y;
		nextDirection = Direction.NONE;

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

	public abstract void move(long deltaTime);

	boolean isInTonel()
	{
		return y == 13 && (x < 4 || x > map.length - 5);
	}

	long animationTime;
	public abstract void animate(long deltaTime);

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
