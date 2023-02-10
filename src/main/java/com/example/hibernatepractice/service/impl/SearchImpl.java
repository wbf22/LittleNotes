package com.example.hibernatepractice.service.impl;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import com.example.hibernatepractice.dao.model.Note;
import com.example.hibernatepractice.dao.repository.NoteRepository;
import com.example.hibernatepractice.service.api.Search;
import com.example.hibernatepractice.util.ConsoleColors;
import com.example.hibernatepractice.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchImpl implements Search {

    @Autowired
    private NoteRepository noteRepository;

    private List<Note> filesNoLongerInSystem = new ArrayList<>();

    private List<String> lastNotes;

    @Override
    public void search(List<String> tags, boolean scanFiles, boolean displayFirstMatch) {
        System.out.print(ConsoleColors.BLUE);

        List<String> files = getRelatedFilesSorted(tags, scanFiles);
        Collections.reverse(files);
        lastNotes = files;
        if (!displayFirstMatch) {
            for (int i = 0; i < files.size(); i++) {
                System.out.print(i + " ");
                System.out.println(files.get(i));
            }
        }
        else {
            try {
                if (files.size() > 0) {
                    System.out.println( new String(Files.readAllBytes(Paths.get(files.get(0)))) );
                }
                else {
                    System.out.println("No notes turned up with that/those tag(s)");
                }
            } catch (IOException e) {
                filesNoLongerInSystem.addAll(
                        noteRepository.retrieveByFileLocation(files.get(0))
                );
            }
        }

        noteRepository.deleteAll(filesNoLongerInSystem);
        System.out.println(ConsoleColors.RESET);
    }

    @Override
    public void open() {
        FileUtil.openFileInTextEdit(lastNotes.get(0));
    }

    @Override
    public void open(int lastSearchNumber) {
        FileUtil.openFileInTextEdit(lastNotes.get(lastSearchNumber));
    }

    @Override
    public void open(String fileLocation) {
        FileUtil.openFileInTextEdit(fileLocation);
    }

    @Override
    public void display(int lastSearchNumber) {
        System.out.print(ConsoleColors.BLUE);
        try {
            System.out.println( new String(Files.readAllBytes(Paths.get(lastNotes.get(lastSearchNumber)))) );
        } catch (Exception e) {
            System.out.println("Couldn't find the file for that number");
        }
        System.out.print(ConsoleColors.RESET);
    }

    @Override
    public void display(String fileLocation) {
        System.out.print(ConsoleColors.BLUE);
        try {
            System.out.println( new String(Files.readAllBytes(Paths.get(fileLocation))) );
        } catch (Exception e) {
            System.out.println("Couldn't find that file");
        }
        System.out.print(ConsoleColors.RESET);
    }


    private List<String> getRelatedFilesSorted(List<String> tags, boolean scanFiles) {
        Map<String, Integer> foundFiles = new HashMap<>();
        for (String t : tags) {
            noteRepository.retrieveByTag(t).forEach(note -> {
                foundFiles.put(note.getFileLocation(),
                        1 + (foundFiles.get(note.getFileLocation()) == null? 0 : foundFiles.get(note.getFileLocation())) );
            });

            if (scanFiles) {
                noteRepository.findAll().forEach(note -> {
                    try {
                        String content = new String(Files.readAllBytes(Paths.get(note.getFileLocation())));
                        if (content.contains(t)) {
                            foundFiles.put(note.getFileLocation(),
                                    1 + (foundFiles.get(note.getFileLocation()) == null? 0 : foundFiles.get(note.getFileLocation())) );
                        }
                    } catch (IOException e) {
                        filesNoLongerInSystem.add(note);
                    }

                });
            }
        }

        return foundFiles.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
