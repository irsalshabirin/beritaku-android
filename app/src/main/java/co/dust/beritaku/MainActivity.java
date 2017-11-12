package co.dust.beritaku;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;

import co.dust.beritaku.adapter.CentroidAdapter;
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

public class MainActivity extends AppCompatActivity implements
        RecyclerViewExpandableItemManager.OnGroupExpandListener,
        SwipeRefreshLayout.OnRefreshListener,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        MugenCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

    private CentroidAdapter centroidAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    private ProgressBar mProgressBar;

    private int currentPage = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstantHelper.initDisplay(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;

        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);


//        final PickUpTransactionAd pickUpTransactionAdapter = new ExpandableExampleAdapter(getDataProvider());
        centroidAdapter = new CentroidAdapter(mRecyclerViewExpandableItemManager, this);

//        setDummyData();
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(centroidAdapter);       // wrap for expanding

        GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        recyclerView.setItemAnimator(animator);
        recyclerView.setHasFixedSize(false);

        if (BuildConfig.DEBUG) {
            showAlertDialogUrl();
        } else {
            loadData(1, CommonConstants.PER_PAGE);
        }

        mRecyclerViewExpandableItemManager.attachRecyclerView(recyclerView);

        Mugen.with(recyclerView, this).start();

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onCreate");
        }

    }

    private void showAlertDialogUrl() {
        new MaterialDialog.Builder(this)
                .title(R.string.url)
                .content(R.string.url)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .cancelable(false)
                .inputRangeRes(4, 50, R.color.colorAccent)
                .input("Base Url", CommonConstants.url(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        ToastUtil.showToast(getApplicationContext(), "input : " + input.toString());

                        if (input.toString().startsWith("http")) {
                            CommonConstants.setUrl(input.toString());
                            loadData(1, CommonConstants.PER_PAGE);
                        } else {
                            CommonConstants.setUrl("http://" + input.toString());
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ToastUtil.showToast(getApplicationContext(), "onNegative");
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ToastUtil.showToast(getApplicationContext(), "onPositive");
                    }
                })
                .show();
    }

    private void loadData(final int page, final int perPage) {

        ApiService apiService = ServiceFactory.createRetrofitService(ApiService.class);

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "load Data");
            Log.e(TAG, "page : " + page);
        }

//        mSwipeRefreshLayout.setRefreshing(true);
        onLoadingStarted();

//        ConstantHelper.showProgressDialog(this, null, getString(R.string.com_facebook_loading), false);

        apiService.getAllCentroid(page, perPage, 1).enqueue(new Callback<ApiService.CentroidResponse>() {
            @Override
            public void onResponse(Call<ApiService.CentroidResponse> call, Response<ApiService.CentroidResponse> response) {
//                ConstantHelper.hideProgressDialog();
                onLoadingFinished();
                mSwipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {

                    if (centroidAdapter != null) {
                        centroidAdapter.onNext(response.body().centroids, page);
                    }

                }
                currentPage = page;

                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "currentPage : " + currentPage);
                }

            }

            @Override
            public void onFailure(Call<ApiService.CentroidResponse> call, Throwable t) {
                onLoadingFinished();
//                ConstantHelper.hideProgressDialog();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private ArrayList<Article> setPostDummy() {

        ArrayList<Article> result = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Article article = new Article();

            article.setId(i + 1);
            article.setTitle("Title ke-" + (i + 1));
            article.setCreatedAt("16 January 2014, 17:55");

            result.add(article);
        }
        return result;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onGroup Expand");
        }
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onGroup Collapse");
        }
    }

    @Override
    public void onRefresh() {

        loadData(1, CommonConstants.PER_PAGE);

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onRefresh");
        }

    }

    public void onLoadingStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void onLoadingFinished() {
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onLoadMore() {
        loadData(currentPage + 1, CommonConstants.PER_PAGE);
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return false;
    }
}
