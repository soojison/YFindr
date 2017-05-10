package io.github.soojison.yfindr;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimary)
                        .buttonsColor(R.color.colorAccent)
                        .image(R.drawable.crowd)
                        .title("Crowdsourced database")
                        .description("Made by the people, for the people")
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.earth)
                .title("From all over the world")
                .description("Made available by your help")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.hand)
                .title("At the tip of your fingers")
                .description("Would you join us?")
                .possiblePermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION})
                .neededPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION})
                .build(), new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
            }
        }, "Grant Permissions"));
        // TODO: Extract string
    }

    @Override
    public void onFinish() {
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        super.onFinish();
    }
}
