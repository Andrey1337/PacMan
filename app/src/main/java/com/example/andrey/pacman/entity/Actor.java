package com.example.andrey.pacman.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import com.example.andrey.pacman.Playfield;

public abstract class Actor extends Entity {

    public boolean isVisible = true;

    Playfield playfield;
    final int ACTOR_X_OFFSET;
    final int ACTOR_Y_OFFSET;

    int actorWidth,actorHeight;
    int frameWidth, frameHeight;

    int frameCount;

    Rect frameToDraw;

    protected int currentFrame = 0;
    int frameLengthInMillisecond;

    protected Actor(Playfield playfield, Bitmap bitmap, int frameWidth, int frameHeight,
                    float actorXOffset, float actorYOffset, int frameCount, int frameMovesCount, float x, float y) {
        super(x, y);

        frameWidth *= 15;
        frameHeight *= 15;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.playfield = playfield;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, frameWidth * frameCount,
                frameHeight * frameMovesCount,false);

        this.frameCount = frameCount;

        actorWidth = (int)((float)bitmap.getWidth() * playfield.scale / (float)frameCount);
        actorHeight = (int)((float)bitmap.getHeight() * playfield.scale / (float) frameMovesCount);

        frameToDraw = new Rect(0, 0, frameWidth, frameHeight);

        ACTOR_X_OFFSET = (int)(actorXOffset / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
        ACTOR_Y_OFFSET = (int)(actorYOffset / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());

    }


    public void onDraw(Canvas canvas)
    {
        if(!isVisible)
            return;

        float left = playfield.X_OFFSET + x * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                - ACTOR_X_OFFSET;
        float top = playfield.Y_OFFSET + y * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                - ACTOR_Y_OFFSET + playfield.STARTPOS_Y;

        frameToDraw = new Rect(frameWidth * currentFrame, 0,
                frameWidth * currentFrame + frameWidth, frameHeight);

        RectF whereToDraw = new RectF(left, top, left + actorWidth, top + actorHeight);

        canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
    }
}
