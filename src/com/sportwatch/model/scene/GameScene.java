package com.sportwatch.model.scene;

import com.sportwatch.manager.ResourcesManager;
import com.sportwatch.manager.SceneManager;
import com.sportwatch.matcher.ClassTouchAreaMacher;
import com.sportwatch.service.OptionsService;
import com.sportwatch.util.ConstantsUtil;
import com.sportwatch.util.SceneType;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class GameScene extends BaseScene {

    private HUD gameHUD;

    private Integer firstTimeCounter;
    private List<Sprite> clockHandList;
    private int numberOfClockHands;
    private OptionsService optionsService;

    private List<Text> stopWatchTextList;
    private List<Float> stopWatchTimeList;

    private Sprite buttonPause, buttonStart, buttonReset;

    private final static Integer FIRST_HAND_CLOCK_TAG = 25;

    private boolean isClockStarted;
    private boolean soundEffectPlayed;

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
        createStopWatch();
        createClockDial();
        createClock();
        createButtons();
    }

    private void createButtons() {
        buttonPause = new Sprite(200, 180, ResourcesManager.getInstance().getButtonPauseTextureRegion(), vertexBufferObjectManager) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionUp()) {
                    unregisterTouchArea(this);
                    setVisible(false);
                    pauseClocks();

                    buttonStart.setVisible(true);
                    registerTouchArea(buttonStart);

                    return true;
                }
                return false;
            }
        };


        buttonStart = new Sprite(200, 180, ResourcesManager.getInstance().getButtonStartTextureRegion(), vertexBufferObjectManager) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionUp()) {
                    startClocks();
                    unregisterTouchArea(this);
                    setVisible(false);

                    buttonPause.setVisible(true);
                    registerTouchArea(buttonPause);
                    return true;
                }
                return false;
            }
        };


        buttonReset = new Sprite(100, 180, ResourcesManager.getInstance().getButtonResetTextureRegion(), vertexBufferObjectManager) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionUp()) {
                    unregisterTouchArea(this);
                    resetClocks();
                    return true;
                }
                return false;
            }
        };

        registerTouchArea(buttonStart);
        registerTouchArea(buttonReset);

        buttonPause.setVisible(false);

        attachChild(buttonPause);
        attachChild(buttonReset);
        attachChild(buttonStart);
    }

    private void pauseClocks() {
        isClockStarted = false;
        soundEffectPlayed = false;
        unregisterUpdateHandlers(new IUpdateHandlerMatcher() {
            @Override
            public boolean matches(IUpdateHandler pItem) {
                if (pItem instanceof TimerHandler) {
                    return true;
                }
                return false;
            }
        });
    }

    private void resetClocks() {

        clearTouchAreas();

        buttonPause.setVisible(false);
        buttonStart.setVisible(true);

        float maxCurrentRotation = -1;
        for (Sprite sprite : clockHandList) {
            float spriteRotation = sprite.getRotation() % 360;
            if (maxCurrentRotation < spriteRotation) {
                maxCurrentRotation = spriteRotation;
            }
        }

        pauseClocks();

        final float rotationLeft = 360 - maxCurrentRotation;
        for (int i = 0; i < numberOfClockHands; i++) {
            clockHandList.get(i).registerEntityModifier(new RotationByModifier(0.5f, rotationLeft));
        }


        registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {

                // Shift angle to be 100% accurate
                for (int i = 0; i < numberOfClockHands; i++) {
                    float currentClockRotation = clockHandList.get(i).getRotation();
                    clockHandList.get(i).setRotation(Math.round(currentClockRotation));
                }

                registerTouchArea(buttonStart);
                registerTouchArea(buttonReset);
            }
        }));


        resetStopWatchForPosition(0);
        resetStopWatchForPosition(1);
        resetStopWatchForPosition(2);
        resetStopWatchForPosition(3);

    }


    private void createClock() {
        int angleBetweenHands = 360 / numberOfClockHands;
        for (int i = 0; i < numberOfClockHands; i++) {
            Sprite sprite = new Sprite(515, 340, ResourcesManager.getInstance().getClockTextureRegionList().get(i), vertexBufferObjectManager);
            if (i == 0) {
                sprite.setTag(FIRST_HAND_CLOCK_TAG);
            }
            sprite.setRotationCenter(0.46f, 0.04f);
            sprite.registerEntityModifier(new RotationByModifier(0.1f, angleBetweenHands * i));
            sprite.setColor(optionsService.getColorForClockHand(i));
            clockHandList.add(sprite);
        }

        for (Sprite clockHand : clockHandList) {
            attachChild(clockHand);
        }

    }

    private void createClockDial() {
        Sprite sprite = new Sprite(515, 255, ResourcesManager.getInstance().getClockDialTextureRegion(), vertexBufferObjectManager);
        Color color = optionsService.getClockDialColor();
        sprite.setColor(color);
        attachChild(sprite);
    }

    private void startClocks() {
        isClockStarted = true;
        registerUpdateHandler(new TimerHandler(0.25f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                for (Sprite clockHand : clockHandList) {
                    clockHand.registerEntityModifier(new RotationByModifier(0.25f, 1.5f));
                }
            }
        }));

        registerUpdateHandler(new TimerHandler(0.25f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                addSeconds(0.25f);
            }
        }));

    }

    private void addSeconds(Float seconds) {

        if (stopWatchTimeList.get(3) > 9.7f) {
            resetStopWatchForPosition(3);
            addForPosition(2, Float.valueOf(1));
        } else {
            addForPosition(3, seconds);
        }
        if (stopWatchTimeList.get(2) > 5.7f) {
            resetStopWatchForPosition(2);
            addForPosition(1, Float.valueOf(1));
        }
        if (stopWatchTimeList.get(1) > 9.7f) {
            resetStopWatchForPosition(1);
            addForPosition(0, Float.valueOf(1));
        }
        if (stopWatchTimeList.get(0) > 5.7f) {
            resetStopWatchForPosition(0);
            resetStopWatchForPosition(1);
            resetStopWatchForPosition(2);
            resetStopWatchForPosition(3);
        }

    }

    private void addForPosition(Integer position, Float seconds) {
        float newValue = stopWatchTimeList.get(position) + seconds;
        stopWatchTimeList.set(position, newValue);
        stopWatchTextList.get(position).setText(String.valueOf((int) newValue));
    }

    private void resetStopWatchForPosition(Integer position) {
        stopWatchTextList.get(position).setText("0");
        stopWatchTimeList.set(position, Float.valueOf(0));
    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAME);
    }

    private void init(Object... objects) {
        clearUpdateHandlers();
        clearTouchAreas();

        optionsService = new OptionsService();

        firstTimeCounter = 0;
        clockHandList = new ArrayList<Sprite>();
        isClockStarted = false;
        soundEffectPlayed = false;

        numberOfClockHands = optionsService.getNumberOfHandClocks();

    }

    private void createStopWatch() {
        stopWatchTextList = new ArrayList<Text>();

        stopWatchTimeList = new ArrayList<Float>();
        stopWatchTimeList.add(new Float(0));
        stopWatchTimeList.add(new Float(0));
        stopWatchTimeList.add(new Float(0));
        stopWatchTimeList.add(new Float(0));

        stopWatchTextList.add(new Text(80, 300, ResourcesManager.getInstance().getWhiteFontBiggest(), "00", vertexBufferObjectManager));
        stopWatchTextList.add(new Text(120, 300, ResourcesManager.getInstance().getWhiteFontBiggest(), "00", vertexBufferObjectManager));
        stopWatchTextList.add(new Text(200, 300, ResourcesManager.getInstance().getWhiteFontBiggest(), "00", vertexBufferObjectManager));
        stopWatchTextList.add(new Text(240, 300, ResourcesManager.getInstance().getWhiteFontBiggest(), "00", vertexBufferObjectManager));


        Color color = optionsService.getClockDialColor();

        Text colon = new Text(160, 300, ResourcesManager.getInstance().getWhiteFontBiggest(), ":", vertexBufferObjectManager);
        colon.setColor(color);
        attachChild(colon);


        for (Text text : stopWatchTextList) {
            text.setColor(color);
            text.setText("0");
            attachChild(text);
        }
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
//            resourcesManager.getStartGameSound().play();

        }

        handleSoundEffects();


        super.onManagedUpdate(pSecondsElapsed);
    }

    private void handleSoundEffects() {
        if (!soundEffectPlayed) {
            for (Sprite clockHand : clockHandList) {
                if ((int) (clockHand.getRotation() % 360) == 0 && isClockStarted) {
                    ResourcesManager.getInstance().getWinSound().play();
                    soundEffectPlayed = true;
                    registerUpdateHandler(new TimerHandler(5.0f, new ITimerCallback() {
                        @Override
                        public void onTimePassed(TimerHandler pTimerHandler) {
                            soundEffectPlayed = false;
                        }
                    }));
                }
            }
        }

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
}
