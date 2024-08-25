package com.example.kioskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;
    MenuFragment menuFragment = new MenuFragment();
    PromoFragment promoFragment = new PromoFragment();
    ChatFragment chatFragment = new ChatFragment();
    CartFragment cartFragment = new CartFragment();

    int invoice_id;

    public interface IdValueCallback {
        void onIdValueReceived(int id);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdFromFirebase(new IdValueCallback() {
            @Override
            public void onIdValueReceived(int id) {
                // Use the id value here
                Log.d("inv no", String.valueOf(id));
                invoice_id=id;
                // You can call other methods or do whatever you need with 'id'
            }
        });


        bottomNavView = findViewById(R.id.bottom_nav);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,menuFragment).commit();

        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.menu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,menuFragment).commit();
                    return true;
                }else if(item.getItemId()==R.id.promo){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,promoFragment).commit();
                    return true;
                }else if(item.getItemId()==R.id.chat){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,chatFragment).commit();

                    return true;
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,cartFragment).commit();
                    return true;
                }


            }
        });

        bottomNavView.setSelectedItemId(R.id.menu);

    }

    public int getMyInvoiceID() {
        return invoice_id;
    }



    private void getIdFromFirebase(final IdValueCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/invoice");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int oid = 0;

                oid = Integer.parseInt(dataSnapshot.getValue().toString());

//                oid=124;

                int id = oid + 1;
                // Call the callback method with the id value
                callback.onIdValueReceived(id);
                myRef.setValue(id);

//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }
}