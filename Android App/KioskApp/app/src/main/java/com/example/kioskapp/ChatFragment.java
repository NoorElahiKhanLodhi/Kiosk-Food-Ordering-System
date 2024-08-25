package com.example.kioskapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private ListView chatListView;
    private EditText messageInput;
    private Button sendButton;
    private ArrayList<String> messagesList;
    private ArrayAdapter<String> adapter;
    private String orderID;
    private DatabaseReference chatRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Retrieve the order ID from the main activity (or another source)
        MainActivity act = (MainActivity) getActivity();
        int invoice_no = act.getMyInvoiceID();
        orderID = String.valueOf(invoice_no);

        chatListView = view.findViewById(R.id.chat_list_view);
        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.send_button);

        messagesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, messagesList);
        chatListView.setAdapter(adapter);

        // Firebase database reference
        chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(orderID);

        // Load existing messages in order based on the timestamps
        loadChatMessages();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    private void loadChatMessages() {
        chatRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String messageData = messageSnapshot.getValue(String.class);
                    if (messageData != null) {
                        String senderTag;
                        if (messageData.startsWith("app::")) {
                            senderTag = "You: ";
                        } else if (messageData.startsWith("admin::")) {
                            senderTag = "Admin: ";
                        } else {
                            continue;
                        }

                        String displayedMessage = messageData.substring(messageData.indexOf("::") + 2);
                        messagesList.add(senderTag + displayedMessage);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!message.isEmpty()) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String formattedMessage = "app::" + message;

            // Store the message in Firebase under the order ID
            chatRef.child(timestamp).setValue(formattedMessage);

            // Clear the input field after sending
            messageInput.setText("");
        }
    }
}
