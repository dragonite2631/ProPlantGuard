package com.proptit.ProPlantGuard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tree {
    private String name;
    private String type;
    private String age;
    private String description;
    private String imageUrl; // New property

    public Tree() {

    }
    @JsonCreator
    public Tree(@JsonProperty("name") String name,  @JsonProperty("type") String type, @JsonProperty("age") String age, @JsonProperty("description") String description, @JsonProperty("imageUrl") String imageUrl) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return name + " (" + type + ") - " + description;
    }
}