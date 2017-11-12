package co.dust.beritaku.tools;

import java.util.concurrent.TimeUnit;

import co.dust.beritaku.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by irsal on 9/21/16.
 */

public class ServiceFactory {

    public static <T> T createRetrofitService(final Class<T> clazz) {

        String BASE_URL = CommonConstants.url() + "api/";

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                ;


        if (BuildConfig.DEBUG) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build();

//            UploadService.HTTP_STACK = new OkHttpStack(client);
            builder.client(client);

        } else {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build();

//            UploadService.HTTP_STACK = new OkHttpStack(client);
            builder.client(client);
        }

        // using https
//        else {
//            try {
//                SSLContext sslContext = SSLContext.getInstance("SSL");
//
//                OkHttpClient client = new OkHttpClient.Builder()
////                    .addInterceptor(interceptor)
//                        .sslSocketFactory(sslContext.getSocketFactory())
////                        .sslSocketFactory()
//                        .connectTimeout(30, TimeUnit.SECONDS)
//                        .readTimeout(30, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true)
//                        .build();
//
//                builder.client(client);
//
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//
//        }

        final Retrofit restAdapter = builder.build();

//        if (BuildConfig.DEBUG) {
//            Log.e("isi service : ", "" + service);
//        }

        return restAdapter.create(clazz);
    }
}
