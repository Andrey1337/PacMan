package com.example.andrey.pacman.entity;

import com.example.andrey.pacman.Direction;
import com.example.andrey.pacman.TileSpecification;

public class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float floatX;
    public float floatY;

    public Point(float x, float y) {
        floatX = x;
        floatY = y;
        this.x = Math.round(x);
        this.y = Math.round(y);
    }

    public double distance(Point point) {
        return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
    }

    public boolean isWall(TileSpecification map[][]) {
        if (x < 0 || x > map.length - 1)
            return true;

        if (y < 0 || y > map[0].length - 1)
            return true;

        return map[x][y] == TileSpecification.WALL;

    }

    public boolean isEqual(Point point) {
        return x == point.x && y == point.y;
    }

    public boolean isFork(TileSpecification map[][], Direction currentDirection) {
        if (currentDirection == Direction.RIGHT || currentDirection == Direction.LEFT) {
            Point newPoint = new Point(x, y + 1);

            if (!newPoint.isWall(map)) {
                return true;
            }

            if (!isWall(map) && map[x][y] != TileSpecification.SPECIFIC) {
                newPoint = new Point(x, y - 1);

                if (!newPoint.isWall(map) && map[newPoint.x][newPoint.y] != TileSpecification.SPECIFIC) {
                    return true;
                }
            }
        }

        if (currentDirection == Direction.UP || currentDirection == Direction.DOWN) {
            Point newPoint = new Point(x - 1, y);
            if (!newPoint.isWall(map)) {
                return true;
            }

            newPoint = new Point(x + 1, y);

            if (!newPoint.isWall(map)) {
                return true;
            }
        }

        return false;
    }

}
