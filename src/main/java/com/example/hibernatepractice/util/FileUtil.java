package com.example.hibernatepractice.util;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

public class FileUtil {


    public static void openFileInTextEdit(String filePath) {
        try {
            Runtime.getRuntime().exec("open -a TextEdit " + filePath);
        } catch (IOException e) {
            System.out.println("Couldn't open file in Mac TextEdit. " +
                    "Maybe file doesn't exist or this isn't a mac.");
        }
    }

    public static void makeFile(String filePath) {
        try {
            Runtime.getRuntime().exec("touch " + filePath);
        } catch (IOException e) {
            System.out.println("Couldn't create file. Check your filepath for the last command");
        }
    }


    public static void listDirectory(String filePath) {
        System.out.print(ConsoleColors.RED);
        Stream.of(Objects.requireNonNull(new File(filePath).listFiles())).forEach(file -> {
            System.out.println(file.toPath());
        });
        System.out.print(ConsoleColors.RESET);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
