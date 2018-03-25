package com.example.andrey.pacman;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

    private PacmanGame game;

    public final int DISPLAY_WIDTH;

    public final int DISPLAY_HEIGHT;

    public GameView(Context context) {
        super(context);
        game = new PacmanGame(this);
        setBackgroundColor(Color.BLACK);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        DISPLAY_HEIGHT = displayMetrics.heightPixels;
        DISPLAY_WIDTH = displayMetrics.widthPixels;
    }

    public void onResume()
    {
        game.onResume();
    }

    public void onPause()
    {
        game.onPause();
    }
    @Override
    public void onDraw(Canvas canvas) {
        game.onDraw(canvas);
    }

    final RefreshHandler redrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            game.tick();
            GameView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                game.handleTouchStart(event);
                break;
            case MotionEvent.ACTION_UP:
                game.handleTouchEnd(event);
                break;
            case MotionEvent.ACTION_MOVE:
                game.handleTouchMove(event);
                break;
        }
        return true;
    }


}
