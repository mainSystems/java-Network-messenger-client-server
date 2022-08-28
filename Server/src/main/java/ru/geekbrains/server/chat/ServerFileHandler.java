package ru.geekbrains.server.chat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;

public class ServerFileHandler {
    private static final String USER_DIR = "userHistory/";
    private static final String FILE_ASGMT = "_history";

    protected static void appendToFile(String fileName, String receiver, String message) {
        String date = DateFormat.getDateInstance().format(new Date());
        checkFile(fileName);
        try {
            PrintWriter file = new PrintWriter(new FileWriter(USER_DIR + fileName + FILE_ASGMT, StandardCharsets.UTF_8, true));
            BufferedWriter bufferedWriter = new BufferedWriter(file);
            bufferedWriter.write(date + ":" + receiver + ": " + message + System.lineSeparator());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void checkFile(String fileName) {
        File dir = new File(USER_DIR);
        dir.mkdir();

        File file = new File(USER_DIR + fileName + FILE_ASGMT);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                }
            } catch (IOException e) {
                System.out.println("An error occurred. File creation.");
                e.printStackTrace();
            }
        }
    }
}
