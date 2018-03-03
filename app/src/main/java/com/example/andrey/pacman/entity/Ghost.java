package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.view.View;
import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.entity.Actor;

public abstract class Ghost extends Actor {

	private Bitmap scaryGhost;
	protected boolean inCage;


	Ghost(Playfield playfield, View view, Bitmap bitmap, float x, float y) {
		super(playfield, bitmap,x, y,8,8);

		movementDirection = Direction.RIGHT;
		frameLengthInMillisecond = 200;
		setSpeed(0.06f);
	}

}
