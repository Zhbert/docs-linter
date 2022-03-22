package ru.zhbert.docslinter;

import ru.zhbert.docslinter.service.ArgumentsService;

import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Docs Linter v.1.0");
        System.out.println("Started at " + new Date().toString());

        ArgumentsService argumentsService = new ArgumentsService(args);
        argumentsService.checkArgs();
    }
}
