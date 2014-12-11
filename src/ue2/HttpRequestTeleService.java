package ue2;


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

class HttpRequestTeleService {
    public static void main(String[] args) throws Exception {

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

        // Programmstart und Portbelegung
        // ---------------------------------------------------------
        host = InetAddress.getLocalHost().getHostName();
        port = 9876;
        System.out.println("Server startet auf " + host + " an " + port);

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
            String parameterLine = zeile.substring(zeile.indexOf("?") + 1, zeile.lastIndexOf(" "));
            System.out.println("Kontrollausgabe: " + parameterLine);
            String[] parameters = parameterLine.split("&");
            System.out.println("Kontrollausgabe: " + parameters.length);
            String[] keyValue = null;
            for (int i=0;i<parameters.length;i++){
                System.out.println("Kontrollausgabe: " + parameters[i]);
            }
//            System.out.println("Kontrollausgabe: " + keyValue.length);



            // Favicon-Requests nicht bearbeiten
            // -------------------------------------------------------
            if (zeile.startsWith("GET /favicon")) {
                System.out.println("Favicon-Request");
                br.close();
                continue;                       // Zum naechsten Request
            }

            // analyse request line
            if (zeile.startsWith("GET /?")) {

                int nameKeyStart = zeile.indexOf('?');
                int nameKeyEnd = zeile.indexOf('=');
                int nameValueEnd = zeile.indexOf('&');
                int numberKeyEnd = zeile.indexOf('=');
                int numberValueEnd = zeile.indexOf(' ');

                System.out.println("? : " + nameKeyStart);
                System.out.println("= : " + nameKeyEnd);
                System.out.println("& : " + nameValueEnd);
                System.out.println("= : " + numberKeyEnd);
                System.out.println("  : " + numberValueEnd);

                String nameKey = zeile.substring(nameKeyStart + 1, nameKeyEnd);
                String nameValue = zeile.substring(nameKeyEnd + 1, nameValueEnd);
//                String numberKey = zeile.substring(nameValueEnd, numberKeyEnd);
//                String numberValue = zeile.substring(nameKeyEnd + 1, nameValueEnd - 1);

                System.out.println(nameKey + " : " + nameValue);
//                System.out.println(numberKey + " : " + "numberValue");
//                System.out.print("Found Index :" );
//                System.out.println(zeile.indexOf( '?' ));
//                System.out.print("Found Index :" );
//                System.out.println(zeile.indexOf( '=' ));
//                System.out.print("Found Index :" );
//                System.out.println(zeile.indexOf( '&' ));
//                System.out.print("Found Index :" );
//                System.out.println(zeile.indexOf( '=' ));
            }

            // Den Request bearbeiten (Hier: nur zuruecksenden)
            // -------------------------------------------------------
            System.out.println("Request wird bearbeitet");
            os = cs.getOutputStream();
            pw = new PrintWriter(os);
            String firstSite =
                    "<html>\n" +
                            "<head><meta charset=\"utf-8\"> </head>\n" +
                            "<body>\n" +
                            "<h2 align=center>Telefonverzeichnis</h2>\n" +
                            "<h3>Sie können nach Name oder nach Telefonnummer oder nach beiden (nebenläufig) suchen.</h3>\n" +
                            "<form method=get action=\"http://MuckAsi-LaPPi:9876\">\n" +
                            "<table>\n" +
                            "<tr> " +
                            "   <td valign=top>Name:</td>    " +
                            "   <td>" +
                            "       <input type=\"search\" " +
                            "               name=\"name\" " +
                            "               title=\"Format <br> String + String\" " +
                            "               placeholder=\"String + String(optional)\"" +
                            "               >" +
                            "</td>    <td></td> </tr>\n" +
                            "<tr> <td valign=top>Nummer:</td> <td><input type=\"search\" name=\"tel\" title=\"Format <br> Number\" placeholder=\"Number\">" +
                            "</td>    <td></td> </tr>\n" +
                            "<tr> " +
                            "<td valign=top><input type=submit value=Suchen></td>\n" +
                            "<td><input type=reset></td>\n" +
                            "<td><input name=exit type=submit value=\"Server beenden\" ></td> " +
                            "</tr>\n" +
                            "</table>\n" +
                            "</form>\n" +
                            "</body>\n" +
                            "</html>";

            pw.println("HTTP/1.1 200 OK");               // Der Header
            pw.println("Content-Type: text/html");
            pw.println();
//            pw.println("<html>");                    // Die HTML-Seite
//            pw.println("<body>");
//            pw.println("<h1><font color=green>");
//            pw.println(zeile);
//            pw.println("</font></h1>");
//            pw.println("</body>");
//            pw.println("</html>");
            pw.println(firstSite);
            pw.println();
            pw.flush();
            pw.close();
            br.close();
        }  // end while
    }  // end main()
}  // end class