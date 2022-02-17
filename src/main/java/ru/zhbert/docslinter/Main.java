package ru.zhbert.docslinter;

import ru.zhbert.docslinter.service.FileChecker;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Docs Linter v.1.0");
        System.out.println("Started at " + new Date().toString());
        ArrayList<String> dict = new ArrayList<String>();
        dict.add("web-интерфейс");
        FileChecker fileChecker = new FileChecker(dict);
        File file = new File("/home/zhbert/testfile");
        fileChecker.checkFile(file);
        }
}
