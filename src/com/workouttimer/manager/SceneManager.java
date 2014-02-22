package com.workouttimer.manager;

import com.google.ads.AdView;
import com.workouttimer.activity.MyActivity;
import com.workouttimer.model.scene.*;
import com.workouttimer.util.ConstantsUtil;
import com.workouttimer.util.SceneType;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class SceneManager {

    private static final SceneManager INSTANCE = new SceneManager();

    private SceneType currentSceneType = SceneType.SPLASH;
    private BaseScene gameScene, menuScene, loadingScene, splashScene, currentScene,
            aboutScene, optionsScene, endGameScene, recordScene, gameTypeScene;

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public void setScene(BaseScene scene) {

        ResourcesManager.getInstance().getEngine().setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void createSplashScene(IGameInterface.OnCreateSceneCallback onCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        onCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    private void setAdVisible() {
        ResourcesManager.getInstance().getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        ((MyActivity) ResourcesManager.getInstance().getActivity()).getAdView().setVisibility(AdView.VISIBLE);
                    }
                }
        );
    }

    public void createMainMenuScene() {
        ResourcesManager.getInstance().loadMainMenuResources();
        ResourcesManager.getInstance().loadLoadingResources();
        setAdVisible();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        setScene(menuScene);
        disposeSplashScene();
    }


    public void loadMenuSceneFrom(SceneType sceneType) {
        setScene(loadingScene);
        switch (sceneType) {
            case GAME:
                gameScene.disposeScene();
                ResourcesManager.getInstance().unloadGameTextures();
                break;
            case OPTIONS:
                ResourcesManager.getInstance().unloadOptionsTextures();
                optionsScene.disposeScene();
                break;
        }
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
                setScene(menuScene);
            }
        }));
    }

    public void loadGameScene() {
        setScene(loadingScene);
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }

    public void loadOptionsScene() {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME / 2, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadOptionsResources();
                optionsScene = new OptionsScene();
                setScene(optionsScene);
            }
        }));
    }


    private void disposeSplashScene() {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }


    public BaseScene getCurrentScene() {
        return currentScene;
    }

}
