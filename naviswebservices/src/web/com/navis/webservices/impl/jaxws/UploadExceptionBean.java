
package com.navis.webservices.impl.jaxws;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.navis.webservices.impl.jaxws.UploadExceptionBean;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0-b26-ea3
 * Generated source version: 2.0
 * 
 */
@XmlRootElement(name = "UploadException", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "UploadException", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
public class UploadExceptionBean {

    private String message;

    /**
     * 
     * @return
     *     returns String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 
     * @param message
     *     the value for the message property
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
