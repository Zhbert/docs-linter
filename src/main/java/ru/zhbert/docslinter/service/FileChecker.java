package ru.zhbert.docslinter.service;

import java.io.*;
import java.util.ArrayList;

public class FileChecker {

    ArrayList<String> dict;
    int dictMaxLen = 0;
    Integer maxLines = 0;

    public FileChecker(ArrayList<String> dict) {
        this.dict = dict;
        for (String word : dict) {
            if (word.length() > dictMaxLen) {
                dictMaxLen = word.length();
            }
        }
    }

    public void checkFile(File file) throws IOException {

        maxLines = getLinesCount(file);
        Integer currentLine = 0;
        int firstColLen = 6;
        if (maxLines.toString().length() >= 6) {
            firstColLen = maxLines.toString().length();
        }
        String format = "| %-" + firstColLen + "s | %-" +
                dictMaxLen +"s | %-" +
                dictMaxLen + "s |%n";
        String horizontalLine = getHorizontalLine(firstColLen, dictMaxLen);

        System.out.println("Checking file: " + file.getName() + " from " + file.getParent() + "...");
        System.out.println(horizontalLine);
        System.out.format(format, "Line N", "In dict", "In docs");
        System.out.println(horizontalLine);

        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            currentLine++;
            for (String word : dict) {
                if (line.toLowerCase().contains(word.toLowerCase())) {
                    int start = line.toLowerCase().indexOf(word.toLowerCase());
                    int end = start + word.length();
                    System.out.format(format, currentLine.toString(), word, line.substring(start, end));
                }
            }
            line = reader.readLine();
        }
        System.out.println(horizontalLine);
    }

    private Integer getLinesCount(File file) throws IOException {
        int count = 0;
        FileReader fr = new FileReader(file);
        LineNumberReader lnr = new LineNumberReader(fr);
        while (lnr.readLine() != null) {
            count++;
        }
        return count;
    }

    private String getHorizontalLine(Integer firstColLen, Integer dictMaxLen) {
        StringBuilder result = new StringBuilder("|-");
        for (int i = 0; i < firstColLen; i++) {
            result.append("-");
        }
        result.append("-|-");
        for (int i = 0; i < dictMaxLen; i++) {
            result.append("-");
        }
        result.append("-|-");
        for (int i = 0; i < dictMaxLen; i++) {
            result.append("-");
        }
        result.append("-|");
        return result.toString();
    }

    public ArrayList<String> getDict() {
        return dict;
    }

    public void setDict(ArrayList<String> dict) {
        this.dict = dict;
    }
}
