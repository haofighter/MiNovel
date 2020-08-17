package com.hao.annotationengine;

import android.os.Bundle;

import com.hao.annotetion.task.BindInfo;
import com.hao.annotetion.task.Type;

import static com.hao.annotationengine.Router.allClass;


public class BindDetailInfo extends BindInfo {
    Bundle bundle;

    public BindDetailInfo(String path) {
        super(null, path);
        prapreDate();
    }

    public BindDetailInfo(String path, Bundle bundle) {
        this(path);
        this.bundle = bundle;
    }

    public BindDetailInfo(Class<?> destination, String path) {
        super(destination, path);
    }

    public BindDetailInfo(Class<?> destination, String path, Type type) {
        super(destination, path, type);
    }


    public void prapreDate() {
        try {
            BindInfo bindInfo = allClass.get(getPath());
            setDestination(bindInfo.getDestination());
//            setElement(bindInfo.getElement());
            setType(bindInfo.getType());
        } catch (Exception e) {

        } finally {
        }
    }


    public void skip() {
        switch (getType()) {
            case ACTIVITY:
                Router.getInstance().skipActivity(this);
                break;
            case CLASS:
                Router.getInstance().skipClass(this);
                break;

        }
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
