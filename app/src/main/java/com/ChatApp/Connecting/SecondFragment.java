package com.ChatApp.Connecting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import static java.util.Objects.requireNonNull;

public class SecondFragment extends AppCompatActivity {
    MaterialEditText email,password;
    Button signin_button;
    FirebaseAuth auth;
    TextView forgot_password;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin_button = findViewById(R.id.signin_button);
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondFragment.this, Reset_password.class));
            }
        });
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = (requireNonNull(email.getText())).toString();
                String txt_password = (requireNonNull(password.getText())).toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(SecondFragment.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    Task<AuthResult> authResultTask = auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SecondFragment.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }
}