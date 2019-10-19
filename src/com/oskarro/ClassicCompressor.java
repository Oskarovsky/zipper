package com.oskarro;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class ClassicCompressor extends Compressor {

    private ZipOutputStream zipOutputStream;

    ClassicCompressor(String inputDir, String outputFile) {
        super(inputDir, outputFile);
    }

    private void compressToZip(File file) throws IOException {
        ZipEntry zipFileEntry = new ZipEntry(file.getName());
        zipOutputStream.putNextEntry(zipFileEntry);     // begins writing a new ZIP file
        FileInputStream fileInputStream = new FileInputStream(file);
        IOUtils.copy(fileInputStream, zipOutputStream);
        fileInputStream.close();
        incrementCount();
    }

    @Override
    public void run() {
        try {
            // creating output file (if it doesn't exist)
            File outputFile = new File(getOutputFile());
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(getOutputFile());
            zipOutputStream = new ZipOutputStream(fileOutputStream);

            // iterating through files which are in the input directory
            // then copying anything that's a file
            File[] files = new File(getInputDir()).listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    compressToZip(file);
                }
            }
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
