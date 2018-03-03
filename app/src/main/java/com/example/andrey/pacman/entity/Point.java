package com.example.andrey.pacman.entity;

public class Point {


    public int x;
    public int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public double distance(Point point)
    {
        return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
    }

    public boolean isWall(TileSpecification map[][])
    {
        if(x < 0 || x >= map.length -1)
            return true;

        if(y < 0 || y >= map[0].length - 1)
            return true;

        return map[x][y] == TileSpecification.WALL;

    }

}
