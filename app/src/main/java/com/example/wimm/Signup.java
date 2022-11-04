package com.example.wimm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    EditText editTextemail, editTextpass, editTextrepass;
    Button signup;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextemail = findViewById(R.id.signup_email);
        editTextpass = findViewById(R.id.pass);
        editTextrepass = findViewById(R.id.repass);
        signup = findViewById(R.id.btnsignup);

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(view -> {
            createUser();
        });
    }

    private void createUser() {
        String email = editTextemail.getText().toString();
        String pass = editTextpass.getText().toString();
        String repass = editTextrepass.getText().toString();

        if (TextUtils.isEmpty(email)) {
            editTextemail.setError("Email không để trống!");
            editTextemail.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            editTextpass.setError("Mật khẩu không để trống!");
            editTextpass.requestFocus();
        } else if (TextUtils.isEmpty(repass)) {
            editTextrepass.setError("Lặp lại mật khẩu!");
            editTextrepass.requestFocus();
        } else if (pass.equals(repass)) {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                        Log.e(Signup.class.getSimpleName(),editTextemail.getText().toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        HashMap object = new HashMap();
                        object.put("email", editTextemail.getText().toString());
                        databaseReference.child("users").push().setValue(object);
                        Toast.makeText(Signup.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, Login.class));
                    } else {
                        Toast.makeText(Signup.this, "Đăng ký không thành công: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(Signup.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
        }
    }
}