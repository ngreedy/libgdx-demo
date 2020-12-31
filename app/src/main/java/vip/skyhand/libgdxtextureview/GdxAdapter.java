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
import vip.skyhand.libgdxtextureview.spine.BoneData;
import vip.skyhand.libgdxtextureview.spine.Skeleton;
import vip.skyhand.libgdxtextureview.spine.SkeletonData;
import vip.skyhand.libgdxtextureview.spine.SkeletonJson;
import vip.skyhand.libgdxtextureview.spine.SkeletonRenderer;
import vip.skyhand.libgdxtextureview.spine.SkeletonRendererDebug;
import vip.skyhand.libgdxtextureview.spine.SlotData;

public class GdxAdapter extends ApplicationAdapter {

    private OrthographicCamera camera;
    private PolygonSpriteBatch batch;
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;
    private TextureAtlas atlas;
    private Skeleton skeleton;
    private AnimationState state;
    private SkeletonJson json;

    String TAG = "GdxAdapter";

    @Override
    public void create() {
        camera = new OrthographicCamera();
        batch = new PolygonSpriteBatch();
        renderer = new SkeletonRenderer();
        //renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setBoundingBoxes(false);
        debugRenderer.setRegionAttachments(false);

        atlas = new TextureAtlas(Gdx.files.internal("old/pet_egg_cat_child.atlas"));
        json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(0.5f);


        TextureAtlas atlas1 = new TextureAtlas(Gdx.files.internal("old/pet_egg_dog_child.atlas"));
        SkeletonJson json1 = new SkeletonJson(atlas1); // This loads skeleton JSON data, which is stateless.
        json.setScale(0.5f);
        SkeletonData dogData = json1.readSkeletonData(Gdx.files.internal("old/pet_egg_dog_child.json"));
        BoneData dogTrail = dogData.findBone("dog_trail");

        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("old/pet_egg_cat_child.json"));
        Object[] items = skeletonData.getSlots().items;
        for (int index = 0; index < items.length; index++) {
            SlotData slotData = skeletonData.getSlots().get(index);
            if (slotData.getName().equals("cat_trail")) {
                slotData.setBoneData(dogTrail);
            }
        }

        Log.i("GdxAdapter", "initSkeleton:" + skeletonData.getWidth() + "..." + skeletonData.getHeight());
        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setPosition(ScreenUtils.dip2px(100), 0);

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//        stateData.setMix("walk", "jump", 1f);
//        stateData.setMix("jump", "walk", 1f);

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(1f); // Slow all animations down to 50% speed.


        // Queue animations on track 0.
//        state.setAnimation(0, "walk", true);

//        state.setEmptyAnimation(0,1f);

        state.addAnimation(0, "move_01", false, 0);
        state.addAnimation(0, "move_02", false, 0);
        state.addAnimation(0, "move_03", false, 0);
        state.addAnimation(0, "move_04", false, 0);
        state.addAnimation(0, "move_05", false, 0);
        state.addAnimation(0, "move_01", true, 0);
//        state.addAnimation(0, "dianzan", false, 0); // Run after the jump.
//        state.addAnimation(0, "huishou", false, 0); // Run after the jump.
//        state.addAnimation(0, "lihuxu", false, 0); // Run after the jump.
//        state.addAnimation(0, "tushetou", false, 0); // Run after the jump.
//        state.addAnimation(0, "shihao", false, 0); // Run after the jump.
//        state.addAnimation(0, "tianzhua", false, 0); // Run after the jump.
//        state.addAnimation(0, "yaoweiba", false, 0); // Run after the jump.
//        state.addAnimation(0, "tiaoyue", false, 0); // Run after the jump.
//        state.addAnimation(0, "xiaoshi", false, 0); // Run after the jump.
//        state.addAnimation(0, "xingzou", false, 0); // Run after the jump.
//        state.addAnimation(0, "xuruo", false, 0); // Run after the jump.
//        state.addAnimation(0, "zhanqi", false, 0); // Run after the jump.
//        state.addAnimation(0, "zuoxia", false, 0); // Run after the jump.

        state.addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void start(AnimationState.TrackEntry entry) {
                super.start(entry);
                Log.e(TAG, "start: " + GdxAdapter.this.hashCode());
            }

            @Override
            public void end(AnimationState.TrackEntry entry) {
                super.end(entry);
            }

            @Override
            public void complete(AnimationState.TrackEntry entry) {
                super.complete(entry);
                Log.e(TAG, "complete: " + GdxAdapter.this.hashCode());
            }
        });
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
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    public void setAnimate() {
        state.clearTracks();
        state.addAnimation(0, "walk", true, 0f);
        state.addAnimation(0, "jump", true, 0f);
        state.addAnimation(0, "walk", true, 0f);
//        state.addEmptyAnimation(0,1f,0f);
    }


    public void setAnimate(String animate) {
        state.addAnimation(0, animate, true, 0);
        state.addEmptyAnimation(0, 1f, 0);
//        state.addAnimation(0, animate, true, 0);
    }

    public void zoomBig() {
        camera.zoom = 0.5f;
    }

    public void zoomSmall() {
        camera.zoom = 1f;
    }
}
