package com.example.andrey.pacman;

import android.graphics.*;

import android.view.View;
import com.example.andrey.pacman.entity.*;
import com.example.andrey.pacman.entity.Point;

import java.util.ArrayList;
import java.util.Random;

public class Playfield {

    FoodDrawManager foodDrawController;

    private TileSpecification map[][];

    private Food foodMap[][];

    public Bitmap mapTexture;
    private Bitmap mapTextureWhite;

    public final int MAP_WIDTH;
    public final int MAP_HEIGHT;

    public final int X_OFFSET;
    public final int Y_OFFSET;

    public final int STARTPOS_Y;

    public final float CELLS_SPACE_PERCENT;

    public final float scale;

    private int countPoints;

    private PacmanGame game;

    private GameMode gameMode;

    Pacman pacman;
    private ArrayList<Ghost> ghosts;

    private Pinky pinky;
    private Inky inky;
    private Blinky blinky;
    private Clyde clyde;

    GameView view;

    public boolean isPing;

    private Random randomizer;

    
    private Point topCagePoint;
    private Point middleCagePoint;
    private Point bottomCagePoint;

    Playfield(PacmanGame game, GameView view) {
        gameMode = GameMode.SCATTER;

        this.game = game;

        this.view = view;
        MAP_WIDTH = 224;
        MAP_HEIGHT = 248;

        int X_OFFSET = 3;
        int Y_OFFSET = 11;

        int CELLS_SPACE = 8;

        topCagePoint = new Point(13.5f, 12.5f);
        bottomCagePoint = new Point(13.5f, 13.5f);
        middleCagePoint = new Point(13.5f, (topCagePoint.floatY + bottomCagePoint.floatY) / 2);

        mapTexture = BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_map);

        scale = (float) view.getContext().getResources().getDisplayMetrics().widthPixels / (float) mapTexture.getWidth();

        mapTexture = Bitmap.createScaledBitmap(mapTexture, (int) (mapTexture.getWidth() * scale),
                (int) (mapTexture.getHeight() * scale), false);

        mapTextureWhite = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_map_pinging),
                (int) (BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_map_pinging).getWidth() * scale),
                (int) (BitmapFactory.decodeResource(view.getResources(), R.drawable.pacman_map_pinging).getHeight() * scale), false);

        this.X_OFFSET = (int) ((float) X_OFFSET / (float) MAP_WIDTH * mapTexture.getWidth());
        this.Y_OFFSET = (int) ((float) Y_OFFSET / (float) MAP_HEIGHT * mapTexture.getHeight());
        this.CELLS_SPACE_PERCENT = (float) CELLS_SPACE / (float) MAP_WIDTH;


        Bitmap hightScoreLabel = BitmapFactory.decodeResource(view.getResources(), R.drawable.high_score);
        hightScoreLabel = Bitmap.createScaledBitmap(hightScoreLabel, (int) (hightScoreLabel.getWidth() * 5 / 6 * scale),
                (int) (hightScoreLabel.getHeight() * 5 / 6 * scale), false);
        STARTPOS_Y = hightScoreLabel.getHeight() / 2 + hightScoreLabel.getHeight();


        initMap();

        randomizer = new Random();

        initCharacters(view);

        foodDrawController = new FoodDrawManager(view, this);
    }

    public Random getRandomizer() {
        return randomizer;
    }

    public Point getBottomCagePoint() {
        return bottomCagePoint;
    }

    public Point getTopCagePoint() {
        return topCagePoint;
    }

    public Point getMiddleCagePoint() {
        return middleCagePoint;
    }

    public int getCountPoints() {
        return countPoints;
    }

    public void restartGame() {
        initMap();
        initCharacters(view);
    }

    public void nextLevel() {
        initMap();
        initCharacters(view);
    }

    public Pinky getPinky() {
        return pinky;
    }

    public Inky getInky() {
        return inky;
    }

    public Blinky getBlinky() {
        return blinky;
    }

    public Clyde getClyde() {
        return clyde;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }


    public void initCharacters(View view) {
        pacman = new Pacman(this, view, 13.5f, 22);
        ghosts = new ArrayList<>();

        blinky = new Blinky(this, view, 13.5f, 10);
        pinky = new Pinky(this, view, 13.5f, 13);
        inky = new Inky(this, view, 11.5f, 13);
        clyde = new Clyde(this, view, 15.5f, 13);

        ghosts.add(blinky);
        ghosts.add(pinky);
        ghosts.add(inky);
        ghosts.add(clyde);
    }

    public void update(long deltaTime) {
        pacman.move(deltaTime);

        for (Ghost ghost : ghosts) {
            ghost.move(deltaTime);
        }


        if (game.getFruitManager().canEatFruit() && Math.abs(pacman.getX() - game.getFruitManager().getFruit().getX()) <= 0.8f && Math.abs(pacman.getY() - game.getFruitManager().getFruit().getY()) <= 0.8f) {
            game.getFruitManager().eatFruit();
        }

        pacmanFoodIntersect();
        charactersIntersect();

        foodDrawController.onUpdate(deltaTime);
    }

    private void pacmanFoodIntersect() {
        Point pacmanPoint = new Point(Math.round(pacman.getX()), Math.round(pacman.getY()));

        if (pacmanPoint.isWall(map))
            return;

        if (foodMap[pacmanPoint.x][pacmanPoint.y] != null) {
            Food food = foodMap[pacmanPoint.x][pacmanPoint.y];
            foodMap[pacmanPoint.x][pacmanPoint.y] = null;
            game.eatPoint(food);
        }

    }


    private void charactersIntersect() {
        for (Ghost ghost : ghosts) {
            if (Math.abs(pacman.getX() - ghost.getX()) <= 0.8f && Math.abs(pacman.getY() - ghost.getY()) <= 0.8f) {
                if (!ghost.isFrightened() && !ghost.isEyes()) {
                    game.getCutsceneManager().addWaitingScene();
                    game.getCutsceneManager().addKillPacmanScene();
                }

                if (gameMode == GameMode.FRIGHTENED) {
                    if (!ghost.isEyes() && ghost.isFrightened()) {
                        game.eatGhost(ghost);
                    }
                }
            }
        }
    }


    public TileSpecification[][] getMap() {
        return this.map;
    }

    public Food[][] getFoodMap() {
        return this.foodMap;
    }

    public Pacman getPacman() {
        return pacman;
    }


    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(isPing ? mapTextureWhite : mapTexture, 0, STARTPOS_Y, null);
        foodDrawController.onDraw(canvas);

        game.getFruitManager().onDraw(canvas);
        pacman.onDraw(canvas);

        for (Ghost ghost : ghosts) {
            ghost.onDraw(canvas);
        }


    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    private void createHorizontalPath(int startPointX, int endPointX, int y, boolean haveFood) {
        for (int i = startPointX; i < endPointX; i++) {
            map[i][y] = TileSpecification.PATH;
            if (haveFood && foodMap[i][y] != Food.POINT) {
                countPoints++;
                foodMap[i][y] = Food.POINT;
            }
        }
    }

    private void createVerticalPath(int startPointY, int endPointY, int x, boolean haveFood) {
        for (int i = startPointY; i < endPointY; i++) {
            map[x][i] = TileSpecification.PATH;
            if (haveFood && foodMap[x][i] != Food.POINT) {
                countPoints++;
                foodMap[x][i] = Food.POINT;
            }
        }
    }

    private void initMap() {

        map = new TileSpecification[28][29];
        foodMap = new Food[28][29];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[1].length; j++) {
                map[i][j] = TileSpecification.WALL;
            }
        }

        createPaths();
        map[12][10] = TileSpecification.SPECIFIC;
        map[15][10] = TileSpecification.SPECIFIC;
        map[12][22] = TileSpecification.SPECIFIC;
        map[15][22] = TileSpecification.SPECIFIC;

        foodMap[1][2] = Food.ENERGIZER;
        foodMap[1][22] = Food.ENERGIZER;
        foodMap[26][2] = Food.ENERGIZER;
        foodMap[26][22] = Food.ENERGIZER;

    }

    private void createPaths() {
        //HORIZONTAL PATHS

        createHorizontalPath(1, 13, 0, true);
        createHorizontalPath(15, 27, 0, true);

        createHorizontalPath(1, 27, 4, true);

        createHorizontalPath(1, 7, 7, true);
        createHorizontalPath(9, 13, 7, true);
        createHorizontalPath(15, 19, 7, true);
        createHorizontalPath(21, 27, 7, true);

        createHorizontalPath(9, 19, 10, false);

        createHorizontalPath(0, 10, 13, false);
        createHorizontalPath(18, 28, 13, false);

        createHorizontalPath(9, 19, 16, false);

        createHorizontalPath(1, 13, 19, true);
        createHorizontalPath(15, 27, 19, true);

        createHorizontalPath(1, 4, 22, true);
        createHorizontalPath(6, 21, 22, false);
        createHorizontalPath(6, 13, 22, true);
        createHorizontalPath(15, 21, 22, true);

        createHorizontalPath(24, 27, 22, true);


        createHorizontalPath(1, 7, 25, true);
        createHorizontalPath(9, 13, 25, true);
        createHorizontalPath(15, 19, 25, true);
        createHorizontalPath(21, 27, 25, true);

        createHorizontalPath(1, 27, 28, true);

        //VERTICAL PATHS
        createVerticalPath(0, 8, 1, true);
        createVerticalPath(19, 23, 1, true);
        createVerticalPath(26, 29, 1, true);
        createVerticalPath(23, 26, 3, true);
        createVerticalPath(0, 26, 6, true);

        createVerticalPath(4, 7, 9, true);
        createVerticalPath(11, 20, 9, false);
        createVerticalPath(23, 26, 9, true);

        createVerticalPath(0, 4, 12, true);
        createVerticalPath(7, 11, 12, false);
        createVerticalPath(19, 23, 12, true);
        createVerticalPath(25, 29, 12, true);

        createVerticalPath(0, 4, 15, true);
        createVerticalPath(7, 11, 15, false);
        createVerticalPath(19, 23, 15, true);
        createVerticalPath(25, 28, 15, true);

        createVerticalPath(4, 7, 18, true);
        createVerticalPath(11, 20, 18, false);
        createVerticalPath(22, 26, 18, true);

        createVerticalPath(0, 26, 21, true);
        createVerticalPath(22, 26, 24, true);

        createVerticalPath(0, 8, 26, true);
        createVerticalPath(19, 23, 26, true);
        createVerticalPath(26, 29, 26, true);

    }


}
