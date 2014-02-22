package com.sportwatch.manager;

import android.graphics.Color;
import com.sportwatch.util.ConstantsUtil;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class ResourcesManager {

    private static final ResourcesManager INSTANCE = new ResourcesManager();
    private BaseGameActivity activity;
    private Engine engine;
    private Camera camera;
    private VertexBufferObjectManager vertexBufferObjectManager;

    private BitmapTextureAtlas splashTextureAtlas, menuFontTextureAtlas, gameFontTextureAtlas, whiteFontBigTextureAtlas,
            whiteFontBiggestTextureAtlas, loadingTextureAtlas;
    private BuildableBitmapTextureAtlas menuTextureAtlas, optionsTextureAtlas, gameBackgroundTextureAtlas, gameTextureAtlas;

    // Game
    private ITextureRegion backgroundGameTextureRegion, clockDialTextureRegion,
            buttonPauseTextureRegion, buttonStartTextureRegion, buttonResetTextureRegion;
    private List<ITextureRegion> clockTextureRegionList;


    // Splash
    private ITextureRegion splashTextureRegion;

    // Menu
    private ITextureRegion buttonNewGameTextureRegion, buttonOptionsTextureRegion, menuBackgroundTextureRegion;

    // Options
    private ITextureRegion optionsBackgroundTextureRegion, optionsTextureRegion, arrowUpTextureRegion, arrowDownTextureRegion,
            colorRectangleTextureRegion;

    //Loading
    private ITextureRegion loadingTextureRegion;


    private List<Sound> winSoundList;
    private Font whiteFont, blackFont, whiteFontBig, whiteFontBiggest;

    public static void prepareManager(Engine engine, BaseGameActivity activity, Camera camera, VertexBufferObjectManager vertexBufferObjectManager) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vertexBufferObjectManager = vertexBufferObjectManager;
    }

    public void loadOptionsResources() {
        loadOptionsGraphics();
    }


    public void loadMainMenuResources() {
        loadMainMenuGraphics();
        loadWhiteFont();
        loadBlackFont();
        loadWhiteFontBig();
        loadWhiteFontBiggest();
    }

    public void loadGameResources() {
        loadGameGraphics();
        loadGameMusic();
    }


    private void loadGameMusic() {

        SoundFactory.setAssetBasePath("mfx/other/");
        winSoundList = new ArrayList<Sound>();

        try {
            winSoundList.add(SoundFactory.createSoundFromAsset(getEngine().getSoundManager(), activity, "bell.ogg"));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadOptionsGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/options/");
        optionsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        optionsBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "background.png");
        optionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "options.png");
        arrowUpTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "arrowUp.png");
        arrowDownTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "arrowDown.png");
        colorRectangleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "colorRectangle.png");

        try {
            optionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            optionsTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }


    private void loadGameGraphics() {

        if (gameTextureAtlas != null) {
            gameTextureAtlas.load();
            gameBackgroundTextureAtlas.load();
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        gameBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        clockTextureRegionList = new ArrayList<ITextureRegion>();
        for (int i = 0; i < ConstantsUtil.MAX_NUMBER_OF_CLOCK_HANDS; i++) {
            clockTextureRegionList.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "clock.png"));
        }
        clockDialTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "clockDial.png");
        buttonResetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "reset.png");
        buttonStartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "start.png");
        buttonPauseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pause.png");


        backgroundGameTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBackgroundTextureAtlas, activity, "background.png");

        try {
            gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(5, 5, 5));
            gameBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(5, 5, 5));
            gameTextureAtlas.load();
            gameBackgroundTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }

    }

    private void loadMainMenuGraphics() {

        if (menuTextureAtlas != null) {
            menuTextureAtlas.load();
            return;
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        menuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "background.jpg");
        buttonNewGameTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_new.png");
        buttonOptionsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_options.png");


        try {
            menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            menuTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    public void loadSplashScreen() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.jpg", 0, 0);
        splashTextureAtlas.load();
    }

    public void loadMenuTextures() {
        menuTextureAtlas.load();
    }

    private void loadWhiteFontBiggest() {
        if (whiteFontBiggestTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        whiteFontBiggestTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        whiteFontBiggest = FontFactory.createStrokeFromAsset(activity.getFontManager(), whiteFontBiggestTextureAtlas, activity.getAssets(), "ChalkPaint.ttf", 100, true, Color.WHITE, 2, Color.WHITE);
        whiteFontBiggestTextureAtlas.load();
        whiteFontBiggest.load();
    }

    private void loadWhiteFontBig() {
        if (whiteFontBigTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        whiteFontBigTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        whiteFontBig = FontFactory.createStrokeFromAsset(activity.getFontManager(), whiteFontBigTextureAtlas, activity.getAssets(), "ChalkPaint.ttf", 50, true, Color.WHITE, 2, Color.WHITE);
        whiteFontBigTextureAtlas.load();
        whiteFontBig.load();
    }

    private void loadBlackFont() {
        if (gameFontTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        gameFontTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        blackFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), gameFontTextureAtlas, activity.getAssets(), "font1.ttf", 50, true, Color.BLACK, 2, Color.BLACK);
        gameFontTextureAtlas.load();
        blackFont.load();
    }


    private void loadWhiteFont() {
        if (menuFontTextureAtlas != null) {
            return;
        }
        FontFactory.setAssetBasePath("font/");
        menuFontTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        whiteFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), menuFontTextureAtlas, activity.getAssets(), "ChalkPaint.ttf", 25, true, Color.WHITE, 2, Color.WHITE);
        menuFontTextureAtlas.load();
        whiteFont.load();
    }

    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splashTextureRegion = null;
    }

    public void unloadOptionsTextures() {
        optionsTextureAtlas.unload();
    }

    public void unloadGameTextures() {
        gameTextureAtlas.unload();
        gameBackgroundTextureAtlas.unload();
    }

    public void unloadMenuTextures() {
        menuTextureAtlas.unload();
    }


    public static ResourcesManager getInstance() {
        return INSTANCE;
    }

    public BaseGameActivity getActivity() {
        return activity;
    }

    public Engine getEngine() {
        return engine;
    }

    public Camera getCamera() {
        return camera;
    }

    public VertexBufferObjectManager getVertexBufferObjectManager() {
        return vertexBufferObjectManager;
    }

    public ITextureRegion getSplashTextureRegion() {
        return splashTextureRegion;
    }

    public ITextureRegion getButtonNewGameTextureRegion() {
        return buttonNewGameTextureRegion;
    }

    public ITextureRegion getButtonOptionsTextureRegion() {
        return buttonOptionsTextureRegion;
    }

    public List<ITextureRegion> getClockTextureRegionList() {
        return clockTextureRegionList;
    }

    public ITextureRegion getMenuBackgroundTextureRegion() {
        return menuBackgroundTextureRegion;
    }

    public ITextureRegion getOptionsBackgroundTextureRegion() {
        return optionsBackgroundTextureRegion;
    }

    public ITextureRegion getOptionsTextureRegion() {
        return optionsTextureRegion;
    }

    public Font getWhiteFont() {
        return whiteFont;
    }

    public Font getBlackFont() {
        return blackFont;
    }

    public Font getWhiteFontBig() {
        return whiteFontBig;
    }

    public Font getWhiteFontBiggest() {
        return whiteFontBiggest;
    }

    public Sound getWinSound() {
        Collections.shuffle(winSoundList);
        return winSoundList.get(0);
    }

    public ITextureRegion getBackgroundGameTextureRegion() {
        return backgroundGameTextureRegion;
    }

    public ITextureRegion getLoadingTextureRegion() {
        return loadingTextureRegion;
    }

    public void loadLoadingResources() {
        if (loadingTextureAtlas != null) {
            loadingTextureAtlas.load();
            return;
        }

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/loading/");
        loadingTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        loadingTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(loadingTextureAtlas, activity, "background.png", 0, 0);
        loadingTextureAtlas.load();
    }

    public ITextureRegion getClockDialTextureRegion() {
        return clockDialTextureRegion;
    }

    public ITextureRegion getButtonPauseTextureRegion() {
        return buttonPauseTextureRegion;
    }

    public ITextureRegion getButtonStartTextureRegion() {
        return buttonStartTextureRegion;
    }

    public ITextureRegion getButtonResetTextureRegion() {
        return buttonResetTextureRegion;
    }

    public ITextureRegion getArrowUpTextureRegion() {
        return arrowUpTextureRegion;
    }

    public ITextureRegion getArrowDownTextureRegion() {
        return arrowDownTextureRegion;
    }

    public ITextureRegion getColorRectangleTextureRegion() {
        return colorRectangleTextureRegion;
    }
}
