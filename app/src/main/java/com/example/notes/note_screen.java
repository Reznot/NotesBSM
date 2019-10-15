package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;

import static com.example.notes.MainActivity.noteText;

public class note_screen extends AppCompatActivity {

    private Button btnChangePassword;
    private Button btnSaveNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);


        final EditText note = (EditText) findViewById(R.id.noteText);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        noteText = sharedPreferences.getString("note", null);
        if (noteText == null) {
            note.setText(getString(R.string.note));
        } else {
            note.setText(noteText);
        }

        btnSaveNote = findViewById(R.id.saveNote);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteText = note.getText().toString();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

                sharedPreferences.edit().putString("note", noteText).apply();
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
