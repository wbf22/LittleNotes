package com.example.hibernatepractice.service.impl;

import com.example.hibernatepractice.dao.model.Note;
import com.example.hibernatepractice.dao.repository.NoteRepository;
import com.example.hibernatepractice.service.api.NoteManager;
import com.example.hibernatepractice.util.ConsoleColors;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// for setting up db https://www.positronx.io/how-to-install-mysql-on-mac-configure-mysql-in-terminal/
//https://hevodata.com/learn/spring-boot-mysql/

//  add /Users/brandon.fowler/Documents/gitCheatSheet.txt git

@Service
public class NoteManagerImpl implements NoteManager {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public void addNote(String fileLocation, List<String> tags) {
        Note note = new Note();
        note.setFileLocation(fileLocation);
        note.setTags(tags);
        noteRepository.save(note);
    }

    @Override
    public void printAllNotes(boolean printTags) {
        System.out.print(ConsoleColors.YELLOW);
        Iterable<Note> notes = noteRepository.findAll();
        notes.forEach(note -> {
            System.out.println(note.getFileLocation());
            if (printTags) note.getTags().forEach(tag -> System.out.println("   -" + tag));
        });
        if(!notes.iterator().hasNext()) {
            System.out.println("Notes db is empty");
        }
        System.out.print(ConsoleColors.RESET);

    }

    @Override
    public void addTagToNote(String filePath, List<String> newTags) {
        List<Note> notes = noteRepository.retrieveByFileLocation(filePath);
        if (notes.size() > 0) {
            Note noteToUpdate = notes.get(0);
            noteToUpdate.addTags(newTags);
            noteRepository.save(noteToUpdate);
        }
        else {
            System.out.println("Couldn't find an entry for that file path. Check your last command");
        }
    }

    @Override
    public void removeTagFromNote(String filePath, List<String> tagsToRemove) {
        List<Note> notes = noteRepository.retrieveByFileLocation(filePath);
        if (notes.size() > 0) {
            Note noteToUpdate = notes.get(0);
            noteToUpdate.removeTags(tagsToRemove);
            noteRepository.save(noteToUpdate);
        }
        else {
            System.out.println("Couldn't find an entry for that file path. Check your last command");
        }
    }

    @Override
    public void deleteNote(String filepath) {
        List<Note> notes = noteRepository.retrieveByFileLocation(filepath);
        if (notes.size() > 0) {
            Note noteToUpdate = notes.get(0);
            noteRepository.delete(noteToUpdate);
            System.out.println("Deleted " + filepath + ", but not the real file");
        }
        else {
            System.out.println(filepath + " note in note manager");
        }

    }
}
