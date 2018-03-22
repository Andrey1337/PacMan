package com.example.andrey.pacman.entity;

public enum FRUIT_TYPE {

    CHERRY(100,0),
    STRAWBERRY(300,1),
    ORANGE(500,2),
    APPLE(700,3),
    MELON(1000,4),
    GALAXIAN_BOSS(2000,5),
    BELL(3000,6),
    KEY(5000,7);



    private final int scorePoints;
    private final int drawPosition;

    private FRUIT_TYPE(int scorePoints, int drawPosition) {
        this.scorePoints = scorePoints;
        this.drawPosition = drawPosition;
    }

    public int getPoints() {
        return scorePoints;
    }

    public int getDrawPosition() {
        return drawPosition;
    }
}
