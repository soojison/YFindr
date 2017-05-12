package io.github.soojison.yfindr;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.github.soojison.yfindr.adapter.PinAdapter;
import io.github.soojison.yfindr.data.MyLatLng;
import io.github.soojison.yfindr.data.Pin;
import io.github.soojison.yfindr.fragment.MapFragment;
import io.github.soojison.yfindr.fragment.RecyclerFragment;

// TODO: get recycler by nearby pins
// TODO: get the nearest pin for emergency navigation --> Firebase data query in MainActivity instead of fragments


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnLocationUpdatedListener{

    public static final String KEY_PIN = "pins";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 202;

    private FragNavController fragNavController;
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;

    public DatabaseReference dbRef;
    private MenuItem searchButton;

    private HashMap<String, Pin> pinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initializeToolbar();
        initializeNavDrawer(toolbar);
        initBottomBar();
        initializeFragmentSwitcher();
        dbRef = FirebaseDatabase.getInstance().getReference(KEY_PIN);
        pinList = new HashMap<>();
        initPinListener();
    }


    private void initPinListener() {
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Pin newPin = dataSnapshot.getValue(Pin.class);
                pinList.put(dataSnapshot.getKey(), newPin);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Pin updatedPin = dataSnapshot.getValue(Pin.class);
                pinList.remove(dataSnapshot.getKey());
                pinList.put(dataSnapshot.getKey(), updatedPin);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                pinList.remove(dataSnapshot.getKey());
                //pinAdapter.removePinByKey(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Toolbar initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;
    }

    private void initBottomBar() {
        BottomNavigationView bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomBar.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);
    }

    private void initializeFragmentSwitcher() {
        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(new MapFragment());
        fragments.add(new RecyclerFragment());
        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.content, fragments);
        fragNavController.switchTab(TAB_FIRST);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_map:
                    fragNavController.switchTab(TAB_FIRST);
                    if (searchButton != null) {
                        searchButton.setVisible(true);
                    }
                    return true;
                case R.id.tab_near_me:
                    fragNavController.switchTab(TAB_SECOND);
                    if (searchButton != null) {
                        searchButton.setVisible(false);
                    }
                    return true;
                case R.id.tab_emergency:
                    // TODO:
                    Toast.makeText(MainActivity.this, "THIS IS AN EMERGENCY", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.tab_map) {
                fragNavController.switchTab(TAB_FIRST);
                if (searchButton != null) {
                    searchButton.setVisible(true);
                }
            }
            if (item.getItemId() == R.id.tab_near_me) {
                fragNavController.switchTab(TAB_SECOND);
                if (searchButton != null) {
                    searchButton.setVisible(false);
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchButton = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(this, "SEARCH", Toast.LENGTH_SHORT).show();
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                Log.i("TAG_SEARCH", e.getLocalizedMessage());
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.i("TAG_SEARCH", e.getLocalizedMessage());
            }
            return true;
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, AddActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {
            // TODO:
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                if (fragment instanceof MapFragment) {
                    ((MapFragment) fragment).moveCamera(place.getLatLng());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("TAG_PLACE", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FirebaseAuth.getInstance().signOut();
            super.onBackPressed();
        }
    }

    @NonNull
    private NavigationView initializeNavDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        TextView tvUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        return navigationView;
    }

    public HashMap<String, Pin> nearbyPins;
    public boolean isNearBy(MyLatLng currentLoc, MyLatLng pinLoc) {
        // 2km = 2000m radius
        return currentLoc.getDistance(pinLoc) <= 2000;
    }

    public void findNearbyPins(Location location) {
        nearbyPins = new HashMap<>();
        for (Map.Entry<String, Pin> current : pinList.entrySet()) {
            if (isNearBy(new MyLatLng(location.getLatitude(), location.getLongitude()),
                    current.getValue().getLatLng())) {
                nearbyPins.put(current.getKey(), current.getValue());
            }
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        findNearbyPins(location);
    }
}


