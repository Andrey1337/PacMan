package com.example.andrey.pacman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gameView =new GameView(this);
        setContentView(gameView);
        //gameBoardView = (GameBoardView) findViewById(R.id.matrix_view);
    }

    @Override
    protected void onStop() {
        gameView.onStop();
        super.onStop();
    }
}
