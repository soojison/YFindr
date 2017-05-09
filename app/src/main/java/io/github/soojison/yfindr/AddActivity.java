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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.soojison.yfindr.data.Pin;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.etNetworkName)
    EditText etNetworkName;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

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
    }

    public void addPin() {
        String key = FirebaseDatabase.getInstance().getReference().
                child(MainActivity.KEY_PIN).push().getKey();
        Pin newPin = new Pin(
                etNetworkName.getText().toString(),
                etAddress.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
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
}
