package com.akryvtsun.timerecorder.properties;

import java.util.Properties;

/**
 * Implements by objects which needs data storing in file system.
 *
 * @author kontiky
 */
public interface Storable {
    void store(Properties props);

    void restore(Properties props);
}
