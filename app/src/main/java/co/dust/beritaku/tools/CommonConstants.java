package co.dust.beritaku.tools;

import co.dust.beritaku.BuildConfig;

/**
 * Created by irsal on 1/29/17.
 */

public class CommonConstants {

    public static final String ID = "id";
    public static int PER_PAGE = 15;

    private static String BASE_URL = "";


    public static String url() {

        String RELEASE_BASE_URL = "http://192.168.43.202/beritaku/public/";

        if (BuildConfig.DEBUG) {

            if (!CommonConstants.BASE_URL.equals("")) {
                if (CommonConstants.BASE_URL.contains("beritaku")) {
                    return CommonConstants.BASE_URL;
                } else {
                    return CommonConstants.BASE_URL + "/beritaku/public/";
                }
            } else {
                return RELEASE_BASE_URL;
            }

        } else {
            return RELEASE_BASE_URL;
        }
    }

    public static void setUrl(String url) {
        BASE_URL = url;
    }
}
