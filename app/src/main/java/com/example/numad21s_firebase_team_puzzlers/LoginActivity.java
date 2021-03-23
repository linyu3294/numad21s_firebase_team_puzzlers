package com.example.numad21s_firebase_team_puzzlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.example.numad21s_firebase_team_puzzlers.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView usernameTextView;
    private User currentUser;
    private ScrollView scrollView;
    private FirebaseMessaging fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Cache UI elements
        scrollView = findViewById(R.id.scroll_users_view);
        usernameTextView = findViewById(R.id.txt_my_user_name);

        db = FirebaseDatabase.getInstance();
        fm = FirebaseMessaging.getInstance();
    }


    /**
     * Button onClick handler.
     *
     * @param view
     */
    public void loginUser(View view) {
        // Get username from text input
        String username = usernameTextView.getText().toString();

        // Validate our username text input
        if (!UserService.usernameIsValid(db, username)) {
            Toast.makeText(getApplicationContext(), "Invalid username!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch unique realtime messenger token
        fm.getToken().addOnCompleteListener(task -> {
            // Handle error
            if (!task.isSuccessful()) {
                task.getException().printStackTrace();
                return;
            }

            String token = task.getResult();

            // Create new user with token
            currentUser = UserService.createNewUser(db, username, token);

            openHomeActivity();
        });
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("myUserInstance", currentUser);
        startActivity(intent);
    }
}
