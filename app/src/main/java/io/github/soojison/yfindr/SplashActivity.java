package io.github.soojison.yfindr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_LENGTH = 3000;
    public static final String KEY_FIRST_TIME = "KEY_FIRST_TIME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Boolean isFirstTime = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(KEY_FIRST_TIME, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstTime) {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    if (auth.getCurrentUser() != null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, SPLASH_LENGTH);
    }

    @Override
    protected void onResume() {
        saveUsed();
        super.onResume();
    }

    private void saveUsed() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_FIRST_TIME, false);
        editor.apply();
    }
}
