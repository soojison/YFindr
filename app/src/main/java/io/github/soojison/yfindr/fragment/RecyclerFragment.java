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

import java.util.Map;

import io.github.soojison.yfindr.MainActivity;
import io.github.soojison.yfindr.R;
import io.github.soojison.yfindr.adapter.PinAdapter;
import io.github.soojison.yfindr.data.Pin;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class RecyclerFragment extends Fragment {

    public static final String TAG = "RecyclerFragment";

    private PinAdapter pinAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        pinAdapter = new PinAdapter(getContext(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewPins);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(pinAdapter);
        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        populateRecycler();
        return rootView;
    }

    private void populateRecycler() {
        for (Map.Entry<String, Pin> current : ((MainActivity) getContext()).nearbyPins.entrySet()) {
            pinAdapter.addPin(current.getValue(), current.getKey());
        }
    }
}
