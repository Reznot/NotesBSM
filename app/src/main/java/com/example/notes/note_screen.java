package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.example.notes.MainActivity.hideKeyboard;
import static com.example.notes.MainActivity.noteText;

public class note_screen extends AppCompatActivity {

    private Button btnChangePassword;
    private Button btnSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        Snackbar sb = Snackbar.make(findViewById(android.R.id.content),"Prawidlowe haslo", Snackbar.LENGTH_SHORT);
        View sbView = sb.getView();
        sbView.setBackgroundColor(Color.GREEN);
        sb.show();


        final EditText note = (EditText) findViewById(R.id.noteText);
        final String currentPass = getIntent().getStringExtra("currentPass");

        noteText = sharedPreferences.getString("note", null);
        if (noteText == null) {
            note.setText(getString(R.string.note));
        } else {
            try {
                Encryption decryption = new Encryption(sharedPreferences.getString("salt", null),
                        currentPass,
                        sharedPreferences.getString("iv", null));
                note.setText(decryption.decrypt(noteText));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnSaveNote = findViewById(R.id.saveNote);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
            @Override
            public void onClick(View view) {
                hideKeyboard(note_screen.this);
                noteText = note.getText().toString();
                System.out.println(currentPass);
                try {
                    Encryption encryption = new Encryption(sharedPreferences.getString("salt", null),
                            currentPass,
                            sharedPreferences.getString("iv", null));
                    String encryptedNote = encryption.encrypt(noteText);
                    sharedPreferences.edit().putString("note", encryptedNote).apply();
                    Snackbar sb = Snackbar.make(findViewById(android.R.id.content),"Zapisano", Snackbar.LENGTH_SHORT);
                    View sbView = sb.getView();
                    sbView.setBackgroundColor(Color.GREEN);
                    sb.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("zapis notatki " + e);
                    Snackbar sb = Snackbar.make(view,"Nie powiodlo sie", Snackbar.LENGTH_LONG);
                    View sbView = sb.getView();
                    sbView.setBackgroundColor(Color.RED);
                    sb.show();
                }
            }
        });

        btnChangePassword = (Button) findViewById(R.id.changePasswordBtn);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
    }

    private void launchActivity() {
        Intent intent = new Intent(this, PasswordChangeActivity.class);
        startActivity(intent);
    }
}
