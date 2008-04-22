
package us.naviscorp.soa.wsdl.clientservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import us.naviscorp.soa.wsdl.clientservices.ClientServices;
import us.naviscorp.soa.wsdl.clientservices.ConfigParams;
import us.naviscorp.soa.wsdl.clientservices.UploadException_Exception;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0-b26-ea3
 * Generated source version: 2.0
 * 
 */
@WebService(name = "ClientServices", targetNamespace = "http://www.naviscorp.us/soa/wsdl/clientservices", wsdlLocation = "http://localhost:8080/clientservices/clientservices?wsdl")
public interface ClientServices {


    /**
     * 
     * @return
     *     returns us.naviscorp.soa.wsdl.clientservices.ConfigParams
     */
    @WebMethod
    @WebResult(name = "configParams", targetNamespace = "")
    @RequestWrapper(localName = "synchConfig", targetNamespace = "http://www.naviscorp.us/soa/wsdl/clientservices", className = "us.naviscorp.soa.wsdl.clientservices.SynchConfig")
    @ResponseWrapper(localName = "synchConfigResponse", targetNamespace = "http://www.naviscorp.us/soa/wsdl/clientservices", className = "us.naviscorp.soa.wsdl.clientservices.SynchConfigResponse")
    public ConfigParams synchConfig();

    /**
     * 
     * @param audio
     * @param template
     * @param priority
     * @return
     *     returns java.lang.String
     * @throws UploadException_Exception
     */
    @WebMethod
    @WebResult(name = "guidValue", targetNamespace = "")
    @RequestWrapper(localName = "upload", targetNamespace = "http://www.naviscorp.us/soa/wsdl/clientservices", className = "us.naviscorp.soa.wsdl.clientservices.Upload")
    @ResponseWrapper(localName = "uploadResponse", targetNamespace = "http://www.naviscorp.us/soa/wsdl/clientservices", className = "us.naviscorp.soa.wsdl.clientservices.UploadResponse")
    public String upload(
        @WebParam(name = "priority", targetNamespace = "")
        String priority,
        @WebParam(name = "template", targetNamespace = "")
        String template,
        @WebParam(name = "audio", targetNamespace = "")
        byte[] audio)
        throws UploadException_Exception
    ;

}
