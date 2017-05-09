package io.github.soojison.yfindr.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.yfindr.MainActivity;
import io.github.soojison.yfindr.R;
import io.github.soojison.yfindr.data.Pin;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.ViewHolder> {

    private Context context;
    private List<Pin> pinList;
    private List<String> pinKeys;
    private String uID;
    private int lastPosition = -1;

    // get the pins branch
    private DatabaseReference databaseReference;

    public PinAdapter(Context context, String uID) {
        this.context = context;
        this.uID = uID;
        this.pinList = new ArrayList<>();
        this.pinKeys = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(MainActivity.KEY_PIN);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View pinView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewholder_item,
                parent,
                false
        );
        return new ViewHolder(pinView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pin newPin = pinList.get(position);
        holder.tvNetworkName.setText(newPin.getNetworkName());
        holder.tvAddress.setText(newPin.getAddress());
        holder.tvReview.setText(newPin.getRating() + "/5.0");
        // TODO: Delete your own pins
    }

    @Override
    public int getItemCount() {
        return pinList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNetworkName;
        public TextView tvReview;
        public TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNetworkName = (TextView) itemView.findViewById(R.id.tvNetworkName);
            tvReview = (TextView) itemView.findViewById(R.id.tvReview);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
        }
    }

    public void addPin(Pin pin, String key) {
        pinList.add(pin);
        pinKeys.add(key);
        notifyDataSetChanged();
    }

    public void removePin(int index) {
        databaseReference.child(pinKeys.get(index)).removeValue();
        pinList.remove(index);
        pinKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removePinByKey(String key) {
        int index = pinKeys.indexOf(key);
        if (index != -1) {
            removePin(index);
        }
    }
}
