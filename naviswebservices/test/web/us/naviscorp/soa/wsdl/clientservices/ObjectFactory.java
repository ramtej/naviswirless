
package us.naviscorp.soa.wsdl.clientservices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import us.naviscorp.soa.wsdl.clientservices.ConfigParams;
import us.naviscorp.soa.wsdl.clientservices.NameValuePair;
import us.naviscorp.soa.wsdl.clientservices.ObjectFactory;
import us.naviscorp.soa.wsdl.clientservices.SynchConfig;
import us.naviscorp.soa.wsdl.clientservices.SynchConfigResponse;
import us.naviscorp.soa.wsdl.clientservices.Upload;
import us.naviscorp.soa.wsdl.clientservices.UploadException;
import us.naviscorp.soa.wsdl.clientservices.UploadResponse;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the us.naviscorp.soa.wsdl.clientservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UploadException_QNAME = new QName("http://www.naviscorp.us/soa/wsdl/clientservices", "UploadException");
    private final static QName _SynchConfigResponse_QNAME = new QName("http://www.naviscorp.us/soa/wsdl/clientservices", "synchConfigResponse");
    private final static QName _SynchConfig_QNAME = new QName("http://www.naviscorp.us/soa/wsdl/clientservices", "synchConfig");
    private final static QName _Upload_QNAME = new QName("http://www.naviscorp.us/soa/wsdl/clientservices", "upload");
    private final static QName _UploadResponse_QNAME = new QName("http://www.naviscorp.us/soa/wsdl/clientservices", "uploadResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: us.naviscorp.soa.wsdl.clientservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SynchConfig }
     * 
     */
    public SynchConfig createSynchConfig() {
        return new SynchConfig();
    }

    /**
     * Create an instance of {@link UploadException }
     * 
     */
    public UploadException createUploadException() {
        return new UploadException();
    }

    /**
     * Create an instance of {@link SynchConfigResponse }
     * 
     */
    public SynchConfigResponse createSynchConfigResponse() {
        return new SynchConfigResponse();
    }

    /**
     * Create an instance of {@link NameValuePair }
     * 
     */
    public NameValuePair createNameValuePair() {
        return new NameValuePair();
    }

    /**
     * Create an instance of {@link Upload }
     * 
     */
    public Upload createUpload() {
        return new Upload();
    }

    /**
     * Create an instance of {@link UploadResponse }
     * 
     */
    public UploadResponse createUploadResponse() {
        return new UploadResponse();
    }

    /**
     * Create an instance of {@link ConfigParams }
     * 
     */
    public ConfigParams createConfigParams() {
        return new ConfigParams();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.naviscorp.us/soa/wsdl/clientservices", name = "UploadException")
    public JAXBElement<UploadException> createUploadException(UploadException value) {
        return new JAXBElement<UploadException>(_UploadException_QNAME, UploadException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SynchConfigResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.naviscorp.us/soa/wsdl/clientservices", name = "synchConfigResponse")
    public JAXBElement<SynchConfigResponse> createSynchConfigResponse(SynchConfigResponse value) {
        return new JAXBElement<SynchConfigResponse>(_SynchConfigResponse_QNAME, SynchConfigResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SynchConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.naviscorp.us/soa/wsdl/clientservices", name = "synchConfig")
    public JAXBElement<SynchConfig> createSynchConfig(SynchConfig value) {
        return new JAXBElement<SynchConfig>(_SynchConfig_QNAME, SynchConfig.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Upload }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.naviscorp.us/soa/wsdl/clientservices", name = "upload")
    public JAXBElement<Upload> createUpload(Upload value) {
        return new JAXBElement<Upload>(_Upload_QNAME, Upload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.naviscorp.us/soa/wsdl/clientservices", name = "uploadResponse")
    public JAXBElement<UploadResponse> createUploadResponse(UploadResponse value) {
        return new JAXBElement<UploadResponse>(_UploadResponse_QNAME, UploadResponse.class, null, value);
    }

}
