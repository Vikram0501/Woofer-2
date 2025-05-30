package com.example.woofer;

public class Post {
    int post_id;
    int user_id;
    String content;

    String datetime;
    int likes;

    public Post(int p, int u, String c, String dt, int l){
        post_id = p;
        user_id = u;
        content = c;
        datetime = dt;
        likes = l;
    }
    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datatime) {
        this.datetime = datatime;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
