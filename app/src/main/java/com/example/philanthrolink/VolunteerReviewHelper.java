package com.example.philanthrolink;

public class VolunteerReviewHelper {
    String volunteerID, review;
    public VolunteerReviewHelper(String review, String volunteerID){
        this.review = review;
        this.volunteerID = volunteerID;
    }

    public String getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(String NGOID) {
        this.volunteerID = volunteerID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public VolunteerReviewHelper(){
    }
}
