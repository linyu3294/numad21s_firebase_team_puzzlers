package com.example.numad21s_firebase_team_puzzlers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class StickerMessengerActivity extends AppCompatActivity {
    private TextView userName;
    private String token;
    private FirebaseDatabase db;
    private String myInstanceId;
    private RadioButton user1;
    private RadioButton user2;
    private RadioButton user3;
    private RadioButton user4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_messenger);
        userName = findViewById(R.id.txt_usr_name);
        db = FirebaseDatabase.getInstance();
        myInstanceId = null;

        user1 = findViewById(R.id.btn_user_1);
        user2 = findViewById(R.id.btn_user_2);
        user3 = findViewById(R.id.btn_user_3);
        user4 = findViewById(R.id.btn_user_4);


    }


    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference userRef = db.getReference("users");
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.getReference().child("users").onDisconnect().removeValue();
    }

    /**
     * Not Used for Now, Keeping here incase there is a need move thread off main.
     *
     * @param type
     */
    public void sendMessageToNews(View type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testFireBase();
            }
        }).start();
    }

    /**
     * Tests that the database is in fact connected.
     * Should write Hello, World when app loads onCreate.
     */
    private void testFireBase() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World");
    }


    public void onUserRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.btn_user_1:
                if (checked) {
                    user2.setChecked(false);
                    user3.setChecked(false);
                    user4.setChecked(false);
                    if (myInstanceId == null) {
                        createUser(db.getReference(), new User("user1"));
                    } else {
                        User user1 = new User("user1", myInstanceId);
                        updateUser(db.getReference(), user1);
                    }
                }
                break;

            case R.id.btn_user_2:
                if (checked) {
                    user1.setChecked(false);
                    user3.setChecked(false);
                    user4.setChecked(false);
                    if (myInstanceId == null) {
                        createUser(db.getReference(), new User("user2"));
                    } else {
                        User user2 = new User("user2", myInstanceId);
                        updateUser(db.getReference(), user2);
                    }
                }
                break;
            case R.id.btn_user_3:
                if (checked) {
                    user1.setChecked(false);
                    user2.setChecked(false);
                    user4.setChecked(false);
                    if (myInstanceId == null) {
                        createUser(db.getReference(), new User("user3"));
                    } else {
                        User user3 = new User("user3", myInstanceId);
                        updateUser(db.getReference(), user3);
                    }
                }
                break;
            case R.id.btn_user_4:
                if (checked) {
                    user1.setChecked(false);
                    user2.setChecked(false);
                    user3.setChecked(false);
                    if (myInstanceId == null) {
                        createUser(db.getReference(), new User("user4"));
                    } else {
                        User user4 = new User("user4", myInstanceId);
                        updateUser(db.getReference(), user4);
                    }
                }
                break;
        }
    }

    /**
     * I don't think this needs a transaction as there is not race condition when creating users.
     *
     * @param postRef
     * @param user
     */
    private void createUser(DatabaseReference postRef, User user) {
        DatabaseReference postsRef = db.getReference();
        DatabaseReference newPostRef = postsRef.push();
        postRef.child("users").push().setValue(user);
        String postID = newPostRef.getKey();
        myInstanceId = postID;
        System.out.println(postID);
    }

    /**
     * Uses transaction to avoid simultaneous updates.
     *
     * @param postRef
     * @param user
     */
    private void updateUser(DatabaseReference postRef, User user) {
        System.out.println(myInstanceId);
        postRef
                .child("users")
                .child(myInstanceId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        User u = mutableData.getValue(User.class);
                        if (u == null) {
                            return Transaction.success(mutableData);
                        }
                        mutableData.setValue(user.username);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                    }
                });
    }


}
