package com.example.andrey.pacman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import com.example.andrey.pacman.Playfield;
import com.example.andrey.pacman.R;
import com.example.andrey.pacman.entity.Food;

public class FoodDrawController {

    private Bitmap pointBitmap;
    private Food foodMap[][];

    private Playfield playfield;

    FoodDrawController(View view, Playfield playfield){
        this.playfield = playfield;
        foodMap = playfield.getFoodMap();

        Bitmap smallPoint  = BitmapFactory.decodeResource(view.getResources(), R.mipmap.point);

        pointBitmap  = Bitmap.createScaledBitmap(smallPoint, (int)(2 / (float) playfield.MAP_WIDTH * playfield.mapTexture.getWidth()),
                (int)(2 / (float) playfield.MAP_WIDTH * playfield.mapTexture.getWidth()),false);

    }

    public void onDraw(Canvas canvas)
    {
        for(int i = 0; i < foodMap.length; i++)
        {
            for(int j = 0; j < foodMap[1].length; j++)
            {
                if(foodMap[i][j] == null)
                    continue;

                switch (foodMap[i][j])
                {
                    case Point:
                        canvas.drawBitmap(pointBitmap, playfield.X_OFFSET + i * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth()
                                ,playfield.Y_OFFSET + j * playfield.CELLS_SPACE_PERCENT * playfield.mapTexture.getWidth(), null);
                        break;
                }
            }
        }

    }

}
