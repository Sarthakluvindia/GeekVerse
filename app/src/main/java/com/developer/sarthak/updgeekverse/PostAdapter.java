package com.developer.sarthak.updgeekverse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

/**
 * Created by ASUS on 23-04-2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostAdaperView> {

    private Context cnt;
    private List<Post> postList;

    public PostAdapter(Context cnt, List<Post> postList) {
        this.cnt = cnt;
        this.postList = postList;
    }

    @Override
    public PostAdaperView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(cnt);
        View view=inflater.inflate(R.layout.tweet_recview,null,true);
        return new PostAdaperView(view);
    }

    @Override
    public void onBindViewHolder(PostAdaperView holder, int position) {
        Post post=postList.get(position);
        holder.tweet.setText(post.getPost());
        holder.tweetname.setText(post.getTname());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostAdaperView extends RecyclerView.ViewHolder{

        TextView tweet,tweetname;
        ImageButton like,share;

        public PostAdaperView(View itemView) {
            super(itemView);
            tweet=(TextView)itemView.findViewById(R.id.simpletweet);
            like=(ImageButton)itemView.findViewById(R.id.like);
            share=(ImageButton)itemView.findViewById(R.id.share);
            tweetname=(TextView)itemView.findViewById(R.id.tweet_name);
            like.setTag(R.drawable.unlike_heart);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer resource = (Integer)like.getTag();
                    if(resource == R.drawable.like_heart){
                        like.setImageResource(R.drawable.unlike_heart);
                        like.setTag(R.drawable.unlike_heart);
                    }else {
                        like.setImageResource(R.drawable.like_heart);
                        like.setTag(R.drawable.like_heart);
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in=new Intent(Intent.ACTION_SEND);
                    in.setType("text/plain");
                    String body=tweet.getText().toString();
                    String sub=tweetname.getText().toString();
                    in.putExtra(Intent.EXTRA_SUBJECT,sub);
                    in.putExtra(Intent.EXTRA_TEXT,body);
                    view.getContext().startActivity(Intent.createChooser(in,"Share using..."));
                }
            });
        }
    }
}
