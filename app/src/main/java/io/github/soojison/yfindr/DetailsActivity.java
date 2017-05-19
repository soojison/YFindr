package io.github.soojison.yfindr;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.soojison.yfindr.data.Pin;

public class DetailsActivity extends AppCompatActivity {

    public static final String PIN_DETAIL_TAG = "PIN_DETAIL_TAG";
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.tvReqKey)
    TextView tvReqKey;

    @BindView(R.id.btnNavigate)
    Button btnNavigate;

    @BindView(R.id.btnReport)
    Button btnReport;

    private LatLng position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(PIN_DETAIL_TAG)) {
            final Pin myPin = Parcels.unwrap(getIntent().getParcelableExtra(PIN_DETAIL_TAG));
            populateActivity(myPin);
        }
        initializeToolbar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions().position(position));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
            }
        });



    }

    private void populateActivity(final Pin myPin) {
        name.setText(myPin.getNetworkName());
        address.setText(myPin.getAddress());
        String lockedStatus = myPin.isLocked() ? getString(R.string.detail_requires_key) : getString(R.string.detail_unlocked_access);
        tvReqKey.setText(lockedStatus);
        position = new LatLng(myPin.getLatLng().getLatitude(), myPin.getLatLng().getLongitude());
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(
                        R.string.google_maps_query,
                        position.latitude,
                        position.longitude
                )));
                startActivity(navigation);
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportDialog(myPin);
            }
        });
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.details_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showReportDialog(final Pin myPin) {
        new AlertDialog.Builder(this).setTitle(R.string.dialog_report_title)
                .setMessage(R.string.dialog_report_message)
                .setPositiveButton(R.string.dialog_logout_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myPin.incrementNumReport();
                        if(myPin.getNumReports() > 100) { // more than hundred people complained!!
                            FirebaseDatabase.getInstance().getReference().child(MainActivity.KEY_PIN).child(myPin.getUid()).removeValue();
                            finish();
                        } else {
                            FirebaseDatabase.getInstance().getReference().
                                    child(MainActivity.KEY_PIN).child(myPin.getUid()).setValue(myPin);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_logout_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.ic_error)
                .show();
    }
}
