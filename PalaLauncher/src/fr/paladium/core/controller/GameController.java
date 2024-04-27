package fr.paladium.core.controller;

import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import fr.paladium.core.distribution.GameDistribution;
import fr.paladium.core.distribution.dto.DistributionOS;
import fr.paladium.core.exception.DiskSpaceException;
import fr.paladium.core.utils.debugger.Debugger;
import fr.paladium.core.utils.desktop.NotificationHelper;
import fr.paladium.core.utils.game.GameBuilder;
import fr.paladium.core.utils.game.ProcessLogManager;
import fr.paladium.core.utils.io.OsCheck;
import fr.paladium.core.utils.io.RamUtils;
import fr.paladium.core.utils.memory.Ram;
import fr.paladium.core.utils.net.Telemetry;
import fr.paladium.core.utils.option.dto.GameDistributionOptions;
import fr.paladium.core.utils.popup.basic.PopupDiskSpace;
import fr.paladium.core.utils.popup.basic.PopupDownloadError;
import fr.paladium.core.utils.popup.basic.PopupInsufficientMemoryStart;
import fr.paladium.core.utils.popup.basic.PopupLowMemory;
import fr.paladium.core.utils.session.SessionStorage;
import fr.paladium.ui.frame.LauncherFrame;
import java.awt.TrayIcon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.util.concurrent.TimeoutException;
import javax.swing.SwingUtilities;

public class GameController {
    private static GameController instance;
    private Process process;

    public Process getProcess() {
        return this.process;
    }

    public void startGame(String distributionId, String distributionToken, String gameId, String url, MinecraftAccount minecraftAccount) {
        GameDistributionOptions options = OptionsController.getInstance().getDistributionOptions(distributionId);
        options.checkInvalidRam();

        new Thread(() -> {
            Debugger.pushState("start_game");
            System.out.println("Checking distribution...");
            getInstance().setDownloadingSpeed("");
            getInstance().sendProgress(0);
            GameDistribution distribution = null;
            try {
                distribution = DistributionController.getInstance().loadDistribution(url);
            } catch (Exception e) {
                e.printStackTrace();

                getInstance().setDownloadingSpeed("");

                getInstance().sendProgress(100);

                if (e instanceof IOException) {
                    NotificationHelper.sendNotification("Impossible d'accéder au serveur de téléchargement de minecraft.");
                    Telemetry.collect(Telemetry.Type.ERR_IO_DISTRIBUTION);
                } else {
                    NotificationHelper.sendNotification("Impossible de trouver la distribution du jeu.");
                    Telemetry.collect(Telemetry.Type.ERR_NO_DISTRIBUTION);
                }
                Debugger.popState();
                return;
            }
            getInstance().sendProgress(10);
            try {
                DistributionController.getInstance().runDownloader(distributionToken);
            } catch (AccessDeniedException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                NotificationHelper.sendNotification("Impossible de télécharger les fichiers, accès refusé.");
                Debugger.popState();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                NotificationHelper.sendNotification("Impossible de télécharger les fichiers, le lien du fichier semble incorrect.");
                Debugger.popState();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                NotificationHelper.sendNotification("Impossible de créer le dossier de téléchargement du jeu.");
                Debugger.popState();
                return;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                NotificationHelper.sendNotification("Impossible de mettre à jour vos fichiers, vérifier que votre jeu n'est pas déjà ouvert.");
                Debugger.popState();
                return;
            } catch (FileSystemException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                NotificationHelper.sendNotification("Le téléchargement de fichier semble corrompu.");
                Telemetry.collect(Telemetry.Type.ERR_DOWNLOAD_CHECKSUM);
                Debugger.popState();
                return;
            } catch (TimeoutException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                NotificationHelper.sendNotification("La connexion avec le serveur de téléchargement semble instable.");
                Debugger.popState();
                return;
            } catch (DiskSpaceException e) {
                e.printStackTrace();
                getInstance().setDownloadingSpeed("");
                getInstance().sendProgress(100);
                new PopupDiskSpace(e).show();
                Debugger.popState();
                return;
            } catch (Exception e) {
                e.printStackTrace();

                getInstance().setDownloadingSpeed("");

                getInstance().sendProgress(100);

                new PopupDownloadError().show();

                Debugger.popState();
                return;
            }
            SessionStorage.getInstance().setItem("game_" + gameId + "_installed", true);

            getInstance().setDownloadingSpeed("");

            getInstance().sendProgress(100);

            Debugger.popState();
            GameDistribution finalDistribution = distribution;
            SwingUtilities.invokeLater(() -> {
                // Your code to launch the game UI here, for example:
                // new GameLauncher(finalDistribution).launch();
            });
        }, "Launcher/Game").start();
    }

    private boolean checkArguments(GameDistributionOptions options) {
        int xms = options.getMemory().getMin() * 1024;
        int xmx = options.getMemory().getMax() * 1024;

        if (xms > xmx) {
            NotificationHelper.sendNotification("Votre configuration mémoire vive (ram) est incorrecte, la valeur minimale ne peut pas être supérieure à la valeur maximale.");
            Telemetry.collect(Telemetry.Type.ERR_MEM_CONFIG);
            return false;
        }

        if (xms < 1024) {
            NotificationHelper.sendSystemNotification("Il est fortement déconseillé d'allouer moins de 1Go de ram au jeu.", TrayIcon.MessageType.WARNING);
        }

        Ram ram = RamUtils.getRam();
        long freeMemory = ram.getFreeMemory();

        if (freeMemory < 536870912L) {
            PopupInsufficientMemoryStart popup = new PopupInsufficientMemoryStart(freeMemory);
            popup.show();
            Telemetry.collect(Telemetry.Type.ERR_MEM_START);
            return false;
        }

        if (freeMemory < 1073741824L) {
            PopupLowMemory popupLowMemory = new PopupLowMemory(freeMemory);
            popupLowMemory.show();
        }

        options.checkInvalidRam();

        if (xms <= 0) {
            NotificationHelper.sendSystemNotification("Vous n'avez pas assez de ram sur votre ordinateur.", TrayIcon.MessageType.ERROR);
            return false;
        }

        return true;
    }
/*     */   private void parseArguments(GameDistributionOptions options, GameDistribution distribution, MinecraftAccount minecraftAccount) {
/* 234 */     String[] newGameArgs = new String[(distribution.getArguments().getGame()).length];
/* 235 */     for (int i = 0; i < (distribution.getArguments().getGame()).length; i++) {
/* 236 */       String arg = distribution.getArguments().getGame()[i];
/* 237 */       String newArg = parseArgument(arg, options, distribution, minecraftAccount);
/* 238 */       newGameArgs[i] = newArg;
/*     */     } 
/* 240 */     distribution.getArguments().setGame(newGameArgs);
/*     */     
/* 242 */     String[] newJvmArgs = new String[(distribution.getArguments().getJvm()).length];
/* 243 */     for (int j = 0; j < (distribution.getArguments().getJvm()).length; j++) {
/* 244 */       String arg = distribution.getArguments().getJvm()[j];
/* 245 */       String newArg = parseArgument(arg, options, distribution, minecraftAccount);
/* 246 */       newJvmArgs[j] = newArg;
/*     */     } 
/* 248 */     distribution.getArguments().setJvm(newJvmArgs);
/*     */   }
/*     */   
/*     */   private String parseArgument(String arg, GameDistributionOptions options, GameDistribution distribution, MinecraftAccount minecraftAccount) {
/* 252 */     return arg
/* 253 */       .replace("${os_lib_ext}", (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) ? "dll" : "dylib")
/* 254 */       .replace("${username}", minecraftAccount.getName())
/* 255 */       .replace("${version}", distribution.getMcVersion())
/* 256 */       .replace("${gameDir}", distribution.getRootDir().getAbsolutePath())
/* 257 */       .replace("${game_directory}", distribution.getRootDir().getAbsolutePath())
/* 258 */       .replace("${assetsDir}", distribution.getRootDir().getAbsolutePath() + File.separator + "assets")
/* 259 */       .replace("${assetIndex}", distribution.getMcVersion())
/* 260 */       .replace("${uuid}", minecraftAccount.getId())
/* 261 */       .replace("${accessToken}", minecraftAccount.getAccessToken())
/* 262 */       .replace("${userProperties}", "{}")
/* 263 */       .replace("${userType}", "msa")
/* 264 */       .replace("${options.library.path}", distribution.getRootDir().getAbsolutePath() + File.separator + "natives" + File.separator + distribution.getMcVersion())
/* 265 */       .replace("${options.memory.min}", (options.getMemory().getMin() * 1024) + "M")
/* 266 */       .replace("${options.memory.max}", (options.getMemory().getMax() * 1024) + "M")
/* 267 */       .replace("/", File.separator)
/* 268 */       .replace("${slash}", "/")
/* 269 */       .replace("${backslash}", "\\");
/*     */   }
/*     */   
/*     */   public void sendProgress(int progress) {
/* 273 */     LauncherFrame.getInstance().getBuilder().execute("window.setProgress(" + progress + ");");
/*     */   }
/*     */   
/*     */   public void setDownloadingSpeed(String speed) {
/* 277 */     LauncherFrame.getInstance().getBuilder().execute("window.downloadingSpeed(\"" + speed + "\");");
/*     */   }
/*     */   
/*     */   public static GameController getInstance() {
/* 281 */     if (instance == null) {
/* 282 */       instance = new GameController();
/*     */     }
/* 284 */     return instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\controller\GameController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */