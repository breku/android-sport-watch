package com.sportwatch.model.scene;

import com.sportwatch.manager.ResourcesManager;
import com.sportwatch.manager.SceneManager;
import com.sportwatch.matcher.ClassTouchAreaMacher;
import com.sportwatch.util.ConstantsUtil;
import com.sportwatch.util.SceneType;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private HUD gameHUD;

    private Integer firstTimeCounter;
    private List<Sprite> clockHandList;
    private boolean isClockStarted;


    /**
     * @param objects objects[0] - levelDifficulty
     *                objects[1] - mathParameter
     */
    public GameScene(Object... objects) {
        super(objects);
    }


    @Override
    public void createScene(Object... objects) {
        init(objects);
        createBackground();
        createHUD();
        createClock();
    }


    private void createClock() {
        int angleBetweenHands = 360/ConstantsUtil.NUMBER_OF_CLOCK_HANDS;
        for(int i =0;i<ConstantsUtil.NUMBER_OF_CLOCK_HANDS;i++){
            Sprite sprite = new Sprite(515, 240, ResourcesManager.getInstance().getClockTextureRegionList().get(i), vertexBufferObjectManager);
            sprite.setRotationCenter(0.04f, 0.46f);
            sprite.registerEntityModifier(new RotationByModifier(1,angleBetweenHands*i));
            clockHandList.add(sprite);
        }

        for (Sprite clockHand : clockHandList) {
            attachChild(clockHand);
        }

    }
    private void startClocks(){
        isClockStarted = true;
        registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                for (Sprite clockHand : clockHandList) {
                    clockHand.registerEntityModifier(new RotationByModifier(0.5f, 3.0f));
                }
            }
        }));
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAME);
    }

    private void init(Object... objects) {
        clearUpdateHandlers();
        clearTouchAreas();

        setOnSceneTouchListener(this);
        firstTimeCounter = 0;
        clockHandList = new ArrayList<Sprite>();
        isClockStarted = false;

    }


    private void createHUD() {
        gameHUD = new HUD();
        camera.setHUD(gameHUD);
    }

    private void createBackground() {
        unregisterTouchAreas(new ClassTouchAreaMacher(Sprite.class));
        clearChildScene();

        int offSet = 1;
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2 + offSet, ConstantsUtil.SCREEN_HEIGHT * 3 / 2 - offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH * 3 / 2 - offSet, ConstantsUtil.SCREEN_HEIGHT * 3 / 2 - offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2 + offSet, ConstantsUtil.SCREEN_HEIGHT / 2 + offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH * 3 / 2 - offSet, ConstantsUtil.SCREEN_HEIGHT / 2 + offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));


    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (firstTimeCounter++ == 1) {
            resourcesManager.getStartGameSound().play();

        }

        super.onManagedUpdate(pSecondsElapsed);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAME;
    }

    @Override
    public void disposeScene() {
        gameHUD.clearChildScene();
        camera.setHUD(null);
        camera.setCenter(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2);
        camera.setChaseEntity(null);
        engine.disableAccelerationSensor(activity);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp() && !isClockStarted) {
            startClocks();
        }
        return false;
    }
}
