
package com.navis.webservices.impl.jaxws;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.navis.webservices.impl.jaxws.Upload;

@XmlRootElement(name = "upload", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices")
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "upload", namespace = "http://www.naviscorp.us/soa/wsdl/clientservices", propOrder = {
    "priority",
    "template",
    "audio"
})
public class Upload {

    @XmlElement(name = "priority", namespace = "")
    private String priority;
    @XmlElement(name = "template", namespace = "")
    private String template;
    @XmlElement(name = "audio", namespace = "")
    private byte[] audio;

    /**
     * 
     * @return
     *     returns String
     */
    public String getPriority() {
        return this.priority;
    }

    /**
     * 
     * @param priority
     *     the value for the priority property
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * 
     * @param template
     *     the value for the template property
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * 
     * @return
     *     returns byte[]
     */
    public byte[] getAudio() {
        return this.audio;
    }

    /**
     * 
     * @param audio
     *     the value for the audio property
     */
    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

}
