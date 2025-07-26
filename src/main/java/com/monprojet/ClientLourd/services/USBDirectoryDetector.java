package com.monprojet.ClientLourd.services;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class USBDirectoryDetector {
    private static final List<String> LINUX_MOUNT_POINTS = 
        Arrays.asList("/media", "/mnt", "/run/media", "/Volumes");
    private static final String CERTIFICATE_FILENAME = "certifica.p7s";

    /**
     * Detects USB drives containing the signature file across OSes
     */
    public static File findUSBDrive() {
        // Check all potential root locations
        for (File root : getSystemRoots()) {
            File usbDrive = scanRootForCertificate(root);
            if (usbDrive != null) {
                return usbDrive;
            }
        }
        return null;
    }

    private static List<File> getSystemRoots() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Arrays.stream(File.listRoots())
                .filter(drive -> drive.getAbsolutePath().matches("[D-Z]:\\\\"))
                .toList();
        } else {
            return LINUX_MOUNT_POINTS.stream()
                .map(File::new)
                .filter(File::exists)
                .toList();
        }
    }

    private static File scanRootForCertificate(File root) {
        try {
            // Check root directly
            if (hasCertificateFile(root)) {
                return root;
            }

            // Check subdirectories (for Linux)
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory() && hasCertificateFile(child)) {
                        return child;
                    }
                }
            }
        } catch (SecurityException e) {
            System.err.println("Access denied scanning: " + root);
        }
        return null;
    }

    private static boolean hasCertificateFile(File directory) {
        File certFile = new File(directory, CERTIFICATE_FILENAME);
        return directory.canRead() && 
               directory.canWrite() && 
               certFile.exists() && 
               certFile.canRead();
    }
}