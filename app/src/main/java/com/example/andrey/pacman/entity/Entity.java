package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public abstract class Entity {

	protected Bitmap bitmap;
	float x;
	float y;
	
	Entity(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float getX()
	{
		return x;
	}
	public float getY()
	{
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public abstract void onDraw(Canvas canvas);

}
