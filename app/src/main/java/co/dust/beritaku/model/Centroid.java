package co.dust.beritaku.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by irsal on 1/29/17.
 */

public class Centroid extends BaseItem {


    @SerializedName("count_member")
    private int countMember;

    @SerializedName("description")
    private String description;

    @SerializedName("words")
    private String words;

    @SerializedName("articles")
    private ArrayList<Article> articles;

    public int getCountMember() {
        return countMember;
    }

    public void setCountMember(int countMember) {
        this.countMember = countMember;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
