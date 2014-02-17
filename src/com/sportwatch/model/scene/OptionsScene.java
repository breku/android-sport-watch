package com.sportwatch.model.scene;

import com.sportwatch.manager.ResourcesManager;
import com.sportwatch.manager.SceneManager;
import com.sportwatch.matcher.ClassIEntityMatcher;
import com.sportwatch.service.OptionsService;
import com.sportwatch.util.ClockHandColor;
import com.sportwatch.util.ConstantsUtil;
import com.sportwatch.util.SceneType;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

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


    @Override
    public void createScene(Object... objects) {
        init();
        createBackground();
        createTextsAndColorRectangles();
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
            int height = 460 - 60 * (i + 1);
            Text text = new Text(150, height, ResourcesManager.getInstance().getWhiteFont(), "Clock hand " + (i + 1) + " color", vertexBufferObjectManager);
            createColorRectangleLine(height, i);
            attachChild(text);
        }
    }

    private void createColorRectangleLine(int height, final int clockNumber) {
        int positionX = 320;
        for (final ClockHandColor color : ClockHandColor.values()) {
            Rectangle rectangle = new Rectangle(positionX, height, 30, 30, vertexBufferObjectManager) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    if (pSceneTouchEvent.isActionUp()) {
                        optionsService.setClockHandColor(clockNumber, color.name());
                        return true;
                    }
                    return false;
                }
            };
            rectangle.setColor(color.getColor());
            rectangle.setTag(clockNumber);
            positionX += 60;
            registerTouchArea(rectangle);
            attachChild(rectangle);
        }

    }


    /**
     * Creates top numbers
     */
    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getOptionsBackgroundTextureRegion(), vertexBufferObjectManager));

        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);


        List<IMenuItem> menuItemList = new ArrayList<IMenuItem>();
        for (int i = 0; i < ConstantsUtil.MAX_NUMBER_OF_CLOCK_HANDS; i++) {
            menuItemList.add(new ScaleMenuItemDecorator(new TextMenuItem(i, ResourcesManager.getInstance().getWhiteFont(), String.valueOf(i + 1), vertexBufferObjectManager), 1.2f, 1));
        }

        for (IMenuItem menuItem : menuItemList) {
            menuScene.addMenuItem(menuItem);
        }

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        for (int i = 0; i < menuItemList.size(); i++) {
            menuItemList.get(i).setPosition(120 * (i + 1), 440);
        }

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);

    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);


        if (removeTexts) {
            removeTexts();
            removeColorRectangles();
            clearTouchAreas();
            createTextsAndColorRectangles();
        }
    }

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
