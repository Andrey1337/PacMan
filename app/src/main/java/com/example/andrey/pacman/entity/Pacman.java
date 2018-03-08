package com.example.andrey.pacman.entity;

import android.graphics.BitmapFactory;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;


public class Pacman extends Actor{



	public Pacman(Playfield playfield, View view, float x, float y) {
		super(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_move) ,x, y, 8,8);
		movementDirection = Direction.NONE;
		lookingDirection = Direction.RIGHT;

		frameLengthInMillisecond = 60;
        turnCutSpeed = 0.02f;
		setSpeed(0.006f);
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
                if(y - frameSpeed < 0 || new Point(currentPoint.x, currentPoint.y - 1).isWall(map) && y - frameSpeed <= currentPoint.y)
                {
                    movementDirection = Direction.NONE;
                    nextPositionY = currentPoint.y;
                }
                else {
                    nextPositionY = y - frameSpeed;
                }
                break;
            case DOWN:
                if(y + frameSpeed >= map[0].length - 1 || new Point(currentPoint.x, currentPoint.y + 1).isWall(map) && y + frameSpeed >= currentPoint.y)
                {
                    movementDirection = Direction.NONE;
                    nextPositionY = currentPoint.y;
                }
                else {
                    nextPositionY = y + frameSpeed;
                }
                break;
            case LEFT:
                if(x - frameSpeed < 0 || new Point(currentPoint.x - 1, currentPoint.y).isWall(map) && x - frameSpeed <= currentPoint.x)
                {
                    movementDirection = Direction.NONE;
                    nextPositionX = currentPoint.x;
                }
                else {
                    nextPositionX = x - frameSpeed;
                }
                break;
            case RIGHT:
                if(x + frameSpeed >= map.length - 1 || new Point(currentPoint.x + 1, currentPoint.y).isWall(map) && x + frameSpeed >= currentPoint.x)
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

        animate();
    }
}

