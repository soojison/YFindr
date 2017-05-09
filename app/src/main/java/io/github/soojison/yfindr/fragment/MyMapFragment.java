package io.github.soojison.yfindr.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.soojison.yfindr.MainActivity;
import io.github.soojison.yfindr.R;
import io.github.soojison.yfindr.data.Pin;

public class MyMapFragment extends Fragment {
    public static final String TAG = "MyMapFragment";
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);

    GoogleMap map;

    private View cachedView;
    private List<MarkerOptions> markerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(cachedView == null) {
            cachedView = inflater.inflate(R.layout.fragment_map, container, false);
        }

        FragmentManager myFM = getChildFragmentManager();

        final SupportMapFragment myMAPF = (SupportMapFragment) myFM
                .findFragmentById(R.id.map);

        initPinListener();

        myMAPF.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                map = googleMap;

                for (int i = 0; i < markerList.size(); i++) {
                    googleMap.addMarker(markerList.get(i));
                }
                googleMap.addMarker(new MarkerOptions().title("TP_LINK")
                .position(new LatLng(47.499439,19.067459)).snippet("See Details"));

                googleMap.addMarker(new MarkerOptions().title("AIT-STUDENTS")
                        .position(new LatLng(47.561223,19.054964)).snippet("See Details"));

                googleMap.setTrafficEnabled(false);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // TODO: googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.561223,19.054964), 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10),2000, null);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // TODO yeah
                        if(marker.isInfoWindowShown()) {
                            marker.hideInfoWindow();
                        } else {
                            marker.showInfoWindow();
                        }
                        return true;
                    }
                });

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Toast.makeText(getContext(), "TODO: show details about the marker", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        return cachedView;
    }

    private void initPinListener() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(MainActivity.KEY_PIN);
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Pin newPin = dataSnapshot.getValue(Pin.class);
                LatLng latLng = getLocationFromAddress(newPin.getAddress());
                if(latLng != null) {
                    MarkerOptions marker = new MarkerOptions().position(latLng)
                            .title(newPin.getNetworkName())
                            .snippet("See details"); // TODO: String extraction
                    markerList.add(marker);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress)
    {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng latLng = null;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress,5);
            if (address == null || address.size() == 0) {
                // handle task asynchronously
                return null;
            }

            // take first possibility from the all possibilities.
            Address location=address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
