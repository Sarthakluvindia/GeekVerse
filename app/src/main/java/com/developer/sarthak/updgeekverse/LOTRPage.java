package com.developer.sarthak.updgeekverse;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LOTRPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button post,feed,search,live;
    NavigationView nav;
    FloatingActionButton eye;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth auth;
    String personName,personEmail,personId;
    Uri personPhoto;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<Post> postList;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotrpage);
        String query="select * from signin_info";
        auth=FirebaseAuth.getInstance();
        postList=new ArrayList<>();
        post = (Button) findViewById(R.id.post_lotr);
        feed = (Button) findViewById(R.id.feed_lotr);
        eye=(FloatingActionButton)findViewById(R.id.eye);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(LOTRPage.this,LOTRTweetPage.class);
                startActivity(in);
            }
        });
        live = (Button) findViewById(R.id.live_lotr);
        search = (Button) findViewById(R.id.search_lotr);
        nav = (NavigationView) findViewById(R.id.nav_lotr);
        nav.setNavigationItemSelectedListener(this);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LOTRPage.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }
        TextView txtProfileName = (TextView) nav.getHeaderView(0).findViewById(R.id.user_name);
        txtProfileName.setText(personName);
        TextView txtProfileEmail = (TextView) nav.getHeaderView(0).findViewById(R.id.email);
        txtProfileEmail.setText(personEmail);
        ImageView img=(ImageView)nav.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.with(LOTRPage.this)
                .load(personPhoto).placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(img);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.setBackgroundColor(Color.rgb(214,215,158));
                post.setTextColor(Color.rgb(36,44,21));
                feed.setBackgroundColor(Color.rgb(36,44,21));
                feed.setTextColor(Color.rgb(214,215,158));
                search.setBackgroundColor(Color.rgb(36,44,21));
                search.setTextColor(Color.rgb(214,215,158));
                live.setBackgroundColor(Color.rgb(36,44,21));
                live.setTextColor(Color.rgb(214,215,158));
                postWithQuery();
            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed.setBackgroundColor(Color.rgb(214,215,158));
                feed.setTextColor(Color.rgb(36,44,21));
                post.setBackgroundColor(Color.rgb(36,44,21));
                post.setTextColor(Color.rgb(214,215,158));
                search.setBackgroundColor(Color.rgb(36,44,21));
                search.setTextColor(Color.rgb(214,215,158));
                live.setBackgroundColor(Color.rgb(36,44,21));
                live.setTextColor(Color.rgb(214,215,158));
                postWithoutQuery();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setBackgroundColor(Color.rgb(214,215,158));
                search.setTextColor(Color.rgb(36,44,21));
                feed.setBackgroundColor(Color.rgb(36,44,21));
                feed.setTextColor(Color.rgb(214,215,158));
                post.setBackgroundColor(Color.rgb(36,44,21));
                post.setTextColor(Color.rgb(214,215,158));
                live.setBackgroundColor(Color.rgb(36,44,21));
                live.setTextColor(Color.rgb(214,215,158));
                Integer resource = (Integer)R.drawable.lotr_background;
                Intent in=new Intent(LOTRPage.this,SearchActivity.class);
                in.putExtra("color","#242C15");
                in.putExtra("image",String.valueOf(resource));
                startActivity(in);
            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                live.setBackgroundColor(Color.rgb(214,215,158));
                live.setTextColor(Color.rgb(36,44,21));
                feed.setBackgroundColor(Color.rgb(36,44,21));
                feed.setTextColor(Color.rgb(214,215,158));
                search.setBackgroundColor(Color.rgb(36,44,21));
                search.setTextColor(Color.rgb(214,215,158));
                post.setBackgroundColor(Color.rgb(36,44,21));
                post.setTextColor(Color.rgb(214,215,158));
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        postWithQuery();
        super.onStart();
    }
    void postWithQuery(){
        String email=null;
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LOTRPage.this);
        if(acct!=null){
            email=acct.getEmail();
        }
        Query query= FirebaseDatabase.getInstance().getReference("posts")
                .orderByChild("email").equalTo(email);
        query.addValueEventListener(valueEventListener);
    }

    void postWithoutQuery(){
        databaseReference=FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(valueEventListener);
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
            PostAdapter adapter=new PostAdapter(LOTRPage.this,postList);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.nav_hp){

            // final MediaPlayer mp=MediaPlayer.create(StarWarsPage.this,R.raw.harry_potter);
            Intent in=new Intent(LOTRPage.this,HarryPotterPage.class);
            startActivity(in);
            //mp.start();
        }
        if(item.getItemId()==R.id.nav_got){
            Intent in=new Intent(LOTRPage.this,GOTPage.class);
            startActivity(in);
        }
        if(item.getItemId()==R.id.nav_lotr){
            Intent in=new Intent(LOTRPage.this,LOTRPage.class);
            startActivity(in);
        }
        if(item.getItemId()==R.id.nav_sw){
            // final MediaPlayer mp=MediaPlayer.create(FirstPage.this,R.raw.star_wars);
            Intent in=new Intent(LOTRPage.this,StarWarsPage.class);
            startActivity(in);
            // mp.start();
        }
        if(item.getItemId()==R.id.delete){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LOTRPage.this,LoginActivity.class));
                                    Toast.makeText(LOTRPage.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LOTRPage.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            startActivity(new Intent(LOTRPage.this,LoginActivity.class));
                            Toast.makeText(LOTRPage.this,"Revoked Access from your Google Account :( Do come back soon.",Toast.LENGTH_SHORT);
                        }
                    });
        }
        if(item.getItemId()==R.id.search_user){
            Intent in=new Intent(LOTRPage.this,SearchActivity.class);
            in.putExtra("color","#ff0000");
            in.putExtra("image","0");
            startActivity(in);
        }
        if(item.getItemId()==R.id.sign_out){
            auth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(i);
                        }
                    });
            startActivity(new Intent(LOTRPage.this,LoginActivity.class));
            Toast.makeText(LOTRPage.this,"Signed Out from the Account",Toast.LENGTH_SHORT);
        }
        return false;
    }
}
