package com.example.hibernatepractice.service.api;

import java.util.List;

public interface Search {
    void search(List<String> tags, boolean scanFiles, boolean displayFirstMatch);
    void open();
    void open(int lastSearchNumber);
    void open(String fileLocation);
    void display(int lastSearchNumber);
    void display(String fileLocation);
}
