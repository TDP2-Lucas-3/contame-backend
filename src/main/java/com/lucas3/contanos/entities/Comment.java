package com.lucas3.contanos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name ="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Incident incident;

    private ECommentCategory category;

    public Comment() {
    }

    public Comment(String comment, User user, Incident incident) {
        this.comment = comment;
        this.user = user;
        this.incident = incident;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public ECommentCategory getCategory() {
        return category;
    }

    public void setCategory(ECommentCategory category) {
        this.category = category;
    }
}
