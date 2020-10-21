package com.hao.minovel.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.hao.minovel.tinker.app.AppContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TypeFaceUtils {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    private static List<TypeFaceInfo> typeFaceInfoList = new ArrayList<TypeFaceInfo>() {
        @Override
        public TypeFaceInfo get(int index) {
            TypeFaceInfo typeFaceInfo = super.get(index);
            typeFaceInfo.setTypeface(getTypeface(typeFaceInfo.typeFaceFileName,typeFaceInfo.getTypeFacename(), AppContext.application));
            return typeFaceInfo;
        }

    };
    private static TypeFaceUtils typeFaceUtils = new TypeFaceUtils();

    private TypeFaceUtils() {
        getTypeFaces();
    }

    public static List<TypeFaceInfo> getTypeFaceInfoList() {
        synchronized (TypeFaceUtils.class) {
            if (typeFaceUtils == null) {
                typeFaceUtils = new TypeFaceUtils();
            }
        }
        return typeFaceInfoList;
    }

    public static Typeface getTypeFaceByName(String typeFaceName) {
        Typeface typeface = fontCache.get(typeFaceName);
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }

    public void addTypeFace(File file, String name) {
        Typeface typeface = fontCache.get(name);
        if (typeface == null) {
            typeface = Typeface.createFromFile(file);
            fontCache.put(name, typeface);
        }
        if (typeFaceInfoList == null) {
            typeFaceInfoList = new ArrayList<>();
        }
        typeFaceInfoList.add(new TypeFaceInfo(typeface, name));
    }


    private static Typeface getTypeface(String fontFilename,String fonttname, Context context) {
        Typeface typeface = fontCache.get(fonttname);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontFilename);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fonttname, typeface);
        }
        return typeface;
    }

    private void getTypeFaces() {
        typeFaceInfoList.add(new TypeFaceInfo(Typeface.DEFAULT, "默认字体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZBWKSJW.TTF", "方正北魏楷书简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZJZJW.TTF", "方正剪纸简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZPTYJW.TTF", "方正胖头鱼"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZTJLSJW.TTF", "方正铁筋隶书简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZY4JW.TTF", "方正粗圆简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("HWCY.TTF", "华文彩云"));
//        typeFaceInfoList.add(new TypeFaceInfo("simfang.ttf", "仿宋"));
//        typeFaceInfoList.add(new TypeFaceInfo("WRJZY.ttf", "微软简中圆"));
//        typeFaceInfoList.add(new TypeFaceInfo("HYZKJ.ttf", "汉仪中楷简"));
//        typeFaceInfoList.add(new TypeFaceInfo("HYCYJ.ttf", "汉仪粗圆简"));
//        typeFaceInfoList.add(new TypeFaceInfo("HYDYTJ.ttf", "汉仪蝶语体简"));
//        typeFaceInfoList.add(new TypeFaceInfo("JQT.TTF", "简启体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZQKBYSJT.TTF", "方正清刻本悦宋简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZPWJT.ttf", "方正胖娃简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("FZSEYVBT.ttf", "方正莎儿硬笔体"));
//        typeFaceInfoList.add(new TypeFaceInfo("HYJBRYJT.ttf", "汉仪井柏然简体"));
//        typeFaceInfoList.add(new TypeFaceInfo("HYWWZJ.ttf", "汉仪娃娃篆简"));
    }


    public class TypeFaceInfo {
        Typeface typeface;
        String typeFacename;
        String typeFaceFileName;

        public TypeFaceInfo(String typeFaceFileName, String typeFacename) {
            this.typeFaceFileName = typeFaceFileName;
            this.typeFacename = typeFacename;
        }

        public TypeFaceInfo(Typeface typeface, String typeFacename) {
            this.typeface = typeface;
            this.typeFacename = typeFacename;
        }

        public Typeface getTypeface() {
            return typeface;
        }

        public void setTypeface(Typeface typeface) {
            this.typeface = typeface;
        }

        public String getTypeFacename() {
            return typeFacename;
        }

        public void setTypeFacename(String typeFacename) {
            this.typeFacename = typeFacename;
        }
    }

}
