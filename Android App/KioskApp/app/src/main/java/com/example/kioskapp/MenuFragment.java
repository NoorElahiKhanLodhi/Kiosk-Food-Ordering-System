package com.example.kioskapp;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

TextView txt;
    public static int item_id = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);





        Bundle args = getArguments();
        if (args != null) {
Log.d("ARGS", String.valueOf(args));

// Create the new fragment instance
            Fragment newFragment = new CartFragment(); // Make sure your fragment class name is correct



            FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.container, newFragment);
            trans.addToBackStack(null);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Code to execute after 2 seconds
                    trans.commit();
                }
            }, 1);


        }






        LinearLayout cat_sec = view.findViewById(R.id.cat_sec);

//QR CODE START

        TextView qrs = new TextView(view.getContext());
        qrs.setText("Scan Menu");
        qrs.setTextSize(14);
        qrs.setGravity(Gravity.CENTER);
        qrs.setTextColor(Color.parseColor("#009688"));

        LinearLayout.LayoutParams px =
                new LinearLayout.LayoutParams(200,200);
        ImageView qr_img = new ImageView(view.getContext());
        qr_img.setLayoutParams(px);


        Picasso.with(view.getContext()).load(R.drawable.menu_link).into(qr_img);

        LinearLayout qr_lay = new LinearLayout(view.getContext());
        qr_lay.setOrientation(LinearLayout.VERTICAL);
        qr_lay.addView(qr_img);
        qr_lay.addView(qrs);
        qr_lay.setBackgroundColor(Color.rgb(230, 230, 230));
        qr_lay.setGravity(Gravity.CENTER);
        qr_lay.setPadding(5,5,5,5);

        LinearLayout qr_line = new LinearLayout(view.getContext());
        qr_line.setMinimumHeight(10);
        qr_line.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        //catergories section being added with category card
        cat_sec.addView(qr_lay);
        cat_sec.addView(qr_line);

//        QR CODE END





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/menu/categories");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String img  = dsp.child("image").getValue().toString();
                    String cat   = dsp.child("cat").getValue().toString();

                    TextView cats = new TextView(view.getContext());
                    cats.setText(cat);
                    cats.setTextSize(15);
                    cats.setGravity(Gravity.CENTER);
                    cats.setTextColor(Color.parseColor("#009688"));

                    LinearLayout.LayoutParams param =
                            new LinearLayout.LayoutParams(200,200);
                    ImageView img_cat = new ImageView(view.getContext());
                    img_cat.setLayoutParams(param);
                    Picasso.with(view.getContext()).load(img).into(img_cat);

//                    RelativeLayout ral1 = new RelativeLayout(view.getContext());
//                    RelativeLayout ral2 = new RelativeLayout(view.getContext());
//
//                    ral1.addView(img_cat);
//                    ral1.setPadding(30,30,30,30);
//
//                    ral2.addView(cats);
//                    ral2.setPadding(100,0,0,0);
//

                    LinearLayout lay1 = new LinearLayout(view.getContext());
                    lay1.setOrientation(LinearLayout.VERTICAL);
                    lay1.addView(img_cat);
                    lay1.addView(cats);
                    lay1.setBackgroundColor(Color.rgb(230, 230, 230));
                    lay1.setGravity(Gravity.CENTER);
                    lay1.setPadding(5,5,5,5);



                    LinearLayout line = new LinearLayout(view.getContext());
                    line.setMinimumHeight(10);
                    line.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    LinearLayout line1 = new LinearLayout(view.getContext());
                    line1.setMinimumHeight(10);
                    line1.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);

                    cat_sec.addView(line);
                    //catergories section being added with category card
                    cat_sec.addView(lay1);
                    cat_sec.addView(line1);

                    //Show Items On Clicking category
                    lay1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GridView item_sec = view.findViewById(R.id.item_sec);
//                            item_sec.removeAllViews();


                            ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
//ITEMS SECTION

                            for(DataSnapshot di : dataSnapshot.child(cat).child("items").getChildren()){

                                String n  = di.child("name").getValue().toString();
                                String p  = di.child("price").getValue().toString();
                                String i  = di.child("image").getValue().toString();
                                String d  = di.child(("discount")).getValue().toString();
                                String s  = di.child(("status")).getValue().toString();
                                String u  = di.child(("unit")).getValue().toString();
                                String id  = di.child(("id")).getValue().toString();
                                String des = "";
                                try {
                                    des = di.child(("description")).getValue().toString();
                                    }catch (Exception e){
                                    des="";
                                    Log.d("Exception","Item Description Error");
                                }


                                //
////Item name txt view
//                                TextView item_n = new TextView(view.getContext());
//                                item_n.setText("\t"+n);
//                                item_n.setTextSize(25);
//                                item_n.setTextColor(Color.parseColor("#00b6a4"));
////Item image tx view
//                                LinearLayout.LayoutParams param2 =
//                                        new LinearLayout.LayoutParams(300,300);
//                                ImageView img_prod = new ImageView(view.getContext());
//                                img_prod.setLayoutParams(param2);
//                                Picasso.with(view.getContext()).load(i).into(img_prod);
////Item pricing tx views
//                                //price tx view
//                                TextView item_price = new TextView(view.getContext());
//                                item_price.setTextSize(12);
//                                item_price.setTextColor(Color.parseColor("#3d2b1f"));
//                                item_price.setPaintFlags(item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                                //discount  tx view
//                                TextView item_discount = new TextView(view.getContext());
//                                item_discount.setTextSize(15);
//                                item_discount.setTextColor(Color.parseColor("#3d2b1f"));
//
//                                if(d.isEmpty()){
//                                    item_discount.setText("\t\t"+p+"/"+u);
//                                }else{
//                                    item_price.setText("\t\t"+p+"/"+u);
//                                    item_discount.setText("\t"+d+"/"+u);
//                                }
//                                LinearLayout prices = new LinearLayout(view.getContext());
//                                prices.addView(item_price);
//                                prices.addView(item_discount);
//
//
//                                LinearLayout rl1 = new LinearLayout(view.getContext());
//                                LinearLayout rl2 = new LinearLayout(view.getContext());
//
//                                rl1.addView(img_prod);
//
//                                rl2.addView(item_n);
//
//                                rl2.addView(prices);
//
//                                rl2.setOrientation(LinearLayout.VERTICAL);
//
//                                LinearLayout lay2 = new LinearLayout(view.getContext());
//
//                                lay2.setBackgroundColor(Color.parseColor("#faf7f7"));
//                                lay2.addView(rl1);
//                                lay2.addView(rl2);
//
//
//                                LinearLayout line2 = new LinearLayout(view.getContext());
//                                line2.setMinimumHeight(10);
//                                line2.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);



                                itemModelArrayList.add(new ItemModel(i, n, p, d, u, des, id));




//                                LinearLayout add_to_cart = new LinearLayout(view.getContext());
//                                add_to_cart.setBackgroundColor(Color.RED);
//                                TextView cart = new TextView(view.getContext());
//                                cart.setText("ADD TO CART \uD83D\uDED2");
//                                cart.setGravity(Gravity.CENTER_HORIZONTAL);
//                                cart.setTextColor(Color.WHITE);
//                                cart.setTextSize(20);
//                                add_to_cart.addView(cart);
//                                add_to_cart.setGravity(Gravity.CENTER_HORIZONTAL);

//                        main.addView(lay2);
//                        main.addView(add_to_cart);
//                        main.addView(line2);



                                ///ADD TO CART ACTION
//                                add_to_cart.setOnClickListener(new View.OnClickListener() {
//                                    /////WORK TO BE DONE IN THIS SECTION items added with a refrence no for each oid
//
//
//                                    @Override
//                                    public void onClick(View v) {
//                                        Toast.makeText(view.getContext(), "Added "+n+" To Cart"+invoice_no, Toast.LENGTH_SHORT).show();
//
//                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
//                                        DatabaseReference orderRef = database.getReference("/carts/"+invoice_no);
//                                        orderRef.child("OID").setValue(invoice_no);
//                                        item_id++;
//                                        orderRef.child("Items").child("item"+item_id).setValue(id);
//
//
//                                    }
//                                });
                                ///ADD TO CART ACTION


                            }
                            //ITEMS SECTION
                            ItemAdapter adapter = new ItemAdapter(requireContext(), itemModelArrayList);

                            item_sec.setAdapter(adapter);





                        }
                    });




                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to get menu.", error.toException());
            }
        });




        // Inflate the layout for this fragment
        return view;
        }


}//Menu Fragement