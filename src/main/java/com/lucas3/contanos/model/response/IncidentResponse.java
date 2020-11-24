package com.lucas3.contanos.model.response;

import com.lucas3.contanos.entities.*;

import java.util.Date;
import java.util.List;

public class IncidentResponse {

    private Long id;

    private String title;
    private String description;

    private double lat;
    private double lon;

    private String location;

    private String hood;

    private List<String> images;


    private String subcategory;

    private User user;

    private IncidentResponse parent;

    private Date creationDate;

    private Date updateDate;

    private Date completeDate;

    private Integer votes;

    private boolean voteByUser;

    List<Comment> comments;

    private String state;

    private String statePrivate;

    private String category;

    public IncidentResponse() {
    }

    public IncidentResponse(Incident incident){
        this.id = incident.getId();
        this.title = incident.getTitle();
        this.description= incident.getDescription();
        this.lat = incident.getLat();
        this.lon = incident.getLon();
        this.location = incident.getLocation();
        this.hood = incident.getHood();
        this.images = incident.getImages();
        this.subcategory= incident.getSubcategory();
        this.user = incident.getUser();
        if(incident.getParent() != null){
            this.parent= new IncidentResponse(incident.getParent());
        }
        this.creationDate = incident.getCreationDate();
        this.updateDate = incident.getUpdateDate();
        this.completeDate = incident.getCompleteDate();
        this.votes = incident.getVotes();
        this.comments = incident.getComments();

        this.state = incident.getState().getValue();
        this.statePrivate = incident.getStatePrivate().getValue();
        this.category = incident.getCategory().getValue();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IncidentResponse getParent() {
        return parent;
    }

    public void setParent(IncidentResponse parent) {
        this.parent = parent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public boolean isVoteByUser() {
        return voteByUser;
    }

    public void setVoteByUser(boolean voteByUser) {
        this.voteByUser = voteByUser;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatePrivate() {
        return statePrivate;
    }

    public void setStatePrivate(String statePrivate) {
        this.statePrivate = statePrivate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
