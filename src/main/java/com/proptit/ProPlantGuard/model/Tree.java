package com.proptit.ProPlantGuard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Tree {
    private String name;
    private String species;
    private String description;
    private String imageUrl;
    private String lastWateredDate;
    private String waterSchedule;
    private String wateredToday;
    private String imagesFolderPath;


public String getImagesFolderPath() {
    return imagesFolderPath;
}

public void setImagesFolderPath(String imagesFolderPath) {
    this.imagesFolderPath = imagesFolderPath;
}
    private String nextWateringDate;
    public Tree() {
    }

    @JsonCreator
    public Tree(
            @JsonProperty("name") String name,
            @JsonProperty("species") String species,
            @JsonProperty("description") String description,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("lastWateredDate") String lastWateredDate,
            @JsonProperty("waterSchedule") String waterSchedule,
            @JsonProperty("wateredToday") String wateredToday,
            @JsonProperty("imageFolderPath") String imageFolderPath){
        this.name = name;
        this.species = species;
        this.description = description;
        this.imageUrl = imageUrl;
        this.lastWateredDate = lastWateredDate;
        this.waterSchedule = waterSchedule;
        this.wateredToday = wateredToday;
        this.imagesFolderPath = imageFolderPath;
    }
    public String getNextWateringDate() {
        return nextWateringDate;
    }

    public void setNextWateringDate(String nextWateringDate) {
        this.nextWateringDate = nextWateringDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastWateredDate() {
        return lastWateredDate;
    }

    public void setLastWateredDate(String lastWateredDate) {
        this.lastWateredDate = lastWateredDate;
    }

    public String getWaterSchedule() {
        return waterSchedule;
    }

    public void setWaterSchedule(String waterSchedule) {
        this.waterSchedule = waterSchedule;
    }

    public String isWateredToday() {
        return wateredToday;
    }

    public void setWateredToday(String wateredToday) {
        this.wateredToday = wateredToday;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tree tree = (Tree) obj;
        return name.equalsIgnoreCase(tree.name);
    }
    @Override
    public String toString() {
        return "Tree{" +
                "name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", lastWateredDate=" + lastWateredDate +
                ", waterSchedule='" + waterSchedule + '\'' +
                ", wateredToday=" + wateredToday +
                '}';
    }

    public String getWateredToday() {
        return wateredToday;
    }

    public void addImageUrl(String absolutePath) {
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            this.imageUrl = absolutePath;
        }
    }


}
