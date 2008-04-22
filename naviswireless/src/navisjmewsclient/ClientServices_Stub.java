package navisjmewsclient;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.microedition.xml.rpc.Operation;
import javax.microedition.xml.rpc.Type;
import javax.microedition.xml.rpc.ComplexType;
import javax.microedition.xml.rpc.Element;

public class ClientServices_Stub implements ClientServices, javax.xml.rpc.Stub {
    
    private String[] _propertyNames;
    private Object[] _propertyValues;
    
    public ClientServices_Stub() {
        _propertyNames = new String[] { ENDPOINT_ADDRESS_PROPERTY };
        _propertyValues = new Object[] { "http://192.168.1.2:8080/clientservices/clientservices" };
    }
    
    public void _setProperty( String name, Object value ) {
        int size = _propertyNames.length;
        for (int i = 0; i < size; ++i) {
            if( _propertyNames[i].equals( name )) {
                _propertyValues[i] = value;
                return;
            }
        }
        String[] newPropNames = new String[size + 1];
        System.arraycopy(_propertyNames, 0, newPropNames, 0, size);
        _propertyNames = newPropNames;
        Object[] newPropValues = new Object[size + 1];
        System.arraycopy(_propertyValues, 0, newPropValues, 0, size);
        _propertyValues = newPropValues;
        
        _propertyNames[size] = name;
        _propertyValues[size] = value;
    }
    
    public Object _getProperty(String name) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            if (_propertyNames[i].equals(name)) {
                return _propertyValues[i];
            }
        }
        if (ENDPOINT_ADDRESS_PROPERTY.equals(name) || USERNAME_PROPERTY.equals(name) || PASSWORD_PROPERTY.equals(name)) {
            return null;
        }
        if (SESSION_MAINTAIN_PROPERTY.equals(name)) {
            return new Boolean(false);
        }
        throw new JAXRPCException("Stub does not recognize property: " + name);
    }
    
    protected void _prepOperation(Operation op) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            op.setProperty(_propertyNames[i], _propertyValues[i].toString());
        }
    }
    
    public String upload(String priority, String template, byte[] audio) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            priority,
            template,
            audio
        };
        
        Operation op = Operation.newInstance( _qname_operation_upload, _type_upload, _type_uploadResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }
        
        return (String )((Object[])resultObj)[0];
    }
    
    public configParams synchConfig() throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
        };
        
        Operation op = Operation.newInstance( _qname_operation_synchConfig, _type_synchConfig, _type_synchConfigResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }
        
        return configParams_fromObject((Object[])((Object[]) resultObj)[0]);
    }
    
    private static nameValuePair[] nameValuePair_ArrayfromObject( Object obj[] ) {
        if(obj == null) return null;
        nameValuePair result[] = new nameValuePair[obj.length];
        for( int i = 0; i < obj.length; i++ ) {
            result[i] = new nameValuePair();
            Object[] oo = (Object[]) obj[i];
            result[i].setName((String )oo[0]);
            result[i].setValue((String )oo[1]);
        }
        return result;
    }
    
    private static configParams configParams_fromObject( Object obj[] ) {
        if(obj == null) return null;
        configParams result = new configParams();
        result.setParams(nameValuePair_ArrayfromObject((Object[]) obj[0] ));
        return result;
    }
    
    protected static final QName _qname_operation_synchConfig = new QName( "http://www.naviscorp.us/soa/wsdl/clientservices", "synchConfig" );
    protected static final QName _qname_operation_upload = new QName( "http://www.naviscorp.us/soa/wsdl/clientservices", "upload" );
    protected static final QName _qname_synchConfigResponse = new QName( "http://www.naviscorp.us/soa/wsdl/clientservices", "synchConfigResponse" );
    protected static final QName _qname_synchConfig = new QName( "http://www.naviscorp.us/soa/wsdl/clientservices", "synchConfig" );
    protected static final QName _qname_upload = new QName( "http://www.naviscorp.us/soa/wsdl/clientservices", "upload" );
    protected static final QName _qname_uploadResponse = new QName( "http://www.naviscorp.us/soa/wsdl/clientservices", "uploadResponse" );
    protected static final Element _type_synchConfig;
    protected static final Element _type_synchConfigResponse;
    protected static final Element _type_upload;
    protected static final Element _type_uploadResponse;
    
    static {
        _type_synchConfig = new Element( _qname_synchConfig, _complexType( new Element[] {
        }), 1, 1, false );
        _type_synchConfigResponse = new Element( _qname_synchConfigResponse, _complexType( new Element[] {
            new Element( new QName( "", "configParams" ), _complexType( new Element[] {
                new Element( new QName( "", "params" ), _complexType( new Element[] {
                    new Element( new QName( "", "name" ), Type.STRING, 0, 1, false ),
                    new Element( new QName( "", "value" ), Type.STRING, 0, 1, false )}), 0, Element.UNBOUNDED, true )}))}), 1, 1, false );
        _type_upload = new Element( _qname_upload, _complexType( new Element[] {
            new Element( new QName( "", "priority" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "", "template" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "", "audio" ), Type.BYTE, 0, Element.UNBOUNDED, false )}), 1, 1, false );
        _type_uploadResponse = new Element( _qname_uploadResponse, _complexType( new Element[] {
            new Element( new QName( "", "guidValue" ), Type.STRING, 0, 1, false )}), 1, 1, false );
    }
    
    private static ComplexType _complexType( Element[] elements ) {
        ComplexType result = new ComplexType();
        result.elements = elements;
        return result;
    }
}
