package fr.paladium.core.controller;
import com.google.gson.JsonElement;

import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import fr.paladium.core.distribution.GameDistribution;
import fr.paladium.core.distribution.dto.DistributionFile;
import fr.paladium.core.distribution.dto.DistributionModel;
import fr.paladium.core.distribution.dto.DistributionOS;
import fr.paladium.core.distribution.property.Properties;
import fr.paladium.core.exception.DiskSpaceException;
import fr.paladium.core.utils.desktop.NotificationHelper;
import fr.paladium.core.utils.io.FileUtils;
import fr.paladium.core.utils.io.OsCheck;
import fr.paladium.core.utils.json.JsonOnlineParser;
import fr.paladium.core.utils.net.ByteDownloadInputStream;
import fr.paladium.core.utils.net.Telemetry;

public class DistributionController {
    private static final Pattern URL_PATTERN =
            Pattern.compile("^(?:http:\\/\\/|www\\.|https:\\/\\/)([^\\/]+)", 8);
    private static DistributionController instance;
    private GameDistribution distribution;

    public GameDistribution getDistribution() {
        return this.distribution;
    }
    public GameDistribution parseDistribution(String url) throws IOException {
        String domain = URL_PATTERN.matcher(url).replaceAll("$1").split("/")[0];
        if (!domain.equals("download.paladium-pvp.fr")) {
            throw new IllegalArgumentException(
                    "Invalid url, please use a valid url.");
        }

        return (GameDistribution) JsonOnlineParser.GSON.fromJson(
                (JsonElement) JsonOnlineParser.parse(url),
                GameDistribution.class);
    }

    public GameDistribution loadDistribution(String url) throws Exception {
        String domain = URL_PATTERN.matcher(url).replaceAll("$1").split("/")[0];
        if (!domain.equals("download.paladium-pvp.fr")) {
            throw new IllegalArgumentException(
                    "Invalid url, please use a valid url.");
        }

        this.distribution = (GameDistribution) JsonOnlineParser.GSON.fromJson(
                (JsonElement) JsonOnlineParser.parse(url),
                GameDistribution.class);
        if (this.distribution == null) {
            throw new IllegalStateException(
                    "Distribution is null, please check your url.");
        }

        this.distribution.init();
        return this.distribution;
    }

    public void runDownloader(String distributionToken) throws Exception {
        if (this.distribution == null) {
            throw new IllegalStateException(
                    "Distribution is not loaded, please use parseDistribution() before.");
        }

        if (distributionToken == null) {
            distributionToken = "";
        }

        GameController.getInstance().setDownloadingSpeed("(vérification)");
        System.out.println("Constructing distribution at "
                + this.distribution.getRootDir().getAbsolutePath());

        if (!this.distribution.getRootDir().exists()) {
            if (!this.distribution.getRootDir().mkdirs()) {
                throw new IllegalAccessException(
                        "Access denied to create directory "
                        + this.distribution.getRootDir().getAbsolutePath());
            }
            this.distribution.getRootDir().setExecutable(true, false);
        }

        DistributionOS os = OsCheck.getOperatingSystemType();

        GameController.getInstance().sendProgress(15);
        System.out.println("Constructing models...");
        buildModels(os);
        System.out.println("Models constructed. ("
                + this.distribution.getModels().size() + " models)");

        GameController.getInstance().sendProgress(25);
        System.out.println("Checking files...");
        LinkedList<DistributionFile> compatibleFiles = getCompatibleFiles(os);
        Map.Entry<Long, Set<DistributionFile>> entry =
                findInvalidFileHashes(compatibleFiles);
        Set<DistributionFile> filesToDownload = entry.getValue();
        long bytesToDownload = ((Long) entry.getKey()).longValue();
        System.out.println("Files checked. (" + filesToDownload.size()
                + " files to download)");

        GameController.getInstance().sendProgress(50);
        System.out.println("Downloading files...");
        if (!filesToDownload.isEmpty()) {
            GameController.getInstance().setDownloadingSpeed(
                    "(téléchargement)");
            NotificationHelper.sendSystemNotification("Téléchargement de "
                            + filesToDownload.size() + " fichiers ("
                            + org.apache.commons.io.FileUtils
                                      .byteCountToDisplaySize(bytesToDownload)
                            + ")",
                    TrayIcon.MessageType.INFO);
            downloadFiles(distributionToken, filesToDownload, bytesToDownload);
            Telemetry.collect(Telemetry.Type.INFO_DOWNLOAD);
        }

        for (DistributionFile file : compatibleFiles) {
            DistributionModel model = this.distribution.getModelByFile(file);

            if (((Boolean) file.getProperty("classpath", model))
                            .booleanValue()) {
                File fileDir = new File(model.getDestDir(), file.getPath());
                this.distribution.addFileToClasspath(fileDir);
            }
        }
    }

    private void buildModels(DistributionOS os) {
        for (int i = 0; i < this.distribution.getModels().size(); i++) {
            DistributionModel model = this.distribution.getModels().get(i);

            if (model.getOs() == null || model.isCompatible(os)) {
                File modelDir = new File(
                        this.distribution.getRootDir(), model.getDest());
                model.setDestDir(modelDir);
                if (!modelDir.exists()) {
                    modelDir.mkdirs();
                } else {
                    List<String> whitelist =
                            (List<String>) Properties.getProperty(
                                    "whitelist", model);
                    List<Pattern> patterns = new ArrayList<>();
                    if (whitelist != null && !whitelist.isEmpty()) {
                        whitelist.forEach(
                                s -> patterns.add(Pattern.compile(s)));
                    }

                    for (File file : FileUtils.listFileTree(modelDir)) {
                        String relativePath =
                                file.getAbsolutePath()
                                        .substring(modelDir.getAbsolutePath()
                                                           .length()
                                                + 1)
                                        .replace("\\", "/");
                        if (patterns.stream().noneMatch(
                                    p -> p.matcher(relativePath).matches())
                                && this.distribution.getFiles().stream().noneMatch(
                                        f
                                        -> file.getAbsolutePath().contains(
                                                f.getPath()))) {
                            file.delete();
                            System.out.println("File " + file.getAbsolutePath()
                                    + " is not in whitelist, removing it...");
                        }
                    }
                    GameController.getInstance().sendProgress(15
                            + (int) (i / this.distribution.getModels().size()
                                    * 10.0F));
                }
            }
        }
    }
    private void downloadFiles(String distributionToken,
            Set<DistributionFile> filesToDownload, long bytesToDownload)
            throws IOException, ExecutionException, TimeoutException,
                   InterruptedException {
        AtomicInteger downloadedFiles = new AtomicInteger();

        int numThreads = Math.min(Runtime.getRuntime().availableProcessors(),
                filesToDownload.size());
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<Boolean>> tasks = new ArrayList<>();
        AtomicBoolean downloading = new AtomicBoolean(true);
        Set<ByteDownloadInputStream> streams = ConcurrentHashMap.newKeySet();
        FileStore fileStore = FileSystems.getDefault().provider().getFileStore(
                this.distribution.getRootDir().toPath());
        long neededSpace = filesToDownload.stream()
                                   .mapToLong(DistributionFile::getSize)
                                   .sum();
        long freeSpace = fileStore.getUsableSpace();

        System.out.println("Disk info: " + neededSpace
                + " bytes needed on disk, " + freeSpace + " bytes available.");

        if (neededSpace > freeSpace) {
            throw new DiskSpaceException(freeSpace, neededSpace);
        }

        Thread downloadTracker = new Thread(() -> {
            long lastDownloadedBytes = 0L;

            long downloadSpeed = 0L;

            long lastUpdate = System.currentTimeMillis();

            while (downloading.get()) {
                long currentlyDownloadedBytes =
                        streams.stream()
                                .mapToLong(ByteDownloadInputStream::
                                                getDownloadedBytes)
                                .sum();
                downloadSpeed += currentlyDownloadedBytes - lastDownloadedBytes;
                if (System.currentTimeMillis() - lastUpdate >= 1000L) {
                    sendDownloadProgress(
                            currentlyDownloadedBytes, bytesToDownload);
                    setDownloadingSpeed(downloadSpeed);
                    lastUpdate = System.currentTimeMillis();
                    downloadSpeed = 0L;
                }
                lastDownloadedBytes = currentlyDownloadedBytes;
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for (DistributionFile file : filesToDownload) {
            DistributionModel model = this.distribution.getModelByFile(file);
            File fileDir = file.getFileDest();

            Callable<Boolean> downloadTask = () -> {
                System.out.println(
                        "Downloading file " + fileDir.getAbsolutePath() + "..");

                ByteDownloadInputStream downloadStream =
                        new ByteDownloadInputStream(file.getUrl(),
                                file.getSize(),
                                ((Boolean) file.getProperty(
                                         "download_token", model))
                                                .booleanValue()
                                        ? distributionToken
                                        : null);

                streams.add(downloadStream);

                if (downloadStream.startDownload(fileDir)) {
                    boolean checkMd5 =
                            ((Boolean) file.getProperty("check_sha1", model))
                                    .booleanValue();

                    if (checkMd5) {
                        if (file.getSize() != FileUtils.getFileSize(fileDir)) {
                            throw new FileSystemException("File "
                                    + fileDir.getAbsolutePath()
                                    + " has invalid size, please check your distribution file.");
                        }

                        if (!FileUtils.checkSha1(fileDir, file.getSha1())) {
                            throw new FileSystemException("File "
                                    + fileDir.getAbsolutePath()
                                    + " has invalid checksum, please check your distribution file.");
                        }
                    }
                    if ((((Boolean) file.getProperty("java", model))
                                        .booleanValue()
                                || ((Boolean) file.getProperty(
                                            "classpath", model))
                                           .booleanValue())
                            && !fileDir.setExecutable(true, false)) {
                        throw new IllegalStateException(
                                "Cannot set java executable, please check your distribution file.");
                    }
                    System.out.println("File " + fileDir.getAbsolutePath()
                            + " downloaded.");
                    downloadedFiles.incrementAndGet();
                    return Boolean.valueOf(true);
                }
                throw new TimeoutException("Cannot download file "
                        + fileDir.getAbsolutePath()
                        + ", please check your internet connection.");
            };
            tasks.add(downloadTask);
        }

        downloadTracker.start();
        List<Future<Boolean>> futures = executor.invokeAll(tasks);

        for (Future<Boolean> future : futures) {
            try {
                boolean downloadSuccessful =
                        ((Boolean) future.get()).booleanValue();

                if (downloadSuccessful) {
                    downloadedFiles.incrementAndGet();
                    continue;
                }
                throw new TimeoutException(
                        "Cannot download file, please check your internet connection.");
            } catch (ExecutionException ex) {
                downloading.set(false);
                throw new ExecutionException(ex);
            }
        }

        executor.shutdown();
        downloading.set(false);
        if (downloadedFiles.get() < filesToDownload.size()) {
            throw new IllegalStateException(
                    "Some files are not downloaded, please check your distribution file.");
        }
    }

    private LinkedList<DistributionFile> getCompatibleFiles(DistributionOS os) {
        LinkedList<DistributionFile> compatibleFiles = new LinkedList<>();

        for (int i = 0; i < this.distribution.getFiles().size(); i++) {
            DistributionFile file = this.distribution.getFiles().get(i);
            DistributionModel model = this.distribution.getModelByFile(file);

            if (model == null) {
                throw new IllegalStateException(
                        "Model is null, please check your distribution file.");
            }

            if (model.getOs() == null || model.isCompatible(os)) {
                if (file.getOs() == null || file.isCompatible(os)) {
                    compatibleFiles.add(file);
                }
            }
        }
        return compatibleFiles;
    }

    private Map.Entry<Long, Set<DistributionFile>> findInvalidFileHashes(
            LinkedList<DistributionFile> compatibleFiles)
            throws IOException, InterruptedException, ExecutionException {
        Set<DistributionFile> filesToDownload = new HashSet<>();
        AtomicLong bytesToDownload = new AtomicLong(0L);
        AtomicInteger fileCheckedCount = new AtomicInteger(0);

        List<Callable<Void>> tasks = new ArrayList<>();

        HashSet<DistributionFile> filesToCheck = new HashSet<>(compatibleFiles);

        for (Iterator<DistributionFile> iterator = filesToCheck.iterator();
                iterator.hasNext();) {
            DistributionFile file = iterator.next();
            DistributionModel model = this.distribution.getModelByFile(file);

            tasks.add(() -> {
                File fileDir = new File(model.getDestDir(), file.getPath());

                file.setFileDest(fileDir);

                boolean needDownload = true;

                if (!fileDir.exists()) {
                    fileDir.mkdirs();

                    fileDir.delete();
                } else {
                    boolean checkMd5 =
                            ((Boolean) file.getProperty("check_sha1", model))
                                    .booleanValue();

                    if (checkMd5
                            && (file.getSize() != FileUtils.getFileSize(fileDir)
                                    || !FileUtils.checkSha1(
                                            fileDir, file.getSha1()))) {
                        fileDir.delete();

                        System.out.println("File " + fileDir.getAbsolutePath()
                                + " is outdated, removing it...");
                    } else {
                        needDownload = false;
                    }
                }
                if (needDownload) {
                    filesToDownload.add(file);
                    bytesToDownload.addAndGet(file.getSize());
                }
                if (((Boolean) file.getProperty("classpath", model))
                                .booleanValue()) {
                    fileDir.setExecutable(true, false);
                }
                boolean hasJavaProperty =
                        ((Boolean) file.getProperty("java", model))
                                .booleanValue();
                if (hasJavaProperty
                        && (file.getName().equals("java")
                                || file.getName().equals("javaw.exe"))) {
                    this.distribution.setJava(fileDir);
                }
                if (hasJavaProperty) {
                    fileDir.setExecutable(true, false);
                }
                int count = fileCheckedCount.incrementAndGet();
                GameController.getInstance().sendProgress(
                        25 + (int) (count / compatibleFiles.size() * 25.0F));
                return null;
            });
        }

        if (!compatibleFiles.isEmpty()) {
            int numThreads =
                    Math.min(Runtime.getRuntime().availableProcessors(),
                            compatibleFiles.size());
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get();
            }
            executor.shutdown();
        }

        return new AbstractMap.SimpleEntry<>(
                Long.valueOf(bytesToDownload.get()), filesToDownload);
    }

    private void sendDownloadProgress(
            long downloadedBytes, long bytesToDownload) {
        int progress = 50
                + (int) ((float) downloadedBytes / (float) bytesToDownload
                        * 49.0F);
        GameController.getInstance().sendProgress(progress);
    }

    private void setDownloadingSpeed(long speed) {
        String speedStr =
                org.apache.commons.io.FileUtils.byteCountToDisplaySize(speed)
                + "/s";
        GameController.getInstance().setDownloadingSpeed("(" + speedStr + ")");
    }

    public static DistributionController getInstance() {
        if (instance == null) {
            instance = new DistributionController();
        }
        return instance;
    }
}