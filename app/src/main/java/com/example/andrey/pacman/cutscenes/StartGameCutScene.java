package com.example.andrey.pacman.cutscenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Point;

public class StartGameCutScene extends Cutscene{

    private Bitmap readyBitmap;

    private int X_OFFSET;
    private int Y_OFFSET;

    private Point drawPoint;

    boolean isVisible;

    public StartGameCutScene(View view,Playfield playfield) {
        super(playfield, 2 * 1000);

        X_OFFSET = (int)(22 / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
        Y_OFFSET = (int)(3 / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());

        drawPoint = new Point(13.65f,16f);
        isVisible = true;
        readyBitmap = BitmapFactory.decodeResource(view.getResources(), R.mipmap.ready);
        readyBitmap = Bitmap.createScaledBitmap(readyBitmap, (int) (readyBitmap.getWidth() * playfield.scale),
                (int)(readyBitmap.getHeight() * playfield.scale), false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float left = playfield.X_OFFSET + drawPoint.floatX * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - X_OFFSET;

        float top = playfield.Y_OFFSET + drawPoint.floatY * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() - Y_OFFSET;

        if(isVisible)
            canvas.drawBitmap(readyBitmap, left, top, null);
    }

    @Override
    public void play(long deltaTime) {

    }

    @Override
    public void startOfScene() {
        playfield.getPacman().isPacManBall = true;
    }

    @Override
    public void endOfScene() {
        playfield.getPacman().isPacManBall = false;
    }
}
