package com.ChatApp.Connecting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class fragment_three extends AppCompatActivity {
    MaterialEditText username,email,password;
    Button signup_button;
    FirebaseAuth auth;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_three);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.Username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup_button = findViewById(R.id.signup_button);
        auth = FirebaseAuth.getInstance();

        signup_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String txt_username_ = Objects.requireNonNull(username.getText()).toString();
                String txt_email_ = Objects.requireNonNull(email.getText()).toString();
                String txt_password_ = Objects.requireNonNull(password.getText()).toString();
                if (TextUtils.isEmpty(txt_username_) || TextUtils.isEmpty(txt_email_) || TextUtils.isEmpty(txt_password_))
                {
                    Toast.makeText(fragment_three.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if (txt_password_.length() < 6){
                    Toast.makeText(fragment_three.this, "Password must be atleast six characters", Toast.LENGTH_SHORT).show();
                }else{
                    signup_button(txt_username_,txt_email_,txt_password_);
                }


            }
        });




    }
    //add users to database
    public void signup_button(final String username, final String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser firebaseUser = auth.getCurrentUser();
                                    assert firebaseUser !=null;
                                    String userid = firebaseUser.getUid();
                                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", userid);
                                    hashMap.put("username", username);
                                    hashMap.put("imageURL", "default");
                                    hashMap.put("status", "offline");
                                    hashMap.put("search", username.toLowerCase());
                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(fragment_three.this, MainActivity.class);
                                                if (BuildConfig.DEBUG) {
                                                    throw new AssertionError("Assertion failed");
                                                }

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(fragment_three.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                                            }


                                        }


                                    });
                                }
                            }
                        });




    }
}
