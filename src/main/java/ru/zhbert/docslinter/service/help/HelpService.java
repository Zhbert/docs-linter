package ru.zhbert.docslinter.service.help;

import java.io.*;

public class HelpService {

    File helpFile;

    public HelpService() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/main/java/ru/zhbert/resource/help");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            // Use resource
        }

    }

    public void viewHelp() throws IOException {
        FileReader fr = new FileReader(helpFile);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
        }
    }
}
