package io.github.soojison.yfindr;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
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
                        .title(getString(R.string.intro_crowd))
                        .description(getString(R.string.intro_crowd_desc))
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.earth)
                .title(getString(R.string.intro_earth))
                .description(getString(R.string.intro_earth_desc))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.hand)
                .title(getString(R.string.intro_hand))
                .description(getString(R.string.intro_hand_desc))
                .possiblePermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                .neededPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                .build(), new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
            }
        }, getString(R.string.intro_permissions_button)));
    }

    @Override
    public void onFinish() {
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        super.onFinish();
    }
}
