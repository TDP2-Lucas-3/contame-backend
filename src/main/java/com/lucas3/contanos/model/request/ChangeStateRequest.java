package com.lucas3.contanos.model.request;

public class ChangeStateRequest {

    private String state;

    private String comment;

    public ChangeStateRequest(String state, String comment) {
        this.state = state;
        this.comment = comment;
    }

    public ChangeStateRequest() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
