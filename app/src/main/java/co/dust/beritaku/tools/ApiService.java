package co.dust.beritaku.tools;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.dust.beritaku.model.Article;
import co.dust.beritaku.model.Centroid;
import co.dust.beritaku.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by irsal on 9/21/16.
 */

public interface ApiService {

//    @Headers("TOKEN-ID: 000")
//    @POST("{path}")
//    Observable<ModelSentResponse> acceptOrder(
//            @Path("path") String path,
//            @Body ModelSentRequest acceptOrderRequest
//    );

    class CentroidResponse extends Message {

        @SerializedName("centroids")
        public ArrayList<Centroid> centroids;

    }

    class ArticleResponse extends Message {

        @SerializedName("article")
        public Article article;

    }

    class Message {

        @SerializedName("response_code")
        int responseCode;

        @SerializedName("error")
        public
        boolean error;

        @SerializedName("message")
        String message;

    }

    class UserResponse extends Message {
        @SerializedName("user")
        User user;
    }

//    @FormUrlEncoded
//    @POST("user/register")
//    Observable<UserResponse> registerOrLoginUser(
//            @Field("id") String id,
//            @Field("name") String name,
//            @Field("photo_url") String url,
//            @Field("email") String email,
//            @Field("login_from") int loginFrom
//    );


//    void sendBehaviourUser();

    // post [START]
    void getDetailPost();

    @GET("centroid")
    Call<CentroidResponse> getAllCentroid(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("limit_article") int limitArticle
    );

    @GET("article/{id}")
    Call<ArticleResponse> getDetailArticle(
            @Path("id") int articleId
    );

    void getAllPostByCategory(int categoryID);

    void getAllPostByCity(int cityId);

    void getAllHistoryPostByUser(int userId);
    // post [END]


    // category [START]
    void getAllCategory();

    /***
     * mengirim kategori yang dipilih oleh user
     */
    void sendCategory();
    // category [END]


    // comment [START]
    void sendComment(int userId, String comment);


    // comment [END]

}
