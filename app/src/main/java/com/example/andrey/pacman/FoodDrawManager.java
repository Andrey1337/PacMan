package com.example.andrey.pacman;

import android.graphics.*;
import android.view.View;
import com.example.andrey.pacman.entity.Food;

public class FoodDrawManager {

    private Bitmap pointBitmap;
    private Bitmap energizerBitmap;

    private Food foodMap[][];

    private Playfield playfield;

    private long energizerTickTimer;
    private long energizerTickTime;
    private boolean isEnergizerVisible;

    private int ENERGIZER_OFFSET_X, ENERGIZER_OFFSET_Y;


    FoodDrawManager(View view, Playfield playfield){
        this.playfield = playfield;
        foodMap = playfield.getFoodMap();

        pointBitmap  = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.point), (int)(2 / (float) playfield.MAP_WIDTH * playfield.mapTexture.getWidth()),
                (int)(2 / (float) playfield.MAP_WIDTH * playfield.mapTexture.getWidth()),false);

        energizerBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(view.getResources(), R.mipmap.energizer), (int)(8 / (float) playfield.MAP_WIDTH * playfield.mapTexture.getWidth()),
                (int)(8 / (float) playfield.MAP_WIDTH * playfield.mapTexture.getWidth()),false);


        ENERGIZER_OFFSET_X = (int)(3 / (float)playfield.MAP_WIDTH * playfield.mapTexture.getWidth());
        ENERGIZER_OFFSET_Y = (int)(3 / (float)playfield.MAP_HEIGHT * playfield.mapTexture.getHeight());

        energizerTickTime = 200;
        isEnergizerVisible = true;
    }

    public void onUpdate(long deltaTime)
    {
        energizerTickTimer += deltaTime;
        if(energizerTickTimer > energizerTickTime)
        {
            energizerTickTimer = 0;
            isEnergizerVisible = !isEnergizerVisible;
        }
    }

    public void onDraw(Canvas canvas)
    {
        for(int i = 0; i < foodMap.length; i++)
        {
            for(int j = 0; j < foodMap[1].length; j++)
            {
                if(foodMap[i][j] == null)
                    continue;

                Food food = foodMap[i][j];

                if(food == Food.POINT) {
                    canvas.drawBitmap(pointBitmap, playfield.X_OFFSET + i * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                            , playfield.Y_OFFSET + j * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth() + playfield.STARTPOS_Y, null);
                }
                if(food == Food.ENERGIZER) {
                    if(isEnergizerVisible)
                        canvas.drawBitmap(energizerBitmap, playfield.X_OFFSET + i * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                                    - ENERGIZER_OFFSET_X
                            , playfield.Y_OFFSET + j * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                                    - ENERGIZER_OFFSET_Y + playfield.STARTPOS_Y, null);
                }


            }
        }

    }

}
