package com.oskarro;

abstract class Compressor extends Thread {
    private String inputDir;
    private String outputFile;
    private long count = 0;

    Compressor(String inputDir, String outputFile) {
        this.inputDir = inputDir;
        this.outputFile = outputFile;
    }

    public String getInputDir() {
        return inputDir;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public long getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public abstract void run();
}
