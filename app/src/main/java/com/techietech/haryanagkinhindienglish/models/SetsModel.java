package com.techietech.haryanagkinhindienglish.models;

public class SetsModel {

    private String imageUrl,title,levelNo;


    public SetsModel(){
        //for FireBase
    }

    public SetsModel(String imageUrl, String title, String levelNo ) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.levelNo=levelNo;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(String levelNo) {
        this.levelNo = levelNo;
    }
}
