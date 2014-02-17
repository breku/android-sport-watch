package com.sportwatch.model.scene;

import com.sportwatch.manager.SceneManager;
import com.sportwatch.util.SceneType;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

/**
 * User: Breku
 * Date: 06.10.13
 */
public class HighScoreScene extends BaseScene implements IOnSceneTouchListener {

    /**
     * Constructor
     *
     * @param objects object[0] - Integer score
     *                object[1] - LevelDifficulty levelDifficulty
     *                object[2] - MathParameter mathParameter
     */
    public HighScoreScene(Object... objects) {
        super(objects);
    }

    @Override
    public void createScene(Object... objects) {
        init();
        setBackground(new Background(Color.WHITE));
        setOnSceneTouchListener(this);
    }

    private void init() {
    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.RECORDS);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.RECORDS;
    }

    @Override
    public void disposeScene() {
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()) {
            SceneManager.getInstance().loadMenuSceneFrom(SceneType.RECORDS);
        }
        return false;
    }
}
