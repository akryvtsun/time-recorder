package com.akryvtsun.timerecorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.akryvtsun.timerecorder.controllers.GrossController;
import com.akryvtsun.timerecorder.controllers.NetController;

/**
 * Time Recorder main frame.
 *
 * @author kontiky
 */
public final class TimeRecorderFrame extends JFrame {
    private static final String APP_NAME = "Time Recorder";
    private static final String APP_VER = "1.41";

    private final GrossController grossController;
    private final NetController netController;

    private final Storage storage;
    
    private TrayIcon trayIcon;

    TimeRecorderFrame() {
        super(APP_NAME);
        
        Image logo = Functions.getLogo(RecorderState.INIT);
        
        setIconImage(logo);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
        		if (SystemTray.isSupported()) {
	        	    try {
	        	    	SystemTray.getSystemTray().add(trayIcon);
	        	    	setVisible(false);
	        	    } catch (AWTException ex) {
	        	        System.err.println("TrayIcon could not be added.");
	        	    }        			
        		}
            }
        });
        
        final ChangeListener l = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				RecorderState state = RecorderState.INIT;
				if (grossController.getTimeAction().isStarted()) {
					state = netController.getTimeAction().isStarted()?
							RecorderState.NET: RecorderState.GROSS;
				}
				setState(TimeRecorderFrame.this, state);
			}
		};

        netController = new NetController();
        netController.getTimeAction().setChangeListener(l);
        grossController = new GrossController(netController);
        grossController.getTimeAction().setChangeListener(l);

        storage = new Storage(netController, grossController);
        
        netController.setRatioListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				String title = APP_NAME + " - " + netController.getRatio();
				setTitle(title);
				if (SystemTray.isSupported()) 
					trayIcon.setToolTip(title);
			}
        });
        
        if (SystemTray.isSupported()) {
//            PopupMenu popup = new PopupMenu();
//            MenuItem defaultItem = new MenuItem("Exit");
//            defaultItem.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					exitApplication();
//				}
//			});
//            popup.add(defaultItem);
            
        	trayIcon = new TrayIcon(logo, APP_NAME, /*popup*/null);
        	trayIcon.setImageAutoSize(true);
        	
	    	ActionListener actionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
					setExtendedState(Frame.NORMAL);
					SystemTray.getSystemTray().remove(trayIcon);
				}
			};       
			trayIcon.addActionListener(actionListener);
        }        
        
		setJMenuBar(createMenuBar());
        add(createContent());
        pack();

        storage.restoreProperties();
		grossController.updateRatio();
    }

    private void exitApplication(final Component frame) {
        int result = JOptionPane.showConfirmDialog(frame,
                "Do you wish to exit the " + APP_NAME + "?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            storage.storeProperties();
            if (SystemTray.isSupported()) {
            	SystemTray.getSystemTray().remove(trayIcon);
            }
            dispose();
            System.exit(0);
        }
    }

	private Action createNewDayAction() {
	    Action action = new AbstractAction("New Workday", Functions.getIcon("new")) {
	        public void actionPerformed(ActionEvent e) {
	        	netController.startNewDay();
	        	grossController.startNewDay();
	        }
	    };
	    action.putValue(Action.MNEMONIC_KEY, Integer.valueOf('N'));
	    return action;
	}

	private Action createExitAction() {
	    Action action = new AbstractAction("Exit") {
	        public void actionPerformed(ActionEvent e) {
	            exitApplication(TimeRecorderFrame.this);
	        }
	    };
	    action.putValue(Action.MNEMONIC_KEY, Integer.valueOf('X'));
	    return action;
	}

	private Action createAboutAction(final Component frame) {
	    Action action = new AbstractAction("About...") {
	        public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(frame,
	                    new JLabel("<html><center>" + APP_NAME + " " + APP_VER + 
	                    		"<br>Copyright &copy; 2007-2013<br>by kontiky"),
	                    "About", JOptionPane.INFORMATION_MESSAGE);
	        }
	    };
	    action.putValue(Action.MNEMONIC_KEY, Integer.valueOf('A'));
	    return action;
	}

	private JMenuBar createMenuBar() {
    	JMenuBar menu = new JMenuBar();
    	menu.add(createFileMenu());
    	menu.add(createHelpMenu());
        return menu;
    }
    
    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic('f');
        menu.add(new JMenuItem(createNewDayAction()));
        menu.addSeparator();
        menu.add(new JMenuItem(createExitAction()));
        return menu;
    }
    
    private JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic('h');
        menu.add(new JMenuItem(createAboutAction(this)));
        return menu;
    }
    
    private JPanel createContent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(grossController.getViewComponent());
        panel.add(netController.getViewComponent());
        return panel;
    }
    
    private void setState(TimeRecorderFrame frame, RecorderState state) {
    	Image logo = Functions.getLogo(state);
		if (SystemTray.isSupported()) {
			frame.trayIcon.setImage(logo);
		}
		frame.setIconImage(logo);
    }
}
