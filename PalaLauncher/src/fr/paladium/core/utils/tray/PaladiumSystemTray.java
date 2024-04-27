package fr.paladium.core.utils.tray;

import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import fr.paladium.Launcher;
import fr.paladium.core.distribution.dto.DistributionOS;
import fr.paladium.core.utils.debugger.Debugger;
import fr.paladium.core.utils.desktop.DesktopUtils;
import fr.paladium.core.utils.desktop.NotificationHelper;
import fr.paladium.core.utils.diagnostic.Diagnostic;
import fr.paladium.core.utils.io.FileUtils;
import fr.paladium.core.utils.io.OsCheck;
import fr.paladium.ui.frame.LauncherFrame;
import fr.paladium.ui.utils.IconUtil;

public class PaladiumSystemTray {
    private static TrayIcon trayIcon;
    private static JMenuBar menuBar;

    public static void create() {
        Debugger.pushState("init_tray");

        try {
            ImageIcon image = IconUtil.getImage();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            menuBar = new JMenuBar();
            JMenu menuBarTab = new JMenu("Boite à outils");
            menuBar.add(menuBarTab);

            JMenuItem appdataItem = new JMenuItem("AppData");
            appdataItem.addActionListener(e -> {
                File appdata = OsCheck.getAppData();
                if (appdata.exists()) {
                    try {
                        Desktop.getDesktop().open(appdata);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            menuBarTab.add(appdataItem);

            JMenuItem dataItem = new JMenuItem("LauncherData");
            dataItem.addActionListener(e -> {
                File install = new File(OsCheck.getAppData(), "paladium-games-inject");
                if (install.exists()) {
                    try {
                        Desktop.getDesktop().open(install);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            menuBarTab.add(dataItem);
            menuBarTab.addSeparator();

            JMenuItem supportItem = new JMenuItem("Support");
            supportItem.addActionListener(e -> DesktopUtils.openURL("https://discord.gg/J2Yh9634zj"));
            menuBarTab.add(supportItem);

            JMenuItem discordItem = new JMenuItem("Discord");
            discordItem.addActionListener(e -> DesktopUtils.openURL("https://discord.gg/paladium"));
            menuBarTab.add(discordItem);

            JMenuItem siteItem = new JMenuItem("Site");
            siteItem.addActionListener(e -> DesktopUtils.openURL("https://paladium-pvp.fr"));
            menuBarTab.add(siteItem);
            menuBarTab.addSeparator();

            JMenuItem diagItem = new JMenuItem("Diagnostic");
            diagItem.addActionListener(e -> Diagnostic.generate());
            menuBarTab.add(diagItem);

            JMenuItem resetItem = new JMenuItem("Réinstaller");
            resetItem.addActionListener(e -> {
                NotificationHelper.sendSystemNotification("Réinstallation en cours.", TrayIcon.MessageType.INFO);
                File install = new File(OsCheck.getAppData(), "paladium-games-inject");
                try {
                    if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
                        File tempLauncherFile = new File(install, "bootstrap.temp.exe");
                        org.apache.commons.io.FileUtils.copyURLToFile(new URL("https://download.paladium-pvp.fr/games/launcher/Paladium%20Games%20Launcher.exe"), tempLauncherFile);
                        if (tempLauncherFile.exists()) {
                            Runtime.getRuntime().exec(tempLauncherFile.getAbsolutePath());
                        } else {
                            NotificationHelper.sendSystemNotification("Impossible de télécharger la mise à jour.", TrayIcon.MessageType.ERROR);
                            return;
                        }
                    } else if (OsCheck.getOperatingSystemType() == DistributionOS.MACOS) {
                        DesktopUtils.openURL("https://download.paladium-pvp.fr/games/launcher/Paladium%20Games%20Launcher.jar");
                    } else {
                        NotificationHelper.sendSystemNotification("Votre système d'exploitation n'est pas supporté.", TrayIcon.MessageType.ERROR);
                        return;
                    }
                    org.apache.commons.io.FileUtils.forceDeleteOnExit(install);
                    Launcher.close(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    NotificationHelper.sendSystemNotification("Impossible de supprimer le dossier d'installation.", TrayIcon.MessageType.ERROR);
                }
            });
            menuBarTab.add(resetItem);

            JMenuItem exitItem = new JMenuItem("Quitter");
            exitItem.addActionListener(e -> Launcher.close(0));
            menuBarTab.add(exitItem);

            if (SystemTray.isSupported()) {
                trayIcon = new TrayIcon((image != null) ? image.getImage() : null, "Paladium Games Launcher");
                trayIcon.addActionListener(e -> {
                    LauncherFrame.getInstance().setVisible(true);
                    LauncherFrame.getInstance().setState(0);
                });
                trayIcon.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger() && !PaladiumSystemTray.getTrayMenu().isVisible()) {
                            PaladiumSystemTray.getTrayMenu().setLocation(e.getX(), e.getY());
                            PaladiumSystemTray.getTrayMenu().setInvoker(PaladiumSystemTray.getTrayMenu());
                            PaladiumSystemTray.getTrayMenu().setVisible(true);
                        }
                    }
                });
                Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
                    if (event instanceof MouseEvent) {
                        MouseEvent mouseEvent = (MouseEvent) event;
                        if (mouseEvent.getID() == MouseEvent.MOUSE_RELEASED && !mouseEvent.getSource().equals(getTrayMenu()) && !mouseEvent.getSource().equals(trayIcon)) {
                            getTrayMenu().setVisible(false);
                        }
                    }
                }, MouseEvent.MOUSE_RELEASED);
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Paladium Games Launcher");
                SystemTray.getSystemTray().add(trayIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Debugger.popState();
    }

    public static TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public static JPopupMenu getTrayMenu() {
        return menuBar.getMenu(0).getPopupMenu();
    }

    public static JMenuBar getMenuBar() {
        return menuBar;
    }
}
