package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.example.andrey.pacman.Playfield;

public class UserInterfaceActor extends Actor {
    protected UserInterfaceActor(Playfield playfield, Bitmap bitmap, int frameWidth, int frameHeight, float actorXOffset, float actorYOffset, int frameCount, int frameMovesCount, float x, float y) {
        super(playfield, bitmap, frameWidth, frameHeight, actorXOffset, actorYOffset, frameCount, frameMovesCount, x, y);
    }

    @Override
    public void onDraw(Canvas canvas) {

        float left = getX();
        float top = getY();

        frameToDraw = new Rect(currentFrame * getFrameWidth(), 0, currentFrame * getFrameWidth() + getFrameWidth(), getFrameHeight());
        RectF whereToDraw = new RectF(left, top, left + getActorWidth(), top + getActorHeight());
        canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
    }
}
