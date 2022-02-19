package ru.zhbert.docslinter.service;

import java.io.*;
import java.util.ArrayList;

public class FileChecker {

    ArrayList<String> dict;
    int dictMaxLen = 0;
    int maxLines = 0;

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
        String Format = "| %-5s | %-" + dictMaxLen +"s | %-" + dictMaxLen + "s |%n";

        System.out.println("Checking file: " + file.getName() + " from " + file.getParent() + "...");

        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            currentLine++;
            for (String word : dict) {
                if (line.toLowerCase().contains(word.toLowerCase())) {
                    int start = line.toLowerCase().indexOf(word.toLowerCase());
                    int end = start + word.length();
                    System.out.format(Format, currentLine.toString(), word, line.substring(start, end));
                }
            }
            line = reader.readLine();
        }
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

    public ArrayList<String> getDict() {
        return dict;
    }

    public void setDict(ArrayList<String> dict) {
        this.dict = dict;
    }
}
