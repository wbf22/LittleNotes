package com.example.hibernatepractice.dao.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Note {


    public Note() {}


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags = new ArrayList<>();

    private String fileLocation;


    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public void removeTags(List<String> tags) {
        this.tags.removeAll(tags);
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
