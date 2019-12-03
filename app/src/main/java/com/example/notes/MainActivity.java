package com.example.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.StandardCharsets;
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
        String iv = sharedPreferences.getString("iv", null);
        if (salt == null) {
            byte[] newSalt = getSalt();
            sharedPreferences.edit().putString("salt", new String(newSalt, StandardCharsets.UTF_8)).apply();
            byte[] newIv = getIv();
            sharedPreferences.edit().putString("iv", new String(newIv, StandardCharsets.UTF_8)).apply(); // TODO Mozna jeszcze to jakos ulepszyc
        }

        btnUnlockNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(MainActivity.this);
                storedPassword = sharedPreferences.getString("password", null);
                try {
                    String passInput = passwordField.getText().toString();
                    if(storedPassword == null) {
                        Encryption encryption = new Encryption(sharedPreferences.getString("salt", null),
                                passInput,
                                sharedPreferences.getString("iv", null));
                        sharedPreferences.edit()
                                .putString("password", encryption.encrypt(passInput))
                                .apply();
                        launchActivity(passInput);
                    } else {
                        Encryption encryption = new Encryption(sharedPreferences.getString("salt", null),
                                passInput,
                                sharedPreferences.getString("iv", null));
                        String storedPasswordDecrypted = encryption.decrypt(storedPassword);
                        if (storedPasswordDecrypted.equals(passInput)) {
                            launchActivity(passInput);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbarWrongPassword = Snackbar.make(view,"Podano bledne haslo", Snackbar.LENGTH_LONG);
                    View sbView = snackbarWrongPassword.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbarWrongPassword.show();
                }
            }
        });
    }

    private void launchActivity(String passInput) {
        Intent intent = new Intent(this, note_screen.class);
        intent.putExtra("currentPass", passInput);
        storedPassword = null;
        passwordField.setText("");
        startActivity(intent);
    }

    private static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] getIv() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
