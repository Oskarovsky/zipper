package com.oskarro;

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Wymagane są 3 argumenty: [sync/async] [folder do kompresji] [plik wyjściowy] ");
        }

        Compressor compressor;

        if(args[0].equals("sync")) {
            System.out.println("Uruchamiamy synchroniczne kompresowanie danych");
            compressor = new ClassicCompressor(args[1], args[2]);
        }


    }
}
