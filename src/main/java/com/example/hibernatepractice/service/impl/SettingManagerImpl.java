package com.example.hibernatepractice.service.impl;

import com.example.hibernatepractice.dao.model.Settings;
import com.example.hibernatepractice.dao.repository.NoteRepository;
import com.example.hibernatepractice.dao.repository.SettingsRepository;
import com.example.hibernatepractice.service.api.SettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

@Service
public class SettingManagerImpl implements SettingsManager {


    @Autowired
    private SettingsRepository settingsRepository;

    private Settings settings;

    @Override
    public void doSetUp() {
        settings = settingsRepository.findById(0L).orElse(null);
        if (settings == null) {
            System.out.println("It looks like you haven't set up a default folder or your " +
                    "previous entry doesn't exist anymore.");
            System.out.println("Enter a folder path to store new notes in:");
            Scanner in = new Scanner(System.in);
            StringTokenizer tokens = new StringTokenizer(in.nextLine());
            set(tokens.nextToken());
        }
    }

    @Override
    public String get() {
        if (settings != null) {
            return settings.getDefaultNotesFolder();
        }
        else {
            settings = settingsRepository.findById(0L).orElse(null);
            return (settings == null)? "null" : settings.getDefaultNotesFolder();
        }
    }

    @Override
    public void set(String defaultNoteLocation) {
        Settings notesFolder = settingsRepository.findById(0L).orElse(null);
        if (notesFolder == null) {
            settingsRepository.save(new Settings(0L, defaultNoteLocation));
        }
        else {
            notesFolder.setDefaultNotesFolder(defaultNoteLocation);
            settingsRepository.save(notesFolder);
        }
    }
}
