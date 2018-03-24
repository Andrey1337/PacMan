package com.example.andrey.pacman.entity;

public enum Food {
    POINT(10),
    ENERGIZER(50);

    private final int scorePoints;

    Food(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    public int getPoints() {
        return scorePoints;
    }

}
