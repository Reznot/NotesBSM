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

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private EditText passwordField;
    private Button btnUnlockNote;
    static String noteText;
    private String storedPassword;
    //Obecne haslo to 123

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.getIntExtra("passwordChanged", -1) == 0){
            Snackbar snackbarPasswordChanged = Snackbar.make(findViewById(android.R.id.content),"Twoje haslo zostalo zmienione", Snackbar.LENGTH_LONG);
            View sbView = snackbarPasswordChanged.getView();
            sbView.setBackgroundColor(Color.GREEN);
            snackbarPasswordChanged.show();
        }

        passwordField = findViewById(R.id.passwordField);
        btnUnlockNote = (Button) findViewById(R.id.unlock_button);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        String salt = sharedPreferences.getString("salt", null);
        System.out.println(salt);
        if (salt == null) {
            byte[] newSalt = getSalt();
            sharedPreferences.edit().putString("salt", new String(newSalt, StandardCharsets.UTF_8)).apply();
        }

        btnUnlockNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storedPassword = sharedPreferences.getString("password", null);
                if(storedPassword == null) {
                    sharedPreferences.edit().putString("password", passwordField.getText().toString()).apply();
                    launchActivity();
                } else if(storedPassword.equals(passwordField.getText().toString())) {
                    launchActivity();
                } else {
                    Snackbar snackbarWrongPassword = Snackbar.make(view,"Podano bledne haslo", Snackbar.LENGTH_LONG);
                    View sbView = snackbarWrongPassword.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbarWrongPassword.show();
                }
            }
        });
    }

    private void launchActivity() {
        passwordField.setText("");
        Intent intent = new Intent(this, note_screen.class);
        startActivity(intent);
    }

    private static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
