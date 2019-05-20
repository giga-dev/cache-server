package org.gigaspaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        final FileService fileService = new FileService();
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i=0; i<20; i++) {
            final int finalI = i;
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Got result for http://localhost/myfolder/myfile"+ (finalI %4 )+ " at " + fileService.get("http://localhost/myfolder/myfile"+ finalI %4));
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
