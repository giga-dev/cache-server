package org.gigaspaces;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class FileService {

    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<String, Object>();
    private final SimpleDateFormat sfg;

    @Value("${localDir}")
    private String locations;

    public FileService() {
        sfg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    private void log(String s) {
        System.out.println(sfg.format(new Date()) + " - ThreadId["+Thread.currentThread().getId()+"] " + s);
    }

    public File get(String url) throws URISyntaxException, IOException {
        String filePath = extractPathFromUrl(url);

        log("> Requesting file " + filePath);

        File localFile = new File(locations, filePath);

        if (localFile.exists()) {
            log(">> File " + filePath +" already exists");
            return localFile;
        }

        Object fileLock;
        boolean newFile=false;
        synchronized (locks) {
            fileLock = locks.get(filePath);
            if (fileLock == null) {
                log("> Creating lock for " + filePath);
                newFile = true;
                fileLock = new Object();
                locks.put(filePath, fileLock);
            } else {
                log("> Using existing lock for " + filePath);
            }

        }

        log("> Retrieved lock for " + filePath);

        synchronized (fileLock) {
            try {
                if (localFile.exists()) {
                    log(">> File "+filePath+" has been downloaded, returning");
                    return localFile;
                } else {
                    log("> Starting to download " + filePath);
                    downloadFile(url, localFile);
                    log(">> Download finished for " + filePath);
                    return localFile;
                }
            } finally {
                if (newFile) {
                    //The one that created the lock also removes it
                    log(">> Removing lock for " + filePath + " - " + locks.keys());
                    locks.remove(filePath);

                    log(">>> Printing Keys: ");
                    locks.forEach((key, v) -> log(">>>> " + key));
                    log(">>> Finished printing keys");
                }
            }
        }
    }

    private String extractPathFromUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return uri.getPath();
    }

    private void downloadFile(String url, File target) throws IOException {
        try {
            long start = System.currentTimeMillis();
            log("> downloadFile - Downloading " + url + " to " + target);

            target.getParentFile().mkdirs();

            File tmp = new File(target.getName() + ".download");
            FileUtils.copyURLToFile(new URL(url), tmp, 10000, 10000);
            FileUtils.moveFile(tmp, target);

            long duration = System.currentTimeMillis() - start;
            log(">> downloadFile - Download of "+url+" has finished. Took: " + TimeUnit.MILLISECONDS.toSeconds(duration)+"s");
        } catch (IOException e) {
            log(">> downloadFile - Download of " + url +" has failed : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
