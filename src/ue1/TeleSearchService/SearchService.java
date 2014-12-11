package ue1.TeleSearchService;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.Stack;

/**
 * Kopfteil haben, der den Dateinamen, den Autor, das Datum und den Zweck der Programmierung
 * Filename : SearchService.java
 * Author   : Nils Kienöl
 * Date     : 09.10.2014
 * <p/>
 * This Program scan the input (system.in) search for a value in data.
 */
public class SearchService {

    // version
    private String version = "TeleSearchService 0.1";

    // menu commands
    private final String closeCommand = "q";
    private final String searchCommand = "s";

    // input
    Scanner in;

    // out
    PrintStream out = System.out;

    // testData
    String[][] searchList = {
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"},
            {"von Rymon-Lipinski", "1337"},
            {"Meier", "4711"},
            {"Schmitt", "0815"},
            {"Ökan", "4711"},
            {"von Hugo", "1337"},
            {"Müller", "4711"},
            {"Meier", "0816"},
            {"Sw0rdiX", "1337"}
    };
    long startTime;

    private String[][] memory;

    private Stack<String> stack = new Stack<String>();

    public SearchService() {

    }

    public static void main(String[] args) throws InterruptedException {
        SearchService service = null;
        service = new SearchService();
        service.start();
    }

    public void start() throws InterruptedException {

        // init scanner
        in = new Scanner(System.in);

        out.println("\t" + version + " by Nils Kienöl (797863)");

        // start loop, endless
        while (true) {

            // show Menu
            out.println("\n\tCommand Menu");
            out.println("\ts :\tsearch");
            out.println("\tq :\tquit");
            out.println();

            // text for user
            out.print("Input");
            out.print("\t\t\t\t:\t");

            // read first line
            String line = in.nextLine();

            // check for input
            if (line.length() > 0) {

                // check for quit command
                if (line.length() == 1) {
//                    out.printf("%s\t:\t%s", "Output", "TeleSearchService will closed...");
                    if (line.contains(closeCommand)) {
                        out.println("\nTeleSearchService will close...");
                        break;
                    }
                    if (line.contains(searchCommand)) {

                        // start search loop
                        while (true) {

                            // text & input
                            out.println("\nSearch format : String(optional) String Nummber");
                            out.print("Input(search)");
                            out.print("\t\t:\t");

                            // get first line
                            String searchLine = in.nextLine();

                            // set new scanner vor split the line in commands
                            Scanner valueReader = new Scanner(searchLine);

                            String name = "";
                            String nr = "";

                            int counter = 0;
                            // check input for variables
                            while (valueReader.hasNext()) {
                                if (counter > 3) {
                                    out.println("too many variables...");
                                    break;
                                }
                                if (valueReader.hasNextInt()) {
                                    nr = valueReader.next();
                                } else {
                                    if (name.isEmpty()) {
                                        name = valueReader.next();
                                    } else {
                                        name = name + " " + valueReader.next();
                                    }
                                }
                                counter++;
                            }

                            // close valueReader
                            valueReader.close();
                            stack.clear();

                            startTime = System.nanoTime();

                            Thread threadSearchName = null;
                            Thread threadSearchNr = null;

                            // check if name not null & not empty -> start thread
                            if (name != null && !name.isEmpty()) {

                                // init search thread
                                SearchThread searchThread = new SearchThread(stack, name, 0, searchList, startTime);
                                threadSearchName = new Thread(searchThread);
                                threadSearchName.start();
//                                out.println("Searching for a Name (" + name + ") in " + thread.getName());
                            }
                            // check if nr not null & not empty -> start thread
                            if (nr != null && !nr.isEmpty()) {

                                // init search thread
                                SearchThread searchThread = new SearchThread(stack, nr, 1, searchList, startTime);
                                threadSearchNr = new Thread(searchThread);
                                threadSearchNr.start();
//                                out.println("Searching for Number (" + nr + ") in " + thread.getName());
                            }
                            // join thread when alive
                            if (threadSearchName != null && threadSearchName.isAlive()) {
                                threadSearchName.join();
//                                Thread.sleep( 2000 );
//                                threadSearchName.interrupt();
                            }
                            // join thread when alive
                            if (threadSearchNr != null && threadSearchNr.isAlive()) {
                                threadSearchNr.join();
//                                Thread.sleep( 2000 );
//                                threadSearchNr.interrupt();
                            }

                            long endTime = System.nanoTime();

                            // print result if something found
                            if (stack.size() >= 1) {
                                out.println("\nFound " + stack.size() + " entrys...");

                                out.printf("\n\t%-20s %-10s %-10s %-20s %-10s\n", "Name", "Nummer", "Thread", "SearchValue", "Time");
                                out.printf("\t%s \n", "--------------------------------------------------------------------");

                                while (!stack.isEmpty()) {
                                    String[] result = stack.pop().split(";");
                                    out.printf("\t%-20s %-10s %-10s %-20s %-10s\n", result[0], result[1], result[2], result[3], result[4]);
                                }

                                out.println("\n\tSearch duration : " + ((endTime - startTime) / 1000000) + "ms (" + endTime + ")");

                            } else {
                                out.println("\n\tSearch for \" " + searchLine + " \" was failed : no results");
                            }

                            // ask for search again
                            out.println();
                            out.print("Search again? [y]es\t:\t");

                            String again = in.nextLine();
                            if (again.length() == 1 && again.matches("([y])")) {
                                continue;
                            } else {
                                break;
                            }

                        }
                    }
                }
            }
        }
        // close scanner
        in.close();
    }
}

class SearchThread implements Runnable {

    private final int cell;
    // out
    PrintStream out = System.out;
    private final long startTime;
    protected final Stack<String> stack;
    private final String searchValue;
    private final String[][] searchData;

    public SearchThread(Stack<String> stack, String searchValue, int cell, String[][] searchData, long startTime) {
        this.stack = stack;
        this.searchValue = searchValue;
        this.searchData = searchData;
        this.startTime = startTime;
        this.cell = cell;
    }

    @Override
    public void run() {

        String threadName = Thread.currentThread().getName();

        // thread name
//        out.println(Thread.currentThread().getName());

        // search data cell
        for (String[] searchRow : searchData) {

            // search for searchValue
            if (searchRow[cell].equals(searchValue)) {

                stack.add(searchRow[0] + ";" + searchRow[1] + ";" + threadName + ";" + searchValue + ";" + ((System.nanoTime() - startTime) / 1000000)
                        + "ms");

            }
        }
    }
}