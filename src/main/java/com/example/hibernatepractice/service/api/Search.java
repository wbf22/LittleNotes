package com.example.hibernatepractice.service.api;

import java.util.List;

public interface Search {
    void search(List<String> tags, boolean scanFiles, boolean displayAllMatches);
    void open();
    void open(String fileLocation);
}
