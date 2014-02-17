package com.sportwatch.activity;

import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.sportwatch.manager.ResourcesManager;
import com.sportwatch.manager.SceneManager;
import com.sportwatch.util.ConstantsUtil;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;

public class MyActivity extends BaseGameActivity {

    private Camera camera;
    private AdView adView;

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, ConstantsUtil.SCREEN_WIDTH, ConstantsUtil.SCREEN_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getRenderOptions().setDithering(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourcesManager.prepareManager(getEngine(), this, camera, getVertexBufferObjectManager());
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);

    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.SPLASH_SCREEN_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                getEngine().unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMainMenuScene();
            }
        }));

        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }

    @Override
    protected void onSetContentView() {

        FrameLayout frameLayout = new FrameLayout(this);

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine, this);

        final FrameLayout.LayoutParams frameLayoutLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);

        try {


            adView = new AdView(this, AdSize.SMART_BANNER, ConstantsUtil.MY_AD_UNIT_ID);
            adView.setTag("adView");
            adView.refreshDrawableState();
            adView.setVisibility(AdView.INVISIBLE);
            adView.setVerticalGravity(Gravity.BOTTOM);


            // Initiate a generic request to load it with an ad
            AdRequest adRequest = new AdRequest();

            adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
            adRequest.addTestDevice("2D91A564A65AF57C28A98B6EC9456D29");
            adRequest.addTestDevice("7F8C8CB8DDF62CBD63E1AE7D4693C1F5");
            adView.loadAd(adRequest);


            final FrameLayout.LayoutParams adViewLayoutParams =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);


            frameLayout.addView(this.mRenderSurfaceView);
            frameLayout.addView(adView, adViewLayoutParams);
        } catch (Exception e) {
            Debug.e("ADS ARE NOT WORKING");
            //ads aren't working. oh well
        }
        this.setContentView(frameLayout, frameLayoutLayoutParams);

    }

    public AdView getAdView() {
        return adView;
    }
}
