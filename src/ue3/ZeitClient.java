package ue3;/* Datei: ZeitClient.java (auf o200)
/* Zweck: Client fuer einen Zeitserver, der auf compute arbeitet
 * -----------------------------------------------------------*/

import java.util.Date;
import java.rmi.*;

public class ZeitClient {

    public ZeitClient() {
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";

        for (String arg : args) {
            host = args[0];
            break;
        }


        long t1 = 0, t2 = 0;
        ZeitInterface remoteZeit =
                (ZeitInterface) Naming.lookup("rmi://" + host + "/telservice");
        t1 = remoteZeit.getDate().getTime();
        t2 = remoteZeit.getDate().getTime();
        System.out.println("RMI brauchte " + (t2 - t1) + " ms");
        System.out.println(remoteZeit.getHello());
        System.out.println(remoteZeit.shutDown());
    }
}