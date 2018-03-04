package com.example.andrey.pacman;

public enum Direction {
    RIGHT(0),
    LEFT(1),
    UP(2),
    DOWN(3),
    NONE(4);

    private final int value;

    private Direction(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public Direction getOposite()
    {
        if(value == RIGHT.getValue())
        {
            return LEFT;
        }

        if(value == LEFT.getValue())
        {
            return RIGHT;
        }

        if(value == UP.getValue())
        {
            return DOWN;
        }

        return UP;
    }
}
