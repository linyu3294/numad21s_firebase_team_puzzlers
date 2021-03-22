package com.example.numad21s_firebase_team_puzzlers;

import com.example.numad21s_firebase_team_puzzlers.model.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.example.numad21s_firebase_team_puzzlers.services.EmojiService;
import com.example.numad21s_firebase_team_puzzlers.services.MessageService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MessagingActivity extends AppCompatActivity {
    private static final String TAG = MessagingActivity.class.getSimpleName();
    private static final String SERVER_KEY = "key=AAAARIbEPBk:APA91bH3zahKhXJ5BBHpVFwQ6BkUv1izI8Ff9q642GqP7vf8cGpf8gANKGF5T-26oL3rcX-l7FaD7a0GOLKmZi0H9fRVGTKQvdrGQdgquDq_5DD-CNUUOTB1EtX17v9DLiQ5ZneJdsvK";

    private User myUserInstance;
    private User targetUser;

    private TextView inputText;
    private Button sendMsgBtn;
    private LinearLayout msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        myUserInstance = (User) getIntent().getSerializableExtra("myUserInstance");
        targetUser = (User) getIntent().getSerializableExtra("targetUser");

        sendMsgBtn = findViewById(R.id.sendMsgButton);
        inputText = findViewById(R.id.messageInput);
        msgList = findViewById(R.id.msgList);

        // Bind messages to UI
        DatabaseReference msgsRef = FirebaseDatabase.getInstance().getReference("messages");
        msgsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message msg = messageSnapshot.getValue(Message.class);

                    // Filter unrelated messages
                    if (msg != null && (msg.userFrom == myUserInstance && msg.userTo == targetUser) ||
                            msg.userFrom == targetUser && msg.userTo == myUserInstance) {

                        Log.println(Log.DEBUG, TAG, "Reading " + msg.getEmojiID() + " from " + msg.getUserFrom().getUsername() + " to " + msg.getUserTo().getUsername());

                        // TODO: render emoji somehow
//                        EmojiService.getEmojiByID(msg.getEmojiID());

                        Button userBtn = new Button(getApplicationContext());
                        userBtn.setText(String.valueOf(msg.emojiID));
                        userBtn.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        ));

                        msgList.addView(userBtn);
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
        // Get msg token from target user
        final String targetToken = targetUser.getMessageToken();

        // Get emoji ID from input text
        final int emojiID = Integer.parseInt(inputText.getText().toString());

        Log.println(Log.DEBUG, TAG, "Sending " + String.valueOf(emojiID) + " from " + myUserInstance.getUsername() + " to " + targetUser.getUsername());

        // Send msg on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Save msg in database
                Message newMsg = MessageService.createNewMessage(FirebaseDatabase.getInstance(), myUserInstance, targetUser, emojiID);

                JSONObject jNotification = new JSONObject();
                JSONObject jdata = new JSONObject();
                JSONObject jPayload = new JSONObject();
                try {
                    jNotification.put("title", "New message");
                    jNotification.put("body", "Emoji msg"); //TODO: we need more than text
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");

                    jdata.put("title", "Test Message");
                    jdata.put("msg", newMsg);

                    /***
                     * The Notification object is now populated.
                     * Next, build the Payload that we send to the server.
                     */
                    jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);
                    jPayload.put("priority", "high");
                    jPayload.put("notification", jNotification);
                    jPayload.put("data", jdata);


                    /***
                     * The Payload object is now populated.
                     * Send it to Firebase to send the message to the appropriate recipient.
                     */
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

                    // Read FCM response.
                    InputStream inputStream = conn.getInputStream();
                    final String resp = convertStreamToString(inputStream);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
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