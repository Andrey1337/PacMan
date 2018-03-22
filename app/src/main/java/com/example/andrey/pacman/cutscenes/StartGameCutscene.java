package com.example.andrey.pacman.cutscenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Actor;
import com.example.andrey.pacman.entity.Point;

public class StartGameCutscene extends Cutscene{

    ReadyLabel readyLabel;

    public StartGameCutscene(View view, Playfield playfield) {
        super(playfield, 2 * 1000);

        readyLabel = new ReadyLabel(playfield, BitmapFactory.decodeResource(view.getResources(), R.mipmap.ready),13.65f,16f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        readyLabel.onDraw(canvas);
    }

    @Override
    public void startOfScene() {
        playfield.getPacman().isPacManBall = true;
    }

    @Override
    public void endOfScene() {
        playfield.getPacman().isPacManBall = false;
    }

    class ReadyLabel extends Actor {

        ReadyLabel(Playfield playfield, Bitmap bitmap, float x, float y) {
            super(playfield, bitmap, 46, 7, 22, 2, 1, 1, x, y);
        }
    }
}
