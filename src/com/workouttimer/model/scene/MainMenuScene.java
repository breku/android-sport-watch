package com.workouttimer.model.scene;

import com.workouttimer.manager.ResourcesManager;
import com.workouttimer.manager.SceneManager;
import com.workouttimer.util.ConstantsUtil;
import com.workouttimer.util.SceneType;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class MainMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {
    private MenuScene menuScene;
    private final int NEW_GAME = 0;
    private final int OPTIONS = 1;


    @Override
    public void createScene(Object... objects) {
        createBackground();
        createButtons();

    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getMenuBackgroundTextureRegion(), vertexBufferObjectManager));
    }

    private void createButtons() {
        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);

        final IMenuItem newGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(NEW_GAME, ResourcesManager.getInstance().getButtonNewGameTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem optionsItem = new ScaleMenuItemDecorator(new SpriteMenuItem(OPTIONS, ResourcesManager.getInstance().getButtonOptionsTextureRegion(), vertexBufferObjectManager), 1.2f, 1);

        menuScene.addMenuItem(newGameItem);
        menuScene.addMenuItem(optionsItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        newGameItem.setPosition(210, 397);
        optionsItem.setPosition(210, 327);

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);

    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.MENU;
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case NEW_GAME:
                SceneManager.getInstance().loadGameScene();
                break;
            case OPTIONS:
                SceneManager.getInstance().loadOptionsScene();
                break;
            default:
                return false;
        }
        return false;
    }
}
