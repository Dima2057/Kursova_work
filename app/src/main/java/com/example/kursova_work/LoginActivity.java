package com.example.kursova_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.invoke.ConstantCallSite;

/**
 * LoginActivity - The class is responsible for user authorization,
 * i.e. sending data for authorization to the server.
 * @version 1.0
 *
 */
public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1, e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText3);
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Sending username/password to the server firebase - authorization.
     * */
    public void Login(View v){
        auth.signInWithEmailAndPassword(e1.getText().toString(), e2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"User logged in successfully", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(LoginActivity.this, MapActivity.class);
                        myIntent.putExtra("email", e1.getText().toString());
                        startActivity(myIntent);

                    } else {
                        Toast.makeText(getApplicationContext(),"Wrong email or password", Toast.LENGTH_LONG).show();
                    }
            }
        });
    }
}
