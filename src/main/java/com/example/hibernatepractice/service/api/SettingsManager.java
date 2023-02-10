package com.example.hibernatepractice.service.api;

public interface SettingsManager {


    void doSetUp();
    String get();
    void set(String defaultNoteLocation);

}
