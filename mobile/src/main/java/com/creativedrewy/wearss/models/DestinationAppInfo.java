package com.creativedrewy.wearss.models;

import android.graphics.drawable.Drawable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * POJO to hold information about an app that can receive an article url
 */
public class DestinationAppInfo {
    @Expose
    private String packageName;
    @Expose
    private String activityInfo;
    @Expose
    private String readableName;
    private Drawable appIcon;

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getActivityInfo() { return activityInfo; }
    public void setActivityInfo(String activityInfo) { this.activityInfo = activityInfo; }

    public String getReadableName() { return readableName; }
    public void setReadableName(String readableName) { this.readableName = readableName; }

    public Drawable getAppIcon() { return appIcon; }
    public void setAppIcon(Drawable appIcon) { this.appIcon = appIcon; }

    public String serialize() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(this);
    }

    public static DestinationAppInfo deserialize(String input) {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .fromJson(input, DestinationAppInfo.class);
    }
}
