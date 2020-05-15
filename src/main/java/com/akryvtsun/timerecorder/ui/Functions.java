package com.akryvtsun.timerecorder.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Common application functions.
 *
 * @author kontiky
 */
public final class Functions {

    /**
     * Loads image from file in defined location into memory.
     * Loads only <i>png</i> image files.
     *
     * @param iconName image file name.
     * @return loaded image
     */
    public static ImageIcon getIcon(String iconName) {
        return new ImageIcon(TimeRecorderFrame.class.getResource("icons/" + iconName + ".png"));
    }

    public static Image getLogo(RecorderState state) {
        return getIcon("logo/" + state.toString().toLowerCase()).getImage();
    }
}
