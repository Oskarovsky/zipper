package com.oskarro;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String pathToCompressingFile = "/home/oskarro/Developer/MyProjects/javaProjects/Zipper/src/com/oskarro/resource/";

        Compressor compressor;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wybierz sposób kompresji:" +
                "\n 1 - sync" +
                "\n 2 - async");
        System.out.print("Twój wybór: ");
        String compressingType = scanner.next();

        while (!compressingType.equals("1") && !compressingType.equals("2")) {
            System.out.println("Opcja \"" + compressingType + "\" nie istnieje!");
            System.out.print("Twój wybór: ");
            compressingType = scanner.next();
        }
        System.out.print("\nPodaj nazwę koncową pliku: ");
        String compressingName = scanner.next();

        System.out.println("================");

        if(compressingType.equals("1")) {
            System.out.println("Uruchamiamy synchroniczne kompresowanie danych");
            compressor = new ClassicCompressor(pathToCompressingFile, compressingName+".zip");
        } else if(compressingType.equals("2")) {
            System.out.println("Uruchamiamy wielowątkowe kompresowanie danych");
            compressor = new MultiCompressor(pathToCompressingFile, compressingName+".zip");
        } else {
            System.out.println("Opcja \"" + compressingType + "\" nie istnieje!" +
                    "\nSystem zakończył działanie");
            return;
        }

        System.out.println("================");
        System.out.println("Rozpoczynamy kompresowanie");
        long startTime = System.nanoTime();

        compressor.start();

        try {
            compressor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Kompresowanie przerwane!");
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double secondElapsed = (double) elapsedTime / 1000000000.0;

        System.out.println("================");
        System.out.println("Kompresja zakonczona!");
        //System.out.printf("Kompresja %s plików została wykonana w %s sekundy", compressor.getCount(), secondElapsed);
    }

}
