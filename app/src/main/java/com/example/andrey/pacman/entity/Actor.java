package com.example.andrey.pacman.entity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;


public abstract class Actor extends Entity{

	private int currentFrame = 0;
	private long lastFrameChangeTime = 0;
	protected int frameLengthInMillisecond = 60;

	private int frameWidth, frameHeight, frameSpace;

	private int frameCount = 2;
    private int framesMovesCount = 4;

	private Rect frameToDraw;

	Playfield playfield;

	Direction movementDirection;
	Direction nextDirection;

	TileSpecification[][] map;
	float speed;
	float nextPositionX;
	float nextPositionY;

	private final int ACTOR_X_OFFSET;
	private final int ACTOR_Y_OFFSET;

	private int actorWidth,actorHeight;

	Actor(Playfield playfield, Bitmap bitmap, float x, float y, float actorXOffset, float actorYOffset) {
		super(x, y);

		this.playfield = playfield;

        frameWidth = 18;
        frameHeight = 18;

		frameWidth *= 6;
		frameHeight *= 6;

        this.bitmap  = Bitmap.createScaledBitmap(bitmap, frameWidth * frameCount + frameSpace * (frameCount - 1),
				frameHeight * framesMovesCount + frameSpace * (framesMovesCount - 1),false);

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

	protected void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void setDirection(Direction direction) {this.movementDirection = direction;}
	
	public Direction getDirection()
	{
		return movementDirection;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		float left = playfield.X_OFFSET + x * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_X_OFFSET;

		float top = playfield.Y_OFFSET + y * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - ACTOR_Y_OFFSET;

		RectF whereToDraw = new RectF(left, top, left + actorWidth, top + actorHeight);

		canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
	}

	public void move()
	{
        int arrayXPos = Math.round(x);
        int arrayYPos = Math.round(y);
		switch (movementDirection) {
			case NONE:
				break;
			case UP:
                if(y - speed <= 0 ) {
                    movementDirection = Direction.NONE;
                    nextPositionY = 0;
                }
                else {
                	if(arrayYPos - 1 >= 0 && map[arrayXPos][arrayYPos - 1] == TileSpecification.WALL && y - speed <= arrayYPos)
					{
						movementDirection = Direction.NONE;
						nextPositionY = arrayYPos;
					}
					else
						nextPositionY = y - speed;
                }
				break;

			case DOWN:
                if(y + speed >= map[0].length - 1) {
                    movementDirection = Direction.NONE;
                    nextPositionY = arrayYPos;
                }
                else {
                	if(arrayYPos + 1 <= map[0].length - 1 && map[arrayXPos][arrayYPos + 1] == TileSpecification.WALL && y + speed >= arrayYPos )
					{
						movementDirection = Direction.NONE;
						nextPositionY = arrayYPos;
					}
					else nextPositionY = y + speed;
                }
				break;
			case LEFT:
                if(x - speed <= 0) {
                    movementDirection = Direction.NONE;
                    nextPositionX = 0;
                }
                else {
                	if(arrayXPos - 1 >= 0 && map[arrayXPos - 1][arrayYPos] == TileSpecification.WALL && x - speed <= arrayXPos) {
						movementDirection = Direction.NONE;
						nextPositionX = arrayXPos;
					}
                    else nextPositionX = x - speed;
                }
				break;
			case RIGHT:
				if(x + speed >= map.length - 1) {
					movementDirection = Direction.NONE;
					nextPositionX = arrayXPos;
				}
				else {
					if(arrayXPos + 1 <= map.length - 1 && map[arrayXPos + 1][arrayYPos] == TileSpecification.WALL && x + speed >= arrayXPos )
					{
						movementDirection = Direction.NONE;
						nextPositionX = arrayXPos;
					}
					else nextPositionX = x + speed;
				}
				break;
		}

		checkNextDirection();

		x = nextPositionX;
		y = nextPositionY;

		animate();
	}

	void animate()
	{
        if(movementDirection == Direction.NONE)
            return;
		long time = System.currentTimeMillis();

        if (time > lastFrameChangeTime + frameLengthInMillisecond) {
            lastFrameChangeTime = time;
            currentFrame++;

            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
        }

		frameToDraw.left = currentFrame * frameWidth + frameSpace * currentFrame;
		frameToDraw.right = frameToDraw.left + frameWidth;

		frameToDraw.top = movementDirection.getValue() * frameHeight + frameSpace * movementDirection.getValue() ;
		frameToDraw.bottom = frameToDraw.top + frameHeight;
    }

	void checkNextDirection()
	{
		int arrayXPos = Math.round(x);
		int arrayYPos = Math.round(y);
		switch (nextDirection) {
			case NONE:
				return;

			case UP:
				if(arrayYPos != 0 && map[arrayXPos][arrayYPos - 1] != TileSpecification.WALL && (x <= arrayXPos
						&& nextPositionX >= arrayXPos || x >= arrayXPos && nextPositionX <= arrayXPos))
				{
					nextPositionX = arrayXPos;
					movementDirection = Direction.UP;
					nextDirection = Direction.NONE;
				}
				break;
			case DOWN:
				if(arrayYPos < map[1].length - 1
						&& map[arrayXPos][arrayYPos + 1] != TileSpecification.WALL
						&&(x <= arrayXPos && nextPositionX >= arrayXPos || x >= arrayXPos && nextPositionX <= arrayXPos)) {
					nextPositionX = arrayXPos;
					movementDirection = Direction.DOWN;
					nextDirection = Direction.NONE;
				}
				break;
			case LEFT:
				if(arrayXPos > 0
						&& map[arrayXPos-1][arrayYPos] != TileSpecification.WALL
						&& (y <= arrayYPos && nextPositionY >= arrayYPos || y >= arrayYPos && nextPositionY <= arrayYPos))
				{
					nextPositionY = arrayYPos;
					movementDirection = Direction.LEFT;
					nextDirection = Direction.NONE;
				}
				break;
			case RIGHT:
				if(arrayXPos < map.length - 1
						&& map[arrayXPos+1][arrayYPos] != TileSpecification.WALL
						&& (y <= arrayYPos && nextPositionY >= arrayYPos || y >= arrayYPos && nextPositionY <= arrayYPos))
				{
				nextPositionY = arrayYPos;
				movementDirection = Direction.RIGHT;
				nextDirection = Direction.NONE;
				}
				break;
		}
	}

}
