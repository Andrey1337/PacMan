package com.example.andrey.pacman.entity;

public enum FOOD {
    POINT(10),
    ENERGIZER(50);

    private final int scorePoints;

    FOOD(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    public int getPoints() {
        return scorePoints;
    }

}
