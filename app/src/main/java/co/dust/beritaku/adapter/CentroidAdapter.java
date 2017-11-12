package co.dust.beritaku.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import co.dust.beritaku.BuildConfig;
import co.dust.beritaku.DetailArticleActivity;
import co.dust.beritaku.R;
import co.dust.beritaku.model.Article;
import co.dust.beritaku.model.Centroid;
import co.dust.beritaku.tools.CommonConstants;
import co.dust.beritaku.tools.ConstantHelper;

/**
 * Created by irsal on 1/29/17.
 */

public class CentroidAdapter extends AbstractExpandableItemAdapter<CentroidAdapter.MyGroupViewHolder,
        AbstractExpandableItemViewHolder> {

    private static final String TAG = CentroidAdapter.class.getSimpleName();
    private static final int DEFAULT_CHILD = 1;
    private static final int VIEW_MORE = 2;
    private final RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    private LinkedHashMap<Integer, List<Centroid>> centroidMap;
    private List<Centroid> centroidList;

    private Activity activity;

    public CentroidAdapter(RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager, Activity activity) {
        this.activity = activity;
        this.mRecyclerViewExpandableItemManager = mRecyclerViewExpandableItemManager;

        centroidMap = new LinkedHashMap<>();
        centroidList = new ArrayList<>();

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    public static class MyGroupViewHolder extends AbstractExpandableItemViewHolder {

        public TextView txtWords;
//        public ExpandableItemIndicator mIndicator;

        public MyGroupViewHolder(View v) {
            super(v);
//            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
            txtWords = (TextView) v.findViewById(R.id.txtWords);
//            txtDateTime = (IconicsTextView) v.findViewById(R.id.txtDateTime);
        }
    }

    public static class MyChildViewHolder extends AbstractExpandableItemViewHolder {

//        public RelativeLayout relativeLayout;
//        public RelativeLayout relativeLayoutText;

        public CardView cardView;
        public ImageView imgViewItem;
        public TextView txtTitleItem;
        public TextView txtDate;

        public ImageView imgViewFeedIcon;
        public TextView txtFeedTitle;

        public MyChildViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.cardViewArticle);
//            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
//            relativeLayoutText = (RelativeLayout) v.findViewById(R.id.relativeLayoutText);

            imgViewItem = (ImageView) v.findViewById(R.id.imgViewItem);
            txtTitleItem = (TextView) v.findViewById(R.id.txtTitleItem);

            txtDate = (TextView) v.findViewById(R.id.txtDate);

            imgViewFeedIcon = (ImageView) v.findViewById(R.id.imgViewFeedIcon);
            txtFeedTitle = (TextView) v.findViewById(R.id.txtFeedTitle);


            // minimal 95 dp
            int height = (int) ((float) ConstantHelper.height / 2.5f);
            int width = (int) ((float) ConstantHelper.width / 3.5f);
            float widthDP = ConstantHelper.pixelsToDp(width, v.getContext());

            android.view.ViewGroup.LayoutParams layoutParams = imgViewItem.getLayoutParams();

            float dp = ConstantHelper.pixelsToDp(height, v.getContext());
            if (dp < 95.f) {
                dp = ConstantHelper.dpToPixel(95.f, v.getContext());
            } else if (dp > widthDP) {
                dp = ConstantHelper.dpToPixel(widthDP, v.getContext());
            } else {
                dp = ConstantHelper.dpToPixel(dp, v.getContext());
            }

            layoutParams.width = (int) dp;
            layoutParams.height = (int) dp;

            imgViewItem.setLayoutParams(layoutParams);
        }
    }

    private class MyChildViewHolder2 extends AbstractExpandableItemViewHolder {

        public CardView cardView;

        public MyChildViewHolder2(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.cvViewMore);
        }
    }

    public Centroid getItem(int position) {

        int listSize = 0;

        if (centroidList.size() > position) {
            return centroidList.get(position);
        }

        centroidList = new ArrayList<>();

        for (List<Centroid> list : centroidMap.values()) {
            centroidList.addAll(list);
            listSize = listSize + list.size();
            if (listSize > (position)) {
                break;
            }
        }

        if (centroidList.size() > 0) {
            return centroidList.get(position);
        }

        return null;
    }

    @Override
    public int getGroupCount() {
        int count = 0;

        for (List<Centroid> list : centroidMap.values()) {
            count = count + list.size();
        }

        return count;
    }

    @Override
    public int getChildCount(int groupPosition) {

        if (getItem(groupPosition).getArticles() == null) {
            return 0;
        } else if (getItem(groupPosition).getArticles().size() > 2) {
            return 2 + 1;
        } else {
            return getItem(groupPosition).getArticles().size() + 1;
        }

    }

    @Override
    public long getGroupId(int groupPosition) {
        return getItem(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (getItem(groupPosition).getArticles().size() == childPosition) {
            return 1000;
        } else {
            return getItem(groupPosition).getArticles().get(childPosition).getId();
        }
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_centroid, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public AbstractExpandableItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == DEFAULT_CHILD) {
            final View v = inflater.inflate(R.layout.item_article_small_image, parent, false);
            return new MyChildViewHolder(v);
        } else if (viewType == VIEW_MORE) {
            final View v = inflater.inflate(R.layout.item_view_more, parent, false);
            return new MyChildViewHolder2(v);
        }

        return null;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        //return super.getChildItemViewType(groupPosition, childPosition);
        //return childPosition;
        if (getItem(groupPosition).getArticles().size() == childPosition) {
            return VIEW_MORE;
        } else {
            return DEFAULT_CHILD;
        }
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, final int groupPosition, int viewType) {

        Centroid centroid = getItem(groupPosition);

        holder.txtWords.setText(String.valueOf(centroid.getWords()));

        final int expandState = holder.getExpandStateFlags();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                    mRecyclerViewExpandableItemManager.collapseGroup(groupPosition);
                } else {
                    mRecyclerViewExpandableItemManager.expandGroup(groupPosition);
                }

            }
        });
    }

    @Override
    public void onBindChildViewHolder(AbstractExpandableItemViewHolder holder, int groupPosition, final int childPosition, int viewType) {

        if (holder instanceof MyChildViewHolder) {
            MyChildViewHolder childViewHolder = (MyChildViewHolder) holder;
            final ArrayList<Article> articles = getItem(groupPosition).getArticles();

            // mark as clickable
            holder.itemView.setClickable(true);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, DetailArticleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConstants.ID, articles.get(childPosition).getId());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
//                Toast.makeText(activity, "lalalla", Toast.LENGTH_SHORT).show();

                }
            });

            if (articles.get(childPosition).getTitle() != null) {
                childViewHolder.txtTitleItem.setText(articles.get(childPosition).getTitle());
            }

            if (articles.get(childPosition).getPublishDate() != null) {
                try {
                    String string = ConstantHelper.getTimeAgo(articles.get(childPosition).getPublishDate());
                    childViewHolder.txtDate.setText(string);
                } catch (ParseException e) {
                    childViewHolder.txtDate.setText(articles.get(childPosition).getPublishDate());
                }

            }

//        if (BuildConfig.DEBUG) {
//            Log.e(TAG, "height : " + holder.relativeLayout.getHeight());
//            Log.e(TAG, "height : " + holder.relativeLayoutText.getHeight());
//        }

            if (articles.get(childPosition).getMediaContentUrl() != null) {
                Glide.clear(childViewHolder.imgViewItem);
                Glide.with(childViewHolder.imgViewItem.getContext())
                        .load(articles.get(childPosition).getMediaContentUrl())
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(childViewHolder.imgViewItem);

            } else {
                Glide.clear(childViewHolder.imgViewItem);
                childViewHolder.imgViewItem.setImageResource(R.mipmap.ic_launcher);
            }

            if (articles.get(childPosition).getFeed() != null) {
                if (articles.get(childPosition).getFeed().getTitle() != null) {
                    childViewHolder.txtFeedTitle.setText(articles.get(childPosition).getFeed().getTitle());
                }
                if (articles.get(childPosition).getFeed().getIconUrl() != null) {
                    Glide.clear(childViewHolder.imgViewFeedIcon);
                    Glide.with(childViewHolder.imgViewFeedIcon.getContext())
                            .load(articles.get(childPosition).getFeed().getIconUrl())
                            .dontAnimate()
                            .thumbnail(0.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(childViewHolder.imgViewFeedIcon);
                } else {
                    Glide.clear(childViewHolder.imgViewFeedIcon);
                    childViewHolder.imgViewFeedIcon.setImageDrawable(null);

                }
            }

        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "on check can expand");
        }

        return false;
    }

    public void onNext(List<Centroid> centroids, int page) {

        if (centroids == null) {
            return;
        }

        if (page == 1) {
            centroidMap = new LinkedHashMap<>();
            centroidList = new ArrayList<>();
        }

        centroidMap.put(page, centroids);
        notifyDataSetChanged();
    }
}
