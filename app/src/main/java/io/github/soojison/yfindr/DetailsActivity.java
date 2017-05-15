package io.github.soojison.yfindr;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            final Pin myPin = getIntent().getParcelableExtra(PIN_DETAIL_TAG);
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
                    // TODO: Collect user reports?
                    Toast.makeText(DetailsActivity.this, "TODO: How to report faulty pins?", Toast.LENGTH_SHORT).show();
                }
            });
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
}
