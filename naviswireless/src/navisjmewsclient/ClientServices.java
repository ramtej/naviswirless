package navisjmewsclient;
import javax.xml.namespace.QName;

public interface ClientServices extends java.rmi.Remote {
    
    /**
     *
     */
    public String upload(String priority, String template, byte[] audio) throws java.rmi.RemoteException;
    
    /**
     *
     */
    public configParams synchConfig() throws java.rmi.RemoteException;
    
}
