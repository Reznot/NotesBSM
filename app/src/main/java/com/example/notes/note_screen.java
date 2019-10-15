package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class note_screen extends AppCompatActivity {

    private Button btnChangePassword;
    private Button btnSaveNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);


        final EditText note = (EditText) findViewById(R.id.noteText);

        note.setText(MainActivity.noteText);

        btnSaveNote = findViewById(R.id.saveNote);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.noteText = note.getText().toString();
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
