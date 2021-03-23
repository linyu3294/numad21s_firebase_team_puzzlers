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

    private FirebaseDatabase db;
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

        db = FirebaseDatabase.getInstance();

        // Bind msgs with UI elements
        adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messages);
        msgListView.setAdapter(adapter);
        db.getReference("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Message msg = messageSnapshot.getValue(Message.class);
                    if (msg == null)
                        return;

                    messages.add(msg);
                    adapter.notifyDataSetChanged();
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
        int emojiID = 0;

        // Attempt to parse emojiID
        try {
            // TODO: Get emojiID from image instead of input text
            emojiID = Integer.parseInt(inputText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int finalEmojiID = emojiID;

        // Get msg token from target user
        String targetToken = targetUser.getMessageToken();

        // Send msg on new thread
        new Thread(() -> {
            // Save msg in database
            Message newMsg = MessageService.createNewMessage(FirebaseDatabase.getInstance(), currentUser, targetUser, finalEmojiID);

            JSONObject jNotification = new JSONObject();
            JSONObject jdata = new JSONObject();
            JSONObject jPayload = new JSONObject();
            try {
                // TODO: (criteria) we need more than text
                jNotification.put("title", "New message");
                jNotification.put("body", "Emoji msg");
                jNotification.put("sound", "default");
                jNotification.put("badge", "1");

                jdata.put("title", "Message");
                jdata.put("data", newMsg);

                jPayload.put("to", targetToken);
                jPayload.put("priority", "high");
                jPayload.put("notification", jNotification);
                jPayload.put("data", jdata);

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", SERVER_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Send FCM message content.
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(jPayload.toString().getBytes());
                outputStream.close();

                Log.d(TAG, "Sending " + String.valueOf(newMsg.getEmojiID()) + " from " + currentUser.getUsername() + " to " + targetUser.getUsername());

                // Read FCM response.
                InputStream inputStream = conn.getInputStream();
                final String resp = convertStreamToString(inputStream);
                Log.d(TAG, resp);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Helper function
     *
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}

