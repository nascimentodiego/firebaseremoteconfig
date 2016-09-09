package br.com.dfn.remoteconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static String HOLIDAY_PARAM = "holiday_promotion_enable";
    private static String HOLIDAY_PARAM_URL = "holiday_promotion_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);
        onLoadPromotion();

    }

    private void onLoadPromotion() {
        final ImageView imageView = (ImageView) findViewById(R.id.my_image_view);

        // cache expiration in seconds
        long cacheExpiration = 0;

        // fetch
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            mFirebaseRemoteConfig.activateFetched();

                            String urlPromotion = mFirebaseRemoteConfig.getString(HOLIDAY_PARAM_URL);

                            int visible = mFirebaseRemoteConfig.getBoolean(HOLIDAY_PARAM) ? View.VISIBLE : View.GONE;
                            imageView.setVisibility(visible);

                            Glide.with(getApplicationContext()).load(urlPromotion).into(imageView);
                        }
                    }
                });
    }
}
