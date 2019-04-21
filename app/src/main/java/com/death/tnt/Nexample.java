package com.death.tnt;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.death.tnt.home.DashboardActivity;
import com.death.tnt.password.ForgotPassword;
import com.death.tnt.signup.EmailSignupModule;
import com.death.tnt.signup.Signup;
import com.death.tnt.tabHost.ActivityTabHost;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * this is the main class
 * where login is used
 *
 *
 * to check the internet connection
 * protected boolean isOnline() {
 *         ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 *         NetworkInfo netInfo = cm.getActiveNetworkInfo();
 *         if (netInfo != null && netInfo.isConnected()) {
 *             return true;
 *         } else {
 *             return false;
 *         }
 *     }
 * to generate hashkey for facebook login
 * try {
 *             PackageInfo info = getPackageManager().getPackageInfo(
 *                     "com.death.tnt",
 *                     PackageManager.GET_SIGNATURES);
 *             for (Signature signature : info.signatures) {
 *                 MessageDigest md = MessageDigest.getInstance("SHA");
 *                 md.update(signature.toByteArray());
 *                 Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
 *             }
 *         } catch (PackageManager.NameNotFoundException e) {
 *
 *         } catch (NoSuchAlgorithmException e) {
 *
 *         }
 *
 *
 */

public class Nexample extends AppCompatActivity {
    final static int for_permission = 1;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    //facebook_login
    LoginButton loginButton;
    CallbackManager mCallbackManager;
    String TAG = "Hey";
    String name, userid, profilepictureurl, phone;
    DatabaseReference databaseReference;

    //email_login
    EditText input_email, input_password;
    AppCompatButton btn_login;
    TextView link_signup, forgot_password;

    //progress dialog
    ProgressDialog progressDialog;

    AlertDialog alertDialog;

    ArrayList<DataModule> a1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nexample);

        /**
         Run-Time Permission
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    for_permission);
        }
        //checking internet connnection
//        MainActivity in = new MainActivity();
//        if (in.isOnline()){
//            Toast.makeText(in, "code works and the internet is connected", Toast.LENGTH_SHORT).show();
//        }

        /**
         * email and password field
         */
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        link_signup = (TextView) findViewById(R.id.link_signup);
        forgot_password = (TextView) findViewById(R.id.forgot_password);

        /**
         * on clicking forgot_password
         * send to forgot_password.xml
         */
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nexample.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        /**
         * on clicking link_signup
         * send to signup.xml
         */
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nexample.this, Signup.class);
                startActivity(intent);
            }
        });

        /**
         * Login Click
         */
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    //show progress dialog
                    progressDialog = new ProgressDialog(Nexample.this);
                    progressDialog.setTitle("Logging in...");
                    progressDialog.setMessage("please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    /**
                     * now sign in with email and password using firebase
                     */


                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Nexample.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        checkIfEmailVerified();
                                    }
                                }
                            });
//                    mAuth.signInWithEmailAndPassword(email, password)
//                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    progressDialog.dismiss();
//                                    SharedPreferences sp = getSharedPreferences("LoginState", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sp.edit();
//                                    editor.putBoolean("state", true);
//                                    editor.apply();
//                                    databaseReference = FirebaseDatabase
//                                            .getInstance()
//                                            .getReference()
//                                            .child("user")
//                                            .child(userid);
//                                    databaseReference.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot d1 : dataSnapshot.getChildren()) {
//                                                EmailSignupModule m = d1.getValue(EmailSignupModule.class);
//                                                a1.add(m);
//                                            }
//                                            for (int i = 0; i < a1.size(); i++) {
//                                                if (mAuth.getCurrentUser().getUid().equals(a1.get(i).getEmailuserid())) {
//                                                    Log.e("Auth", "called");
//                                                    progressDialog.dismiss();
//                                                    Intent intent = new Intent(Nexample.this, ActivityTabHost.class);
//                                                    startActivity(intent);
//                                                    break;
//                                                } else {
//                                                    progressDialog.dismiss();
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            progressDialog.dismiss();
//                                            Log.e("Auth", "cancled");
//                                        }
//                                    });
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(Nexample.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });

                } else {
                    //show Alert Message
//                    alertDialog = new AlertDialog.Builder(Nexample.this).create();
//                    alertDialog.setTitle("Alert!");
//                    alertDialog.setMessage("Fields can't be empty");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK"
//                            ,new DialogInterface.OnClickListener(){
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
                    btn_login.setEnabled(false);
                }
            }
        });


        /**
         * facebook login
         */
        loginButton = (LoginButton) findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i(TAG, "Hello" + loginResult.getAccessToken().getToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Nexample.this, "cancled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Nexample.this, "" + error, Toast.LENGTH_LONG).show();
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Toast.makeText(Nexample.this, "STATE CHANGED", Toast.LENGTH_SHORT).show();
            }
        };


    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (user.isEmailVerified()) {
            progressDialog.dismiss();
            finish();
            databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("user")
                    .child(userid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
//                        EmailSignupModule m = d1.getValue(EmailSignupModule.class);
                        DataModule dataModule = d1.getValue(DataModule.class);
                        a1.add(dataModule);
                    }
                    for (int i = 0; i < a1.size(); i++) {
                        if (mAuth.getCurrentUser().getUid().equals(a1.get(i).getUserid())) {
                            Log.e("Auth", "called");
                            progressDialog.dismiss();
                            Intent intent = new Intent(Nexample.this, ActivityTabHost.class);
                            startActivity(intent);
                            break;
                        } else {
                            Toast.makeText(Nexample.this, "no user Account", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Log.e("Auth", "cancled");
                }
            });
        } else {
            Toast.makeText(this, "Please verify your email First", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(Nexample.this, "Success",
//                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                name = user.getDisplayName();
                                userid = user.getUid();
                                profilepictureurl = user.getPhotoUrl().toString();
                                phone = user.getPhoneNumber();

                                DataModule dm = new DataModule();
                                dm.setName(name);
                                dm.setUserid(userid);
                                dm.setProfilepictureurl(profilepictureurl);
                                dm.setPhone(phone);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                        .child("user").child(userid);
                                databaseReference.setValue(dm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Nexample.this, "push to database and sign in success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Nexample.this, DashboardActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Nexample.this, "" + e, Toast.LENGTH_LONG).show();
                                    }
                                });

//                    Toast.makeText(Nexample.this, "" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Nexample.this, "something went wrong \n Please Try Again", Toast.LENGTH_LONG).show();
                            }
//                            Intent intent = new Intent(Nexample.this, DashboardActivity.class);
//                            startActivity(intent);

                        } else {
                            Toast.makeText(Nexample.this, "Authentication error",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    @Override
    protected void onRestart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Nexample.this, DashboardActivity.class);
            startActivity(intent);
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Nexample.this, DashboardActivity.class);
            startActivity(intent);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp2 = getSharedPreferences("LoginState", MODE_PRIVATE);
        boolean mystate = sp2.getBoolean("state", false);
        if (mystate == true) {
            Intent i = new Intent(Nexample.this, DashboardActivity.class);
            startActivity(i);
        }
    }

    /**
     * Add Method onBackPressed
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case for_permission: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                }

                return;
            }
        }
    }

}