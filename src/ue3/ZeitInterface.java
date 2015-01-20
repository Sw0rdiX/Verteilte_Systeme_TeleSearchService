package ue3;/* Datei: ZeitInterface.java (auf compute und auf o200)
 /* Zweck: Schnittstelle fuer einen Zeitserver
 * Das ist die "remote" Methode!
 * -------------------------------------------------- */

import java.rmi.*;
public interface ZeitInterface extends Remote {
    java.util.Date getDate() throws RemoteException;
    public String getHello() throws RemoteException;
    public String shutDown() throws RemoteException;
}