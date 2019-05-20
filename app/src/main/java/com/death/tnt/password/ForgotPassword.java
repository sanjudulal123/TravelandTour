package com.death.tnt.password;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.death.tnt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    TextView textlink,back;
    EditText input_email_reset;
    AppCompatButton btn_reset_link;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        textlink = (TextView)findViewById(R.id.textlink);
        input_email_reset = (EditText)findViewById(R.id.input_email_reset);
        btn_reset_link = (AppCompatButton)findViewById(R.id.btn_reset_link);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_reset_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = input_email_reset.getText().toString();
                firebaseAuth = FirebaseAuth.getInstance();
                if (TextUtils.isEmpty(emailAddress)) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    textlink.setText("Link sent !\nPlease check your email");
                                    // do something when mail was sent successfully.
                                } else {
                                    // ...
                                }
                            }
                        });
            }
        });
    }
}
