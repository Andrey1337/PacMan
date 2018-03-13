package com.example.andrey.pacman;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.example.andrey.pacman.entity.Food;
import com.example.andrey.pacman.entity.Pacman;

import java.util.Date;

public class PacmanGame{

	private Playfield playfield;

	private GhostManager ghostModeController;

	private GameView view;

	private int pacmanLives = 4;

	private long lastTime;

	private float touchDX;
	private float touchDY;
	private float touchStartX;
	private float touchStartY;
	private boolean touchCanceld = true;

	private int countPoints;

	private float tickInterval;

	PacmanGame(GameView view)
	{
	    this.view = view;
		playfield = new Playfield(this,view);
		countPoints = playfield.getCountPoints();

		ghostModeController = new GhostManager(playfield);

		tickInterval = 1000 / 90;
		setTimeout();
		lastTime = new Date().getTime();
	}

	private void nextLevel()
	{
		playfield.nextLevel();
		countPoints = playfield.getCountPoints();
		ghostModeController.nextLevel();

	}


	public void killPacman()
	{
		pacmanLives--;
		playfield.initCharacters(view);
		ghostModeController.pacmanDied();
	}


	public void eatPoint(Food food)
	{
		countPoints--;
        ghostModeController.increaseEatenDots();

        if(food == Food.ENERGIZER)
        {
            ghostModeController.startFrightened();
        }

		if(countPoints <= 0)
		{
			nextLevel();
		}
	}

	public void onDraw(Canvas canvas)
	{
		playfield.onDraw(canvas);
	}

	private void setTimeout() {
		view.redrawHandler.sleep(Math.round(tickInterval));
	}


	public void tick() {
		long now = new Date().getTime();

		if(view.isGameRunning) {
			playfield.update(now - lastTime);
			ghostModeController.update(now - lastTime);
		}

		lastTime = now;

		setTimeout();
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
		if (absDx > 15 && absDy < absDx ) {
			pacman.setRequestDirection(touchDX > 0 ? Direction.RIGHT : Direction.LEFT);
		} else if (absDy > 15 && absDx < absDy) {
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
