package com.example.hibernatepractice.dao.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Settings {

    @Id
    private Long id;

    private String defaultNotesFolder;


    public Settings(Long id, String defaultNotesFolder) {
        this.id = id;
        this.defaultNotesFolder = defaultNotesFolder;
    }

    public Settings() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDefaultNotesFolder() {
        return defaultNotesFolder;
    }

    public void setDefaultNotesFolder(String defualtNotesFolder) {
        this.defaultNotesFolder = defualtNotesFolder;
    }
}
