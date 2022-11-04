package com.example.wimm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText editTextemail, editTextpassword;
    Button btnlogin;
    TextView register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextemail = (EditText) findViewById(R.id.email);
        editTextpassword = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        register = (TextView) findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, Signup.class));
        });

        btnlogin.setOnClickListener(view -> {
            loginUser();
        });
    }

    private  void loginUser(){
        String email = editTextemail.getText().toString();
        String pass = editTextpassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            editTextemail.setError("Email không được để trống!");
            editTextemail.requestFocus();
        } else  if(TextUtils.isEmpty(pass)){
            editTextpassword.setError("Mật khẩu không được để trống!");
            editTextpassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }else{
                        Toast.makeText(Login.this, "Đăng nhập không thành công: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}