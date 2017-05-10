package io.github.soojison.yfindr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.soojison.yfindr.data.MyLatLng;
import io.github.soojison.yfindr.data.Pin;

public class AddActivity extends AppCompatActivity {

    public static final int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etNetworkName)
    EditText etNetworkName;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

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

        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addPin() {
        String key = FirebaseDatabase.getInstance().getReference().
                child(MainActivity.KEY_PIN).push().getKey();
        Pin newPin = new Pin(
                etNetworkName.getText().toString(),
                etAddress.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                new MyLatLng(place.getLatLng().latitude, place.getLatLng().longitude),
                ratingBar.getRating()
        );

        FirebaseDatabase.getInstance().getReference().
                child(MainActivity.KEY_PIN).child(key).setValue(newPin);

        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    public boolean isValidPin() {
        if(TextUtils.isEmpty(etNetworkName.getText().toString())) {
            etNetworkName.setError("Give the network a name");
            return false;
        } else if(TextUtils.isEmpty(etAddress.getText().toString())) {
            etAddress.setError("Give the address please");
            return false;
            //TODO: Extract strings
            //TODO: give a google maps view so that you can actually pick a place
        } else {
            return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
