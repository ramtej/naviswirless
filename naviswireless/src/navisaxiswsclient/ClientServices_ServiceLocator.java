/**
 * ClientServices_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2 May 03, 2005 (02:20:24 EDT) WSDL2Java emitter.
 */

package navisaxiswsclient;

public class ClientServices_ServiceLocator extends org.apache.axis.client.Service implements navisaxiswsclient.ClientServices_Service {

    public ClientServices_ServiceLocator() {
    }


    public ClientServices_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ClientServices_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ClientServicesPort
    private java.lang.String ClientServicesPort_address = "http://192.168.1.2:8080/clientservices/clientservices";

    public java.lang.String getClientServicesPortAddress() {
        return ClientServicesPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ClientServicesPortWSDDServiceName = "ClientServicesPort";

    public java.lang.String getClientServicesPortWSDDServiceName() {
        return ClientServicesPortWSDDServiceName;
    }

    public void setClientServicesPortWSDDServiceName(java.lang.String name) {
        ClientServicesPortWSDDServiceName = name;
    }

    public navisaxiswsclient.ClientServices_PortType getClientServicesPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ClientServicesPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getClientServicesPort(endpoint);
    }

    public navisaxiswsclient.ClientServices_PortType getClientServicesPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            navisaxiswsclient.ClientServicesPortBindingStub _stub = new navisaxiswsclient.ClientServicesPortBindingStub(portAddress, this);
            _stub.setPortName(getClientServicesPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setClientServicesPortEndpointAddress(java.lang.String address) {
        ClientServicesPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (navisaxiswsclient.ClientServices_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                navisaxiswsclient.ClientServicesPortBindingStub _stub = new navisaxiswsclient.ClientServicesPortBindingStub(new java.net.URL(ClientServicesPort_address), this);
                _stub.setPortName(getClientServicesPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ClientServicesPort".equals(inputPortName)) {
            return getClientServicesPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.naviscorp.us/soa/wsdl/clientservices", "ClientServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.naviscorp.us/soa/wsdl/clientservices", "ClientServicesPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("ClientServicesPort".equals(portName)) {
            setClientServicesPortEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
