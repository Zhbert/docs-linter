package ru.zhbert.docslinter.service;

import ru.zhbert.docslinter.domain.DictTerm;

import java.io.*;
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

        maxLines = getLinesCount(file);
        Integer currentLine = 0;
        int firstColLen = 6;
        if (maxLines.toString().length() >= 6) {
            firstColLen = maxLines.toString().length();
        }
        String format = "| %-" + firstColLen + "s | %-" +
                dictMaxLen +"s | %-" +
                dictMaxLen + "s |%n";
        TableLinesService tableLinesService = new TableLinesService(dictMaxLen, firstColLen);

        System.out.println("Checking file: " + file.getName() + " from " + file.getParent() + "...");
        tableLinesService.printUpLine();
        System.out.format(format, "Line N", "In dict", "In docs");
        tableLinesService.printMediumLine();

        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            currentLine++;
            for (DictTerm dictTerm : dict) {
                if (line.toLowerCase().contains(dictTerm.getMainForm().toLowerCase())) {
                    int start = line.toLowerCase().indexOf(dictTerm.getMainForm().toLowerCase());
                    int end = start + dictTerm.getMainForm().length();
                    String innerStr = line.substring(start, end);
                    if (!innerStr.equals(dictTerm.getMainForm())) {
                        System.out.format(format, currentLine.toString(),
                                dictTerm.getMainForm(), innerStr);
                    }
                    if (start == 0) {
                        if (!innerStr.equals(dictTerm.getFirstFromLineForm())) {
                            System.out.format(format, currentLine.toString(),
                                    dictTerm.getFirstFromLineForm(), innerStr);
                        }
                    }
                }
            }
            line = reader.readLine();
        }
        tableLinesService.printDownLine();
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
