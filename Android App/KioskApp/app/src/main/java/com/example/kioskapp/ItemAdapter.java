package com.example.kioskapp;

import static com.example.kioskapp.MenuFragment.item_id;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import android.graphics.Paint;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemAdapter  extends ArrayAdapter<ItemModel> {

//public static int item_id;
//    MenuFragment menu = new MenuFragment();
//    item_id = menu.getID();
    public ItemAdapter(@NonNull Context context, ArrayList<ItemModel> itemModelArrayList) {
        super(context, 0, itemModelArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_holder, parent, false);
        }


        ItemModel itemModel = getItem(position);
        ImageView item_image = listitemView.findViewById(R.id.item_image);
        TextView item_name = listitemView.findViewById(R.id.item_name);
        TextView item_price = listitemView.findViewById(R.id.item_price);
        TextView item_discount = listitemView.findViewById(R.id.item_discount);
        TextView item_des = listitemView.findViewById(R.id.item_des);
        Button add_to_cart = listitemView.findViewById(R.id.add_to_cart);

        assert itemModel != null;
        String image = itemModel.getImgid();
        String price = itemModel.get_price();
        String dis = itemModel.get_dis();
        String unit = itemModel.get_unit();
        String des = itemModel.get_des();
        String id = itemModel.get_id();


        Picasso.with(listitemView.getContext()).load(image).into(item_image);

        item_name.setText(itemModel.get_name());
        
        item_price.setPaintFlags(item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                if(dis.isEmpty()){
                                    item_discount.setText("\t\t"+price+"/"+unit);
                                }else{
                                    item_price.setText("\t"+price+"/"+unit);
                                    item_discount.setText("\t"+dis+"/"+unit);
                                }
                    item_des.setText(des);


        MainActivity act = (MainActivity)getContext();
        int invoice_no = act.getMyInvoiceID();
        View finalListitemView = listitemView;


        add_to_cart.setOnClickListener(new View.OnClickListener() {
                                    /////WORK TO BE DONE IN THIS SECTION items added with a refrence no for each oid

                                    @Override
                                    public void onClick(View v) {
Log.d("Eror Find","CP1");
                                        Toast.makeText(finalListitemView.getContext(), "Added "+item_name+" To Cart"+invoice_no, Toast.LENGTH_SHORT).show();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference orderRef = database.getReference("/carts/"+invoice_no);
                                        orderRef.child("OID").setValue(invoice_no);
                                                                               item_id++;
                                        //orderRef.child("Items").child("item"+item_id).setValue(id);
                                        orderRef.child("Items").child(id).child("qty").setValue("1");

                                        orderRef.child("Items").child(id).child("mod").setValue("");

                                    }
        });


        return listitemView;
    }



}
