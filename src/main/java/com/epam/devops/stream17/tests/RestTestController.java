package com.epam.devops.stream17.tests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestTestController {

    @GetMapping("/test/cpuOverload")
    void testCpuOverload(@RequestParam String duration) throws Exception {
        System.out.println("Duration: " + duration);
        long testDuration = Long.parseLong(duration);
        Runnable r = () -> {
            long startTime = System.currentTimeMillis();
            try {
                System.out.println("Started! " + System.currentTimeMillis());
                long duration1 = testDuration;
                String text = "Encrypt me";
                String key = "XXX12345xxx54321";
                Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);

                while ((System.currentTimeMillis() - startTime) < duration1) {
                    byte[] encrypted = cipher.doFinal(text.getBytes());
                    cipher.update(encrypted);
                    //Thread.sleep(1L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("DONE in " + (System.currentTimeMillis() - startTime));
            }
        };
        Thread cpuLoadThread = new Thread(r);
        cpuLoadThread.start();

    }

    @GetMapping("/test/heapOverflow")
    void testHeapOverflow() throws Exception {
        class OOM {
            private byte[] data;

            public OOM() {
                this.data = new byte[1024 * 1024];
            }
        }
        List<OOM> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 30000) {
            try {
                while (true) {
                    list.add(new OOM());
                }
            } catch (Throwable t) {

            }
            Thread.sleep(1000L);
        }
    }
}
