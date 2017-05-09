package io.github.soojison.yfindr;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.yfindr.fragment.MyMapFragment;
import io.github.soojison.yfindr.fragment.RecyclerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_PIN = "pins";


    private FragNavController fragNavController;

    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initializeToolbar();
        NavigationView navigationView = initializeNavDrawer(toolbar);

        // set email address of user on the header (same way you can change the name and profile pic)
        TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        // Firebase is a singleton model
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        TextView tvUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        List<Fragment> fragments = new ArrayList<>(2);

        //add fragments to list
        fragments.add(new MyMapFragment());
        fragments.add(new RecyclerFragment());

        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.content,fragments);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);

        fragNavController.switchTab(TAB_FIRST);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_map:
                    fragNavController.switchTab(TAB_FIRST);
                    return true;
                case R.id.tab_near_me:
                    fragNavController.switchTab(TAB_SECOND);
                    return true;
                case R.id.tab_emergency:
                    Toast.makeText(MainActivity.this, "THIS IS AN EMERGENCY", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    private BottomNavigationView.OnNavigationItemReselectedListener  onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.tab_map) {
                fragNavController.switchTab(TAB_FIRST);
            }
            if (item.getItemId() == R.id.tab_near_me) {
                fragNavController.switchTab(TAB_SECOND);
            }
        }
    };

    @NonNull
    private NavigationView initializeNavDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        return navigationView;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, AddActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_settings) {

        } else if(id == R.id.nav_about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
