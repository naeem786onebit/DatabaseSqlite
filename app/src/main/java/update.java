package com.example.databasehandler;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etDob;
    private Button btnUpdate;
    private DbHandler dbHandler;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etUsername = findViewById(R.id.etUsernameUpdate);
        etPassword = findViewById(R.id.etPasswordUpdate);
        etDob = findViewById(R.id.etDobUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
        dbHandler = new DbHandler(this);

        // Get the email from the Intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Load existing user data into the fields
        loadUserData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = etUsername.getText().toString();
                String newPassword = etPassword.getText().toString();
                String newDob = etDob.getText().toString();

                if (newUsername.isEmpty() || newPassword.isEmpty() || newDob.isEmpty()) {
                    Toast.makeText(UpdateActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update data in the database
                int rowsAffected = dbHandler.updateUser(userEmail, newUsername, newPassword, newDob);

                if (rowsAffected > 0) {
                    Toast.makeText(UpdateActivity.this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateActivity.this, "Failed to update data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUserData() {
        if (userEmail != null) {
            Cursor cursor = dbHandler.getUserData(userEmail);
            if (cursor != null && cursor.moveToFirst()) {
                // Column indexes must match your DbHandler
                // email=0, username=1, password=2, dob=3
                String username = cursor.getString(1);
                String dob = cursor.getString(3);

                etUsername.setText(username);
                etDob.setText(dob);
                // We don't show the old password for security reasons
                etPassword.setHint("Enter new password");
                cursor.close();
            }
        }
    }
}

