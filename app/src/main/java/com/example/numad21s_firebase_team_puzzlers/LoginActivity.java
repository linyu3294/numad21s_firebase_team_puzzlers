package com.example.numad21s_firebase_team_puzzlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.example.numad21s_firebase_team_puzzlers.services.UserService;
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
    private User myUserInstance;
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

    public void onLogInClick(View view) {
        myUserName = myUserNameView.getText().toString();

        // Basic username validation
        if (myUserName != null && myUserName.length() > 0) {
            myInstanceId = UserService.createNewUser(db, myUserName);
            openHomeActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid username!", Toast.LENGTH_LONG).show();
        }
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

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("myUserName", myUserName);
        intent.putExtra("myInstanceId", myInstanceId);
        intent.putExtra("myUserInstance", myUserInstance);
        startActivity(intent);
    }
}
