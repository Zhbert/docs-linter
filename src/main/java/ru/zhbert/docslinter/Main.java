package ru.zhbert.docslinter;

import ru.zhbert.docslinter.domain.DictTerm;
import ru.zhbert.docslinter.service.CheckFileService;
import ru.zhbert.docslinter.service.dicts.DictLoadService;
import ru.zhbert.docslinter.service.help.HelpService;

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

        if (args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case "--help":
                    case "-h":
                        HelpService helpService = new HelpService();
                        helpService.viewHelp();
                        break;
                }
            }
        } else System.out.println("You must specify the file to check or the" +
                " parameters of the application. Read --help.txt for more information.");

        CheckFileService checkFileService = new CheckFileService(dict);
        File file = new File("/home/zhbert/testfile");
        checkFileService.checkFile(file);
    }
}
