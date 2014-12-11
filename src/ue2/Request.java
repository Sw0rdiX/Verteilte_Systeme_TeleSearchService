package ue2;

// Datei: Request.java
// Autor: Brecht
// Datum: 24.05.14
// Thema: Stream-Socket-Verbindungen zwischen Browser und Web-
//        Server. GET-Request herausfiltern, falls POST-Requests
//        nicht benutzt werden.
// -------------------------------------------------------------

import java.io.*;      // Fuer den Reader
import java.net.*;     // Fuer den Socket

class Request {
    public static void main(String[] args) throws Exception {

        // Vereinbarungen
        // ---------------------------------------------------------
        ServerSocket ss       = null;  // Fuer das accept()
        Socket cs             = null;  // Fuer die Requests
        InputStream is        = null;  // Aus dem Socket lesen
        InputStreamReader isr = null;
        BufferedReader br     = null;
        OutputStream os       = null;  // In den Socket schreiben
        PrintWriter pw        = null;
        String zeile          = null;  // Eine Zeile aus dem Socket
        String host           = null;  // Der Hostname
        int port              = 0;     // Der lokale Port

        // Programmstart und Portbelegung
        // ---------------------------------------------------------
        host = InetAddress.getLocalHost().getHostName();
        port = 9876;
        System.out.println("Server startet auf "+host+" an "+port);

        // ServerSocket einrichten und in einer Schleife auf
        // Requests warten.
        // ---------------------------------------------------------
        ss = new ServerSocket(port);
        while(true) {
            System.out.println("Warte im accept()");
            cs = ss.accept();               // <== Auf Requests warten

            // Den Request lesen (Hier nur erste Zeile)
            // -------------------------------------------------------
            is    = cs.getInputStream();
            isr   = new InputStreamReader(is);
            br    = new BufferedReader(isr);
            zeile = br.readLine();
            System.out.println("Kontrollausgabe: "+zeile);

            // Favicon-Requests nicht bearbeiten
            // -------------------------------------------------------
            if(zeile.startsWith("GET /favicon")) {
                System.out.println("Favicon-Request");
                br.close();
                continue;                       // Zum naechsten Request
            }

            // Den Request bearbeiten (Hier: nur zuruecksenden)
            // -------------------------------------------------------
            System.out.println("Request wird bearbeitet");
            os  = cs.getOutputStream();
            pw  = new PrintWriter(os);

            pw.println("HTTP/1.1 200 OK");               // Der Header
            pw.println("Content-Type: text/html");
            pw.println();
            pw.println("<html>");                    // Die HTML-Seite
            pw.println("<body>");
            pw.println("<h1><font color=green>");
            pw.println(zeile);
            pw.println("</font></h1>");
            pw.println("</body>");
            pw.println("</html>");
            pw.println();
            pw.flush();
            pw.close();
            br.close();
        }  // end while
    }  // end main()
}  // end class