package com.death.tnt.ADMIN.adminhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.death.tnt.R;

public class AdminLogin extends AppCompatActivity {
    EditText input_email, input_password;
    AppCompatButton btn_login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminlogin);
        input_email = (EditText)findViewById(R.id.input_email);
        input_password = (EditText)findViewById(R.id.input_password);
        btn_login =(AppCompatButton)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    if (email.equals("nepalyoges") && password.equals("@gmail.com")) {
                        Intent intent = new Intent(AdminLogin.this,AdminHome.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminLogin.this, "error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLogin.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
