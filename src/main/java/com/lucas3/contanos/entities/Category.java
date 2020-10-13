package com.lucas3.contanos.entities;

import com.lucas3.contanos.model.request.CategoryRequest;

import javax.persistence.*;

@Entity
@Table(name ="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private String description;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name.toUpperCase();
        this.description = description;
    }

    public Category(CategoryRequest request){
        this.name = request.getName().toUpperCase();
        this.description = request.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
