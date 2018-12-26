package com.developer.sarthak.updgeekverse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LOTRTweetPage extends AppCompatActivity {
    InputMethodManager key;
    Button tweet;
    FloatingActionButton close;
    EditText lotr_tweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotrtweet_page);
        tweet=(Button)findViewById(R.id.lotr_post);
        close=(FloatingActionButton) findViewById(R.id.lotr_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key.hideSoftInputFromWindow(view.getWindowToken(),0);
                LOTRTweetPage.this.finish();
            }
        });
        lotr_tweet=(EditText)findViewById(R.id.lotr_tweet);
        TextWatcher tw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>140){
                    tweet.setEnabled(false);
                    lotr_tweet.setTextColor(Color.rgb(255,0,0));
                }
                else if(charSequence.length()<=140){
                    tweet.setEnabled(true);
                    lotr_tweet.setTextColor(Color.rgb(36,44,21));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        lotr_tweet.addTextChangedListener(tw);

        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LOTRTweetPage.this);
                if (acct != null) {
                    String personEmail = acct.getEmail();
                    String personId=acct.getId();
                    String personName=acct.getDisplayName();
                    String post=lotr_tweet.getText().toString();
                    try {
                        DatabaseReference dbpost = FirebaseDatabase.getInstance().getReference("posts");
                        String posiid = dbpost.push().getKey();
                        Post post1 = new Post(personId, personEmail, post, posiid, personName);
                        dbpost.child(posiid).setValue(post1);
                    }catch (Exception e){
                        Toast.makeText(LOTRTweetPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(LOTRTweetPage.this,"Message Flashed.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LOTRTweetPage.this,LOTRPage.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        key=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        key.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        key.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}
