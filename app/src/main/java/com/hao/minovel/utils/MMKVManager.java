package com.hao.minovel.utils;

import android.os.Parcelable;

import com.hao.minovel.tinker.app.AppContext;
import com.tencent.mmkv.MMKV;

import java.util.Set;

/**
 * 作者：Tangren on 2019-03-28
 * 包名：com.szxb.yps.db.sp
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class MMKVManager {

    private volatile static MMKVManager instance = null;

    private MMKV mmkv;

    private MMKVManager() {
        MMKV.initialize(AppContext.application);
        mmkv = MMKV.defaultMMKV();
    }

    private MMKVManager(String path) {
        MMKV.initialize(path);
        mmkv = MMKV.defaultMMKV();
    }

    public static MMKVManager getInstance(String path) {
        if (instance == null) {
            synchronized (MMKVManager.class) {
                if (instance == null) {
                    instance = new MMKVManager(path);
                }
            }
        }
        return instance;
    }

    public static MMKVManager getInstance() {
        if (instance == null) {
            synchronized (MMKVManager.class) {
                if (instance == null) {
                    instance = new MMKVManager();
                }
            }
        }
        return instance;
    }


    /**
     * @param key   属性名
     * @param value 属性值
     */
    public void put(String key, Object value) {
        if (value instanceof Boolean) {
            mmkv.encode(key, (Boolean) value);
        } else if (value instanceof Long) {
            mmkv.encode(key, (Long) value);
        } else if (value instanceof Integer) {
            mmkv.encode(key, (Integer) value);
        } else if (value instanceof Double) {
            mmkv.encode(key, (Double) value);
        } else if (value instanceof Float) {
            mmkv.encode(key, (Float) value);
        } else if (value instanceof byte[]) {
            mmkv.encode(key, (byte[]) value);
        } else if (value instanceof Set) {
            //Set仅支持String
            mmkv.putStringSet(key, (Set<String>) value);
        } else if (value instanceof Parcelable) {
            mmkv.encode(key, (Parcelable) value);
        } else {
            mmkv.encode(key, (String) value);
        }
    }

    /**
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public Object get(String key, Object defaultValue) {
        if (defaultValue instanceof Boolean) {
            return mmkv.decodeBool(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Long) {
            return mmkv.decodeLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return mmkv.decodeInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Double) {
            return mmkv.decodeDouble(key, (Double) defaultValue);
        } else if (defaultValue instanceof Float) {
            return mmkv.decodeFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof byte[]) {
            return mmkv.decodeBytes(key);
        } else if (defaultValue instanceof Set) {
            return mmkv.getStringSet(key, (Set<String>) defaultValue);
        } else {
            return mmkv.decodeString(key, (String) defaultValue);
        }
    }

    /**
     * @param key          属性名
     * @param tClass       类型
     * @param defaultValue 默认
     * @param <T>          必须是实现了Parcelable
     * @return Parcelable实体类
     */
    public <T extends Parcelable> Object get(String key, Class<T> tClass, T defaultValue) {
        return mmkv.decodeParcelable(key, tClass, defaultValue);
    }

    /**
     * @param key    属性名
     * @param tClass 属性类型
     * @param <T>    泛型
     * @return 属性值
     */
    public <T> Object get(String key, Class<T> tClass) {
        if (tClass == Boolean.class) {
            return mmkv.decodeBool(key);
        } else if (tClass == Long.class) {
            return mmkv.decodeLong(key);
        } else if (tClass == Integer.class) {
            return mmkv.decodeInt(key);
        } else if (tClass == Double.class) {
            return mmkv.decodeDouble(key);
        } else if (tClass == Float.class) {
            return mmkv.decodeFloat(key);
        } else if (tClass == byte[].class) {
            return mmkv.decodeBytes(key);
        } else {
            return mmkv.decodeString(key);
        }
    }

    /**
     * @param key    属性名
     * @param tClass 属性类型
     * @param <T>    泛型
     * @return Parcelable
     */
    public <T extends Parcelable> Object getParcelable(String key, Class<T> tClass) {
        return mmkv.decodeParcelable(key, tClass);
    }

    /**
     * @param key 属性名
     */
    public void removeValueForKey(String key) {
        if (mmkv.containsKey(key)) {
            mmkv.removeValueForKey(key);
        }
    }

    /**
     * @param key 属性名
     *            批量移除value
     */
    public void removeValuesForKeys(String key[]) {
        mmkv.removeValuesForKeys(key);
    }

}
