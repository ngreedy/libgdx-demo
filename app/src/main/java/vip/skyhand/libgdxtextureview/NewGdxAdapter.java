package vip.skyhand.libgdxtextureview;

import android.util.Log;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import vip.skyhand.libgdxtextureview.spine.AnimationState;
import vip.skyhand.libgdxtextureview.spine.AnimationStateData;
import vip.skyhand.libgdxtextureview.spine.Skeleton;
import vip.skyhand.libgdxtextureview.spine.SkeletonData;
import vip.skyhand.libgdxtextureview.spine.SkeletonJson;
import vip.skyhand.libgdxtextureview.spine.SkeletonRenderer;
import vip.skyhand.libgdxtextureview.spine.SkeletonRendererDebug;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class NewGdxAdapter extends ApplicationAdapter {

    private OrthographicCamera camera;
    private PolygonSpriteBatch batch;
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;
    private TextureAtlas atlas;
    private Skeleton skeleton;
    private AnimationState state;
    private SkeletonJson json;
    private String filePath;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        batch = new PolygonSpriteBatch();
        renderer = new SkeletonRenderer();
        //renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setBoundingBoxes(false);
        debugRenderer.setRegionAttachments(false);

        Log.e("GdxAdapter", "create: " + filePath);
        File file = new File(filePath);
        String[] split = file.getName().split("\\.");
        Log.e("GdxAdapter", "create: " + file.getName());
        Log.e("GdxAdapter", "create: " + split[0]);

        File parent = new File(file.getParent());

        atlas = new TextureAtlas(Gdx.files.external(file.getParent() + "/" + split[0] + ".atlas"));
        json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(2f);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.external(file.getParent() + "/" + split[0] + ".json"));
        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        float width = skeletonData.getWidth();
        float height = skeletonData.getHeight();
        skeleton.setPosition(ScreenUtils.getScreenWidth() / 2, 0);
        int screenWidth = ScreenUtils.getScreenWidth();
        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//        stateData.setMix("walk", "jump", 0.2f);
//        stateData.setMix("jump", "walk", 0.2f);

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//        state.setTimeScale(1.0f); // Slow all animations down to 50% speed.

        // Queue animations on track 0.
        state.setAnimation(0, "animation", true);
//        state.setAnimation(0, "walk", true);

//        state.addAnimation(0, "walk", true, 0); // Run after the jump.
    }


    @Override
    public void render() {
        state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glClearColor(0, 0, 0, 0);

        state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
        skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

        // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
        camera.update();
        batch.getProjectionMatrix().set(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton); // Draw the skeleton images.
        batch.end();

//        debugRenderer.draw(skeleton); // Draw debug lines.
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false); // Update camera with new size.
        camera.viewportWidth = ScreenUtils.getScreenWidth();
        camera.viewportHeight = ScreenUtils.getScreenHeight();
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    public void setAnimate() {
//        setAnimate("jump");
//        setAnimate("walk"); // Run after the jump.
    }

    public void setAnimate(String animate) {
        state.addAnimation(0, animate, true, 0);
    }

    public void zoomBig() {
        camera.zoom = 0.5f;
    }

    public void zoomSmall() {
        camera.zoom = 1f;
    }

    public void setName(@NotNull String filePath) {
        this.filePath = filePath;
    }
}
