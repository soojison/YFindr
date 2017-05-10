package io.github.soojison.yfindr.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.soojison.yfindr.MainActivity;
import io.github.soojison.yfindr.R;
import io.github.soojison.yfindr.adapter.PinAdapter;
import io.github.soojison.yfindr.data.Pin;

public class RecyclerFragment extends Fragment {

    public static final String TAG = "RecyclerFragment";

    public static final String KEY_PIN = "pins";
    private PinAdapter pinAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        pinAdapter = new PinAdapter(getContext(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewPins);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(pinAdapter);
        
        initPinListener();
        
        return rootView;
    }

    private void initPinListener() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(MainActivity.KEY_PIN);
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Pin newPin = dataSnapshot.getValue(Pin.class);
                pinAdapter.addPin(newPin, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                pinAdapter.removePinByKey(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
