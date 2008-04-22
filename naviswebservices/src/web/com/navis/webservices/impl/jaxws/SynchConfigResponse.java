
package com.navis.webservices.impl.jaxws;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.navis.webservices.impl.jaxws.SynchConfigResponse;

@XmlRootElement(name = "synchConfigResponse", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "synchConfigResponse", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
public class SynchConfigResponse {

    @XmlElement(name = "configParams", namespace = "")
    private com.navis.webservices.model.ConfigParams _return;

    /**
     * 
     * @return
     *     returns ConfigParams
     */
    public com.navis.webservices.model.ConfigParams get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(com.navis.webservices.model.ConfigParams _return) {
        this._return = _return;
    }

}
