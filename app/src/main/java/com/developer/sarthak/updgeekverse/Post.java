package com.developer.sarthak.updgeekverse;

/**
 * Created by ASUS on 22-04-2018.
 */

public class Post {

    String id;
    String email;
    String post;
    String postid;
    String tname;

    Post(){

    }

    public Post(String id, String email, String post, String postid, String tname) {
        this.id=id;
        this.email = email;
        this.post = post;
        this.postid = postid;
        this.tname=tname;
    }

    public String getPostid() {
        return postid;
    }

    public String getTname() {
        return tname;
    }

    public String getId() {

        return id;
    }

    public String getEmail() {

        return email;
    }

    public String getPost() {
        return post;
    }
}
