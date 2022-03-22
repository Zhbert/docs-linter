package ru.zhbert.docslinter.service;

import ru.zhbert.docslinter.domain.DictTerm;
import ru.zhbert.docslinter.service.dicts.DictLoadService;
import ru.zhbert.docslinter.service.help.HelpService;

import java.io.IOException;
import java.util.ArrayList;

public class ArgumentsService {

    String[] args;
    DictLoadService dictLoadService;
    ArrayList<DictTerm> dict;

    public ArgumentsService(String[] args) {
        this.args = args;
        dictLoadService = new DictLoadService();
        dict = dictLoadService.getDict();
    }

    public ArgumentsService() {
    }

    public void checkArgs() throws IOException {
        if (args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case "--help":
                    case "-h":
                        HelpService helpService = new HelpService();
                        helpService.viewHelp();
                        break;
                    case ".":
                    case "./*":
                        String currentPath = new java.io.File(".").getCanonicalPath();
                        CheckFileService checkFileService = new CheckFileService(dict);
                        checkFileService.checkFilesInFolder(currentPath);
                }
            }
        } else System.out.println("You must specify the file to check or the" +
                " parameters of the application. Read --help.txt for more information.");
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
