package com.death.tnt.signup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.death.tnt.DataModule;
import com.death.tnt.Nexample;
import com.death.tnt.R;
import com.death.tnt.home.DashboardActivity;
import com.death.tnt.home.GridComments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    //input fields and button
    EditText input_email_signup, input_password_signup, first_name, last_name;
    AppCompatButton btn_signup;

    //progress dialog
    ProgressDialog progressDialog;

    //alert dialog
    AlertDialog alertDialog;

    String firstname, lastname, email, password, userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        input_email_signup = (EditText) findViewById(R.id.input_email_signup);
        input_password_signup = (EditText) findViewById(R.id.input_password_signup);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        /**
         * btn_signup click
         */
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = first_name.getText().toString();
                email = input_email_signup.getText().toString();
                password = input_password_signup.getText().toString();
                lastname = last_name.getText().toString();
                if (!TextUtils.isEmpty(firstname)
                        && !TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(lastname)) {
                    if (first_name.length() >= 2) {
                        if (email.contains("@gmail.com") || email.contains("@yahoo.com")) {
                            if (password.length() > 6) {
                                if (last_name.length() >= 3) {


                                    progressDialog = new ProgressDialog(Signup.this);
                                    progressDialog.setTitle("Registering...");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    /**
                                     * create user with email and password using firebase
                                     */

                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (!task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        sendVerificationEmailToUser();
                                                    }
                                                }
                                            });


//                                    mAuth.createUserWithEmailAndPassword(email, password)
//                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                                @Override
//                                                public void onSuccess(AuthResult authResult) {
//                                                    progressDialog.dismiss();
//                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                                                    userID = currentUser.getUid();
//                                                    databaseReference = FirebaseDatabase
//                                                            .getInstance()
//                                                            .getReference()
//                                                            .child("user").child(userID);
//                                                    postData(databaseReference);
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(Signup.this, "Exception : " + e, Toast.LENGTH_SHORT).show();
//                                        }
//                                    });


                                } else {
                                    progressDialog.dismiss();
                                    last_name.setError("At least 5 characters");
                                }
                            } else {
                                Toast.makeText(Signup.this, "password must be 6 or more characters", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Signup.this, "incorrect email", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        first_name.setError("At least 5 characters");
                    }


                } else {

                    btn_signup.setEnabled(false);
                }

            }
        });

    }

    private void sendVerificationEmailToUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();


                    final String use = user.getUid();
                    databaseReference = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("user");

                    final DataModule dataModule = new DataModule();
                    dataModule.setFirstname(firstname);
                    dataModule.setLastname(lastname);
                    dataModule.setEmail(email);
                    dataModule.setUserid(userID);
                    dataModule.setName(firstname + " " + lastname);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(use).exists()) {
                                Log.e("Old user", "user already present in database");
                                Intent intent = new Intent(Signup.this, DashboardActivity.class);
                                startActivity(intent);
                            } else{
                                Log.e("new User","add user to database");
                                databaseReference.child(use).setValue(dataModule).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Signup.this, "push to database and sign in success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Signup.this, DashboardActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Signup.this, "" + e, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    postData(databaseReference);
                    //startActivity(new Intent(Signup.this, Nexample.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    //restart this activity
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                }
            }
        });

    }

//    public void postData(DatabaseReference databaseReference) {
//        DataModule dataModule = new DataModule();
//        dataModule.setFirstname(firstname);
//        dataModule.setLastname(lastname);
//        dataModule.setEmail(email);
//        dataModule.setUserid(userID);
//        dataModule.setName(firstname + " " + lastname);
//        EmailSignupModule emailSignupModule = new EmailSignupModule();
//        emailSignupModule.setFirstname(firstname);
//        emailSignupModule.setLastname(lastname);
//        emailSignupModule.setEmail(email);
//        emailSignupModule.getEmailuserid(userID);
//        databaseReference.setValue(dataModule).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.e("Database", "push success");
//                Intent intent = new Intent(Signup.this, Nexample.class);
//                startActivity(intent);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("Database", "Error : " + e);
//            }
//        });
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

