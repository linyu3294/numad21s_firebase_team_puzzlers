package com.example.numad21s_firebase_team_puzzlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.example.numad21s_firebase_team_puzzlers.services.EmojiService;
import com.example.numad21s_firebase_team_puzzlers.services.UserService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView welcomeMsg;
    private List<User> otherUsers;
    private String myUserName;
    private String myInstanceId;
    private User myUserInstance;
    private LinearLayout userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseDatabase.getInstance();
        myUserName = getIntent().getStringExtra("myUserName");
        welcomeMsg = (TextView) findViewById(R.id.txt_welcome);
        myInstanceId = getIntent().getStringExtra("myInstanceId");
        myUserInstance = (User) getIntent().getSerializableExtra("myUserInstance");
        userLayout = findViewById(R.id.scroll_users_view_layout);

        welcomeMsg.setText("Hello, " + myUserName + " !");
        otherUsers = new ArrayList<User>();

        UserService.bindUsersToLayout(db, this, userLayout);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("My Instance ID   " + myInstanceId);
//        db.getReference().child("users").child(myInstanceId).setValue(null);
    }

}