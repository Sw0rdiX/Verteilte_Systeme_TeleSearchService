package ue3;
/* Datei: ZeitServer.java (auf compute)
 * -------------------------------------------------------- */

import java.util.Date;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class ZeitServer extends UnicastRemoteObject
        implements ZeitInterface {

    public ZeitServer() throws RemoteException {}

    public Date getDate() throws RemoteException {
        return new Date();
    }

    @Override
    public String getHello() throws RemoteException {
        return "Hello Client...";
    }

    @Override
    public String shutDown() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
        System.out.println("Server wird beendet...");

        return "RMI Server wurde beendet...";
    }

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);     // Port 1099
        ZeitServer zeit = new ZeitServer();

        // Anmeldung des Dienstes mit
        // rmi://Serverhostname/Eindeutige Bezeichnung des Dienstes
        // ---------------------------------------------------------
        Naming.rebind("rmi://localhost/telservice", zeit);
        System.out.println("Server wartet auf RMIs");
    }
}