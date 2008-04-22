/**
 * ClientServices_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2 May 03, 2005 (02:20:24 EDT) WSDL2Java emitter.
 */

package navisaxiswsclient;

public interface ClientServices_PortType extends java.rmi.Remote {
    public navisaxiswsclient.SynchConfigResponse synchConfig(navisaxiswsclient.SynchConfig parameters) throws java.rmi.RemoteException;
    public navisaxiswsclient.UploadResponse upload(navisaxiswsclient.Upload parameters) throws java.rmi.RemoteException, navisaxiswsclient.UploadException;
}
