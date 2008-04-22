/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.navis.webservices;

import com.navis.webservices.exception.UploadException;
import com.navis.webservices.model.ConfigParams;

/**
 *
 * @author dr. xinyu liu
 */
public interface ClientServices {
    
    public ConfigParams synchConfig();
    
    public String upload(String priority, String template, byte[] audio) throws UploadException;
    
    
}
