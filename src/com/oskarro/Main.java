package com.oskarro;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        args = new String[3];

        args[0] = "async";
        args[1] = "/home/oskarro/Developer/MyProjects/javaProjects/Zipper/src/com/oskarro/resource/";
        args[2] = "oskar.zip";

        if (args.length != 3) {
            System.out.println("Wymagane są 3 argumenty: [sync/async] [folder do kompresji] [plik wyjściowy] ");
        }

        Compressor compressor;

        System.out.println("Wybierz sposób kompresji:" +
                "\n 1 - sync" +
                "\n 2 - async");
        System.out.print("Twój wybór: ");
        Scanner scanner = new Scanner(System.in);
        int compressingType = scanner.nextInt();
        System.out.print("\nPodaj nazwę koncową pliku: ");
        String compressingName = scanner.next();



        if(compressingType == 1) {
            System.out.println("Uruchamiamy synchroniczne kompresowanie danych");
            compressor = new ClassicCompressor(args[1], compressingName+".zip");
        } else {
            System.out.println("Uruchamiamy wielowątkowe kompresowanie danych");
            compressor = new MultiCompressor(args[1], compressingName+".zip");
        }

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

        System.out.println("Kompresja zakonczona!");
        System.out.printf("Kompresja %s plików została wykonana w %s sekundy", compressor.getCount(), secondElapsed);


    }
}
