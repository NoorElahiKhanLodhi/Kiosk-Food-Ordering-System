package com.example.kioskapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromoFragment extends Fragment {

    private View view;
    private ImageView slideshowImage;
    private TextView itemName, originalPrice, discountedPrice, description;
    private Button prevButton, nextButton;
    private List<Map<String, Object>> itemList = new ArrayList<>();
    private int currentIndex = 0;
    private Handler handler = new Handler();
    private Runnable runnable;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_promo, container, false);



        slideshowImage = view.findViewById(R.id.slideshow_image);

        RelativeLayout.LayoutParams param =
                new RelativeLayout.LayoutParams(1450,1450);
        slideshowImage.setLayoutParams(param);


        itemName = view.findViewById(R.id.item_name);
        originalPrice = view.findViewById(R.id.original_price);
        discountedPrice = view.findViewById(R.id.discounted_price);
        description = view.findViewById(R.id.description);

        prevButton = view.findViewById(R.id.prev_button);
        nextButton = view.findViewById(R.id.next_button);

        databaseReference = FirebaseDatabase.getInstance().getReference("menu/categories");

        prevButton.setOnClickListener(v -> showPreviousItem());
        nextButton.setOnClickListener(v -> showNextItem());

        fetchDataFromFirebase();

        return view;
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : categorySnapshot.child("items").getChildren()) {
                        Map<String, Object> item = (Map<String, Object>) itemSnapshot.getValue();
                        if (item != null && item.get("discount") != null && !item.get("discount").toString().isEmpty()) {
                            itemList.add(item);
                        }
                    }
                }
                if (!itemList.isEmpty()) {
                    startSlideshow();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void startSlideshow() {
        runnable = new Runnable() {
            @Override
            public void run() {
                showNextItem();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void showNextItem() {
        if (!itemList.isEmpty()) {
            currentIndex = (currentIndex + 1) % itemList.size();
            updateUI();
        }
    }

    private void showPreviousItem() {
        if (!itemList.isEmpty()) {
            currentIndex = (currentIndex - 1 + itemList.size()) % itemList.size();
            updateUI();
        }
    }

    private void updateUI() {
        Map<String, Object> item = itemList.get(currentIndex);
        Picasso.with(view.getContext()).load(item.get("image").toString()).into(slideshowImage);

        itemName.setText(item.get("name").toString());
        originalPrice.setText("Rs." + item.get("price").toString());
        discountedPrice.setText("Rs." + item.get("discount").toString());
        description.setText(item.get("description").toString());

        originalPrice.setPaintFlags(originalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }
}
