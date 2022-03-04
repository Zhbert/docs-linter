package ru.zhbert.docslinter.service.dicts;

import ru.zhbert.docslinter.domain.DictTerm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class DictLoadService {

    ArrayList<DictTerm> dictTerms;
    final private String settingsPath = System.getProperty("user.home")
            + File.separator
            + ".docs-linter";

    public DictLoadService() {

        AtomicBoolean dictFinded = new AtomicBoolean(false);
        dictTerms = new ArrayList<>();

        File dictPath = new File(settingsPath);
        if (!dictPath.exists()) {
            dictPath.mkdir();
        }
        try {
            Files.walk(Paths.get(settingsPath))
                    .forEach(file -> {
                        if (file.toFile().isFile() && file.toFile().getPath().endsWith(".dclntr")) {
                            dictFinded.set(true);
                            System.out.println("Dictionary found: " + file.getFileName());
                            try {
                                parseDictionary(file.toFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!dictFinded.get()) System.out.println("Dictionary files not found!");
    }

    private void parseDictionary(File dict) throws IOException {
        FileReader fr = new FileReader(dict);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            DictTerm dictTerm = new DictTerm();
            ArrayList<String> incForms = new ArrayList<>();
            String[] parts = line.split(";");
            if (parts.length > 0 && parts.length <= 3) {
                if (!parts[0].isEmpty()) {
                    dictTerm.setMainForm(parts[0]);
                }
                if (!parts[1].isEmpty()) {
                    dictTerm.setFirstFromLineForm(parts[1]);
                }
                if (parts.length == 3) {
                    if (!parts[2].isEmpty()) {
                        dictTerm.addWrongForm(parts[2]);
                    }
                }
            }
            this.dictTerms.add(dictTerm);
            line = reader.readLine();
        }
    }

    public ArrayList<DictTerm> getDict() {
        if (!dictTerms.isEmpty()) return dictTerms;
        return null;
    }

}
