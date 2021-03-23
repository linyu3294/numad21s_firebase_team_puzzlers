package com.example.numad21s_firebase_team_puzzlers;

import com.example.numad21s_firebase_team_puzzlers.model.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.example.numad21s_firebase_team_puzzlers.services.MessageService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MessagingActivity extends AppCompatActivity {
    private static final String TAG = MessagingActivity.class.getSimpleName();
    private static final String SERVER_KEY = "key=AAAARIbEPBk:APA91bH3zahKhXJ5BBHpVFwQ6BkUv1izI8Ff9q642GqP7vf8cGpf8gANKGF5T-26oL3rcX-l7FaD7a0GOLKmZi0H9fRVGTKQvdrGQdgquDq_5DD-CNUUOTB1EtX17v9DLiQ5ZneJdsvK";

    private TextView inputText;
    private Button sendMsgBtn;
    private ListView msgListView;
    private ArrayList<Message> messages = new ArrayList();

    private User currentUser;
    private User targetUser;

    private ArrayAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // Grab current & target user
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        targetUser = (User) getIntent().getSerializableExtra("targetUser");

        // Cache UI elements
        sendMsgBtn = findViewById(R.id.sendMsgButton);
        msgListView = findViewById(R.id.MsgListView);

        // TODO: Get emoji ID from UI images instead of input text
        inputText = findViewById(R.id.messageInput);

        // Bind msgs with UI elements
        adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messages);
        msgListView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Message msg = messageSnapshot.getValue(Message.class);
                    if (msg == null)
                        continue;

                    // Skip unrelated messages
                    if ((msg.getUserFrom().EqualsTo(targetUser) && msg.getUserTo().EqualsTo(currentUser))
                            || (msg.getUserFrom().EqualsTo(currentUser) && msg.getUserTo().EqualsTo(targetUser))) {
                        messages.add(msg);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "added");

                    } else {
                        Log.d(TAG, "skip");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Button onClick handler.
     */
    public void sendMessage(View type) {
        try {
            Message newMsg = MessageService.createNewMessage(FirebaseDatabase.getInstance(), currentUser, targetUser, Integer.parseInt(inputText.getText().toString()));

            // TODO: get notifications to work
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

