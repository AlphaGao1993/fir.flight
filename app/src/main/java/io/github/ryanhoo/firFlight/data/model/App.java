package io.github.ryanhoo.firFlight.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/16/16
 * Time: 11:07 PM
 * Desc: App
 */
/*
{
    "id": "564d8604e75e2d71e5000008",
    "user_id": "55cdb07ff2fc42680d00000c",
    "type": "android",
    "name": "牙医经理人",
    "short": "bailu",
    "bundle_id": "com.Bailu.DentistManager",
    "genre_id": 0,
    "is_opened": true,
    "web_template": "default",
    "custom_market_url": "",
    "has_combo": false,
    "created_at": 1447921156,
    "icon_url": "http://firicon.fir.im/ad11104bab7047cc40760c2bbcfcb3e154071cd8?t=1458042185.912362",
    "master_release": {
      "version": "2.6.5",
      "build": "16031502",
      "release_type": "inhouse",
      "distribution_name": "",
      "supported_platform": null,
      "created_at": 1458030884
    }
  }
*/
public class App implements Parcelable {

    public static final String TYPE_ANDROID = "android";
    public static final String TYPE_IOS = "ios";

    public App() {
        // Empty Constructor
    }

    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("type")
    private String type;

    @SerializedName("name")
    private String name;

    @SerializedName("bundle_id")
    private String bundleId;

    @SerializedName("short")
    private String shortUrl;

    @SerializedName("custom_market_url")
    private String customMarketUrl;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("master_release")
    private Release masterRelease;

    public static String getTypeAndroid() {
        return TYPE_ANDROID;
    }

    public static String getTypeIos() {
        return TYPE_IOS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getCustomMarketUrl() {
        return customMarketUrl;
    }

    public void setCustomMarketUrl(String customMarketUrl) {
        this.customMarketUrl = customMarketUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Release getMasterRelease() {
        return masterRelease;
    }

    public void setMasterRelease(Release masterRelease) {
        this.masterRelease = masterRelease;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.bundleId);
        dest.writeString(this.shortUrl);
        dest.writeString(this.customMarketUrl);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeString(this.iconUrl);
        dest.writeParcelable(this.masterRelease, flags);
    }

    protected App(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.type = in.readString();
        this.name = in.readString();
        this.bundleId = in.readString();
        this.shortUrl = in.readString();
        this.customMarketUrl = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.iconUrl = in.readString();
        this.masterRelease = in.readParcelable(Release.class.getClassLoader());
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };
}
