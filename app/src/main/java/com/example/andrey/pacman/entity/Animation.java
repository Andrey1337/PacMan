package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Animation {

	Rect destRect; 
	Rect sourceRect;
	Bitmap image;
	public Animation(Bitmap imageSource)
	{
		image = imageSource;
		destRect = new Rect(0,0,imageSource.getWidth(),imageSource.getHeight());
		sourceRect = new Rect(0,0, 16,16);
	}
		
	public void Draw(Canvas canvas)
	{
		canvas.drawBitmap(image, sourceRect, destRect, null);
	}
}
