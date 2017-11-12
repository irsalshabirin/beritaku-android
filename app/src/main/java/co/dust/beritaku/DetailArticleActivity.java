package co.dust.beritaku;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;

import co.dust.beritaku.databinding.ActivityDetailArticleBinding;
import co.dust.beritaku.model.Article;
import co.dust.beritaku.tools.ApiService;
import co.dust.beritaku.tools.CommonConstants;
import co.dust.beritaku.tools.ConstantHelper;
import co.dust.beritaku.tools.ServiceFactory;
import co.dust.beritaku.tools.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by irsal on 8/29/16.
 */
public class DetailArticleActivity extends AppCompatActivity {

    private static final String TAG = DetailArticleActivity.class.getSimpleName();

    private int articleId = 0;

    //    private MediumTextView mediumTextView;
//    private TextView textViewTitle;
    //    private TextView textViewContent;

    //    private HtmlTextView textViewContent;
    private ActivityDetailArticleBinding binding;
    private Article article;

//    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail_article);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_article);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            articleId = bundle.getInt(CommonConstants.ID, 0);
        } else {
            ToastUtil.showToast(this, getString(R.string.failed_to_fetch_data));
            finish();
        }


        // setting back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mediumTextView = (MediumTextView) findViewById(R.id.mediumTextView);
//        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
//        binding.textViewContent = (HtmlTextView) findViewById(R.id.textViewContent);
//        webView = (WebView) findViewById(R.id.webView);

        ConstantHelper.showProgressDialog(this, null, getString(R.string.com_facebook_loading), false);

        ApiService apiService = ServiceFactory.createRetrofitService(ApiService.class);

        apiService.getDetailArticle(articleId).enqueue(new Callback<ApiService.ArticleResponse>() {
            @Override
            public void onResponse(Call<ApiService.ArticleResponse> call, Response<ApiService.ArticleResponse> response) {
                ConstantHelper.hideProgressDialog();

                if (response.isSuccessful()) {
                    if (!response.body().error) {
//                        mediumTextView.setText(response.body().article.getContent());
//                        textViewTitle.setText(response.body().article.getTitle());

                        article = response.body().article;

                        getSupportActionBar().setTitle(response.body().article.getTitle());
//                        textViewContent.setText(Html.fromHtml((response.body().article.getContent())));
                        binding.textViewContent.setHtml(response.body().article.getContent(), new HtmlHttpImageGetter(binding.textViewContent));

//                        webView.loadData(response.body().article.getContent(), "text/html; charset=utf-8", "UTF-8");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiService.ArticleResponse> call, Throwable t) {
                ConstantHelper.hideProgressDialog();

            }
        });

        binding.tvViewOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailArticleActivity.this, WebViewActivity.class);
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "link : " + article.getLink());
                }
                intent.putExtra(WebViewActivity.URL_TAG, article.getLink());
                startActivity(intent);


                //
//                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
//                builder.setToolbarColor(color);
//                // you can custom menuItem
//                // you can custom menuIcon Also
//                CustomTabsIntent customTabsIntent = builder.build();
//                customTabsIntent.launchUrl(DetailArticleActivity.this, Uri.parse(article.getLink()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
