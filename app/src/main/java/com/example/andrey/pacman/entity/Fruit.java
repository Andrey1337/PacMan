package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.example.andrey.pacman.Playfield;

public class Fruit extends Actor {

    private FRUIT_TYPE fruit_type;

    public Fruit(Playfield playfield, Bitmap bitmap, FRUIT_TYPE fruit_type) {
        super(playfield, bitmap, 13.5f, 16, 7 , 7 , 8,1);

        this.fruit_type = fruit_type;
    }

    public int getScore()
    {
        return fruit_type.getPoints();
    }

    public int getDrawPos()
    {
        return fruit_type.getDrawPosition();
    }

    @Override
    public void onDraw(Canvas canvas) {

        float left = playfield.X_OFFSET + x * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                - ACTOR_X_OFFSET;
        float top = playfield.Y_OFFSET + y * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                - ACTOR_Y_OFFSET + playfield.STARTPOS_Y;

        canvas.drawBitmap(bitmap, new Rect(frameWidth * fruit_type.getDrawPosition(),
                        0, frameWidth * fruit_type.getDrawPosition() + frameWidth, frameHeight)
                , new RectF(left, top, left + actorWidth, top + actorHeight),
                null);
    }

}
