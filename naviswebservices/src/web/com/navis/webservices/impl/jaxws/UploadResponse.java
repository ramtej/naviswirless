
package com.navis.webservices.impl.jaxws;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.navis.webservices.impl.jaxws.UploadResponse;

@XmlRootElement(name = "uploadResponse", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "uploadResponse", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
public class UploadResponse {

    @XmlElement(name = "guidValue", namespace = "")
    private String _return;

    /**
     * 
     * @return
     *     returns String
     */
    public String get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(String _return) {
        this._return = _return;
    }

}
