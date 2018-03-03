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

		setSpeed(0.08f);
	}
}

