package co.dust.beritaku.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irsal on 9/21/16.
 */

public class User {

    public static final int LOGIN_FROM_FACEBOOK = 1;
    public static final int LOGIN_FROM_GOOGLE = 2;

    @SerializedName("id")
    public
    String id;

    String facebookId;

    public String name;
    public String email;

    public String photoUrl;

    public String gender;
    public String age;
}
