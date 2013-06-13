package org.timerecorder;

import java.util.Properties;

/**
 * Implements by objects which needs data storing in file system.
 *
 * @author kontiky
 */
public interface IStorable {
    public void store(Properties props);
    public void restore(Properties props);    
}
