package ue1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Author   : Nils Kienöl
 * Date     : 07.10.2014
 */
public class Telefonverzeichnis {


    //    "Meier 4711",
//            "Schmitt 0815",
//            "Müller 4711",
//            "Meier 0816"
    public final static Stack stack = new Stack();
    public final static String[][] telefonliste = {
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Müller", "4711"},
            {"Meier", "0816"}
    };

    public static void main(String[] args) {
        zeigeMenu();
        startScanner();

    }

    public static void zeigeMenu() {
        System.out.println("#############################################");
        System.out.println();
        System.out.println("Telefonverzeichnis");
        System.out.println();
        System.out.println("Wählen Sie eine Option aus!");
        System.out.println("\t0: Programm beenden.");
        System.out.println("\t1: Nach Namen suchen.");
        System.out.println("\t2: Nach Nummer suchen.");
        System.out.println("\t3: Nach Name & Nummer suchen.");
        System.out.println();
        System.out.println("#############################################");
        System.out.print("Option : ");
    }

    public static void beenden() {
        System.out.println("Danke für die Nutzung des Telefonverzeichnis-Dienstes...");
    }

    public static void startScanner() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int option = scanner.nextInt();
            if (option == 0) {
                beenden();
                break;
            } else {
                options(option);
                break;
            }
//            System.out.println("Ausgabe : " + option);
        }
        scanner.close();
    }
    public static void continueSearch(){

    }

    public static void searchForName() {
        System.out.print("Name eingeben : ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String searchValue = scanner.next();
            Sucher searchWorker = new Sucher(stack, searchValue, telefonliste);
            Thread searchThread = new Thread(searchWorker);
            searchThread.start();
            try {
                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Ausgabe : " + searchWorker.stack);
            break;
        }
        scanner.close();
//        zeigeMenu();
    }

    public static void options(int option) {
        switch (option) {
            case 1:
                searchForName();
                break;
            case 2:
                System.out.print("Ausgabe : " + option);
                break;
            case 0:
                System.out.print("Ausgabe : " + option);
                break;
            default:
                System.out.println("Falsche Eingabe!");
                break;
        }
    }

}

class Sucher implements Runnable {

    protected final Stack stack;
    private final String searchValue;
    private final String[][] searchList;

    public Sucher(Stack stack, String searchValue, String[][] searchList) {
        this.stack = stack;
        this.searchValue = searchValue;
        this.searchList = searchList;
    }

    @Override
    public void run() {
        System.out.println("Ich bin " + Thread.currentThread().getName());
        for (String[] categorie : searchList) {
            if (categorie[0].contains(searchValue)) {
                stack.add(categorie[0].toString()+ " "+categorie[1].toString());
            }
        }

    }
}
