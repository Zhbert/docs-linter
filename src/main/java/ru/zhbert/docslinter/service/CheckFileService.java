package ru.zhbert.docslinter.service;

import ru.zhbert.docslinter.domain.DictTerm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CheckFileService {

    ArrayList<DictTerm> dict;
    int dictMaxLen = 0;
    Integer maxLines = 0;

    public CheckFileService(ArrayList<DictTerm> dict) {
        this.dict = dict;
        for (DictTerm dictTerm : dict) {
            if (dictTerm.getMainForm().length() > dictMaxLen) {
                dictMaxLen = dictTerm.getMainForm().length();
            }
        }
    }

    public void checkFile(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;

        maxLines = getLinesCount(file);
        boolean codeBlock = false;
        boolean foundWords = false;
        int currentLine = 0;
        int firstColLen = 6;
        if (maxLines.toString().length() >= 6) {
            firstColLen = maxLines.toString().length();
        }
        String format = "| %-" + firstColLen + "s | %-" +
                dictMaxLen +"s | %-" +
                dictMaxLen + "s |%n";
        TableLinesService tableLinesService = new TableLinesService(dictMaxLen, firstColLen);

        System.out.println("Checking file: " + file.getName() + " from " + file.getParent() + "...");
        System.setOut(ps);
        tableLinesService.printUpLine();
        System.out.format(format, "Line N", "In dict", "In docs");
        tableLinesService.printMediumLine();

        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            currentLine++;
            if (line.contains("```") &&  line.indexOf("```") == 0) {
                if (!codeBlock) {
                    codeBlock = true;
                } else codeBlock = false;
            }
            if (!codeBlock) {
                for (DictTerm dictTerm : dict) {
                    if (line.toLowerCase().contains(dictTerm.getMainForm().toLowerCase())) {
                        int start = line.toLowerCase().indexOf(dictTerm.getMainForm().toLowerCase());
                        int end = start + dictTerm.getMainForm().length();
                        String innerStr = line.substring(start, end);
                        String correctForm = "";
                        if (start > 0) {
                            correctForm = dictTerm.getMainForm();
                        } else correctForm = dictTerm.getFirstFromLineForm();
                        if (!innerStr.equals(correctForm)) {
                            System.out.format(format, currentLine, correctForm, innerStr);
                            foundWords = true;
                        }
                    }
                    if (!dictTerm.getWrongForms().isEmpty()) {
                        for (String wrongForm : dictTerm.getWrongForms()) {
                            if (line.toLowerCase().contains(wrongForm.toLowerCase())) {
                                int start = line.toLowerCase().indexOf(wrongForm.toLowerCase());
                                int end = start + wrongForm.length();
                                String innerStr = line.substring(start, end);
                                String correctForm = "";
                                if (start > 0) {
                                    correctForm = dictTerm.getMainForm();
                                } else correctForm = dictTerm.getFirstFromLineForm();
                                System.out.format(format, currentLine, correctForm, innerStr);
                                foundWords = true;
                            }
                        }
                    }
                }
            }
            line = reader.readLine();
        }
        tableLinesService.printDownLine();
        System.out.flush();
        System.setOut(old);
        if (foundWords) {
            System.out.println(baos.toString());
        }
    }

    public void checkFilesInFolder(String path) {
        try {
            Files.walk(Paths.get(path))
                    .forEach(file -> {
                        String fileName = file.getFileName().toString();
                        int i = fileName.lastIndexOf('.');
                        if (i > 0) {
                            if (fileName.substring(i+1).equals("md") ||
                                    fileName.substring(i+1).equals("txt") ||
                                    fileName.substring(i+1).equals("liquid")) {
                                try {
                                    checkFile(file.toFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
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

    public ArrayList<DictTerm> getDict() {
        return dict;
    }

    public void setDict(ArrayList<DictTerm> dict) {
        this.dict = dict;
    }
}
