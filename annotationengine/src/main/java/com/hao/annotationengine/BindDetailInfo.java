package com.hao.annotationengine;

import android.os.Bundle;

import com.hao.annotetion.annotation.Bind;
import com.hao.annotetion.task.BindInfo;
import com.hao.annotetion.task.Type;

import java.util.Map;

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


    public void prapreDate() {
        try {
            Map<String, BindInfo> bindInfoMap = allClass.get(getPath().split("/")[0]);
            setDestination(bindInfoMap.get(getPath()).getDestination());
//            setDestination(bindInfo.getDestination());
            setType(bindInfoMap.get(getPath()).getType());
        } catch (Exception e) {
            e.printStackTrace();
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
