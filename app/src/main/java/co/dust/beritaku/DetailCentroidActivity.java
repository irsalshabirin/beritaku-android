package co.dust.beritaku;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import co.dust.beritaku.tools.ToastUtil;

/**
 * Created by irsal on 7/25/17.
 */

public class DetailCentroidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(0);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

        } else {
            ToastUtil.showToast(this, "Gagal mendapatkan centroid_id");
            finish();
            return;
        }
    }

}
