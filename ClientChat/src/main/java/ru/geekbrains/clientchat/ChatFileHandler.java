package ru.geekbrains.clientchat;

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFileHandler {
    private static final String USER_DIR = "userHistory/";
    private static final String FILE_ASGMT = "_history";
    private static final int loadHistory = 100;

    public static String appendFromFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();

        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(USER_DIR + fileName + FILE_ASGMT), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null && result.size() < loadHistory) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("cant read file: " + fileName);
        }

        for (String str : result) {
            sb.insert(0, str + "\n");
        }
        System.out.println(sb.toString().length());
        return sb.toString();
    }

    public static boolean checkFile(String fileName) {
        return (new File(USER_DIR + fileName + FILE_ASGMT).exists());
    }
}
