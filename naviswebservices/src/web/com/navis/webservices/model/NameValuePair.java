/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.navis.webservices.model;

import java.io.Serializable;

/**
 *
 * @author dr. xinyu liu
 */
public class NameValuePair implements Serializable {

    private String name, value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public NameValuePair(){}
    
}
