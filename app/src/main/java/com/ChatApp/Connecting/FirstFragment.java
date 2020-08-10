package com.ChatApp.Connecting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstFragment extends AppCompatActivity {
    Button sign_in,sign_up;
    FirebaseUser firebaseUser;




    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            Intent intent =  new Intent (FirstFragment.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);
        sign_in = findViewById(R.id.button_first);
        sign_up = findViewById(R.id.button_second);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstFragment.this, SecondFragment.class));

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstFragment.this, fragment_three.class));
            }
        });
    }
}
