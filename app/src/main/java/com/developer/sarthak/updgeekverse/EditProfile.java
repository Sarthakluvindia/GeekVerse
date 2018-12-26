package com.developer.sarthak.updgeekverse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    CircleImageView civ;
    EditText name,tag,email;
    Spinner sp;
    ArrayAdapter aa;
    Button edit;
    String userEmail,userId;
    String[] fandoms={
            "Choose Your Fandom!!","Harry Potter", "Star Wars", "Lord of the Rings", "Game of Thrones"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name=(EditText)findViewById(R.id.ep_name);
        tag=(EditText)findViewById(R.id.ep_tag);
        email=(EditText)findViewById(R.id.ep_email);
        sp=(Spinner)findViewById(R.id.ep_fandom);
        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,fandoms);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aa);
        civ=(CircleImageView)findViewById(R.id.imageView);
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in,0);
            }
        });
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    userEmail = firebaseUser.getEmail();
                }
            }
        };
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user=dataSnapshot.getValue(User.class);
                name.setText(user.getFname()+" "+user.getLname());
                email.setText(user.getEmail());
                sp.setSelection(aa.getPosition(user.getFandom()));
                userId=user.getUserId();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        edit=(Button)findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname=name.getText().toString();
                String firstn="";
                String lastn="";
                if(fullname.split("\\w+").length>1){

                    lastn = fullname.substring(fullname.lastIndexOf(" ")+1);
                    firstn = fullname.substring(0, fullname.lastIndexOf(' '));
                }
                else{
                    firstn = fullname;
                }

                String e_mail=email.getText().toString();
                String fan=sp.getSelectedItem().toString();
                DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("users").child(userId);
                User user=new User(firstn,lastn,e_mail,fan,userId);
                databaseReference1.setValue(user);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==0){
                Picasso.with(EditProfile.this)
                        .load(data.getData()).placeholder(R.drawable.ic_account_circle_black_24dp)
                        .error(R.drawable.ic_error_outline_black_24dp)
                        .into(civ);
            }
        }else {
            Toast.makeText(EditProfile.this,"Sorry, something went wrong!",Toast.LENGTH_SHORT).show();
        }
    }
}
