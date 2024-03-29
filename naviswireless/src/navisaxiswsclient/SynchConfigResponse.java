/**
 * SynchConfigResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2 May 03, 2005 (02:20:24 EDT) WSDL2Java emitter.
 */

package navisaxiswsclient;

public class SynchConfigResponse  implements java.io.Serializable {
    private navisaxiswsclient.ConfigParams configParams;

    public SynchConfigResponse() {
    }

    public SynchConfigResponse(
           navisaxiswsclient.ConfigParams configParams) {
           this.configParams = configParams;
    }


    /**
     * Gets the configParams value for this SynchConfigResponse.
     * 
     * @return configParams
     */
    public navisaxiswsclient.ConfigParams getConfigParams() {
        return configParams;
    }


    /**
     * Sets the configParams value for this SynchConfigResponse.
     * 
     * @param configParams
     */
    public void setConfigParams(navisaxiswsclient.ConfigParams configParams) {
        this.configParams = configParams;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SynchConfigResponse)) return false;
        SynchConfigResponse other = (SynchConfigResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.configParams==null && other.getConfigParams()==null) || 
             (this.configParams!=null &&
              this.configParams.equals(other.getConfigParams())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getConfigParams() != null) {
            _hashCode += getConfigParams().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SynchConfigResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.naviscorp.us/soa/wsdl/clientservices", "synchConfigResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("configParams");
        elemField.setXmlName(new javax.xml.namespace.QName("", "configParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.naviscorp.us/soa/wsdl/clientservices", "configParams"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
