package com.example.andrey.pacman;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.example.andrey.pacman.entity.Pacman;

import java.util.Date;

public class PacmanGame implements Runnable{

	private Playfield playfield;

	private GhostManager gameModeController;

	private GameView view;

	private int pacmanLives = 4;


	long lastUpdateTime;

	private float touchDX;
	private float touchDY;
	private float touchStartX;
	private float touchStartY;
	private boolean touchCanceld = true;


	PacmanGame(GameView view)
	{
	    this.view = view;
		playfield = new Playfield(this,view);
		gameModeController = new GhostManager(playfield);

		new Thread(this, "update game").start();

	}

	public void nextLevel()
	{
		playfield.nextLevel();
		gameModeController = new GhostManager(playfield);

	}

	public void killPacman()
	{
		pacmanLives--;
		if(pacmanLives <= 0)
		{

		}
		else
		{
			playfield.initCharacters(view);
			gameModeController = new GhostManager(playfield);
		}
	}

	public void onDraw(Canvas canvas)
	{
		playfield.onDraw(canvas);
	}

	@Override
	public void run() {

		lastUpdateTime = System.currentTimeMillis();
		while(true)
		{
		    if(view.isGameRunning) {
                playfield.update();
                gameModeController.update(System.currentTimeMillis() - lastUpdateTime);
                lastUpdateTime = System.currentTimeMillis();
            }

			try {
				Thread.sleep(13);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Playfield getPlayfield() { return playfield; }
	Pacman getPacman()
	{
		return playfield.getPacman();
	}


	void handleTouchStart(MotionEvent e) {
		touchDX = 0;
		touchDY = 0;
		if (e.getPointerCount() == 1) {
			touchCanceld = false;
			touchStartX = e.getX(0);
			touchStartY = e.getY(0);
		}
	}

	void handleTouchMove(MotionEvent e) {
		if (touchCanceld) {
			return;
		}

		if (e.getPointerCount() > 1) {
			cancelTouch();
		} else {
			touchDX = e.getX(0) - touchStartX;
			touchDY = e.getY(0) - touchStartY;
		}
	}

	void handleTouchEnd(MotionEvent e) {
		if (touchCanceld) {
			return;
		}

		float absDx = Math.abs(touchDX);
		float absDy = Math.abs(touchDY);
		Pacman pacman = getPacman();
		if (absDx > 15 && absDy < absDx * 2 / 3) {
			pacman.setRequestDirection(touchDX > 0 ? Direction.RIGHT : Direction.LEFT);
		} else if (absDy > 15 && absDx < absDy * 2 / 3) {
			pacman.setRequestDirection(touchDY > 0 ? Direction.DOWN : Direction.UP);
		}
		cancelTouch();
	}

	private void cancelTouch() {
		touchStartX = Float.NaN;
		touchStartY = Float.NaN;
		touchCanceld = true;
	}

}
