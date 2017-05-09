package io.github.soojison.yfindr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    @BindView(R.id.btnDone)
    Button btnDone;

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

    @OnClick(R.id.btnDone)
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
}
