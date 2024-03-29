/**
 * UploadResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2 May 03, 2005 (02:20:24 EDT) WSDL2Java emitter.
 */

package navisaxiswsclient;

public class UploadResponse  implements java.io.Serializable {
    private java.lang.String guidValue;

    public UploadResponse() {
    }

    public UploadResponse(
           java.lang.String guidValue) {
           this.guidValue = guidValue;
    }


    /**
     * Gets the guidValue value for this UploadResponse.
     * 
     * @return guidValue
     */
    public java.lang.String getGuidValue() {
        return guidValue;
    }


    /**
     * Sets the guidValue value for this UploadResponse.
     * 
     * @param guidValue
     */
    public void setGuidValue(java.lang.String guidValue) {
        this.guidValue = guidValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UploadResponse)) return false;
        UploadResponse other = (UploadResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.guidValue==null && other.getGuidValue()==null) || 
             (this.guidValue!=null &&
              this.guidValue.equals(other.getGuidValue())));
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
        if (getGuidValue() != null) {
            _hashCode += getGuidValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.naviscorp.us/soa/wsdl/clientservices", "uploadResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("guidValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "guidValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
