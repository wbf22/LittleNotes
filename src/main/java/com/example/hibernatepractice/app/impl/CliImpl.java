package com.example.hibernatepractice.app.impl;

import com.example.hibernatepractice.app.api.Cli;
import com.example.hibernatepractice.service.api.NoteManager;
import com.example.hibernatepractice.service.api.Search;
import com.example.hibernatepractice.service.api.SettingsManager;
import com.example.hibernatepractice.util.ConsoleColors;
import com.example.hibernatepractice.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CliImpl implements Cli {
    @Autowired
    private Search search;

    @Autowired
    private NoteManager noteManager;

    @Autowired
    private SettingsManager settingsManager;


    @Scheduled(initialDelay = 1, fixedDelay=Long.MAX_VALUE)
    public void getUserInput() {
        boolean run = true;
        settingsManager.doSetUp();
        System.out.print(">");
        while (run) {
            try{
                Scanner in = new Scanner(System.in);
                if (in.hasNextLine()) {
                    StringTokenizer tokens = new StringTokenizer(in.nextLine());
                    List<String> args = new ArrayList<>();
                    tokens.asIterator().forEachRemaining(t -> args.add(t.toString()));
                    handleCommand(args);
                    System.out.print(">");
                }
            } catch (Exception e) {
                System.out.print(ConsoleColors.RED);
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Something went wrong with your last command. We probably " +
                        "forgot to handle something in the code :(");
                System.out.println("Check your last command for mistakes. Sorry!");
                System.out.print(ConsoleColors.RESET);
            }
        }
    }

    @Override
    public void handleCommand(List<String> args) {
        if (args.get(0).equals("help")) {
            System.out.print(ConsoleColors.PURPLE);
            System.out.println("**********************");
            System.out.println("LITTLE SQL NOTES");
            System.out.println("**********************");
            System.out.println();
            System.out.println("add <file location> <tags...>");
            System.out.println("delete <file location>");
            System.out.println("search <tags...>");
            System.out.println("    -d print the first matching file");
            System.out.println("    -a will open files and scan for tag matches. (Slower)");
            System.out.println("all");
            System.out.println("    displays all notes in db");
            System.out.println("    -t prints all notes with the tags");
            System.out.println("open");
            System.out.println("    <file to open> or leave blank to open first file from last search command");
            System.out.println("    <number from last search> to open file from last search command");
            System.out.println("create <file name> <tags...>");
            System.out.println("    creates a new entry as well as new file which then opens in text edit");
            System.out.println("    in the default folder");
            System.out.println("    -f <file path+name> <tags...> to create in a different folder");
            System.out.println("addtag <file location> <tag(s)...>");
            System.out.println("removeTag <file location> <tag(s)...>");
            System.out.println("ls <path>");
            System.out.println("    -h prints contents of default notes folder");
            System.out.println("display");
            System.out.println("    <file to open> to open first file from last search command");
            System.out.println("    <number from last search> to open file from last search command");
            System.out.println(ConsoleColors.RESET);
        }

        // add new note entry
        if (args.get(0).equals("add")) {
            noteManager.addNote(args.get(1), args.subList(2, args.size()));
        }

        // delete note reference
        if (args.get(0).equals("delete")) {
            noteManager.deleteNote(args.get(1));
        }

        // search, search -d
        if (args.get(0).equals("search")) {
            List<String> modifiers = args.stream()
                    .filter(s -> s.startsWith("-"))
                    .collect(Collectors.toList());

            List<String> tags = args.subList(0, args.size()).stream()
                    .filter(s -> !s.startsWith("-"))
                    .collect(Collectors.toList());

            boolean displayFirstResult = modifiers.stream().anyMatch(s -> s.contains("d"));
            boolean scanFiles = modifiers.stream().anyMatch(s -> s.contains("a"));

            search.search(tags, scanFiles, displayFirstResult);
        }

        // see all note objects
        if (args.get(0).equals("all")) {
            boolean printTags = args.size() > 1 && args.get(1).equals("-t");
            noteManager.printAllNotes(printTags);
        }

        // open note to edit
        if (args.get(0).equals("open")) {
            if (args.size() > 1) {
                if (FileUtil.isNumeric(args.get(1))){
                    search.open(Integer.parseInt(args.get(1)));
                }
                else {
                    search.open(args.get(1));
                }
            }
            else {
                search.open();
            }
        }

        // create new note file and entry
        if (args.get(0).equals("create")) {
            List<String> modifiers = args.stream()
                    .filter(s -> s.startsWith("-"))
                    .collect(Collectors.toList());

            List<String> tags = args.subList(0, args.size()).stream()
                    .filter(s -> !s.startsWith("-"))
                    .collect(Collectors.toList());

            if (modifiers.stream().anyMatch(s -> s.contains("f"))) {
                noteManager.addNote(args.get(1), args.subList(2, args.size()));
                FileUtil.makeFile(args.get(1));
                FileUtil.openFileInTextEdit(args.get(1));
            }
            else {
                noteManager.addNote(settingsManager.get() + "/" + args.get(1), args.subList(2, args.size()));
                FileUtil.makeFile(settingsManager.get() + "/" + args.get(1));
                FileUtil.openFileInTextEdit(settingsManager.get() + "/" + args.get(1));
            }


        }

        // update note entry
        if (args.get(0).equals("addtag")) {
            noteManager.addTagToNote(args.get(1), args.subList(2, args.size()));
        }

        // update note entry
        if (args.get(0).equals("removeTag")) {
            noteManager.removeTagFromNote(args.get(1), args.subList(2, args.size()));
        }

        // ls in directory
        if (args.get(0).equals("ls")) {
            if (args.size() == 1 || args.get(1).equals("-h")) {
                FileUtil.listDirectory(settingsManager.get());
            }
            else {
                FileUtil.listDirectory(args.get(1));
            }
        }

        // display contents of file
        if (args.get(0).equals("display")) {
            if (args.size() > 1 && FileUtil.isNumeric(args.get(1))) {
                search.display(Integer.parseInt(args.get(1)));
            }
            else {
                System.out.println("Something was wrong with your command. Enter 'help' if you need it");
            }
        }


    }
}
