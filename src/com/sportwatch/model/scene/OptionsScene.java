package com.sportwatch.model.scene;

import com.sportwatch.manager.ResourcesManager;
import com.sportwatch.manager.SceneManager;
import com.sportwatch.matcher.ClassIEntityMatcher;
import com.sportwatch.service.OptionsService;
import com.sportwatch.util.ClockHandColor;
import com.sportwatch.util.ConstantsUtil;
import com.sportwatch.util.SceneType;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class OptionsScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

    private MenuScene menuScene;

    private Integer numberOfHandClocks;
    private OptionsService optionsService;
    private boolean removeTexts;

    private final int RECTANGLE_SIZE = 30;
    private final float RECTANGLE_SCALE = 1.5f;
    private final float RECTANGLE_SCALE_TIME = 0.25f;

    public static final int CLOCK_DIAL_NUMER = 100;




    @Override
    public void createScene(Object... objects) {
        init();
        createBackground();
        createTopLeftCaption();
        createTopNumbers();
        createTextsAndColorRectangles();
        createTextAndRectangleForClockDial();
    }

    private void init() {
        optionsService = new OptionsService();
        numberOfHandClocks = optionsService.getNumberOfHandClocks();
    }


    /**
     * Creates middle captions + rectangles
     */
    private void createTextsAndColorRectangles() {
        for (int i = 0; i < numberOfHandClocks; i++) {
            int height = 400 - 60 * (i + 1);
            Text text = new Text(150, height, ResourcesManager.getInstance().getWhiteFont(), "Clock hand " + (i + 1) + " color", vertexBufferObjectManager);
            createColorRectangleLine(height, i);
            attachChild(text);
        }
    }

    private void createTextAndRectangleForClockDial(){
        Text text = new Text(150, 400, ResourcesManager.getInstance().getWhiteFont(), "Color of clock dial", vertexBufferObjectManager);
        attachChild(text);
        createColorRectangleLine(400,CLOCK_DIAL_NUMER);

    }

    private void createColorRectangleLine(final int height, final int clockNumber) {



        int positionX = 320;
        for (final ClockHandColor color : ClockHandColor.values()) {
            Rectangle rectangle = new Rectangle(positionX, height, RECTANGLE_SIZE, RECTANGLE_SIZE, vertexBufferObjectManager) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    if (pSceneTouchEvent.isActionUp()) {

                        if(clockNumber == CLOCK_DIAL_NUMER){
                            optionsService.setClockDialColor(clockNumber, color.name());
                        }else {
                            optionsService.setClockHandColor(clockNumber, color.name());
                        }


                        resetPreviousRectangle(height);
                        registerEntityModifier(new ScaleModifier(RECTANGLE_SCALE_TIME, 1.0f, RECTANGLE_SCALE));

                        return true;
                    }
                    return false;
                }
            };
            if(optionsService.isClockHandColored(clockNumber,color)){
                rectangle.setScale(RECTANGLE_SCALE);
            }
            rectangle.setColor(color.getColor());
            positionX += 60;
            registerTouchArea(rectangle);
            attachChild(rectangle);
        }

    }

    private void resetPreviousRectangle(int height) {
        for (IEntity entity : mChildren) {
            Debug.e(String.valueOf(entity.getWidth()));
            if (entity instanceof Rectangle && entity.getY() == height) {
                if (entity.getScaleX() == RECTANGLE_SCALE) {
                    entity.registerEntityModifier(new ScaleModifier(RECTANGLE_SCALE_TIME, RECTANGLE_SCALE, 1.0f));
                }
            }

        }
    }

    /**
     * Creates top numbers
     */
    private void createTopNumbers(){
        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);


        List<IMenuItem> menuItemList = new ArrayList<IMenuItem>();
        for (int i = 0; i < ConstantsUtil.MAX_NUMBER_OF_CLOCK_HANDS; i++) {
            TextMenuItem textMenuItem = new TextMenuItem(i, ResourcesManager.getInstance().getWhiteFontBig(), String.valueOf(i + 1), vertexBufferObjectManager);
            menuItemList.add(new ScaleMenuItemDecorator(textMenuItem, 1.2f, 1));
        }

        for (IMenuItem menuItem : menuItemList) {
            menuScene.addMenuItem(menuItem);
        }

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        for (int i = 0; i < menuItemList.size(); i++) {
            menuItemList.get(i).setPosition(300 + 80 * (i + 1), 440);
        }

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getOptionsBackgroundTextureRegion(), vertexBufferObjectManager));
    }

    private void createTopLeftCaption(){
        Text text = new Text(200,440,ResourcesManager.getInstance().getWhiteFont(),"Number of clock hands",vertexBufferObjectManager);
        attachChild(text);
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);


        if (removeTexts) {
            removeTexts();
            removeColorRectangles();
            clearTouchAreas();
            createTextAndRectangleForClockDial();
            createTextsAndColorRectangles();
            createTopLeftCaption();

        }
    }

    /**
     * Removes:
     * topLeftText next to Numbers
     * middleTexts next to rectangles, the first for dial included
     */
    private void removeTexts() {
        removeTexts = false;
        IEntity entity;
        do {
            entity = getChildByMatcher(new ClassIEntityMatcher(Text.class));
            if (entity != null) {
                entity.detachSelf();
            }
        } while (entity != null);
    }

    private void removeColorRectangles() {
        IEntity entity;
        do {
            entity = getChildByMatcher(new ClassIEntityMatcher(Rectangle.class));
            if (entity != null) {
                entity.detachSelf();
            }
        } while (entity != null);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.OPTIONS);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.OPTIONS;
    }

    @Override
    public void disposeScene() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        updateNumberOfHandClocks(pMenuItem.getID());
        return true;
    }

    private void updateNumberOfHandClocks(int handClockNumber) {
        optionsService.updateNumberOfHandClocks(handClockNumber + 1);
        numberOfHandClocks = optionsService.getNumberOfHandClocks();
        removeTexts = true;
    }
}
