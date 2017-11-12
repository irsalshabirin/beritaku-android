package co.dust.beritaku.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irsal on 7/31/16.
 */
public class Article extends BaseItem {

    @SerializedName("title")
    private String title;

    @SerializedName("media_content_url")
    private String mediaContentUrl;

    @SerializedName("link")
    private String link;

    @SerializedName("content")
    private String content;

    @SerializedName("centroid_id")
    private int centroidId;

    @SerializedName("centroid")
    private Centroid centroid;

    @SerializedName("publish_date")
    private String publishDate;

    @SerializedName("feed")
    private Feed feed;

    @SerializedName("feed_id")
    private int feedId;


//    @SerializedName("summary_comment")
//    private ArrayList<Comment> summaryComments;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaContentUrl() {
        return mediaContentUrl;
    }

    public void setMediaContentUrl(String mediaContentUrl) {
        this.mediaContentUrl = mediaContentUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCentroidId() {
        return centroidId;
    }

    public void setCentroidId(int centroidId) {
        this.centroidId = centroidId;
    }

    public Centroid getCentroid() {
        return centroid;
    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
