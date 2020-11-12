package com.hao.minovel.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;

public class TypeFaceUtils {
    private static TypeFaceList typeFaceInfoList = new TypeFaceList();

    public static TypeFaceList getTypeFaceInfoList() {
        return typeFaceInfoList;
    }

    public static void init(Context context) {
        getTypeFaces(context);
    }


    public static Typeface getTypeFaceByName(String typeFaceName) {
        TypeFaceInfo typeface = typeFaceInfoList.get(typeFaceName);
        if (typeface == null || typeface.getTypeface() == null) {
            return Typeface.DEFAULT;
        }
        return typeface.getTypeface();
    }

    public void addTypeFace(File file, String name) {
        TypeFaceInfo typeface = typeFaceInfoList.get(name);
        if (typeface == null || typeface.getTypeface() == null) {
            typeface = new TypeFaceInfo(file, name);
            typeFaceInfoList.add(typeface);
        }
    }


    private Typeface getTypeface(String fontFilename, String fonttname, Context context) {
        TypeFaceInfo typeface = typeFaceInfoList.get(fonttname);
        if (typeface == null || typeface.getTypeface() == null) {
            typeface = new TypeFaceInfo(fontFilename, fontFilename, context);
            typeFaceInfoList.add(typeface);
        }
        return typeface.getTypeface();
    }

    private static void getTypeFaces(Context context) {
        typeFaceInfoList.add(new TypeFaceInfo(Typeface.DEFAULT, "默认字体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZBWKSJW.TTF", "方正北魏楷书简体", context));
        typeFaceInfoList.add(new TypeFaceInfo("FZJZJW.TTF", "方正剪纸简体", context));
        typeFaceInfoList.add(new TypeFaceInfo("FZPTYJW.TTF", "方正胖头鱼", context));
        typeFaceInfoList.add(new TypeFaceInfo("FZTJLSJW.TTF", "方正铁筋隶书简体", context));
        typeFaceInfoList.add(new TypeFaceInfo("HWCY.TTF", "华文彩云", context));
        typeFaceInfoList.add(new TypeFaceInfo("WRJZY.TTF", "微软简中圆", context));
        typeFaceInfoList.add(new TypeFaceInfo("HYZKJ.ttf", "汉仪中楷简", context));
        typeFaceInfoList.add(new TypeFaceInfo("HYCYJ.ttf", "汉仪粗圆简", context));
        typeFaceInfoList.add(new TypeFaceInfo("HYDYTJ.ttf", "汉仪蝶语体简", context));
        typeFaceInfoList.add(new TypeFaceInfo("JQT.TTF", "简启体", context));
        typeFaceInfoList.add(new TypeFaceInfo("FZQKBYSJT.TTF", "方正清刻本悦宋简体", context));
        typeFaceInfoList.add(new TypeFaceInfo("FZPWJT.ttf", "方正胖娃简体", context));
        typeFaceInfoList.add(new TypeFaceInfo("FZSEYVBT.ttf", "方正莎儿硬笔体", context));
        typeFaceInfoList.add(new TypeFaceInfo("HYWWZJ.ttf", "汉仪娃娃篆简", context));
    }


    public static class TypeFaceInfo {
        Typeface typeface;
        String typeFacename;
        String typeFaceFileName;

        public TypeFaceInfo(String typeFaceFileName, String typeFacename, Context context) {
            this.typeFaceFileName = typeFaceFileName;
            this.typeFacename = typeFacename;
            this.typeface = Typeface.createFromAsset(context.getAssets(), typeFaceFileName);
        }

        public TypeFaceInfo(Typeface typeface, String typeFacename) {
            this.typeface = typeface;
            this.typeFacename = typeFacename;
        }

        public TypeFaceInfo(File file, String typeFacename) {
            this.typeface = Typeface.createFromFile(file);
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

    static class TypeFaceList extends ArrayList<TypeFaceInfo> {

        public TypeFaceInfo get(String name) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getTypeFacename().equals(name)) {
                    return get(i);
                }
            }
            return null;
        }

        @Override
        public boolean add(TypeFaceInfo typeFaceInfo) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getTypeFacename().equals(typeFaceInfo.getTypeFacename())) {
                    return false;
                }
            }
            if (TextUtils.isEmpty(typeFaceInfo.getTypeFacename())) {
                return false;
            }
            if (typeFaceInfo.getTypeface() == null) {
                return false;
            }
            return super.add(typeFaceInfo);
        }
    }

}
