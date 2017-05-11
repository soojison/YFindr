package io.github.soojison.yfindr;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.soojison.yfindr.data.Pin;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.tvReqKey)
    TextView tvReqKey;

    @BindView(R.id.btnNavigate)
    Button btnNavigate;

    private LatLng position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        if(getIntent().hasExtra("PIN_DETAIL")) {
            Pin myPin = getIntent().getParcelableExtra("PIN_DETAIL");
            name.setText(myPin.getNetworkName());
            address.setText(myPin.getAddress());
            String lockedStatus = myPin.isLocked() ? "Requires key" : "Unlocked access";
            tvReqKey.setText(lockedStatus);
            position = new LatLng(myPin.getLatLng().getLatitude(), myPin.getLatLng().getLongitude());
            btnNavigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("http://maps.google.com/maps?daddr="
                                    + position.latitude + ","
                                    + position.longitude));
                    startActivity(navigation);
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
