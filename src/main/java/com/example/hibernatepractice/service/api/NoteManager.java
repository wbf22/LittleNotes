package com.example.hibernatepractice.service.api;

import java.util.List;

public interface NoteManager {
    void addNote(String fileLocation, List<String> tags);
    void printAllNotes(boolean printTags);

    void addTagToNote(String filePath, List<String> newTags);
    void removeTagFromNote(String filePath, List<String> tagsToRemove);
    void deleteNote(String filepath);
}
