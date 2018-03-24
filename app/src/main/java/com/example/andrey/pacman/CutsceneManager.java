package com.example.andrey.pacman;


import android.graphics.Canvas;
import com.example.andrey.pacman.cutscenes.*;
import com.example.andrey.pacman.entity.Ghost;

import java.util.LinkedList;
import java.util.Queue;

public class CutsceneManager {

    private ResumeGameCutscene resumeGameCutScene;

    private Queue<Cutscene> scenes;
    GameView view;
    private PacmanGame pacmanGame;
    private Playfield playfield;

    CutsceneManager(GameView gameView,PacmanGame game, Playfield playfield) {
        view = gameView;
        pacmanGame = game;
        this.playfield = playfield;
        scenes = new LinkedList<>();
    }

    public void addStartGameScene() {
        scenes.add(new StartGameCutscene(view, playfield));
    }

    public void addKillPacmanScene() {
        scenes.add(new KillPacmanCutscene(pacmanGame, playfield));
    }

    public void addWaitingScene() {
        scenes.add(new WaitingCutscene(pacmanGame,playfield));
    }

    public void addEatingGhostScene(Ghost ghost, int ghostMultiplyer) {
        scenes.add(new EatingGhostCutscene(view,playfield, ghost, ghostMultiplyer));
    }

    public void addGameOverScene() {
        scenes.add(new GameOverCutscene(view, pacmanGame, playfield));
    }

    public void addNextLevelScene() {
        scenes.add(new NextLevelCutscene(playfield));
    }

    public void addPlayfieldPingingScene() {
        scenes.add(new PlayfieldPingingCutscene(pacmanGame,playfield));

    }

    public void addResumeScene() {
        for(Cutscene scene : scenes)
        {
            if(scene.getClass() == StartGameCutscene.class && currentScene == null)
                return;
        }
        resumeGameCutScene = new ResumeGameCutscene(view, playfield);
        resumePlayingTime = 0;
    }

    public boolean hasScene() {
        return currentScene != null || scenes.toArray().length > 0 || resumeGameCutScene != null;
    }

    private Cutscene currentScene;

    private long resumePlayingTime;
    private long currentScenePlayingTime;

    public void playScene(long deltaTime) {
        if (resumeGameCutScene != null)
        {
            resumePlayingTime += deltaTime;
            resumeGameCutScene.play(deltaTime);
            if(resumePlayingTime >= resumeGameCutScene.getSceneTime()) {
                resumeGameCutScene = null;
                resumePlayingTime = 0;
            }
            return;
        }

        if(currentScene == null) {
            currentScene = scenes.remove();
            currentScene.startOfScene();
        }

        currentScenePlayingTime += deltaTime;

        currentScene.play(deltaTime);

        if(currentScenePlayingTime >= currentScene.getSceneTime()) {
            currentScene.endOfScene();
            currentScene = null;
            currentScenePlayingTime = 0;
        }
    }

    public void onDraw(Canvas canvas)
    {
        if(resumeGameCutScene != null)
        {
            resumeGameCutScene.onDraw(canvas);
            return;
        }

        if(currentScene != null)
            currentScene.onDraw(canvas);
    }
}
