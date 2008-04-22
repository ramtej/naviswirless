
package us.naviscorp.soa.wsdl.clientservices;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import us.naviscorp.soa.wsdl.clientservices.UploadResponse;


/**
 * <p>Java class for uploadResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="uploadResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="guidValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "uploadResponse", propOrder = {
    "guidValue"
})
public class UploadResponse {

    protected String guidValue;

    /**
     * Gets the value of the guidValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuidValue() {
        return guidValue;
    }

    /**
     * Sets the value of the guidValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuidValue(String value) {
        this.guidValue = value;
    }

}
