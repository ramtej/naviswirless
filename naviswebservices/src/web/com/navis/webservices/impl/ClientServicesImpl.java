/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navis.webservices.impl;

import com.navis.webservices.ClientServices;

import com.navis.webservices.exception.UploadException;
import com.navis.webservices.model.ConfigParams;
import com.navis.webservices.model.NameValuePair;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.annotation.security.RolesAllowed;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dr. xinyu liu
 */
@WebService(targetNamespace = "http://www.naviscorp.us/soa/wsdl/clientservices", name = "ClientServices", portName = "ClientServicesPort", serviceName = "ClientServices")
@SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public class ClientServicesImpl implements ClientServices {

    public static final String TEST_NETWORK_INTERVAL = "testNetworkInterval";
    public static final String REMOTE_CORP_URL = "remoteCorpUrl";
    public static final String REMOTE_WS_URL = "remoteWsUrl";
    public static final String CONTACT_CORP_PHONE = "contactCorpPhone";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USERFULLNAME = "userFullName";
    public static final String MIN_USERNAME_LENGTH = "minUserNameLength";
    public static final String MAX_USERNAME_LENGTH = "maxUserNameLength";
    public static final String MIN_PASSWORD_LENGTH = "minPasswordLength";
    public static final String MAX_PASSWORD_LENGTH = "maxPasswordLength";
    public static final String MIN_AUDIO_LENGTH = "minAudioLength";
    public static final String MAX_AUDIO_LENGTH = "maxAudioLength";
    public static final String MAX_AUDIO_STORED = "maxAudioStored";
    public static final String AUDIO_FORMAT_FREQUENCY = "audioFormatFrequency";
    public static final String AUDIO_FORMAT_BIT = "audioFormatBit";
    public static final String AUDIO_FORMAT_STEREO = "audioFormatStereo";
    public static final String AUDIO_FORMAT_CODEC = "audioFormatCodec";
    public static final String AUDIO_FORMAT_ENDIAN = "audioFormatEndian";    
    public static final String AUDIO_FORMAT_FILETYPE = "audioFormatFileType";    
    
    public static final String TEMPLATE_COUNT = "templateCount";
    public static final String TEMPLATE_CODE = "templateCode";
    public static final String TEMPLATE_NAME = "templateName";
    public static final String PRIORITY_COUNT = "priorityCount";
    public static final String PRIORITY_CODE = "priorityCode";
    public static final String PRIORITY_NAME = "priorityName";
    
    public static final String UPLOAD_FOLDER = "uploadFolder";
    public static final String CLIENT_NAME = "clientName"; 
    public static final String CLIENT_CODE = "clientCode"; 
    
    @Resource
    WebServiceContext wsContext;    // Dependency Injection

    public WebServiceContext getWsContext() {
        return wsContext;
    }

    public void setWsContext(WebServiceContext wsContext) {
        this.wsContext = wsContext;
    }

    @WebMethod(operationName = "synchConfig")
    @RolesAllowed("Client")
    public 
    @WebResult(name = "configParams")
    ConfigParams synchConfig() {
        
        Principal principal = wsContext.getUserPrincipal();
        if(principal!=null)
        {
            System.out.println("User Name : "+principal.getName());
        }
        
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        list.add(new NameValuePair(TEST_NETWORK_INTERVAL, "2000")); //millisecond
        list.add(new NameValuePair(REMOTE_CORP_URL, "http://192.168.1.2:8080"));
        list.add(new NameValuePair(REMOTE_WS_URL, "http://192.168.1.2:8080/clientservices/clientservices"));
        list.add(new NameValuePair(CONTACT_CORP_PHONE, "1-800-111-1111"));
        list.add(new NameValuePair(USERFULLNAME, "Fran Leonard"));
        list.add(new NameValuePair(MIN_USERNAME_LENGTH, "6"));
        list.add(new NameValuePair(MAX_USERNAME_LENGTH, "8"));
        list.add(new NameValuePair(MIN_PASSWORD_LENGTH, "6"));
        list.add(new NameValuePair(MAX_PASSWORD_LENGTH, "8"));
        list.add(new NameValuePair(MIN_AUDIO_LENGTH, "30"));
        list.add(new NameValuePair(MAX_AUDIO_LENGTH, "300"));
        list.add(new NameValuePair(MAX_AUDIO_STORED, "20"));
        list.add(new NameValuePair(AUDIO_FORMAT_FREQUENCY, "8000"));
        list.add(new NameValuePair(AUDIO_FORMAT_BIT, "8"));
        list.add(new NameValuePair(AUDIO_FORMAT_STEREO, "mono"));
        list.add(new NameValuePair(AUDIO_FORMAT_CODEC, "ULAW"));
        list.add(new NameValuePair(AUDIO_FORMAT_ENDIAN, "bigEndian"));
        list.add(new NameValuePair(AUDIO_FORMAT_FILETYPE, "WAVE"));
        list.add(new NameValuePair(UPLOAD_FOLDER, "upload"));
                
        list.add(new NameValuePair(CLIENT_CODE, "12345"));
        list.add(new NameValuePair(CLIENT_NAME, "Rajesh"));
             
        list.add(new NameValuePair(PRIORITY_COUNT, "4"));       
        list.add(new NameValuePair(PRIORITY_CODE+"_0", "1"));
        list.add(new NameValuePair(PRIORITY_NAME+"_0", "4hr"));
        list.add(new NameValuePair(PRIORITY_CODE+"_1", "2"));
        list.add(new NameValuePair(PRIORITY_NAME+"_1", "8hr"));
        list.add(new NameValuePair(PRIORITY_CODE+"_2", "3"));
        list.add(new NameValuePair(PRIORITY_NAME+"_2", "24hr"));      
        list.add(new NameValuePair(PRIORITY_CODE+"_3", "4"));
        list.add(new NameValuePair(PRIORITY_NAME+"_3", "48hr"));             
        
        list.add(new NameValuePair(TEMPLATE_COUNT, "3"));       
        list.add(new NameValuePair(TEMPLATE_CODE+"_0", "001"));
        list.add(new NameValuePair(TEMPLATE_NAME+"_0", "20001WristFracture"));
        list.add(new NameValuePair(TEMPLATE_CODE+"_1", "002"));
        list.add(new NameValuePair(TEMPLATE_NAME+"_1", "20001CompoundFracture"));
        list.add(new NameValuePair(TEMPLATE_CODE+"_2", "003"));
        list.add(new NameValuePair(TEMPLATE_NAME+"_2", "20002Fracture"));

        
        return new ConfigParams(list);
    }

    @WebMethod(operationName = "upload")
    @RolesAllowed("Client")
    public @WebResult(name = "guidValue") String upload(
            @WebParam(name = "priority") String priority,
            @WebParam(name = "template") String template,
            @WebParam(name = "audio") byte[] audio
            ) throws UploadException 
    {
        //String username = wsContext.getUserPrincipal().getName();
        try{
            FileOutputStream fos = new FileOutputStream(new File("Test.wav"));
            fos.write(audio);
            fos.close();
        }catch(Exception e)
        {
            
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(audio);
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(bais);
            
            AudioFormat format = ais.getFormat();
            
            // get an AudioInputStream of the desired format for playback (doen't work in mysaifu)
            //AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
            
            AudioInputStream playbackInputStream = ais;
            
            if (playbackInputStream == null) {
                System.err.println("Unable to generate audio input stream.");
            }

            // define the required attributes for our line, 
            // and make sure a compatible line is supported.

            DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                    format);
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Line matching " + info + " not supported.");
            }

            // get and open the source data line for playback.

            SourceDataLine line = null;
            int bufSize = 16384;
            
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, bufSize);
            } catch (LineUnavailableException ex) {
                System.err.println("Unable to open the line: " + ex);
            }

            // play back the captured audio data

            int frameSizeInBytes = ais.getFormat().getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;
            int totalNumBytesRead = 0;

            // start the source data line
            line.start();

            long deltaTime = 0;

            while (true) {
                                
                try {
                    if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                        break;
                    }
                    totalNumBytesRead += numBytesRead;
                    int numBytesRemaining = numBytesRead;
                    
                    while (numBytesRemaining > 0) {
                        
                        numBytesRemaining -= line.write(data, 0, numBytesRemaining);                      
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error during playback: " + e);
                    break;
                }

            }
            // we reached the end of the stream.  let the data play out, then
            // stop and close the line.

            line.drain();
            line.stop();
            line.close();
            line = null;           
            
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
                
        return "F96EB3B9-C9F1-11D2-95EB-0060089BB2DA";
    }
}
