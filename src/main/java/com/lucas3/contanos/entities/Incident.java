package com.lucas3.contanos.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name ="incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;

    private double lat;
    private double lon;

    private String location;

    private String hood;

    @ElementCollection
    private List<String> images;

    private EIncidentCategory category;

    private String subcategory;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Incident parent;

    private Date creationDate;

    private Date updateDate;

    private Date completeDate;

    private EIncidentStatePublic state;

    private EIncidentStatePrivate statePrivate;

    @Transient
    private Integer votes;

    @Transient
    private boolean voteByUser;

    @Transient
    List<Comment> comments;

    public Incident() {}

    public Incident(String title, EIncidentCategory category){
        this.title = title;
        this.category = category;
        this.creationDate = new Date();
        this.updateDate = new Date();
    }

    public Incident(String title, EIncidentCategory category, String description, double lat, double lon){
        this.title = title;
        this.category = category;
        this.description = description;
        this.lat= lat;
        this.lon = lon;
        this.creationDate = new Date();
        this.updateDate = new Date();

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EIncidentStatePublic getState() {
        return state;
    }

    public void setState(EIncidentStatePublic state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public Incident getParent() {
        return parent;
    }

    public void setParent(Incident parent) {
        this.parent = parent;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public EIncidentCategory getCategory() {
        return category;
    }

    public void setCategory(EIncidentCategory category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public EIncidentStatePrivate getStatePrivate() {
        return statePrivate;
    }

    public void setStatePrivate(EIncidentStatePrivate statePrivate) {
        this.statePrivate = statePrivate;
    }
}
