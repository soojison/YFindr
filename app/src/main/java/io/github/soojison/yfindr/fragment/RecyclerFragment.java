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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.soojison.yfindr.MainActivity;
import io.github.soojison.yfindr.R;
import io.github.soojison.yfindr.adapter.PinAdapter;
import io.github.soojison.yfindr.data.Pin;

public class RecyclerFragment extends Fragment {

    public static final String TAG = "RecyclerFragment";

    @BindView(R.id.layoutSadface)
    RelativeLayout layoutSadface;

    @BindView(R.id.layoutRecycler)
    LinearLayout layoutRecycler;

    private PinAdapter pinAdapter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, rootView);

        pinAdapter = new PinAdapter(getContext(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewPins);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(pinAdapter);
        Toast.makeText(getContext(), "Created a recyclerview ... for the first time?", Toast.LENGTH_SHORT).show();

        populateRecycler();
        return rootView;
    }

    private void populateRecycler() {
        if(((MainActivity) getContext()).getNearbyPins().isEmpty()) {
            // show condolences
            layoutSadface.setVisibility(View.VISIBLE);
            layoutRecycler.setVisibility(View.INVISIBLE);
        } else {
            //layoutSadface.setVisibility(View.INVISIBLE);
            layoutRecycler.setVisibility(View.VISIBLE);
            for (Map.Entry<String, Pin> current : ((MainActivity) getContext()).getNearbyPins().entrySet()) {
                pinAdapter.addPin(current.getValue(), current.getKey());
            }
        }
    }

}
