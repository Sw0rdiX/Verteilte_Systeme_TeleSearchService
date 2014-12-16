//package ue2;


/**
 * Filename : HttpRequestTeleService.java
 * Author   : Nils Kienöl
 * Date     : 09.10.2014
 * <p/>
 * This Program combines a HttpRequest Server and a TeleSearchService.
 * The HttpRequest Server handle the Request and call the TeleSearchService
 * with Searchparameters.
 *
 * This basic code is from Request.java written by Prof. Dr. Werner Brecht.
 * All original comments were not delete.
 */

// Datei: Request.java
// Autor: Brecht
// Datum: 24.05.14
// Thema: Stream-Socket-Verbindungen zwischen Browser und Web-
//        Server. GET-Request herausfiltern, falls POST-Requests
//        nicht benutzt werden.
// -------------------------------------------------------------

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

class HttpRequestTeleService {

    // out
    PrintStream out = System.out;

    // testData
    String[][] searchList = {
            {"Meier", "4711"},
            {"Müller", "0815"},
            {"Sommer", "0815"},
            {"Gehrke", "0815"},
            {"von Mörks", "1337"},
            {"von Oben", "1337"},
            {"von Unter", "1337"},
            {"von Drüben", "1337"},
            {"Heiko", "5488"},
            {"Heiko", "8888"},
            {"Heiko", "8848"},
            {"Heiko", "8888"},
            {"Mirko", "3433"},
            {"Heiko", "4433"},
            {"Heiko", "8488"},
            {"Heiko", "8388"},
            {"Kienöl", "2208"},
            {"Brecht", "2014"}
    };
    long startTime;

    private Stack<String> stack = new Stack<String>();

    public HttpRequestTeleService() {

    }

    public static void main(String[] args) throws Exception {

        HttpRequestTeleService service = null;
        service = new HttpRequestTeleService();
        service.start();
    }

    public void start() throws Exception {

        // Vereinbarungen
        // ---------------------------------------------------------
        ServerSocket ss = null;  // Fuer das accept()
        Socket cs = null;  // Fuer die Requests
        InputStream is = null;  // Aus dem Socket lesen
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;  // In den Socket schreiben
        PrintWriter pw = null;
        String zeile = null;  // Eine Zeile aus dem Socket
        String host = null;  // Der Hostname
        int port = 0;     // Der lokale Port
        String host_port = null;
        String contentLines = "";

        // Programmstart und Portbelegung
        // ---------------------------------------------------------
        host = InetAddress.getLocalHost().getHostName();
        port = 9876;
        host_port = "http://" + host + ":" + port;
        System.out.println("Server startet auf " + host + " an " + port);
        System.out.println("URL : " + host_port);

        // ServerSocket einrichten und in einer Schleife auf
        // Requests warten.
        // ---------------------------------------------------------
        ss = new ServerSocket(port);
        while (true) {
            System.out.println("Warte im accept()");
            cs = ss.accept();               // <== Auf Requests warten

            // Den Request lesen (Hier nur erste Zeile)
            // -------------------------------------------------------
            is = cs.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            zeile = br.readLine();
            System.out.println("Kontrollausgabe: " + zeile);
            System.out.println("Kontrollausgabe: " + cs.getInetAddress());

            // Favicon-Requests nicht bearbeiten
            // -------------------------------------------------------
            if (zeile.startsWith("GET /?exit=Server+beenden HTTP/1.1")) {
                System.out.println("Beende Server...");
                br.close();
                return;                       // stoppe server...
            }

            // Favicon-Requests nicht bearbeiten
            // -------------------------------------------------------
            if (zeile.startsWith("GET /favicon")) {
//                System.out.println("Favicon-Request");
                br.close();
                continue;                       // Zum naechsten Request
            }


            // analyse request line
            if (zeile.startsWith("GET /?")) {

                String converter = zeile.replace("GET /?", "").replace(" HTTP/1.1", "");
                String[] param = converter.split("&");
                String name = null;
                String tel = null;
                Boolean telFalse = false;
                for (String aParam : param) {
//                    System.out.println(aParam);

                    if (aParam.startsWith("name=")) {
                        name = aParam.replace("name=", "");
                        if (!name.isEmpty()) {
//                            System.out.println(name);
                            name = name.replace("%C3%A4", "ä")
                                    .replace("%C3%B6", "ö")
                                    .replace("%C3%BC", "ü")
                                    .replace("+", " ")
                                    .replace("%C3%9F", "ß");
//                            System.out.println(name);

                        }
                    }
                    if (aParam.startsWith("tel=")) {
                        tel = aParam.replace("tel=", "");
                        if (!tel.isEmpty()) {
                            try {
                                Integer.parseInt(tel);
                            } catch (NumberFormatException e) {
                                telFalse = true;
                            }

                        }
                    }
                }

                if (telFalse) {
                    System.out.println("Request wird bearbeitet");
                    os = cs.getOutputStream();
                    pw = new PrintWriter(os);
                    contentLines = "<h2 align=center>Telefonverzeichnis<h2>";
                    contentLines += "<h3>Sie können nach Name oder nach Telefonnummer oder nach beiden (nebenläufig) suchen.</h3>";
                    contentLines += "<form method=get action=" + host_port + ">";
                    contentLines += "<table>";
                    contentLines += "<tr><td valign=top>Name:</td>";
                    contentLines += "<td><input type=search name=name title=\"Format <br> String + String\" placeholder=\"String + String(optional)\"></td>";
                    contentLines += "<tr><td valign=top>Tel:</td>";
                    contentLines += "<td><input type=\"search\" name=\"tel\" title=\"Format <br> Number\" placeholder=\"Number\"></td>";
                    contentLines += "</tr><tr>";
                    contentLines += "<td valign=top><input type=submit value=Suchen></td>";
                    contentLines += "<td><input type=reset></td>";
                    contentLines += "</tr>";
                    contentLines += "</table>";
                    contentLines += "</form>";

                    contentLines += "<form method=get action=" + host_port + ">";
                    contentLines += "<input name=exit type=submit value=\"Server beenden\" >";
                    contentLines += "</form>";

                    contentLines += "<p>Wrong Input - Tel is not a Number!</p>";

                    pw.println("HTTP/1.1 200 OK");               // Der Header
                    pw.println("Content-Type: text/html");
                    pw.println();
                    pw.println(htmlBuild(contentLines));
                    pw.println();
                    pw.flush();
                    pw.close();
                    br.close();
                } else {


                    Thread threadSearchName = null;
                    Thread threadSearchNr = null;

                    // check if name not null & not empty -> start thread
                    if (name != null && !name.isEmpty()) {

                        // init search thread
                        SearchThread searchThread = new SearchThread(stack, name, 0, searchList, startTime);
                        threadSearchName = new Thread(searchThread);
                        threadSearchName.start();
                        out.println("Searching for a Name (" + name + ") in " + threadSearchName.getName());
                    }
                    // check if nr not null & not empty -> start thread
                    if (tel != null && !tel.isEmpty()) {

                        // init search thread
                        SearchThread searchThread = new SearchThread(stack, tel, 1, searchList, startTime);
                        threadSearchNr = new Thread(searchThread);
                        threadSearchNr.start();
                        out.println("Searching for Number (" + tel + ") in " + threadSearchNr.getName());
                    }
                    // join thread when alive
                    if (threadSearchName != null && threadSearchName.isAlive()) {
                        threadSearchName.join();
                    }
                    // join thread when alive
                    if (threadSearchNr != null && threadSearchNr.isAlive()) {
                        threadSearchNr.join();
                    }

                    long endTime = System.nanoTime();

                    // print result if something found
                    if (stack.size() >= 1) {

                        out.println("\nFound " + stack.size() + " entrys...");
                        contentLines = "<table>";
                        contentLines += "<tr><td>Found " + stack.size() + " entrys...</td></tr>";

                        out.printf("\n\t%-20s %-10s %-10s %-20s %-10s\n", "Name", "Nummer", "Thread", "SearchValue", "Time");
                        out.printf("\t%s \n", "--------------------------------------------------------------------");
                        contentLines += "<tr><td>Name</td><td>Nummer</td><td>Thread</td><td>SearchValue</td><td>Time</td></tr>";


                        while (!stack.isEmpty()) {
                            String[] result = stack.pop().split(";");
                            out.printf("\t%-20s %-10s %-10s %-20s %-10s\n", result[0], result[1], result[2], result[3], result[4]);
                            contentLines += "<tr><td>" + result[0] + "</td><td>" + result[1] + "</td><td>" + result[2] + "</td><td>" + result[3] + "</td><td>" + result[4] + "</td></tr>";
                        }
                        contentLines += "</table>";
                        contentLines += "<a href=" + host_port + ">";
                        contentLines += "<input Type=\"button\" VALUE=\"Back\" ></a>";

                        out.println("\n\tSearch duration : " + ((endTime - startTime) / 1000000) + "ms (" + endTime + ")");
                        System.out.println("Request wird bearbeitet");
                        os = cs.getOutputStream();
                        pw = new PrintWriter(os);


                        pw.println("HTTP/1.1 200 OK");               // Der Header
                        pw.println("Content-Type: text/html");
                        pw.println();
                        pw.println(htmlBuild(contentLines));
                        pw.println();
                        pw.flush();
                        pw.close();
                        br.close();


                    } else {
                        out.println("\n\tSearch for \" searchLine  \" was failed : no results");
                        System.out.println("Request wird bearbeitet");
                        os = cs.getOutputStream();
                        pw = new PrintWriter(os);
                        contentLines = "<h2 align=center>Telefonverzeichnis<h2>";
                        contentLines += "<h3>Sie können nach Name oder nach Telefonnummer oder nach beiden (nebenläufig) suchen.</h3>";
                        contentLines += "<form method=get action=" + host_port + ">";
                        contentLines += "<table>";
                        contentLines += "<tr><td valign=top>Name:</td>";
                        contentLines += "<td><input type=search name=name title=\"Format <br> String + String\" placeholder=\"String + String(optional)\"></td>";
                        contentLines += "<tr><td valign=top>Tel:</td>";
                        contentLines += "<td><input type=\"search\" name=\"tel\" title=\"Format <br> Number\" placeholder=\"Number\"></td>";
                        contentLines += "</tr><tr>";
                        contentLines += "<td valign=top><input type=submit value=Suchen></td>";
                        contentLines += "<td><input type=reset></td>";
                        contentLines += "</tr>";
                        contentLines += "</table>";
                        contentLines += "</form>";

                        contentLines += "<form method=get action=" + host_port + ">";
                        contentLines += "<input name=exit type=submit value=\"Server beenden\" >";
                        contentLines += "</form>";

                        contentLines += "<p>No Results!</p>";

                        pw.println("HTTP/1.1 200 OK");               // Der Header
                        pw.println("Content-Type: text/html");
                        pw.println();
                        pw.println(htmlBuild(contentLines));
                        pw.println();
                        pw.flush();
                        pw.close();
                        br.close();
                    }
                }

            } else {


                // Den Request bearbeiten (Hier: nur zuruecksenden)
                // -------------------------------------------------------
                System.out.println("Request wird bearbeitet");
                os = cs.getOutputStream();
                pw = new PrintWriter(os);
                contentLines = "<h2 align=center>Telefonverzeichnis<h2>";
                contentLines += "<h3>Sie können nach Name oder nach Telefonnummer oder nach beiden (nebenläufig) suchen.</h3>";
                contentLines += "<form method=get action=" + host_port + ">";
                contentLines += "<table>";
                contentLines += "<tr><td valign=top>Name:</td>";
                contentLines += "<td><input type=search name=name title=\"Format <br> String + String\" placeholder=\"String + String(optional)\"></td>";
                contentLines += "<tr><td valign=top>Tel:</td>";
                contentLines += "<td><input type=\"search\" name=\"tel\" title=\"Format <br> Number\" placeholder=\"Number\"></td>";
                contentLines += "</tr><tr>";
                contentLines += "<td valign=top><input type=submit value=Suchen></td>";
                contentLines += "<td><input type=reset></td>";
                contentLines += "</tr>";
                contentLines += "</table>";
                contentLines += "</form>";

                contentLines += "<form method=get action=" + host_port + ">";
                contentLines += "<input name=exit type=submit value=\"Server beenden\" >";
                contentLines += "</form>";

                pw.println("HTTP/1.1 200 OK");               // Der Header
                pw.println("Content-Type: text/html");
                pw.println();
                pw.println(htmlBuild(contentLines));
                pw.println();
                pw.flush();
                pw.close();
                br.close();
            }
        }  // end while
    }  // end main()

    private String htmlBuild(String content) {
        String htmlHead = "<html><head><meta charset=\"utf-8\"></head><body>";
        String htmlFooter = "</body></html>";
        return htmlHead + content + htmlFooter;
    }
}  // end class

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