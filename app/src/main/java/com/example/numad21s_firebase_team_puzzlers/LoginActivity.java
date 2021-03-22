package com.example.numad21s_firebase_team_puzzlers;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView myUserNameView;
    private String myUserName;
    private User myUserInstance;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance();
        scrollView = findViewById(R.id.scroll_users_view);

        myUserNameView = (TextView) findViewById(R.id.txt_my_user_name);
        myUserName = myUserNameView.getText().toString();
    }

    public void onLogInClick(View view) {
        myUserName = myUserNameView.getText().toString();

        // If username is valid
        if (myUserName != null && myUserName.length() > 0) {
            // Get Messaging user token, and create user with username & token attached
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            // Update myUserInstance
                            myUserInstance = UserService.createNewUser(db, myUserName, task.getResult());
                            openHomeActivity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Invalid username!", Toast.LENGTH_LONG).show();
        }
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("myUserInstance", myUserInstance);
        startActivity(intent);
    }
}

//    /**
//     * Update User.
//     * This Method is NOT in USE. For reference only.
//     * Uses transaction to avoid simultaneous updates.
//     *
//     * @param postRef
//     * @param user
//     */
//    private void updateUser(DatabaseReference postRef, User user) {
//        System.out.println(myInstanceId);
//        postRef
//                .child("users")
//                .child(myInstanceId)
//                .runTransaction(new Transaction.Handler() {
//                    @Override
//                    public Transaction.Result doTransaction(MutableData mutableData) {
//                        User u = mutableData.getValue(User.class);
//                        if (u == null) {
//                            return Transaction.success(mutableData);
//                        }
//                        mutableData.setValue(user);
//                        return Transaction.success(mutableData);
//                    }
//
//                    @Override
//                    public void onComplete(DatabaseError databaseError, boolean b,
//                                           DataSnapshot dataSnapshot) {
//                        // Transaction completed
//                    }
//                });
//    }

