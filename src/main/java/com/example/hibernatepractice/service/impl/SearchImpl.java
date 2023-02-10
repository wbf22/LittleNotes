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

    private String lastNoteFileLocation;

    @Override
    public void search(List<String> tags, boolean scanFiles, boolean displayAllMatches) {

        List<String> files = getRelatedFilesSorted(tags, scanFiles);
        Collections.reverse(files);
        if (displayAllMatches) {
            files.forEach(System.out::println);
            lastNoteFileLocation = files.get(0);
        }
        else {
            try {
                if (files.size() > 0) {
                    System.out.print(ConsoleColors.BLUE);
                    System.out.println( new String(Files.readAllBytes(Paths.get(files.get(0)))) );
                    System.out.println(ConsoleColors.RESET);

                    lastNoteFileLocation = files.get(0);
                }
                else {
                    System.out.print(ConsoleColors.BLUE);
                    System.out.println("No notes turned up with that/those tag(s)");
                    System.out.print(ConsoleColors.RESET);
                }
            } catch (IOException e) {
                filesNoLongerInSystem.addAll(
                        noteRepository.retrieveByFileLocation(files.get(0))
                );
            }
        }

        noteRepository.deleteAll(filesNoLongerInSystem);
    }

    @Override
    public void open() {
        FileUtil.openFileInTextEdit(lastNoteFileLocation);
    }

    @Override
    public void open(String fileLocation) {
        FileUtil.openFileInTextEdit(fileLocation);
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
