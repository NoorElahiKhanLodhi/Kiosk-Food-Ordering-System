package com.example.kioskapp.databaseFunctions;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Cart {




    public static void cartQty(String cart_no, String item_id, boolean increase) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carts/"+cart_no+"/Items/"+item_id+"/qty");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//on + button
                    if(increase){
                        String qty = String.valueOf(Integer.parseInt(dataSnapshot.getValue(String.class))+1);
                        databaseReference.setValue(qty);
                    }
//on - button
                    else {
                        String qty = String.valueOf(Integer.parseInt(dataSnapshot.getValue(String.class))-1);
    //if item removed
                        if(Integer.parseInt(dataSnapshot.getValue(String.class))-1 <=0){
                            FirebaseDatabase.getInstance().getReference("carts/"+cart_no+"/Items/"+item_id).removeValue();
    //if item only reduced
                        }else databaseReference.setValue(qty);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(databaseError.toString(), getClass() +" - "+ this);
            }});
    }



    public interface OnQtyRetrievedListener {
        void onQtyRetrieved(String qty);
    }
    public static String qtyOrMod = " ";
public static void getItemInfo(String cart_no, String item_id, boolean qty, final OnQtyRetrievedListener listener){
    DatabaseReference databaseReference;
        if(qty){
            databaseReference = FirebaseDatabase.getInstance().getReference("carts/"+cart_no+"/Items/"+item_id+"/qty");
        }else{
            databaseReference = FirebaseDatabase.getInstance().getReference("carts/"+cart_no+"/Items/"+item_id+"/mod");
        }


    // Add a listener to get the current value of "qty"
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                // Get the current value of "qty"
                String val = dataSnapshot.getValue(String.class);

                listener.onQtyRetrieved(val);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(databaseError.toString(), getClass() +" - "+ this);
        }
    });

}

public static void mod_update(String cart_no, String item_id, String mod){

    DatabaseReference mod_ref = FirebaseDatabase.getInstance().getReference("carts/"+cart_no+"/Items/"+item_id+"/mod");
    mod_ref.setValue(mod);
}

}
