package com.akryvtsun.timerecorder;

import java.awt.Image;

import javax.swing.ImageIcon;

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
        return new ImageIcon(AppMainFrame.class.getResource("icons/" + iconName + ".png"));
    }

    public static Image getLogo(RecorderState state) {
        return getIcon("logo/" + state.toString().toLowerCase()).getImage();
    }
}
