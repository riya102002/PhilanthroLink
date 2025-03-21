package com.example.philanthrolink;

public class NGOReviewHelper {
    String NGOID, review;
    public NGOReviewHelper(String review, String NGOID){
        this.review = review;
        this.NGOID = NGOID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getNGOID() { return NGOID; }

    public void setNGOID(String volunteerID) { this.NGOID = NGOID; }

    public NGOReviewHelper(){
    }
}
