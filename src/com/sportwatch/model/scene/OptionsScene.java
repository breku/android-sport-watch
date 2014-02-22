package com.sportwatch.model.scene;

import android.widget.Toast;
import com.sportwatch.manager.ResourcesManager;
import com.sportwatch.manager.SceneManager;
import com.sportwatch.service.OptionsService;
import com.sportwatch.util.ClockHandColor;
import com.sportwatch.util.ClockHandText;
import com.sportwatch.util.ConstantsUtil;
import com.sportwatch.util.SceneType;
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

    public static final int CLOCK_DIAL_NUMER = 100;


    // Enums
    private ClockHandText currentClockHandText;
    private ClockHandColor currentClockHandColor;

    // Sprites
    private Text clockHandText;
    private Sprite colorRectangle;


    @Override
    public void createScene(Object... objects) {
        init();
        createBackground();
        createTopNumbersAndCaption();
        createArrows();
        initTextAndRectangle();
    }

    private void initTextAndRectangle() {
        clockHandText = new Text(300,280,ResourcesManager.getInstance().getWhiteFont(),"Clock hand 1 color:        ",vertexBufferObjectManager);
        clockHandText.setText(currentClockHandText.getText());
        colorRectangle = new Sprite(600,280,ResourcesManager.getInstance().getColorRectangleTextureRegion(),vertexBufferObjectManager);
        colorRectangle.setColor(optionsService.getColorForClockHand(currentClockHandText.getNumber()));
        attachChild(clockHandText);
        attachChild(colorRectangle);
    }

    private void createArrows() {
        // Arrows of text
        Sprite arrowUpLeft = new Sprite(300,350,ResourcesManager.getInstance().getArrowUpTextureRegion(),vertexBufferObjectManager){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionUp()){
                    currentClockHandText = currentClockHandText.previous();

                    colorRectangle.setColor(optionsService.getColorForClockHand(currentClockHandText.getNumber()));
                    clockHandText.setText(currentClockHandText.getText());
                    return true;
                }
                return false;
            }
        };

        // Arrows of color
        Sprite arrowUpRight = new Sprite(600,350,ResourcesManager.getInstance().getArrowUpTextureRegion(),vertexBufferObjectManager){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionUp()){
                    currentClockHandColor = currentClockHandColor.previous();
                    colorRectangle.setColor(currentClockHandColor.getColor());
                    optionsService.setClockDialColor(currentClockHandText.getNumber(),currentClockHandColor.name());
                    return true;
                }
                return false;
            }
        };

        // Arrows of text
        Sprite arrowDownLeft = new Sprite(300,200,ResourcesManager.getInstance().getArrowDownTextureRegion(),vertexBufferObjectManager){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionUp()){
                    currentClockHandText = currentClockHandText.next();

                    colorRectangle.setColor(optionsService.getColorForClockHand(currentClockHandText.getNumber()));
                    clockHandText.setText(currentClockHandText.getText());
                    return true;
                }
                return false;
            }
        };

        // Arrows of color
        Sprite arrowDownRight = new Sprite(600,200,ResourcesManager.getInstance().getArrowDownTextureRegion(),vertexBufferObjectManager){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionUp()){
                    currentClockHandColor = currentClockHandColor.next();
                    colorRectangle.setColor(currentClockHandColor.getColor());
                    optionsService.setClockDialColor(currentClockHandText.getNumber(),currentClockHandColor.name());
                    return true;
                }
                return false;
            }
        };

        registerTouchArea(arrowDownLeft);
        registerTouchArea(arrowDownRight);
        registerTouchArea(arrowUpLeft);
        registerTouchArea(arrowUpRight);

        attachChild(arrowDownLeft);
        attachChild(arrowDownRight);
        attachChild(arrowUpLeft);
        attachChild(arrowUpRight);
    }

    private void init() {
        optionsService = new OptionsService();
        numberOfHandClocks = optionsService.getNumberOfHandClocks();
        currentClockHandText = ClockHandText.A;
        currentClockHandColor = ClockHandColor.WHITE;

    }


    /**
     * Creates top numbers
     */
    private void createTopNumbersAndCaption() {
        Text text = new Text(400, 440, ResourcesManager.getInstance().getWhiteFont(), "Number of clock hands", vertexBufferObjectManager);
        attachChild(text);

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
            menuItemList.get(i).setPosition(100 + 80 * (i + 1), 400);
        }

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getOptionsBackgroundTextureRegion(), vertexBufferObjectManager));
    }



    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);


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
        showToastNumberOfClockHandsSaved();
        return true;
    }

    private void showToastNumberOfClockHandsSaved(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Number of clock hands saved", 2).show();
            }
        });
    }

    private void updateNumberOfHandClocks(int handClockNumber) {
        optionsService.updateNumberOfHandClocks(handClockNumber + 1);
        numberOfHandClocks = optionsService.getNumberOfHandClocks();
    }
}
