
package us.naviscorp.soa.wsdl.clientservices;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import us.naviscorp.soa.wsdl.clientservices.ConfigParams;
import us.naviscorp.soa.wsdl.clientservices.SynchConfigResponse;


/**
 * <p>Java class for synchConfigResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="synchConfigResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="configParams" type="{http://www.naviscorp.us/soa/wsdl/clientservices}configParams" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "synchConfigResponse", propOrder = {
    "configParams"
})
public class SynchConfigResponse {

    protected ConfigParams configParams;

    /**
     * Gets the value of the configParams property.
     * 
     * @return
     *     possible object is
     *     {@link ConfigParams }
     *     
     */
    public ConfigParams getConfigParams() {
        return configParams;
    }

    /**
     * Sets the value of the configParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigParams }
     *     
     */
    public void setConfigParams(ConfigParams value) {
        this.configParams = value;
    }

}
