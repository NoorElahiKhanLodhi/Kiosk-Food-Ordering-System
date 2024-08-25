package com.example.kioskapp;

import com.example.kioskapp.databaseFunctions.Cart;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class CartFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    LinearLayout main;
    View view;
double total = 0;
public String invoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_cart, container, false);
total=0;
        MainActivity act = (MainActivity)getActivity();
        int invoice_no = act.getMyInvoiceID();
invoice = String.valueOf(invoice_no);

        TextView cart_id = view.findViewById(R.id.cart_id);
if(invoice_no!=0) {
    cart_id.setText("CART ID: "+invoice_no);
}else{
    cart_id.setText("YOUR CART");
}
        ///////////
        main = view.findViewById(R.id.orderList);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("/carts/"+invoice_no+"/Items");



        cartRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int item_no = 0;

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    item_no++;

                    Log.d("TEST: ID",String.valueOf(dsp.getKey()));

                    String str = String.valueOf(dsp.getValue());

                    Log.d("TEST: ",str);

                    String detail = str
                            .replace("{","")
                            .replace("mod=","")
                            .replace("qty=","")
                            .replace("}","");

                    String item_id =String.valueOf(dsp.getKey());

//                    String item_mods ="No Modifications";
//
//                    String[] split = detail.split(", ");

//                    Log.d("TEST: ", Arrays.toString(split));
//
//                    if(split[0] != "" ){
//
//                         item_mods = split[0];
//
//                    }


//                    Log.d("TEST: XX","out split block"+" "+split[0]);
//                    if(split[1] == null){
//                        Log.d("TEST: XX","split block");
//                        Log.d("TEST: XX",split[0]+"\n"+split[0]);
//                    }
//                    int item_qty = Integer.parseInt(split[1]);
//                    Log.d("TEST:1 QTY",String.valueOf(item_qty));
//                    Log.d("TEST:1 MODS",String.valueOf(item_mods));


                    //Product INfo
                if(!item_id.isEmpty()){
                    itemInfo(item_id);
                }

                    //Product Info



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        LinearLayout pay = new LinearLayout(view.getContext());
        pay.setBackgroundColor(Color.BLUE);
        TextView place_order = new TextView(view.getContext());
        place_order.setText("PAY ✔");
        place_order.setGravity(Gravity.CENTER_HORIZONTAL);
        place_order.setTextColor(Color.WHITE);
        place_order.setTextSize(20);
        pay.addView(place_order);
        pay.setGravity(Gravity.CENTER_HORIZONTAL);
        main.addView(pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// Create the new fragment instance
                Fragment newFragment = new order_pay(); // Make sure your fragment class name is correct

// Create a Bundle to pass the parameters
                Bundle args = new Bundle();
                args.putString("param1", invoice); // Replace with your actual parameter name and value

// Set the arguments for the new fragment
                newFragment.setArguments(args);

// Replace the current fragment with the new one
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();


            }
        });


        return view;
    }

    void itemInfo(String item_id){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Replace "menu/categories" with the path to your categories node
        DatabaseReference categoriesRef = mDatabase.child("/menu/categories");

        // Replace "item_id_to_search" with the ID of the item you want to search for
        String itemIdToSearch = item_id;
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {

                        String categoryName = categorySnapshot.getKey();
                        // Get the items node for the current category
                        DataSnapshot itemsSnapshot = dataSnapshot.child(categoryName+"/items/"+itemIdToSearch);



                        // Check if the item with the specified ID exists in this category
                        if (itemsSnapshot.child("name").getValue()!=null) {
                            // Get the item information
                            Log.d("MessageXC2:", itemIdToSearch); //id
                            String itemName = itemsSnapshot.child("name").getValue().toString(); //name
                            String unit = itemsSnapshot.child("unit").getValue().toString(); //unit
    double itemPrice = Double.parseDouble(itemsSnapshot.child("price").getValue().toString()); //price
                            String image = itemsSnapshot.child("image").getValue().toString();//image
//discount if there
                            double itemDiscount;
                            boolean discount;
                            if(!itemsSnapshot.child("discount").getValue().toString().equals("")) {
                                itemDiscount = Double.parseDouble(itemsSnapshot.child("discount").getValue().toString());
                                discount=true;
                            }else{
                                itemDiscount=itemPrice;
                                discount=false;
                            }


                            TextView total_amount = view.findViewById(R.id.total);






                            //discount if there [end]
                            //Get modifiers
                            HashMap<String, String> modifiers = new HashMap<String, String>();
                            for (DataSnapshot mods : itemsSnapshot.child("modifier").getChildren()){
                                modifiers.put(mods.getKey(),(String) mods.getValue());
                            }

                            //Got Modifiers



                            // Log the item information
                            Log.d("MessageXC:", "Name: " + itemName);
                            Log.d("MessageXC:", "Price: " + itemPrice);
                            Log.d("MessageXC:", "Discount: " + itemDiscount);
                            Log.d("MessageXC: Modifier", String.valueOf(modifiers));


                            ///Adding to UI
//Item name txt view
                            TextView item_n = new TextView(view.getContext());
                            item_n.setText("\t"+itemName);
                            item_n.setTextSize(25);
                            item_n.setTextColor(Color.parseColor("#00b6a4"));

                            //Item image tx view
                            LinearLayout.LayoutParams param2 =
                                    new LinearLayout.LayoutParams(300,300);
                            ImageView img_prod = new ImageView(view.getContext());
                            img_prod.setLayoutParams(param2);
                            Picasso.with(view.getContext()).load(image).into(img_prod);
//Item pricing tx views
                            //price tx view
                            TextView item_price = new TextView(view.getContext());
                            item_price.setTag(item_id+".price");
                            item_price.setTextSize(12);
                            item_price.setTextColor(Color.parseColor("#3d2b1f"));
                            item_price.setPaintFlags(item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            //discount  tx view
                            TextView item_discount = new TextView(view.getContext());
                            item_discount.setTextSize(15);
                            item_discount.setTextColor(Color.parseColor("#3d2b1f"));
int price_item;
                            if(discount==false){
                                item_discount.setText("\t\t"+itemPrice+"/"+unit);
                                price_item = (int) itemPrice;
                            }else{
                                item_price.setText("\t\t"+itemPrice+"/"+unit);
                                item_discount.setText("\t"+itemDiscount+"/"+unit);
                                price_item = (int) itemDiscount;
                            }

                            LinearLayout prices = new LinearLayout(view.getContext());
                            prices.addView(item_price);
                            prices.addView(item_discount);


                            LinearLayout rl1 = new LinearLayout(view.getContext());
                            LinearLayout rl2 = new LinearLayout(view.getContext());
                            LinearLayout rl3 = new LinearLayout(view.getContext());

                            Button sub = new Button(view.getContext());
                            sub.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                            sub.setText("-");



                            TextView qty = new TextView(view.getContext());
                            qty.setTag(item_id+".qty");



                            //set quantity on ui
                            qty.setText("1");

//????????
//                            ???
                            Cart.getItemInfo(invoice, item_id, true, new  Cart.OnQtyRetrievedListener() {
                                @Override
                                public void onQtyRetrieved(String qty) {
                                    if (qty != null) {
                                        // Handle the retrieved qty value
                                        int quantity = Integer.parseInt(qty);
                                        // For example, you can update a TextView with the retrieved quantity


                                        try{
                                            TextView qtyTextView = getView().findViewWithTag(item_id+".qty");
                                            qtyTextView.setText(String.valueOf(quantity));
                                            qtyTextView.setText(String.valueOf(quantity));
                                            total += (Integer.parseInt(String.valueOf(qtyTextView.getText()))-1)*price_item;
                                            total_amount.setText("TOTAL: " + total);
                                        }catch (Exception e){
Log.d("Exception: ","QTY view already added");
                                        }


                                        //onqty change
//                                        Log.d("XXX: ", String.valueOf(quantity));

//                                        ???
//                                        ?????

//                                        TextView total_amount = view.findViewById(R.id.total);

                                    } else {
                                        // Handle the case where the qty value does not exist or an error occurred
                                    }
                                }
                            });
//                            ???


//                                total+= Integer.parseInt((String) qty.getText())*price_item;


//                            total_amount.setText("TOTAL: "+total);

//                            qty.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                            Button add = new Button(view.getContext());
                            add.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                            add.setText("+");

                            rl3.addView(sub);
                            rl3.addView(qty);
                            rl3.addView(add);

                            rl1.addView(img_prod);

                            rl2.addView(item_n);

                            rl2.addView(prices);



                            //Modifier traverse to add text and button to add to rl2
                            for(String mod : modifiers.keySet()){
                                LinearLayout rlx = new LinearLayout(view.getContext());
//                                TextView txt_mod = new TextView(view.getContext());

                                Switch mod_switch = new Switch(view.getContext());
                                mod_switch.setTag(item_id+","+mod+","+"mod");

                                String mod_value = modifiers.get(mod);
//                                Log.d("Error Find",mod_value);

boolean ad = false;
                                if (!mod_value.isEmpty()) {
                                    mod_switch.setText(String.format("%s : %s  ", mod, mod_value));
                                    ad = true;
                                }else{
                                    mod_switch.setText(String.format("Remove %s", mod));
                                    ad = false;
                                }

                          //      rlx.addView(txt_mod);

                             //   mod_switch.setId(Integer.parseInt(itemName+"_"+txt_mod));


                                //mod_switch.setText(txt_mod.toString());
                                boolean finalAd = ad;
                                mod_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        Log.d("Error Find ", String.valueOf(finalAd));
        if(isChecked && finalAd){
            total+=Double.parseDouble(mod_value)*Integer.parseInt((String) qty.getText());
total_amount.setText("TOTAL: "+total);
Cart.mod_update(invoice, item_id, String.valueOf(mod_switch.getTag()).split(",")[1]);
        }
        else{

            if(finalAd){
            total-=Double.parseDouble(mod_value)*Integer.parseInt((String) qty.getText());
            total_amount.setText("TOTAL: "+total);
                Cart.mod_update(invoice, item_id, "");
            }
        }
    }
});

                                rlx.addView(mod_switch);
                                rl2.addView(rlx);
                            }
                            //

                            rl2.setOrientation(LinearLayout.VERTICAL);

                            LinearLayout lay2 = new LinearLayout(view.getContext());

                            lay2.setBackgroundColor(Color.parseColor("#faf7f7"));
                            lay2.addView(rl1);
                            lay2.addView(rl2);
                            lay2.addView(rl3);


                            LinearLayout line2 = new LinearLayout(view.getContext());
                            line2.setMinimumHeight(10);
                            line2.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);



                            TextView qt = rl3.findViewWithTag(item_id+".qty");


                            total += price_item;
                            total_amount.setText("TOTAL: " + total);





if(!String.valueOf(main.findViewWithTag(item_id+"lay2")).contains("android.widget.LinearLayout{")){
    Log.d("XXX: ", "Allow Add");
    lay2.setTag(item_id+"lay2");
    line2.setTag(item_id+"line2");



    main.addView(lay2);
    main.addView(line2);
}


//                            try {
//                                total += (Integer.parseInt(String.valueOf(qt.getText()))) * price_item;
//                                total_amount.setText("TOTAL: " + total);
//                            }catch(Exception e){
//                                total += (Integer.parseInt(String.valueOf(qty.getText()))) * price_item;
//                                total_amount.setText("TOTAL: " + total);
//                                e.printStackTrace();
//                            }


//                            total+=(Integer.parseInt(String.valueOf(quantity)))*price_item;
////
//                            total_amount.setText("TOTAL: "+total);



                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String q = String.valueOf(qty.getText());
                                    int temp_val = Integer.parseInt(q);
                                    ++temp_val;
                                    total+=price_item;
                                    total_amount.setText("TOTAL: "+total);
                                    qty.setText(String.valueOf(temp_val));
                                    Cart.cartQty(String.valueOf(invoice), item_id, true);


// Create the new fragment instance
                                    Fragment newFragment = new MenuFragment(); // Make sure your fragment class name is correct

// Create a Bundle to pass the parameters
                                    Bundle args = new Bundle();
                                    args.putString("param1", invoice); // Replace with your actual parameter name and value

// Set the arguments for the new fragment
                                    newFragment.setArguments(args);

// Replace the current fragment with the new one
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, newFragment);
                                    transaction.addToBackStack(null);

// Commit the transaction
                                    transaction.commit();



                                }
                            });

                            sub.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String q = String.valueOf(qty.getText());
                                    int temp_val = Integer.parseInt(q);
                                    Log.d("TEMP", String.valueOf(temp_val));
                                    --temp_val;

                                    Log.d("TEMP", String.valueOf(temp_val));

                                    if(temp_val<=0){
                                        main.removeView(lay2);
                                    }

                                    total-=price_item;
                                    total_amount.setText("TOTAL: "+total);
                                    qty.setText(String.valueOf(temp_val));
                                    Cart.cartQty(String.valueOf(invoice), item_id, false);


// Create the new fragment instance
                                    Fragment newFragment = new MenuFragment(); // Make sure your fragment class name is correct

// Create a Bundle to pass the parameters
                                    Bundle args = new Bundle();
                                    args.putString("param1", invoice); // Replace with your actual parameter name and value

// Set the arguments for the new fragment
                                    newFragment.setArguments(args);

// Replace the current fragment with the new one
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, newFragment);
                                    transaction.addToBackStack(null);

// Commit the transaction
                                    transaction.commit();





                                }
                            });





                            ///Adding to UI [end]


                        }


                    }



                } else {
                    Log.d("MessageXC:", "No categories found");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MessageXC:", "Database error: " + databaseError.getMessage());
            }
        });


    }

}