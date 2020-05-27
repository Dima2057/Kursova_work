package com.example.kursova_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

/**
 * RegisterActivity - data processing for authorization (mail) of a new user.
 * @version 1.0
 */
public class RegisterActivity extends AppCompatActivity {

    EditText e6_email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e6_email = (EditText) findViewById(R.id.editText6);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog (this);
    }

    /**
     *  Processing received email, authentication, switching to PasswordActivity
     * @see PasswordActivity
     * */
    public void goToPasswordActivity(View v){
        dialog.setMessage("Checking email address");
        dialog.show();

        auth.fetchSignInMethodsForEmail(e6_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                    if (task.isSuccessful()){
                        dialog.dismiss();
                        boolean check = !task.getResult().getSignInMethods().isEmpty();
                        auth.getFirebaseAuthSettings();

                        if (!check){
                            Intent myIntent = new Intent(RegisterActivity.this, PasswordActivity.class);
                            myIntent.putExtra("email",e6_email.getText().toString());
                            startActivity(myIntent);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });
    }
}
