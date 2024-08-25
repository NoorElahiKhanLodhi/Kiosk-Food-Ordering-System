package com.example.kioskapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class order_pay extends Fragment {

    private DatabaseReference mDatabase;
    private String invoice;
    private TextView totalDisp;
    private double totalAmount = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize Firebase reference
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_pay, container, false);
        totalDisp = view.findViewById(R.id.total_disp); // Initialize total_disp TextView

        MainActivity act = (MainActivity) getActivity();
        int invoiceNo = Objects.requireNonNull(act).getMyInvoiceID();
        invoice = String.valueOf(invoiceNo);

        TextView bill = view.findViewById(R.id.id_disp);
        bill.append(invoice);

        // Calculate total amount
        calculateTotal(invoiceNo);



        Button cod = view.findViewById(R.id.cod);
        Button pos = view.findViewById(R.id.pos);
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"PROCESSING ORDER",Toast.LENGTH_SHORT).show();
                Log.d("TAGA", totalAmount +"\n"+invoice);

// Get the current date
                Date currentDate = new Date();

                // Format the date to "dd:mm:yyyy"
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
                String formattedDate = dateFormat.format(currentDate);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("orders/"+invoice);
                myRef.child("amt").setValue(String.valueOf(totalAmount));
                myRef.child("status").setValue("cod");
                myRef.child("date").setValue(formattedDate);

                RelativeLayout screen = view.findViewById(R.id.all);
                screen.removeAllViews();


                Toast.makeText(getContext(),"ORDER CONFIRMED",Toast.LENGTH_LONG).show();



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Code to execute after 2 seconds
                        restartApp();
                    }
                }, 3000);

            }

        });
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"PROCESSING ORDER",Toast.LENGTH_SHORT).show();
                Log.d("TAGA", totalAmount +"\n"+invoice);

// Get the current date
                Date currentDate = new Date();

                // Format the date to "dd:mm:yyyy"
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
                String formattedDate = dateFormat.format(currentDate);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("orders/"+invoice);
                myRef.child("amt").setValue(String.valueOf(totalAmount));
                myRef.child("status").setValue("paid");
                myRef.child("date").setValue(formattedDate);

                RelativeLayout screen = view.findViewById(R.id.all);
                screen.removeAllViews();


                Toast.makeText(getContext(),"ORDER CONFIRMED",Toast.LENGTH_LONG).show();



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Code to execute after 2 seconds
                        restartApp();
                    }
                }, 3000);

            }
        });

        return view;
    }

    private void calculateTotal(int invoiceNo) {
        DatabaseReference cartRef = mDatabase.child("carts").child(String.valueOf(invoiceNo));
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalAmount = 0.0; // Reset total amount

                // Retrieve items from cart
                DataSnapshot itemsSnapshot = dataSnapshot.child("Items");
                final int[] itemCount = {Math.toIntExact(itemsSnapshot.getChildrenCount())};
                final int[] processedCount = {0}; // To track processed items

                for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                    String itemId = itemSnapshot.getKey();
                    String quantityStr = itemSnapshot.child("qty").getValue(String.class);
                    int quantity = Integer.parseInt(quantityStr != null ? quantityStr : "0");

                    // Fetch item price
                    DatabaseReference itemRef = mDatabase.child("menu").child("categories");
                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot categoriesSnapshot) {
                            boolean itemFound = false;
                            for (DataSnapshot categorySnapshot : categoriesSnapshot.getChildren()) {
                                DataSnapshot itemsInCategory = categorySnapshot.child("items");
                                if (itemsInCategory.hasChild(itemId)) {
                                    DataSnapshot itemDetails = itemsInCategory.child(itemId);
                                    String priceStr = itemDetails.child("price").getValue(String.class);
                                    double price = Double.parseDouble(priceStr != null ? priceStr : "0");

                                    // Update total amount for this item
                                    totalAmount += price * quantity;
                                    itemFound = true;
                                    break; // Exit loop once item is found
                                }
                            }

                            processedCount[0]++;
                            if (processedCount[0] == itemCount[0]) {
                                // Update the TextView with the total amount
                                totalDisp.append(String.valueOf(totalAmount));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("order_pay", "loadItem:onCancelled", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("order_pay", "loadCart:onCancelled", databaseError.toException());
            }
        });
    }

    private void restartApp() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(
                getContext(),
                mPendingIntentId,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager mgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);  // Close all activities
        System.exit(0);    // Terminate the process
    }

}
