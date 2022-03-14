package ru.zhbert.docslinter.service.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HelpService {

    String contents;

    public HelpService() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/help.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            contents = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public void viewHelp() {
        System.out.println(contents);
    }
}
