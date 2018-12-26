package com.developer.sarthak.updgeekverse;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StarWarsPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button post,feed,search,live;
    NavigationView nav;
    FloatingActionButton death_star;
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
        setContentView(R.layout.activity_star_wars_page);
        auth=FirebaseAuth.getInstance();
        Intent in=getIntent();
        postList=new ArrayList<>();
        post = (Button) findViewById(R.id.post);
        feed = (Button) findViewById(R.id.feed);
        live = (Button) findViewById(R.id.live);
        death_star=(FloatingActionButton) findViewById(R.id.death_star);
        death_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(StarWarsPage.this,StarWarsTweetPage.class);
                startActivity(in);
            }
        });
        search = (Button) findViewById(R.id.search);
        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(StarWarsPage.this);
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
        Picasso.with(StarWarsPage.this)
                .load(personPhoto).placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(img);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.setBackgroundColor(Color.rgb(0, 0, 0));
                post.setTextColor(Color.rgb(247, 175, 66));
                feed.setBackgroundColor(Color.rgb(247, 175, 66));
                feed.setTextColor(Color.rgb(0,0,0));
                search.setBackgroundColor(Color.rgb(247, 175, 66));
                search.setTextColor(Color.rgb(0,0,0));
                live.setBackgroundColor(Color.rgb(247, 175, 66));
                live.setTextColor(Color.rgb(0,0,0));
                postWithQuery();
            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed.setBackgroundColor(Color.rgb(0, 0, 0));
                feed.setTextColor(Color.rgb(247, 175, 66));
                post.setBackgroundColor(Color.rgb(247, 175, 66));
                post.setTextColor(Color.rgb(0,0,0));
                search.setBackgroundColor(Color.rgb(247, 175, 66));
                search.setTextColor(Color.rgb(0,0,0));
                live.setBackgroundColor(Color.rgb(247, 175, 66));
                live.setTextColor(Color.rgb(0,0,0));
                postWithoutQuery();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setBackgroundColor(Color.rgb(0, 0, 0));
                search.setTextColor(Color.rgb(247, 175, 66));
                feed.setBackgroundColor(Color.rgb(247, 175, 66));
                feed.setTextColor(Color.rgb(0,0,0));
                post.setBackgroundColor(Color.rgb(247, 175, 66));
                post.setTextColor(Color.rgb(0,0,0));
                live.setBackgroundColor(Color.rgb(247, 175, 66));
                live.setTextColor(Color.rgb(0,0,0));
                Integer resource = (Integer)R.drawable.st_background;
                Intent in=new Intent(StarWarsPage.this,SearchActivity.class);
                in.putExtra("color","#F7AF42");
                in.putExtra("image",String.valueOf(resource));
                startActivity(in);
            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                live.setBackgroundColor(Color.rgb(0, 0, 0));
                live.setTextColor(Color.rgb(247, 175, 66));
                feed.setBackgroundColor(Color.rgb(247, 175, 66));
                feed.setTextColor(Color.rgb(0,0,0));
                search.setBackgroundColor(Color.rgb(247, 175, 66));
                search.setTextColor(Color.rgb(0,0,0));
                post.setBackgroundColor(Color.rgb(247, 175, 66));
                post.setTextColor(Color.rgb(0,0,0));
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

    void postWithQuery(){
        String email=null;
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(StarWarsPage.this);
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

    void userWithQuery(){
        databaseReference=FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
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
            PostAdapter adapter=new PostAdapter(StarWarsPage.this,postList);
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
            Intent in=new Intent(StarWarsPage.this,HarryPotterPage.class);
            startActivity(in);
            //mp.start();
        }
        if(item.getItemId()==R.id.nav_got){
            Intent in=new Intent(StarWarsPage.this,GOTPage.class);
            startActivity(in);
        }
        if(item.getItemId()==R.id.nav_lotr){
            Intent in=new Intent(StarWarsPage.this,LOTRPage.class);
            startActivity(in);
        }
        if(item.getItemId()==R.id.nav_sw){
            // final MediaPlayer mp=MediaPlayer.create(FirstPage.this,R.raw.star_wars);
            Intent in=new Intent(StarWarsPage.this,StarWarsPage.class);
            startActivity(in);
            // mp.start();
        }
        if(item.getItemId()==R.id.search_user){
            Intent in=new Intent(StarWarsPage.this,SearchActivity.class);
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
            startActivity(new Intent(StarWarsPage.this,LoginActivity.class));
            Toast.makeText(StarWarsPage.this,"Signed Out from the Account",Toast.LENGTH_SHORT);
        }
        if(item.getItemId()==R.id.delete){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(StarWarsPage.this,LoginActivity.class));
                                    Toast.makeText(StarWarsPage.this, "Your profile is deleted :( Create a account now!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StarWarsPage.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            startActivity(new Intent(StarWarsPage.this,LoginActivity.class));
                            Toast.makeText(StarWarsPage.this,"Revoked Access from your Google Account :( Do come back soon.",Toast.LENGTH_SHORT);
                        }
                    });
        }
        if(item.getItemId()==R.id.edit_profile){
            Intent in=new Intent(StarWarsPage.this,EditProfile.class);
            startActivity(in);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StarWarsPage.this);
        alertDialogBuilder.setMessage("Are you sure yoy want to exit.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppExit();
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(StarWarsPage.this,StarWarsPage.class));
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void AppExit()
    {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
