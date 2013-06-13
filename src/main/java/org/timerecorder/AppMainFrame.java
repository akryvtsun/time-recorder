package org.timerecorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.timerecorder.controllers.GrossController;
import org.timerecorder.controllers.NetController;

/**
 * Time Recorder application starter and main frame.
 * 
 * TODO add net start/stop actions to tray menu
 * http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/systemtray
 * 
 * TODO Avoid memory leaks (?)
 * TODO NPE while loading icons (?)
 * TODO Prevent start of multi applications at the same time
 * TODO Add date for Start Time when gross control was started
 * 
 * TODO for 1.1: turning memory settings for JVM in start.bat
 * http://h21007.www2.hp.com/dspp/tech/tech_TechDocumentDetailPage_IDX/1,1701,1604,00.html
 * http://www.caucho.com/resin-3.0/performance/jvm-tuning.xtp
 * http://www.hp.com/products1/unix/java/infolibrary/prog_guide/hotspot.html
 * 
 * TODO for 1.5: new net counter for time switching
 * TODO for 1.5: use JProgressBar for showing ratio at the top or bottom of time controller components
 * TODO for 1.5: change main frame logo icon while net controller is running
 * 
 * TODO for 2.0: data base time storage
 * TODO for 2.0: charts on a base of history data
 * 
 * TODO use local DB like JavaDB
 * TODO use TrayIcon.displayMessage for workday finish notification
 * TODO use java.util.prefs API instead of ini-file.
 * TODO add gross Start Date field
 * TODO add net Switch Counter field
 *
 * @author kontiky
 */
public class AppMainFrame extends JFrame {
    private static final String APP_NAME = "Time Recorder";
    private static final String APP_VER = "1.41";

    private static final File INI_FILE = new File("trec.ini");

    private final GrossController grossController;
    private final NetController netController;
    
    private TrayIcon trayIcon_;

    AppMainFrame() {
        super(APP_NAME);
        
        Image logo = Functions.getLogo(RecorderState.INIT);
        
        setIconImage(logo);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
        		if (SystemTray.isSupported()) {
	        	    try {
	        	    	SystemTray.getSystemTray().add(trayIcon_);
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
				setState(state); 
			}
		};

        netController = new NetController();
        netController.getTimeAction().setChangeListener(l);
        grossController = new GrossController(netController);
        grossController.getTimeAction().setChangeListener(l);
        
        netController.setRatioListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				String title = APP_NAME + " - " + netController.getRatio();
				setTitle(title);
				if (SystemTray.isSupported()) 
					trayIcon_.setToolTip(title);
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
            
        	trayIcon_ = new TrayIcon(logo, APP_NAME, /*popup*/null);
        	trayIcon_.setImageAutoSize(true);
        	
	    	ActionListener actionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
					setExtendedState(Frame.NORMAL);
					SystemTray.getSystemTray().remove(trayIcon_); 					
				}
			};       
			trayIcon_.addActionListener(actionListener);
        }        
        
		setJMenuBar(createMenuBar());
        add(createContent());
        pack();
    }

    private void exitApplication() {
        int result = JOptionPane.showConfirmDialog(Instance_,
                "Do you wish to exit the " + APP_NAME + "?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            storeProperties();
            if (SystemTray.isSupported()) {
            	SystemTray.getSystemTray().remove(trayIcon_);
            }
            dispose();
            System.exit(0);
        }
    }

    private void storeProperties() {
//	    if (grossController.getTimeAction().isStarted()) {
	        // stores data only if gross timer was started
	        Properties props = new Properties();
	        grossController.store(props);
	        netController.store(props);
	        FileOutputStream fos = null;
	        try {
	            fos = new FileOutputStream(INI_FILE);
	            props.store(fos, null);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (fos != null)
	                try {
	                    fos.close();
	                } catch (IOException e) {}
	        }
//	    } else if (INI_FILE.exists()) {
//	        // delete data file if user exits application normally
//	        // while gross timer was stopped  
//	        INI_FILE.delete();
//	    }
	}

	private void restoreProperties() {
	    if (INI_FILE.exists()) {
	        // restore settings if ini file presents in file system
	        Properties props = new Properties();
	        FileInputStream fis = null;
	        try {
	            fis = new FileInputStream(INI_FILE);
	            props.load(fis);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (fis != null)
	                try {
	                    fis.close();
	                } catch (IOException e) {}
	        }
	        grossController.restore(props);
	        netController.restore(props);
	        grossController.updateRatio();
	        // delete ini file after data loading
	        INI_FILE.delete();
	    }
	}

	private Action createNewDayAction() {
	    Action action = new AbstractAction("New Workday", Functions.getIcon("new")) {
	        public void actionPerformed(ActionEvent e) {
	        	netController.startNewDay();
	        	grossController.startNewDay();
	        }
	    };
	    action.putValue(Action.MNEMONIC_KEY, new Integer('N'));
	    return action;
	}

	private Action createExitAction() {
	    Action action = new AbstractAction("Exit") {
	        public void actionPerformed(ActionEvent e) {
	            exitApplication();
	        }
	    };
	    action.putValue(Action.MNEMONIC_KEY, new Integer('X'));
	    return action;
	}

	private Action createAboutAction() {
	    Action action = new AbstractAction("About...") {
	        public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(Instance_,
	                    new JLabel("<html><center>" + APP_NAME + " " + APP_VER + 
	                    		"<br>Copyright &copy; 2007-2013<br>by kontiky"),
	                    "About", JOptionPane.INFORMATION_MESSAGE);
	        }
	    };
	    action.putValue(Action.MNEMONIC_KEY, new Integer('A'));
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
        menu.add(new JMenuItem(createAboutAction()));
        return menu;
    }
    
    private JPanel createContent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(grossController.getViewComponent());
        panel.add(netController.getViewComponent());
        return panel;
    }
    
    private static AppMainFrame Instance_;
    
    private void setState(RecorderState state) {
    	Image logo = Functions.getLogo(state);
		if (SystemTray.isSupported()) {
			Instance_.trayIcon_.setImage(logo);
		}    	
		Instance_.setIconImage(logo);
    }

    public static void main(String... args) {
    	Instance_ = new AppMainFrame();
    	Instance_.restoreProperties();
    	Instance_.setLocationRelativeTo(null);
    	Instance_.setVisible(true);
    }
}
