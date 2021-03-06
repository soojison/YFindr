package io.github.soojison.yfindr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.soojison.yfindr.data.MyLatLng;
import io.github.soojison.yfindr.data.Pin;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class AddActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etNetworkName)
    EditText etNetworkName;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.btnCustomToggle)
    CardView btnCustomToggle;

    @BindView(R.id.imgLock)
    ImageView imgLock;

    @BindView(R.id.tvReqKey)
    TextView tvReqKey;

    private boolean selected = false;

    private Place place;
    private SmallBang mSmallBang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        smallBangAnimation();
        initializeToolbar();

        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddress.setError(null);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_add);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void smallBangAnimation() {
        mSmallBang = SmallBang.attach2Window(this);
        btnCustomToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color;
                if(selected) {
                    selected = false;
                    color = android.R.color.transparent;
                } else {
                    selected = true;
                    color = R.color.colorCadetBlue;
                }
                mSmallBang.bang(btnCustomToggle,50,new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {
                        btnCustomToggle.setRadius(8);
                        btnCustomToggle.setCardBackgroundColor(getResources().getColor(color));
                        if(selected) {
                            tvReqKey.setTextColor(getResources().getColor(android.R.color.white));
                            imgLock.setColorFilter(getResources().getColor(android.R.color.white));
                        } else {
                            tvReqKey.setTextColor(getResources().getColor(android.R.color.black));
                            imgLock.setColorFilter(getResources().getColor(android.R.color.black));
                        }
                    }

                    @Override
                    public void onAnimationEnd() {
                    }
                });
            }
        });
    }

    private void addPin() {
        String key = FirebaseDatabase.getInstance().getReference().
                child(MainActivity.KEY_PIN).push().getKey();
        Pin newPin = new Pin(
                etNetworkName.getText().toString(),
                etAddress.getText().toString(),
                key,
                new MyLatLng(place.getLatLng().latitude, place.getLatLng().longitude),
                selected
        );

        newPin.addRating(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                ratingBar.getRating());

        FirebaseDatabase.getInstance().getReference().
                child(MainActivity.KEY_PIN).child(key).setValue(newPin);

        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    private boolean isValidPin() {
        if(TextUtils.isEmpty(etNetworkName.getText().toString())) {
            etNetworkName.requestFocus();
            etNetworkName.setError(getString(R.string.network_name_invalid));
            return false;
        } else if(TextUtils.isEmpty(etAddress.getText().toString())) {
            etAddress.setFocusableInTouchMode(true);
            etAddress.requestFocus();
            etAddress.setError(getString(R.string.network_address_invalid));
            return false;
        } else {
            return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            if(isValidPin()) {
                addPin();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                etAddress.setText(place.getAddress());
            }
        }
    }
}
