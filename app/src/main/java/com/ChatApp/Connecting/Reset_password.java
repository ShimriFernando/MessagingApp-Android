package com.ChatApp.Connecting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import static android.widget.Toast.makeText;

public class Reset_password extends AppCompatActivity {
    MaterialEditText send_email;
    Button reset_button;
    FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Reset password");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        send_email = findViewById(R.id.send_email);
        reset_button = findViewById(R.id.reset_button);
        firebaseAuth = FirebaseAuth.getInstance();
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Objects.requireNonNull(send_email.getText()).toString();
                if (email.equals("")) {
                    makeText(Reset_password.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(Reset_password.this, SecondFragment.class));
                            } else {
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                makeText(Reset_password.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });
    }
}