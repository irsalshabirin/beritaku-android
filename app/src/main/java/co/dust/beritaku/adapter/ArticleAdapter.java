package co.dust.beritaku.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by irsal on 7/26/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    public ArticleAdapter() {

    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ArticleHolder extends RecyclerView.ViewHolder {

        public ArticleHolder(View itemView) {
            super(itemView);
        }
    }
}
