package com.akryvtsun.timerecorder.properties;

import java.io.*;
import java.util.Properties;

public final class Storage {
    private final File iniFile;
    private final Storable[] components;

    public Storage(Storable... components) {
        this(new File("trec.ini"), components);
    }

    Storage(File iniFile, Storable... components) {
        this.iniFile = iniFile;
        this.components = components;
    }

    public void restoreProperties() {
        if (iniFile.exists()) {
            Properties props = new Properties();
            try {
                try (InputStream fis = new FileInputStream(iniFile)) {
                    props.load(fis);
                }
            } catch (IOException e) {
                e.printStackTrace();        // TODO store problem into log
            }
            for (Storable s : components)
                s.restore(props);
            iniFile.delete();
        }
    }

    public void storeProperties() {
        Properties props = new Properties();
        for (Storable s : components)
            s.store(props);
        try {
            try (OutputStream os = new FileOutputStream(iniFile)) {
                props.store(os, null);
            }
        } catch (IOException e) {
            e.printStackTrace();        // TODO store problem into log
        }
    }
}
