package com.badlogic.gdx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

import java.util.HashMap;

/**
 * game_werewolf
 * 2021/2/19
 * hjn
 * <p>
// * 皮肤管理类  每个部位是一个文件， 以其region的name命名
 */
public enum SkinHelper {
    INSTANCE;

    private HashMap<String, TextureData> regionMap = new HashMap<String, TextureData>();

    private HashMap<String, String> currentSkinMap = new HashMap<String, String>(); //当前激活的



    public Texture loadSkin(String name) {
        if (currentSkinMap.get(name) == null) return null;
        TextureData textureData = regionMap.get(name);
        if (textureData == null) {
//            textureData = TextureData.Factory.loadFromFile(Gdx.files.external(currentSkinMap.get(name)), Pixmap.Format.RGBA8888, false);
            textureData = TextureData.Factory.loadFromFile(Gdx.files.internal("old/head_01.png"), Pixmap.Format.RGBA8888, false);
            regionMap.put(name, textureData);
        }
        return new Texture(textureData);
    }

    /**
     * 激活某部位的皮肤
     */
    public void activeSkin(String name, String skinImgPath) {
        if (skinImgPath == null || "".equals(skinImgPath)) return;
        currentSkinMap.clear();
        currentSkinMap.put(name, skinImgPath);
    }

    public void cancelSkin() {
        currentSkinMap.clear();
    }
}
