package ru.zhbert.docslinter;

import ru.zhbert.docslinter.domain.DictTerm;
import ru.zhbert.docslinter.service.CheckFileService;
import ru.zhbert.docslinter.service.dicts.DictLoadService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Docs Linter v.1.0");
        System.out.println("Started at " + new Date().toString());
        DictLoadService dictLoadService = new DictLoadService();
        ArrayList<DictTerm> dict = dictLoadService.getDict();
        CheckFileService checkFileService = new CheckFileService(dict);
        File file = new File("/home/zhbert/testfile");
        checkFileService.checkFile(file);
    }
}
