package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class PasswordChangeActivity extends AppCompatActivity {

    private EditText prevPasswwordField;
    private EditText newPasswordField;
    private Button btnChangePassword;
    private String storedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        prevPasswwordField = findViewById(R.id.prevPassword);
        newPasswordField = findViewById(R.id.newPassowrd);
        btnChangePassword = findViewById(R.id.confirmPasswordChangeBtn);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                storedPassword = sharedPreferences.getString("password", null);
                if (prevPasswwordField.getText().toString().equals(storedPassword)) {
                    sharedPreferences.edit().putString("password", newPasswordField.getText().toString()).apply();
                    passwordChanged();
                } else {
                    Snackbar snackbarWrongPassword = Snackbar.make(view,"Podano bledne obecne haslo", Snackbar.LENGTH_LONG);
                    View sbView = snackbarWrongPassword.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbarWrongPassword.show();
                }
            }
        });
    }

    private void passwordChanged() {
        prevPasswwordField.setText("");
        newPasswordField.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("passwordChanged", 0);
        startActivity(intent);
    }
}
