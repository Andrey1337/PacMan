package com.example.andrey.pacman;

import android.graphics.*;

import android.util.Log;
import android.view.View;
import com.example.andrey.pacman.entity.*;

import java.util.ArrayList;

public class Playfield {

    FoodDrawManager foodDrawController;

	private TileSpecification map[][];

	private Food foodMap[][];

	public Bitmap mapTexture;

    public final int MAP_WIDTH;
	public final int MAP_HEIGHT;

	public final int X_OFFSET;
	public final int Y_OFFSET;

    public final float CELLS_SPACE_PERCENT;

    public final float scale;

    private int countPoints;

    private PacmanGame game;

    GameMode gameMode;

	Pacman pacman;
	private ArrayList<Ghost> ghosts;

	private Pinky pinky;

	GameView view;


	Playfield(PacmanGame game,GameView view)
	{

		long testTime = System.currentTimeMillis();
	    gameMode = GameMode.SCATTER;

	    this.game = game;

		this.view = view;
		MAP_WIDTH = 224;
		MAP_HEIGHT = 248;


		int X_OFFSET = 11;
		int Y_OFFSET = 11;

        int CELLS_SPACE = 8;

		mapTexture = BitmapFactory.decodeResource(view.getResources(), R.mipmap.pacman_map);

		scale = (float)view.getContext().getResources().getDisplayMetrics().widthPixels / (float)mapTexture.getWidth();

		mapTexture = Bitmap.createScaledBitmap(mapTexture, (int)(mapTexture.getWidth() * scale),
				(int)(mapTexture.getHeight() * scale),false);

		this.X_OFFSET = (int)((float) X_OFFSET / (float)MAP_WIDTH * mapTexture.getWidth());
		this.Y_OFFSET = (int)((float) Y_OFFSET / (float)MAP_HEIGHT * mapTexture.getHeight());
        this.CELLS_SPACE_PERCENT = (float)CELLS_SPACE / (float)MAP_WIDTH ;

		map = new TileSpecification[26][29];
		foodMap = new Food[26][29];
		initMap();

        initCharacters(view);

		foodDrawController = new FoodDrawManager(view, this);

		long num =System.currentTimeMillis() - testTime;
		Log.i("test", Long.toString(System.currentTimeMillis() - testTime));
	}

	public void restartGame()
    {
        initMap();
        initCharacters(view);
    }

    public void nextLevel()
	{
		changeGameMode(GameMode.SCATTER);
		initCharacters(view);
		initMap();
	}

    public Pinky getPinky() {
        return pinky;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void changeGameMode(GameMode newGameMode)
	{
		gameMode = newGameMode;

		for(Ghost ghost : ghosts)
			ghost.changeMode(gameMode);

	}


    public void initCharacters(View view)
	{
		pacman = new Pacman(this, view,12.5f,22);
		ghosts = new ArrayList<>();
		ghosts.add(new Blinky(this, view,12.5f, 10));

		pinky = new Pinky(this, view,12.5f, 13);
        ghosts.add(pinky);
	}

	public void update(long deltaTime)
	{
		pacman.move(deltaTime);

		for (Ghost ghost : ghosts)
        {
            ghost.move(deltaTime);
        }

		pacmanFoodIntersect();
		charactersIntersect();

	}

	private void pacmanFoodIntersect()
	{
		int arrayPosX = Math.round(pacman.getX());
		int arrayPosY = Math.round(pacman.getY());

		if(foodMap[arrayPosX][arrayPosY] == Food.Point)
		{
			foodMap[arrayPosX][arrayPosY] = null;
			countPoints--;
			if(countPoints <= 0)
            {
                game.nextLevel();
            }
		}
	}

	private void charactersIntersect()
	{
		for(Ghost ghost : ghosts) {
			if(Math.abs(pacman.getX() - ghost.getX()) <= 0.5f && Math.abs(pacman.getY() - ghost.getY()) <= 0.5f)
			{
				game.killPacman();
			}

		}
	}


	public TileSpecification[][] getMap()
	{
		return this.map;
	}

	public Food[][] getFoodMap()
	{
		return this.foodMap;
	}

	public Pacman getPacman() {
		return pacman;
	}

	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}

	public void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(mapTexture,0,0, null);
        foodDrawController.onDraw(canvas);

		pacman.onDraw(canvas);


		for (Ghost ghost : ghosts)
		{
			ghost.onDraw(canvas);
		}
	}

	private void createHorizontalPath(int startPointX, int endPointX, int y, boolean haveFood)
	{
		for(int i = startPointX; i < endPointX; i++)
		{
			map[i][y] = TileSpecification.PATH;
			if(haveFood && foodMap[i][y] != Food.Point)
			{
				countPoints++;
				foodMap[i][y] = Food.Point;
			}
		}
	}

	private void createVerticalPath(int startPointY, int endPointY, int x, boolean haveFood)
	{
		for(int i = startPointY; i < endPointY; i++)
		{
			map[x][i] = TileSpecification.PATH;
			if(haveFood && foodMap[x][i] != Food.Point)
			{
				countPoints++;
				foodMap[x][i] = Food.Point;
			}
		}
	}

	private void initMap()
	{
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[1].length; j++)
			{
				map[i][j] = TileSpecification.WALL;
			}
		}

		createPaths();
		map[11][10] = TileSpecification.SPECIFIC;
		map[14][10] = TileSpecification.SPECIFIC;
		map[11][22]= TileSpecification.SPECIFIC;
		map[14][22]= TileSpecification.SPECIFIC;
	}

	private void createPaths()
	{
		//HORIZONTAL PATHS

		createHorizontalPath(0, 12, 0,true);
		createHorizontalPath(14, 26, 0,true);

		createHorizontalPath(0, 26, 4,true);

		createHorizontalPath(0, 6, 7,true);
		createHorizontalPath(8, 12, 7,true);
		createHorizontalPath(14, 18, 7,true);
		createHorizontalPath(20, 26, 7,true);

		createHorizontalPath(8, 18, 10,false);

		createHorizontalPath(0, 9, 13,false);
		createHorizontalPath(17, 26, 13,false);

		createHorizontalPath(8, 18, 16,false);

		createHorizontalPath(0, 12, 19,true);
		createHorizontalPath(14, 26, 19,true);

		createHorizontalPath(0, 3, 22,true);
		createHorizontalPath(5, 20, 22,false);
		createHorizontalPath(5, 12, 22,true);
		createHorizontalPath(14, 20, 22,true);

		createHorizontalPath(23, 26, 22,true);



		createHorizontalPath(0, 6, 25,true);
		createHorizontalPath(8, 12, 25,true);
		createHorizontalPath(14, 18, 25,true);
		createHorizontalPath(20, 26, 25,true);

		createHorizontalPath(0, 26, 28,true);

		//VERTICAL PATHS
		createVerticalPath(0,8,0,true);
		createVerticalPath(19,23,0,true);
		createVerticalPath(26,29,0,true);
		createVerticalPath(23,26,2,true);
		createVerticalPath(0, 26, 5,true);

		createVerticalPath(4,7,8,true);
		createVerticalPath(11,20,8,false);
		createVerticalPath(23,26,8,true);

		createVerticalPath(0,4,11,true);
		createVerticalPath(7,11,11,false);
		createVerticalPath(19,23,11,true);
		createVerticalPath(25,29,11,true);

		createVerticalPath(0,4,14,true);
		createVerticalPath(7,11,14,false);
		createVerticalPath(19,23,14,true);
		createVerticalPath(25,28,14,true);

		createVerticalPath(4,7,17,true);
		createVerticalPath(11,20,17,false);
		createVerticalPath(22,26,17,true);

		createVerticalPath(0,26,20,true);
		createVerticalPath(22,26,23,true);

		createVerticalPath(0,8,25,true);
		createVerticalPath(19,23,25,true);
		createVerticalPath(26,29,25,true);

	}


}
