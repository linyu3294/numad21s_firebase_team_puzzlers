package com.example.numad21s_firebase_team_puzzlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView myUserNameView;
    private String myUserName;
    private String myInstanceId;
    private ScrollView scrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance();
        myInstanceId = null;
        scrollView = findViewById(R.id.scroll_users_view);

        myUserNameView = (TextView) findViewById(R.id.txt_my_user_name);
        myUserName = myUserNameView.getText().toString();

    }
    @Override
    protected void onStop() {
        super.onStop();
        db.getReference().child("users").onDisconnect().removeValue();
    }

    public void onLogInClick(View view) {
        myUserName = myUserNameView.getText().toString();
        createUser(db.getReference(), new User(myUserName));
        openHomeActivity();
    }

    /**
     * I don't think this needs a transaction as there is not a race condition when creating users.
     *
     * @param postRef
     * @param user
     */
    private void createUser(DatabaseReference postRef, User user) {
        DatabaseReference dbPostsRef = db.getReference();
        DatabaseReference childPostRef = postRef.child("users").push();
        childPostRef.setValue(user);
        String postID = childPostRef.getKey();
        myInstanceId = postID;
        System.out.println(postID);
    }


    /**
     * Update User.
     * This Method is NOT in USE. For reference only.
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
                        mutableData.setValue(user);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                    }
                });
    }


    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("myUserName", myUserName);
        startActivity(intent);
    }

}
