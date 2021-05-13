package com.badlogic.gdx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;


/**
 * game_werewolf
 * 2021/2/19
 * hjn
 * <p>
 */
public enum SkinHelper {
    INSTANCE;

    private final HashMap<String, TextureAtlas.AtlasRegion> regionCache = new HashMap<String, TextureAtlas.AtlasRegion>();

    private final HashMap<String, TextureAtlas.AtlasRegion> originRegionMap = new HashMap<String, TextureAtlas.AtlasRegion>();

    public final HashMap<String, HashMap<String, String>> currentSkinMap = new HashMap<String, HashMap<String, String>>();


    private final HashMap<String, TextureData> textureDataCache = new HashMap<String, TextureData>();// 初始region，

    public TextureData getTextureData(String regionName) {
        try {
            TextureData textureData = textureDataCache.get(regionName);
            if (textureData == null) {
//                String filePath = currentSkinMap.get("roleId").get("dog");
//                if (filePath == null || "".equals(filePath)) {
//                    return textureData;
//                }
                System.out.println("getTextureData====> " + regionName);
                textureData = TextureData.Factory.loadFromFile(Gdx.files.internal("skin/" + regionName + ".png"), Pixmap.Format.RGBA8888, false);
                textureDataCache.put(regionName, textureData);
            }
            return textureData;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     */
    public void activeSkin(String petId, String name, String skinImgPath) {
        System.out.println("pet active skin " + petId + "...." + name + "...." + skinImgPath);
        if (skinImgPath == null || "".equals(skinImgPath)) return;
        if (currentSkinMap.get(petId) == null) {
            currentSkinMap.put(petId, new HashMap<String, String>());
        }
        currentSkinMap.get(petId).put(name, skinImgPath);
    }

    public void cancelSkin(String petId) {
        System.out.println("pet cancel skin " + petId);
        if (currentSkinMap.get(petId) != null) {
            currentSkinMap.get(petId).clear();
            currentSkinMap.remove(petId);
        }
    }

    public TextureAtlas.AtlasRegion getOriginalRegion(String name) {
        return originRegionMap.get(name);
    }

    public void saveOriginRegion(String name, TextureAtlas.AtlasRegion region) {
        originRegionMap.put(name, region);
    }
}
