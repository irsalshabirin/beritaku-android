package co.dust.beritaku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import co.dust.beritaku.databinding.ActivityWebviewBinding;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by irsal on 6/30/17.
 */

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    public static final String URL_TAG = "url";

    private String url;

    private boolean isError = false;
    private boolean isRefreshPage = false;

    private ActivityWebviewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);

        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.webView.setScrollbarFadingEnabled(false);

        Bundle bundle = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (bundle != null) {

            url = bundle.getString(URL_TAG, "");

//            if (url.contains("http://")) {
//                url = url.replace("http://", "https://");
//            }

            if (BuildConfig.DEBUG) {
//                url = "http://dustudio.co";
//                url = "http://detik.com";
                Log.e(TAG, "url : " + url);
            }

            binding.webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100 && binding.progressBar.getVisibility() == ProgressBar.GONE) {
                        binding.progressBar.setVisibility(ProgressBar.VISIBLE);
                    }

                    binding.progressBar.setProgress(progress);
                    if (progress == 100) {
                        binding.progressBar.setVisibility(ProgressBar.GONE);
                    }
                }
            });

            binding.webView.getSettings().setJavaScriptEnabled(true);

            binding.webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
//                    return true;
//                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (!isNetworkAvailable()) {
//                    showInfoMessageDialog("network not available");
//                    System.out.println("network not available");
//                    Toast.makeText(MainActivity.this, "network not available", Toast.LENGTH_SHORT).show();
                        binding.errorView.setVisibility(View.VISIBLE);
                        binding.textError.setText(R.string.no_internet_tap_to_retry);
                        binding.webView.setVisibility(View.GONE);
                        isError = true;
                    } else {
                        isError = false;
//                    System.out.println("network available");
                    }

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!isError) {
                        binding.errorView.setVisibility(View.GONE);
                        binding.webView.setVisibility(View.VISIBLE);
                    } else {
                        isRefreshPage = false;
                    }

                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "isError : " + isError);
                        Log.e(TAG, "title : " + view.getTitle());
                    }

                    WebViewActivity.this.setTitle(view.getTitle());
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    if (BuildConfig.DEBUG) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.e(TAG, "" + error.getDescription().toString());
                        }
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "error : " + description);
                    }
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    if (BuildConfig.DEBUG) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Log.e(TAG, "req : " + errorResponse.getResponseHeaders().toString());
                        }
                    }
                }
            });

            binding.webView.loadUrl(url);

            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Terakhir");
            }

        } else {
            finish();
        }


        binding.ivRefreshPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRefreshPage) {
                    binding.webView.reload();
                    isRefreshPage = true;
                    binding.textError.setText(R.string.loading);
                }
            }
        });
    }


    private boolean isNetworkAvailable() {
        NetworkInfo info = ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return !(info == null || !info.isAvailable() || !info.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
