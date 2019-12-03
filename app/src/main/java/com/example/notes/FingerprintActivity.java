package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.Executor;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;

public class FingerprintActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt = null;
    private Executor executor = new MainThreadExecutor();

    private BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                biometricPrompt.cancelAuthentication();
            toast(errString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            launchActivity();
            toast("Rozpoznano odcisk palca");
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            toast("Nie udalo sie rozpoznac odcisku palca");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        if (biometricPrompt == null)
            biometricPrompt = new BiometricPrompt(this, executor, callback);

        findViewById(R.id.selectFingerPrintButton).setOnClickListener(view -> {
            BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
            biometricPrompt.authenticate(promptInfo);
        });
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private BiometricPrompt.PromptInfo buildBiometricPrompt() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Odblokuj")
                .setSubtitle("Odblokuj swoja sekretna notatke")
                .setDescription("Dotknij czytnika linii papilarnych")
                .setNegativeButtonText("Anuluj")
                .build();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
