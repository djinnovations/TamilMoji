package dj.example.main.model;

import java.util.Map;

import dj.example.main.MyApplication;
import dj.example.main.utils.IDUtils;

/**
 * Created by DJphy on 08-07-2017.
 */

public class NavigationDataObject implements MyApplication.IFragmentData{

    public static final int ACTIVITY = IDUtils.generateViewId();
    public static final int WEB_ACTIVITY = IDUtils.generateViewId();
    public static final int LOGOUT = IDUtils.generateViewId();
    public static final int SHARE = IDUtils.generateViewId();
    public static final int RATE_US = IDUtils.generateViewId();
    public static final int FRAGMENT_VIEW = IDUtils.generateViewId();

    private int viewId;
    private Class targetClass;
    private String title;
    private int targetType;
    private String urlIfWeb;
    private Object param;

    public NavigationDataObject(int viewId, Class targetClass, int targetType) {
        this.viewId = viewId;
        this.targetClass = targetClass;
        this.targetType = targetType;
    }

    public NavigationDataObject(int viewId, Class targetClass, String title, int targetType, String urlIfWeb) {
        this.viewId = viewId;
        this.targetClass = targetClass;
        this.title = title;
        this.targetType = targetType;
        this.urlIfWeb = urlIfWeb;
    }


    public NavigationDataObject(int viewId, String title, int targetType, String urlIfWeb) {
        this.viewId = viewId;
        this.title = title;
        this.targetType = targetType;
        this.urlIfWeb = urlIfWeb;
    }


    public String getUrlIfWeb() {
        return urlIfWeb;
    }

    public void setUrlIfWeb(String urlIfWeb) {
        this.urlIfWeb = urlIfWeb;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }


    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    @Override
    public Class getView() {
        return targetClass;
    }

    @Override
    public String getID() {
        return viewId+"";
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public Object getParam() {
        return null;
    }

    @Override
    public void setParam(Object var1) {
        param = var1;
    }

    @Override
    public Object getActionValue() {
        return param;
    }

    @Override
    public String getActionType() {
        return targetType+"";
    }
}
