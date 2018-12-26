package com.developer.sarthak.updgeekverse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
EditText search;
LinearLayout layout,layout1;
RecyclerView recyclerView;
    List<Post> postList;
    DatabaseReference databaseReference;
    String color,image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent in=getIntent();
        color=in.getStringExtra("color");
        image=in.getStringExtra("image");
        search=(EditText)findViewById(R.id.edsearch);
        layout=(LinearLayout)findViewById(R.id.llsearch);
        layout.setBackgroundColor(Color.parseColor(color));
        layout1=(LinearLayout)findViewById(R.id.llrec);
        if(image!="0") {
            if (Integer.parseInt(image) == R.drawable.st_background) {
                layout1.setBackgroundResource(R.drawable.st_background);
            } else if (Integer.parseInt(image) == R.drawable.lotr_background) {
                layout1.setBackgroundResource(R.drawable.lotr_background);
            } else if (Integer.parseInt(image) == R.drawable.hp_background) {
                layout1.setBackgroundResource(R.drawable.hp_background);
            } else if (Integer.parseInt(image) == R.drawable.got_texture) {
                layout1.setBackgroundResource(R.drawable.got_texture);
            }
        }
        else
            layout1.setBackgroundColor(Color.WHITE);
        recyclerView=(RecyclerView)findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList=new ArrayList<>();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                postWithQuery();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    void postWithQuery(){
        String se=search.getText().toString();
        Query query= FirebaseDatabase.getInstance().getReference("posts")
                .orderByChild("tname")
                .startAt(se.toUpperCase())
                .endAt(se+"\uf8ff");
        query.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            postList.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot postsnap:dataSnapshot.getChildren()){
                    Post post=postsnap.getValue(Post.class);
                    postList.add(post);
                }
            }
            PostAdapter adapter=new PostAdapter(SearchActivity.this,postList);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    void postWithoutQuery(){
        databaseReference=FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        postWithoutQuery();
    }
}
