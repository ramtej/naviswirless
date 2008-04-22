/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.navis.webservices.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author dr. xinyu liu
 */
public class ConfigParams implements Serializable {

    private List<NameValuePair> params;

    public List<NameValuePair> getParams() {
        return params;
    }

    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }

    public ConfigParams() {
    }

    public ConfigParams(List<NameValuePair> params) {
        this.params = params;
    }
    
}
