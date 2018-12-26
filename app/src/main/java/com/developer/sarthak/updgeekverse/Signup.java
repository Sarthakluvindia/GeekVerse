package com.developer.sarthak.updgeekverse;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    private VideoView mVideoView;
    String[] fandoms={
            "Choose Your Fandom!!","Harry Potter", "Star Wars", "Lord of the Rings", "Game of Thrones"
    };
    String email_regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    String fan;
    Button signup;
    Spinner sp;
    EditText fname,lname,pass,repass,email;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=(Button)findViewById(R.id.sign_up);
        fname=(EditText)findViewById(R.id.f_name);
        lname=(EditText)findViewById(R.id.l_name);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        repass=(EditText)findViewById(R.id.re_pass);
        auth = FirebaseAuth.getInstance();
        mVideoView = (VideoView) findViewById(R.id.bgVideoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login);

        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        sp=(Spinner)findViewById(R.id.spin);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,fandoms);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aa);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fname.getText().toString().trim().equalsIgnoreCase("")){
                    fname.setError("Please enter your first name");
                }
                else if(lname.getText().toString().trim().equalsIgnoreCase("")){
                    lname.setError("Please enter your last name");
                }
                else if(email.getText().toString().trim().equalsIgnoreCase("")){
                    email.setError("Please enter your email");
                }
                else if(pass.getText().toString().trim().equalsIgnoreCase("")){
                    pass.setError("Please enter your password");
                }
                else if(repass.getText().toString().trim().equalsIgnoreCase("")){
                    repass.setError("Please confirm your password");
                }
                else if(!repass.getText().toString().equals(pass.getText().toString())){
                    pass.setError("Please check your password");
                    repass.setError("Please check your password");
                }
                else if(!email.getText().toString().matches(email_regex)) {
                    email.setError("Please enter a valid email");
                }
                else {
                    //Firebase Database
                    String firstn=fname.getText().toString();
                    String lastn=lname.getText().toString();
                    String e_mail=email.getText().toString();
                    fan=sp.getSelectedItem().toString();
                    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("users");
                    String userId = mDatabase.push().getKey();
                    User user = new User(firstn, lastn, e_mail, fan, userId);
                    mDatabase.child(userId).setValue(user);

                    auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Signup.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(Signup.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pass.setError(null);
            }
        });
        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fname.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fname.setError(null);
            }
        });
        lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lname.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lname.setError(null);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email.setError(null);
            }
        });
        repass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                repass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                repass.setError(null);
            }
        });
    }
}
