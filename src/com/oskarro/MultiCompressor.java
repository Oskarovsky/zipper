package com.oskarro;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiCompressor extends Compressor {
    private FileSystem compressorFileSystem;    // for creating file systems providing access to other types of file
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Visitor visitor = new Visitor();

    class Callable implements java.util.concurrent.Callable<Integer> {
        private Path pathToFile;

        Callable(Path pathToFile) {
            super();
            this.pathToFile = pathToFile;
        }

        @Override
        public Integer call() throws Exception {
            // copy input file to ZipFileSystem
            FileInputStream fileInputStream = new FileInputStream(pathToFile.toFile());
            OutputStream outputStream = Files.newOutputStream(compressorFileSystem.getPath(pathToFile.getFileName().toString()));

            IOUtils.copy(fileInputStream, outputStream);
            fileInputStream.close();
            outputStream.close();
            return 0;
        }
    }


    class Visitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path pathToFile, BasicFileAttributes attributes) throws IOException {
            // checking type of file - ignore directories and symbolic links
            if (attributes.isRegularFile()) {
                executorService.submit(new Callable(pathToFile));
                incrementCount();
            }
            return FileVisitResult.CONTINUE;
        }
    }

    MultiCompressor(String inputDir, String outputFile) {
        super(inputDir, outputFile);
    }

    private void createZipFileCompressor() throws IOException {
        // setup and config zip file compressor
        Map<String, String> compressorEnvironment = new HashMap<>();
        compressorEnvironment.put("create", "true");
        URI uriForZip = URI.create(String.format("jar:file:" + getInputDir() + "/%s", getOutputFile()));
        System.out.println(uriForZip);
        System.out.println(getOutputFile());
        compressorFileSystem = FileSystems.newFileSystem(uriForZip, compressorEnvironment);
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run() {
        try {
            this.createZipFileCompressor();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // walk input directory using our visitor class
        FileSystem fileSystem = FileSystems.getDefault();       // return the default FileSystems
        try {
            Files.walkFileTree(fileSystem.getPath(this.getInputDir()), visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // shutdown ExecutorService and block till tasks are complete
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            compressorFileSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
