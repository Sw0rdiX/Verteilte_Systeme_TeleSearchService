package ue1;

import java.util.Scanner;
import java.util.Stack;

/**
 * Author   : Nils Kienöl
 * Date     : 07.10.2014
 */
public class TeleService {

    Scanner scanner;
    String in = "";
    Stack<String> stack = new Stack<String>();
    String[][] searchList = {
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Meier", "081555"},
    };

    public static void main(String[] args) throws InterruptedException {
        TeleService teleService = new TeleService();
        teleService.start();
//        TeleService teleService = null;
//        teleService.start();
//        try { my = new TelefonServer(); } catch (IOException e) { e.printStackTrace(); }
//        try { teleService.start(); } catch (IOException e) { e.printStackTrace(); }
    }

    public void start() throws InterruptedException {

        // init scanner for read lines
        scanner = new Scanner(System.in);

        System.out.println("TeleService is ready...");

        // teleService in endless loop
        while (true) {

            showMenu();
            System.out.print("Input : ");

            in = scanner.next();
            if (checkInput(in)) {
                break;
            }
//            System.out.println("Output : " + in);
        }

        close();
    }

    private boolean checkInput(String in) throws InterruptedException {
        if (in.equals("0")) {
            return true;
        }
        if (in.equals("1")) {
            searchForValue(0);
        }
        if (in.equals("2")) {
            searchForValue(1);
        }
        if (in.equals("3")) {
            searchForValues(0,1);
        }
        return false;
    }

    private void searchForValues(int sc1, int sc2) {
        System.out.print("Search name : ");
        String in_1 = scanner.next();
        System.out.print("Search number : ");
        String in_2 = scanner.next();
        searchSize();
    }


    private void searchForValue(int searchColumn) throws InterruptedException {
        // searchColumn = 0 : name
        // searchColumn = 1 : number

        System.out.print("Search value : ");
        in = scanner.next();
        searchSize();
        TeleSearchTask teleSearchTask = new TeleSearchTask(stack, in, searchList, searchColumn);
        Thread thread = new Thread(teleSearchTask);
        thread.start();
        thread.join();
        while (!teleSearchTask.stack.empty()){
            Object result = teleSearchTask.stack.pop();
            System.out.println(result.toString());
        }
        if (searchAgain()){
            searchForValue(searchColumn);
        }
        return;
    }
    private void searchSize(){
        System.out.println("searching for value in " + searchList.length + " entrys...");
    }

    private boolean searchAgain() throws InterruptedException {
        System.out.print("Search again? [y/n]: ");
        in = scanner.next();
        if (in.equals("y")) {
            return true;
        }
        System.out.println("\nshow menu...\n");
        return false;
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("\t1 : search for a name");
        System.out.println("\t2 : search for a number");
        System.out.println("\t3 : search for a name & number");
        System.out.println("\t0 : quit");
        System.out.println();
    }

    private void close() {
        System.out.println("TeleService > : closing until next time...");
        scanner.close();
    }
}

class TeleSearchTask implements Runnable {

    protected final Stack stack;
    private final String searchValue;
    private final String[][] searchList;
    private final int searchColumn;

    public TeleSearchTask(Stack<String> stack, String searchValue, String[][] searchList, int searchColumn) {
        this.stack = stack;
        this.searchValue = searchValue;
        this.searchList = searchList;
        this.searchColumn = searchColumn;
    }

    @Override
    public void run() {
        System.out.println("\t\ti am " + Thread.currentThread().getName() + "...");
        int counter = 0;
        for (String[] searchElement : searchList) {
            if (searchElement[searchColumn].contains(searchValue)) {
                String result = "";
                for (String resultElement : searchElement) {
                    result = result +" "+ resultElement;
                }
                System.out.println(result);
                stack.add(result);
                counter++;
            }
        }
        System.out.println("\t\tfound " + counter + " entrys...");
    }
}
