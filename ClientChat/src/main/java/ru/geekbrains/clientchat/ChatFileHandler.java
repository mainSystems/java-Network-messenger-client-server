package ru.geekbrains.clientchat;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatFileHandler {
    private static final String USER_DIR = "userHistory/";
    private static final String FILE_ASGMT = "_history";
    private static final int loadHistory = 100;
    private static final Logger logger = LogManager.getLogger(ChatFileHandler.class);

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
            logger.error("cant read file: " + fileName);
        }

        for (String str : result) {
            sb.insert(0, str + "\n");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(sb.toString().length());
        }
        return sb.toString();
    }

    public static boolean checkFile(String fileName) {
        return (new File(USER_DIR + fileName + FILE_ASGMT).exists());
    }
}
