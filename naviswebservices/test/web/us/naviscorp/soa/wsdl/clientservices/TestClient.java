package us.naviscorp.soa.wsdl.clientservices;

import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.xml.ws.BindingProvider;

/**
 *
 * @author dr. xinyu liu
 */
public class TestClient {

    public static void main(String args[]) {
        
        try{      
            File file = new File("C:\\CSTech2008\\projects\\NetBeansProjects\\naviswebservices\\test\\web\\classes\\u0415080149_4_003.wav");
            byte[] audio = new byte[(int)file.length()] ;            
            FileInputStream fis = new FileInputStream(file);
            fis.read(audio);
                            
            ClientServices port = (new ClientServices_Service()).getClientServicesPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "admin1"); 
            ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "admin1");

             String response = port.upload("2", "002", audio);
             System.out.println(">>>>>>>>>>>>>> "+response);
             
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    
}
