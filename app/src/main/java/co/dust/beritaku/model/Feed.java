package co.dust.beritaku.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irsal on 9/23/16.
 */

public class Feed extends BaseItem {

    @SerializedName("title")
    private String title;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("feed_url")
    private String feedUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }
}
