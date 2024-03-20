package com.example.kioskapp.ui.transform;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransformViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mTexts;
    public TransformViewModel() {


        mTexts = new MutableLiveData<>();
        List<String> texts = new ArrayList<>();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/menu/categories");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String img  = dsp.child("image").getValue().toString();
                    String cat   = dsp.child("cat").getValue().toString();
                    Log.d(TAG, "Category: " + cat);
                    Log.d(TAG, "Image: " + img);

                    texts.add(cat);

                }
                mTexts.setValue(texts);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to get categories.", error.toException());
            }
        });


    }

    public LiveData<List<String>> getTexts() {
        return mTexts;
    }
}