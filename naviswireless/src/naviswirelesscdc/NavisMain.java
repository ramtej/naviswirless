/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naviswirelesscdc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.sound.sampled.AudioFileFormat;
import navisaxiswsclient.ClientServicesPortBindingStub;
import navisaxiswsclient.ClientServices_ServiceLocator;
import navisaxiswsclient.NameValuePair;
import navisaxiswsclient.SynchConfig;
import navisaxiswsclient.SynchConfigResponse;
import navisjmewsclient.ClientServices_Stub;

/**
 *
 * @author dr. xinyu liu
 */
public class NavisMain {

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
    public static final String FLOW_STATE_INIT = "flowStateInit";
    public static final String FLOW_STATE_MENU = "flowStateMenu";
    public static final String FLOW_STATE_RECORD = "flowStateRecord";
    public static final String FLOW_STATE_EDITPENDING = "flowStateEditPending";
    public static final String FLOW_STATE_SAVE = "flowStateSave";
    private String stage = FLOW_STATE_INIT;
    private String fileSeparator = System.getProperty("file.separator");
    private boolean firstTimeUser = false;
    private boolean online = false; // must be false
    private java.util.Properties configurations = new java.util.Properties();
    private Dimension windowSize;
    private MainFrame mainFrame;
    private HeaderPanel headerPanel;
    private LoginPanel loginPanel;
    private MenuPanel menuPanel;
    private InfoPanel infoPanel;
    private PendingPanel pendingPanel;
    private RecordPanel recordPanel;
    private SavePanel savePanel;
    private DeleteConfirmPanel deleteConfirmPanel;
    private ProfilePanel profilePanel;
    private AudioProcessor audioProcessor;

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public PendingPanel getPendingPanel() {
        return pendingPanel;
    }

    public RecordPanel getRecordPanel() {
        return recordPanel;
    }

    public SavePanel getSavePanel() {
        return savePanel;
    }

    public Properties getConfigurations() {
        return configurations;
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    public AudioProcessor getAudioProcessor() {
        return audioProcessor;
    }

    public DeleteConfirmPanel getDeleteConfirmPanel() {
        return deleteConfirmPanel;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public void setAudioProcessor(AudioProcessor audioProcessor) {
        this.audioProcessor = audioProcessor;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new NavisMain().preinit();
    }

    public void preinit() {

        this.mainFrame = new MainFrame(this);

        this.windowSize = this.mainFrame.getSize();

        this.headerPanel = new HeaderPanel(mainFrame);
        
        this.testFirstTimeUser();   
        
        this.testOnline();

        this.mainFrame.setVisible(true);   // order is important, must be placed here.      
        
        this.init();

        File uploadFolder = new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER));
        uploadFolder.mkdir();            
        
        new Thread(){
            public void run(){
                while(true)
                {
                    if (!online) {
                        NavisMain.this.testOnline();
                        if(online)
                        {
                            if (stage.equalsIgnoreCase(FLOW_STATE_INIT)) {
                                init();
                            } else if (stage.equalsIgnoreCase(FLOW_STATE_MENU)) {
                                menu();
                            }                            
                        }
                    }else
                    {
                        NavisMain.this.testOnline();
                        if(!online)
                        {
                            if (stage.equalsIgnoreCase(FLOW_STATE_INIT)) {
                                init();
                            } else if (stage.equalsIgnoreCase(FLOW_STATE_MENU)) {
                                menu();
                            }                            
                        }                        
                    }
                        
                    try {
                        Thread.sleep(Long.valueOf(configurations.getProperty(TEST_NETWORK_INTERVAL)).longValue());
                    } catch (InterruptedException ex) {
                        // ignore
                    }         
                }
            }
        }.start();            
    }

    private void testFirstTimeUser() {

        configurations = new java.util.Properties();
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(new java.io.File("." + fileSeparator + "config.properties"));
            configurations.load(fis);
            fis.close();
        } catch (IOException ex) {
            // 1st time user
            firstTimeUser = true;

            try {
                configurations.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            // ignore, shouldn't happen
            }
        }
    }

    private void testOnline() {
        try {
            java.net.URL url = new java.net.URL(configurations.getProperty(REMOTE_CORP_URL));
            java.net.URLConnection connection = url.openConnection();
            connection.getContent();
            online = true;
            headerPanel.getLabel1().setText("Online");
        } catch (Exception ex) {
            online = false;
            headerPanel.getLabel1().setText("Offline");
        }
    }

    private void init() {

        stage = FLOW_STATE_INIT;
        
        try{
            this.mainFrame.remove(1);
        }catch(Exception ex)
        {
            // ignore
        }

        if (firstTimeUser && online) {
            this.postInitFirstTimeUserOnline();
        }
        if (firstTimeUser && (!online)) {
            this.postInitFirstTimeUserOffline();
        }
        if ((!firstTimeUser) && online) {
            this.postInitReturnUserOnline();
        }
        if ((!firstTimeUser) && (!online)) {
            this.postInitReturnUserOffline();
        }

        mainFrame.pack();
    }

    private void postInitFirstTimeUserOnline() {

        headerPanel.getButton1().setEnabled(true);
        headerPanel.getLabel2().setText("Welcome");
        headerPanel.getLabel3().setText("Please login now");

        loginPanel = new LoginPanel(mainFrame);
        loginPanel.getTextField1().setText("");
        loginPanel.getTextField2().setText("");
        loginPanel.clearLabels();
    }

    private void postInitFirstTimeUserOffline() {

        headerPanel.getButton1().setEnabled(true);
        headerPanel.getLabel2().setText("Welcome");
        headerPanel.getLabel3().setText("No network connections, pls connect");

        infoPanel = new InfoPanel(mainFrame);
        infoPanel.reset();
    }

    private void postInitReturnUserOnline() {

        headerPanel.getButton1().setEnabled(false);
        headerPanel.getLabel2().setText(configurations.getProperty(USERFULLNAME));
        headerPanel.getLabel3().setText("Download updates from server, pls wait");

        infoPanel = new InfoPanel(mainFrame);
        infoPanel.reset();
        infoPanel.getNonProgressBar().begin();
        
        mainFrame.pack();   // pack must be used along with thread
        new Thread() {  // mysaifu refreshes display only after method returns

            public void run() {
                if (NavisMain.this.synchronizeConfig(configurations.getProperty(USERNAME), configurations.getProperty(PASSWORD))) {
                    NavisMain.this.infoPanel.getNonProgressBar().stop();
                    NavisMain.this.headerPanel.getLabel3().setText("Download completed, click Menu to start");
                    NavisMain.this.headerPanel.getButton1().setEnabled(true);
                } else {
                    NavisMain.this.infoPanel.getNonProgressBar().stop();
                    NavisMain.this.headerPanel.getLabel3().setText("Download failed, click Menu to start");
                    NavisMain.this.headerPanel.getButton1().setEnabled(true);
                    NavisMain.this.infoPanel.getLabel2().setText("You may need to Edit Account.");
                }
            }
        }.start();
    }

    private void postInitReturnUserOffline() {

        headerPanel.getButton1().setEnabled(true);
        headerPanel.getLabel2().setText(configurations.getProperty(USERFULLNAME));
        headerPanel.getLabel3().setText("No network connections, pls click Menu");

        infoPanel = new InfoPanel(mainFrame);
        infoPanel.reset();
    }

    /**
     * Login and Synchronization
     */
    public void login() {

        if ((loginPanel.getTextField1().getText() == null) || (loginPanel.getTextField1().getText().trim().length() < Integer.valueOf(configurations.getProperty(MIN_USERNAME_LENGTH)).intValue()) || (loginPanel.getTextField1().getText().trim().length() > Integer.valueOf(configurations.getProperty(MAX_USERNAME_LENGTH)).intValue())) {
            loginPanel.clearLabels();
            loginPanel.getLabel3().setText("User name must be in " + configurations.getProperty(MIN_USERNAME_LENGTH) + "-" + configurations.getProperty(MAX_USERNAME_LENGTH) + " chars.");

        } else if ((loginPanel.getTextField2().getText() == null) || (loginPanel.getTextField2().getText().trim().length() < Integer.valueOf(configurations.getProperty(MIN_PASSWORD_LENGTH)).intValue()) || (loginPanel.getTextField2().getText().trim().length() > Integer.valueOf(configurations.getProperty(MAX_PASSWORD_LENGTH)).intValue())) {
            loginPanel.clearLabels();
            loginPanel.getLabel3().setText("User password must be in " + configurations.getProperty(MIN_PASSWORD_LENGTH) + "-" + configurations.getProperty(MAX_PASSWORD_LENGTH) + " chars.");
        } else {

            headerPanel.getLabel3().setText("Please wait…");
            loginPanel.getButton1().setEnabled(false);
            loginPanel.clearLabels();
            
            if (synchronizeConfig(loginPanel.getTextField1().getText().trim(), loginPanel.getTextField2().getText().trim())) {
                mainFrame.remove(1);

                headerPanel.getButton1().setEnabled(true);
                headerPanel.getLabel2().setText(configurations.getProperty(USERFULLNAME));
                headerPanel.getLabel3().setText("Click Menu to start");

                if (infoPanel == null) {
                    this.infoPanel = new InfoPanel(mainFrame);
                } else {
                    this.mainFrame.add(infoPanel, BorderLayout.CENTER, 1);
                }

                infoPanel.reset();

                infoPanel.getLabel2().setText("You’ve successfully logged in as user");
                infoPanel.getLabel3().setText(configurations.getProperty(USERFULLNAME) + ".");
                infoPanel.getLabel4().setText("If you have any question,");
                infoPanel.getLabel5().setText("please call " + configurations.getProperty(CONTACT_CORP_PHONE) + ",");
                infoPanel.getLabel6().setText("or log into the navis website");
                infoPanel.getLabel7().setText(configurations.getProperty(REMOTE_CORP_URL));
                infoPanel.getLabel8().setText("to update your account.");

                mainFrame.pack();
                
                // change all audio files to unfinished
                File uploadFolder = new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER));
                String[] uploadFiles = uploadFolder.list();            
                for (int i = 0; i < uploadFiles.length; i++) {
                    if(uploadFiles[i].indexOf("f")==0)
                    {
                        File file = new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER) + fileSeparator + uploadFiles[i]);
                        File renamedfile = new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER) + fileSeparator + "u" + uploadFiles[i].substring(1));
                        file.renameTo(renamedfile);
                    }
                }
                
            } else {
                loginPanel.getButton1().setEnabled(true);
                headerPanel.getLabel3().setText("Please login now");
                loginPanel.clearLabels();

                loginPanel.getLabel3().setText("Login failed. Please try again.");
                loginPanel.getLabel4().setText("If you have any question,");
                loginPanel.getLabel5().setText("please call " + configurations.getProperty(CONTACT_CORP_PHONE) + ",");
                loginPanel.getLabel6().setText("or log into the navis website");
                loginPanel.getLabel7().setText(configurations.getProperty(REMOTE_CORP_URL));
                loginPanel.getLabel8().setText("to update your account.");

            }
        }
    }

    private boolean synchronizeConfig(String username, String password) {

        try {
            //Web Services
            //JavaME WS Client JSR-172
//            ClientServices_Stub wsStub = new ClientServices_Stub();
//            wsStub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, username);
//            wsStub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, password);
//            nameValuePair[] params = wsStub.synchConfig().getParams();

            //AXIS WS Client
            ClientServicesPortBindingStub stub = (ClientServicesPortBindingStub)new ClientServices_ServiceLocator().getClientServicesPort();
            stub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, username);
            stub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, password);
            SynchConfigResponse response = stub.synchConfig(new SynchConfig());
            NameValuePair[] params = response.getConfigParams().getParams();

            configurations.clear();

            configurations.setProperty(USERNAME, username);
            configurations.setProperty(PASSWORD, password);

            for (int i = 0; i < params.length; i++) {
                configurations.put(params[i].getName(), params[i].getValue());
            }

            if ((configurations.getProperty(USERFULLNAME) != null) && (configurations.getProperty(USERFULLNAME).length() > 20)) {
                configurations.put(USERFULLNAME, configurations.getProperty(USERFULLNAME).substring(0, 20));
            }

            java.io.File configFile = new java.io.File("." + fileSeparator + "config.properties");
            java.io.FileOutputStream os = new java.io.FileOutputStream(configFile);
            configurations.store(os, "Download from the remote corp server");
            os.close();

            firstTimeUser = false;

            return true;

        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void menu() {
        
        if ((menuPanel != null) && (this.mainFrame.getComponent(1) == menuPanel)) {
            if (!online) {
                menuPanel.getButton3().setEnabled(false);
            } else {
                menuPanel.getButton3().setEnabled(true);
            }
            return;
        }      

        if (audioProcessor != null) {
            if (audioProcessor.capture.thread != null) {
                audioProcessor.capture.stop();
                do {
                } while (!audioProcessor.stable);

            } else if (audioProcessor.playback.thread != null) {
                audioProcessor.playback.stop();
                do {
                } while (!audioProcessor.stable);

            } else if (audioProcessor.rewind.thread != null) {
                audioProcessor.rewind.stop();
                do {
                } while (!audioProcessor.stable);

            } else if (audioProcessor.fastforward.thread != null) {
                audioProcessor.fastforward.stop();
                do {
                } while (!audioProcessor.stable);
            }
            audioProcessor.reset();
        }

        stage = FLOW_STATE_MENU;

        File uploadFolder = new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER));
        String[] uploadFiles = uploadFolder.list();

        mainFrame.remove(1);

        headerPanel.getButton1().setEnabled(true);

        if (menuPanel == null) {
            menuPanel = new MenuPanel(mainFrame);
        } else {
            this.mainFrame.add(menuPanel, java.awt.BorderLayout.CENTER, 1);
        }

        if (firstTimeUser && (!online)) {
            headerPanel.getLabel3().setText("Menu options");              
            menuPanel.getButton1().setEnabled(false);
            menuPanel.getButton2().setEnabled(false);
            menuPanel.getButton3().setEnabled(false);
            menuPanel.getButton4().setEnabled(true);    //exit
            menuPanel.getButton5().setEnabled(false);
        } else if (firstTimeUser && online) {
            headerPanel.getLabel3().setText("Menu options");              
            menuPanel.getButton1().setEnabled(false);
            menuPanel.getButton2().setEnabled(false);
            menuPanel.getButton3().setEnabled(true);    // edit account
            menuPanel.getButton4().setEnabled(true);    //exit
            menuPanel.getButton5().setEnabled(false);
        } else {
            if (uploadFiles.length >= Integer.valueOf(configurations.getProperty(MAX_AUDIO_STORED)).intValue()) {
                headerPanel.getLabel3().setText(uploadFiles.length + " pending record(s), no space for new");
                menuPanel.getButton1().setEnabled(false);
            } else {
                headerPanel.getLabel3().setText(uploadFiles.length + " pending record(s)");
                menuPanel.getButton1().setEnabled(true);
            }
            if (uploadFiles.length <= 0) {
                menuPanel.getButton2().setEnabled(false);
            } else {
                menuPanel.getButton2().setEnabled(true);
            }
            if (!online) {
                menuPanel.getButton3().setEnabled(false);
            } else {
                menuPanel.getButton3().setEnabled(true);
            }
        }
        mainFrame.pack();
    }

    public void editAccount() {
        stage = FLOW_STATE_INIT;

        mainFrame.remove(1);
        headerPanel.getButton1().setEnabled(true);
        headerPanel.getLabel3().setText("Please login now");

        if (loginPanel == null) {
            loginPanel = new LoginPanel(mainFrame);
        } else {
            this.mainFrame.add(loginPanel, BorderLayout.CENTER, 1);
        }

        loginPanel.getTextField1().setText("");
        loginPanel.getTextField2().setText("");
        loginPanel.clearLabels();

        mainFrame.pack();
    }

    public void recordNew() {
        stage = FLOW_STATE_RECORD;

        if (audioProcessor == null) {
            audioProcessor = new AudioProcessor(this);
        }
        audioProcessor.reset();

        mainFrame.remove(1);

        headerPanel.getButton1().setEnabled(true);
        headerPanel.getLabel3().setText("Click Record button to start recording");

        int minAudioLength = (Integer.valueOf(configurations.getProperty(MIN_AUDIO_LENGTH)).intValue());
        int maxAudioLength = (Integer.valueOf(configurations.getProperty(MAX_AUDIO_LENGTH)).intValue());

        if (recordPanel == null) {
            recordPanel = new RecordPanel(mainFrame, maxAudioLength, minAudioLength, 0);
        } else {
            this.mainFrame.add(recordPanel, BorderLayout.CENTER, 1);
        }

        recordPanel.getProgessBar().resetProgress(0, 0);

        recordPanel.getLabel1().setText("Min " + minAudioLength / 60 + "'" + minAudioLength % 60 + "\"" + ", Max " + maxAudioLength / 60 + "'" + maxAudioLength % 60 + "\"  ");  // reserve some space
        recordPanel.getLabel2().setText("  0'0\""); // reserve some space

        recordPanel.getButton8().setEnabled(false);
        recordPanel.getButton9().setEnabled(false);

        mainFrame.pack();
    }

    public void captureAudio() {

        if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        if (audioProcessor.capture.thread != null) {    // resume
            // doing nothing

            //audioProcessor.capture.running = true;    // resume for mysaifu
            //audioProcessor.capture.line.start();      // resume for JVM
        } else if (recordPanel.getProgessBar().getCount() < recordPanel.getProgessBar().getMax()) {

            audioProcessor.capture.start();
            this.getHeaderPanel().getLabel3().setText("Recording...");
        }
    }

    public void rewindAudio() {

        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        if (audioProcessor.rewind.thread != null) {
        // doing nothing
        } else {
            audioProcessor.rewind.start();
        }

        this.getHeaderPanel().getLabel3().setText("Rewinding...");
    }

    public void stopAudio() {

        // pause - not needed
        //if (audioProcessor.capture.thread != null) {
        //    //audioProcessor.capture.line.stop();   // doesn't work in mysaifu
        //    audioProcessor.capture.running = false;
        //} else {
        //    if (audioProcessor.playback.thread != null) {
        //        //audioProcessor.playback.line.stop();  // doesn't work in mysaifu
        //        audioProcessor.playback.running = false;
        //    }
        //}

        // full stop      
        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        this.getHeaderPanel().getLabel3().setText("Stop");
    }

    public void fastforwardAudio() {

        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);
        }

        if (audioProcessor.fastforward.thread != null) {
        // doing nothing
        } else {
            audioProcessor.fastforward.start();
        }

        this.getHeaderPanel().getLabel3().setText("Fastforwarding...");
    }

    public void playAudio() {

        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        if (audioProcessor.playback.thread != null) {   // resume
        // do nothing

        //audioProcessor.playback.running = true;   // resume for mysaifu
        //audioProcessor.playback.line.start();     // resume for JVM
        } else // play from begining
        {
            audioProcessor.playback.start();
        }

        this.getHeaderPanel().getLabel3().setText("Playing...");
    }

    public void startAudio() {
        this.getHeaderPanel().getLabel3().setText("Go to the beginning...");

        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        audioProcessor.start();

        this.getHeaderPanel().getLabel3().setText("Stop");
    }

    public void endAudio() {
        this.getHeaderPanel().getLabel3().setText("Go to the end...");

        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        audioProcessor.end();

        this.getHeaderPanel().getLabel3().setText("Stop");
    }

    public void saveAudio() {
        stage = FLOW_STATE_SAVE;

        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        mainFrame.remove(1);
        headerPanel.getButton1().setEnabled(false);
        headerPanel.getLabel3().setText("Save or Upload audio file");

        if (savePanel == null) {
            savePanel = new SavePanel(this.mainFrame);
        } else {
            mainFrame.add(savePanel, java.awt.BorderLayout.CENTER, 1);
        }

        savePanel.reset();

        savePanel.getButton3().setEnabled(true);

        if (audioProcessor.fileName != null) {
            savePanel.getLabel1().setText("File Name: " + audioProcessor.fileName);
        } else {
            savePanel.getLabel1().setText("File Name: untitled");
        }

        for (int i = 0; i < Integer.parseInt(this.getConfigurations().getProperty(PRIORITY_COUNT)); i++) {
            savePanel.getList1().add(this.getConfigurations().getProperty(PRIORITY_CODE + "_" + i) + " (" + this.getConfigurations().getProperty(PRIORITY_NAME + "_" + i) + ")");
        }
        for (int i = 0; i < Integer.parseInt(this.getConfigurations().getProperty(TEMPLATE_COUNT)); i++) {
            savePanel.getList2().add(this.getConfigurations().getProperty(TEMPLATE_CODE + "_" + i) + " (" + this.getConfigurations().getProperty(TEMPLATE_NAME + "_" + i) + ")");
        }

        if ((audioProcessor.fileName != null) && (!audioProcessor.fileName.equalsIgnoreCase("untitled"))) {
            StringTokenizer tokens = new StringTokenizer(audioProcessor.fileName, "_");    // no split(regex)
            String status = tokens.nextToken();
            String priority = tokens.nextToken();
            String template = new StringTokenizer(tokens.nextToken(), ".").nextToken();

            for (int i = 0; i < savePanel.getList1().getItemCount(); i++) {
                if (savePanel.getList1().getItem(i).indexOf(priority) == 0) {
                    savePanel.getList1().select(i);
                    break;
                }
            }
            for (int i = 0; i < savePanel.getList2().getItemCount(); i++) {
                if (savePanel.getList2().getItem(i).indexOf(template) == 0) {
                    savePanel.getList2().select(i);
                    break;
                }
            }

            if ((savePanel.getList1().getSelectedItem() != null) && (savePanel.getList2().getSelectedItem() != null)) {
                savePanel.getButton1().setEnabled(true);
                savePanel.getButton2().setEnabled(true);
            }
        }
        mainFrame.pack();
    }

    public void deleteAudio() {
        if (audioProcessor.capture.thread != null) {
            audioProcessor.capture.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.playback.thread != null) {
            audioProcessor.playback.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.rewind.thread != null) {
            audioProcessor.rewind.stop();
            do {
            } while (!audioProcessor.stable);

        } else if (audioProcessor.fastforward.thread != null) {
            audioProcessor.fastforward.stop();
            do {
            } while (!audioProcessor.stable);
        }

        deletePending();
    }

    public void deleteConfirm() {
        try {
            if (this.stage == FLOW_STATE_RECORD) {
                if (this.audioProcessor.file != null) {
                    this.audioProcessor.audioInputStream.close();
                    this.audioProcessor.file.delete();
                }
                audioProcessor.reset();

            } else if (this.stage == FLOW_STATE_EDITPENDING) {
                if (this.pendingPanel.getList1().getSelectedItem() != null) {
                    new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER) + fileSeparator + pendingPanel.getList1().getSelectedItem()).delete();
                }
            } else if (this.stage == FLOW_STATE_SAVE) {
                if (this.audioProcessor.file != null) {
                    this.audioProcessor.audioInputStream.close();
                    this.audioProcessor.file.delete();
                }
                audioProcessor.reset();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.displayInfo();
        this.getInfoPanel().getLabel2().setText("Auido clip was deleted.");
        this.getHeaderPanel().getLabel3().setText("Click Menu to return");
        this.getHeaderPanel().getButton1().setEnabled(true);
        mainFrame.pack();
    }

    public void cancelConfirm() {
        if (this.stage == FLOW_STATE_RECORD) {
            this.mainFrame.remove(1);
            this.getHeaderPanel().getLabel3().setText("Stop");
            this.mainFrame.add(recordPanel, java.awt.BorderLayout.CENTER, 1);

        } else if (this.stage == FLOW_STATE_EDITPENDING) {
            this.mainFrame.remove(1);
            this.getHeaderPanel().getLabel3().setText("Select a file");
            this.mainFrame.add(pendingPanel, java.awt.BorderLayout.CENTER, 1);
            if (pendingPanel.getList1().getSelectedItem() == null) // mysaifu bug
            {
                pendingPanel.getButton1().setEnabled(false);
                pendingPanel.getButton2().setEnabled(false);

                pendingPanel.getLabel1().setText("");
                pendingPanel.getLabel2().setText("");
                pendingPanel.getLabel3().setText("");
                pendingPanel.getLabel4().setText("");
                pendingPanel.getLabel5().setText("");
                pendingPanel.getLabel6().setText("");
            }

        } else if (this.stage == FLOW_STATE_SAVE) {
            mainFrame.remove(1);
            headerPanel.getButton1().setEnabled(false);
            headerPanel.getLabel3().setText("Save or Upload audio file");
            mainFrame.add(savePanel, java.awt.BorderLayout.CENTER, 1);

            // overcome a mysaifu bug
            if ((audioProcessor.fileName != null) && (!audioProcessor.fileName.equalsIgnoreCase("untitled"))) {
                StringTokenizer tokens = new StringTokenizer(audioProcessor.fileName, "_");    // no split(regex)
                String status = tokens.nextToken();
                String priority = tokens.nextToken();
                String template = new StringTokenizer(tokens.nextToken(), ".").nextToken();

                for (int i = 0; i < savePanel.getList1().getItemCount(); i++) {
                    if (savePanel.getList1().getItem(i).indexOf(priority) == 0) {
                        savePanel.getList1().select(i);
                        break;
                    }
                }
                for (int i = 0; i < savePanel.getList2().getItemCount(); i++) {
                    if (savePanel.getList2().getItem(i).indexOf(template) == 0) {
                        savePanel.getList2().select(i);
                        break;
                    }
                }

                if ((savePanel.getList1().getSelectedItem() != null) && (savePanel.getList2().getSelectedItem() != null)) {
                    savePanel.getButton1().setEnabled(true);
                    savePanel.getButton2().setEnabled(true);
                }
            }
        }
        mainFrame.pack();
    }

    public void editPending() {
        stage = FLOW_STATE_EDITPENDING;

        File uploadFolder = new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER));
        String[] uploadFiles = uploadFolder.list();

        if (uploadFiles.length >= Integer.valueOf(configurations.getProperty(MAX_AUDIO_STORED)).intValue()) {
            headerPanel.getLabel3().setText(uploadFiles.length + " pending record(s), no space for new");
        } else {
            headerPanel.getLabel3().setText(uploadFiles.length + " pending record(s)");
        }

        mainFrame.remove(1);
        if (pendingPanel == null) {
            pendingPanel = new PendingPanel(mainFrame);
        } else {
            mainFrame.add(pendingPanel, java.awt.BorderLayout.CENTER, 1);
        }

        pendingPanel.reset();

        for (int i = 0; i < uploadFiles.length; i++) {
            pendingPanel.getList1().add(uploadFiles[i]);
        }
        mainFrame.pack();
    }

    public void updatePending() {
        stage = FLOW_STATE_RECORD;

        if (audioProcessor == null) {
            audioProcessor = new AudioProcessor(this);
        }
        audioProcessor.reset();

        mainFrame.remove(1);
        headerPanel.getButton1().setEnabled(true);
        headerPanel.getLabel3().setText("Click Record button to start recording");

        int minAudioLength = (Integer.valueOf(configurations.getProperty(MIN_AUDIO_LENGTH)).intValue());
        int maxAudioLength = (Integer.valueOf(configurations.getProperty(MAX_AUDIO_LENGTH)).intValue());

        audioProcessor.createAudioInputStream(new File("." + fileSeparator + configurations.getProperty(UPLOAD_FOLDER) + fileSeparator + pendingPanel.getList1().getSelectedItem()));

        if (recordPanel == null) {
            recordPanel = new RecordPanel(mainFrame, maxAudioLength, minAudioLength, (int) audioProcessor.duration);
        } else {
            this.mainFrame.add(recordPanel, BorderLayout.CENTER, 1);
        }

        recordPanel.getProgessBar().resetProgress(0, (int) audioProcessor.duration);

        recordPanel.getLabel1().setText("Min " + minAudioLength / 60 + "'" + minAudioLength % 60 + "\"" + ", Max " + maxAudioLength / 60 + "'" + maxAudioLength % 60 + "\"  ");  // reserve some space
        recordPanel.getLabel2().setText("  0'0\""); // reserve some space

        if (recordPanel.getProgessBar().getThrust() > 0) {
            recordPanel.getButton9().setEnabled(true);
        }
        if (recordPanel.getProgessBar().getThrust() >= recordPanel.getProgessBar().getMark()) {
            recordPanel.getButton8().setEnabled(true);
        }
        mainFrame.pack();
    }

    public void deletePending() {
        this.mainFrame.remove(1);

        if (deleteConfirmPanel == null) {
            deleteConfirmPanel = new DeleteConfirmPanel(this.mainFrame);
        } else {
            this.mainFrame.add(deleteConfirmPanel, java.awt.BorderLayout.CENTER, 1);
        }

        String fileName = "";
        if (this.stage == FLOW_STATE_RECORD) {
            fileName = audioProcessor.fileName;

        } else if (this.stage == FLOW_STATE_EDITPENDING) {
            fileName = pendingPanel.getList1().getSelectedItem();

        } else if (this.stage == FLOW_STATE_SAVE) {
            fileName = audioProcessor.fileName;
        }

        deleteConfirmPanel.getLabel1().setText("File Name: " + fileName);
        this.getHeaderPanel().getLabel3().setText("Confirm deletion");

        mainFrame.pack();
    }

    public void saveFile() {

        headerPanel.getLabel3().setText("Save audio file");
        this.displayInfo();
        infoPanel.getNonProgressBar().begin();
        
        mainFrame.pack();   // pack must be used along with thread
        new Thread() {  // mysaifu refreshes display only after method returns

            public void run() {
                // save audio clip (same code in uploadFile method)
                String priority = new StringTokenizer(NavisMain.this.savePanel.getList1().getSelectedItem(), " ").nextToken();
                String template = new StringTokenizer(NavisMain.this.savePanel.getList2().getSelectedItem(), " ").nextToken();

                DateFormat dataFormat = new SimpleDateFormat("MMddyyHHmm");
                String date = dataFormat.format(new Date());

                String fileName = "u" + date + "_" + priority + "_" + template + ".";

                String fileType = NavisMain.this.getConfigurations().getProperty(AUDIO_FORMAT_FILETYPE);

                if (fileType.equalsIgnoreCase("WAVE")) {
                    fileName += "wav";
                    audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.WAVE);

                } else if (fileType.equalsIgnoreCase("AIFC")) {
                    fileName += "aifc";
                    audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.AIFC);

                } else if (fileType.equalsIgnoreCase("AIFF")) {
                    fileName += "aiff";
                    audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.AIFF);

                } else if (fileType.equalsIgnoreCase("AU")) {
                    fileName += "au";
                    audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.AU);

                } else if (fileType.equalsIgnoreCase("SND")) {
                    fileName += "snd";
                    audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.SND);
                }

                NavisMain.this.infoPanel.getNonProgressBar().stop();

                NavisMain.this.getInfoPanel().getLabel2().setText("Auido clip was saved.");
                NavisMain.this.getInfoPanel().getLabel3().setText("File Name: " + fileName);
                NavisMain.this.getInfoPanel().getLabel4().setText("Status: Unfinished");
                NavisMain.this.getInfoPanel().getLabel5().setText("Priority: " + priority);
                NavisMain.this.getInfoPanel().getLabel6().setText("Template: " + template);
                NavisMain.this.getHeaderPanel().getLabel3().setText("Click Menu to return");
                NavisMain.this.getHeaderPanel().getButton1().setEnabled(true);

                // deleting old file has to happen after save
                try {
                    if (audioProcessor.file != null) {
                        audioProcessor.audioInputStream.close();    // close IO
                        audioProcessor.file.delete();
                    }
                } catch (Exception ex) {
                //
                }
                audioProcessor.reset();
            }
        }.start();
    }

    public void uploadFile() {
        headerPanel.getLabel3().setText("Upload audio file");
        this.displayInfo();
        infoPanel.getNonProgressBar().begin();

        mainFrame.pack();   // pack must be used along with thread
        new Thread() {  // mysaifu refreshes display only after method returns

            public void run() {

                String acknowledge = "Unknown response";

                // save audio clip (same code in saveFile method)
                String priority = new StringTokenizer(NavisMain.this.savePanel.getList1().getSelectedItem(), " ").nextToken();
                String template = new StringTokenizer(NavisMain.this.savePanel.getList2().getSelectedItem(), " ").nextToken();

                DateFormat dataFormat = new SimpleDateFormat("MMddyyHHmm");
                String date = dataFormat.format(new Date());

                String fileName = "f" + date + "_" + priority + "_" + template + ".";
                String fileType = NavisMain.this.getConfigurations().getProperty(AUDIO_FORMAT_FILETYPE);

                try {
                    if (fileType.equalsIgnoreCase("WAVE")) {
                        fileName += "wav";
                        audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.WAVE);

                    } else if (fileType.equalsIgnoreCase("AIFC")) {
                        fileName += "aifc";
                        audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.AIFC);

                    } else if (fileType.equalsIgnoreCase("AIFF")) {
                        fileName += "aiff";
                        audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.AIFF);

                    } else if (fileType.equalsIgnoreCase("AU")) {
                        fileName += "au";
                        audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.AU);

                    } else if (fileType.equalsIgnoreCase("SND")) {
                        fileName += "snd";
                        audioProcessor.saveToFile("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName, AudioFileFormat.Type.SND);
                    }

                    // deleting old file has to happen after save
                    try {
                        if (audioProcessor.file != null) {
                            audioProcessor.audioInputStream.close();    // close IO
                            audioProcessor.file.delete();
                        }
                    } catch (Exception ex) {
                    //
                    }
                    audioProcessor.reset();

                    //Web Services
                    //JavaME WS Client JSR-172
                    ClientServices_Stub wsStub = new ClientServices_Stub();
                    wsStub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, NavisMain.this.getConfigurations().getProperty(NavisMain.USERNAME));
                    wsStub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, NavisMain.this.getConfigurations().getProperty(NavisMain.PASSWORD));

                    // with metaheader
                    File file = new File("." + fileSeparator + getConfigurations().getProperty(UPLOAD_FOLDER) + fileSeparator + fileName);
                    byte[] audio = new byte[(int) file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    fis.read(audio);
                    fis.close();

                    acknowledge = wsStub.upload(NavisMain.this.savePanel.getList1().getSelectedItem(),
                            NavisMain.this.savePanel.getList2().getSelectedItem(),
                            audio);

                    // upload succeeded
                    NavisMain.this.infoPanel.getNonProgressBar().stop();

                    NavisMain.this.getHeaderPanel().getLabel3().setText("Click Menu to return");
                    NavisMain.this.getInfoPanel().getLabel2().setText("Upload succeeded.");
                    NavisMain.this.getInfoPanel().getLabel3().setText(acknowledge);
                    NavisMain.this.getHeaderPanel().getButton1().setEnabled(true);

                    file.delete();  // delete new file

                } catch (Exception ex) {
                    // upload failed
                    NavisMain.this.infoPanel.getNonProgressBar().stop();

                    if (online) {
                        NavisMain.this.headerPanel.getLabel3().setText(ex.getMessage());
                    } else {
                        NavisMain.this.headerPanel.getLabel3().setText("No network connections, pls click Menu");
                    }

                    NavisMain.this.getInfoPanel().getLabel2().setText("Upload failed.");
                    NavisMain.this.getInfoPanel().getLabel3().setText("Audio clip was saved.");
                    NavisMain.this.getInfoPanel().getLabel4().setText("File Name: " + fileName);
                    NavisMain.this.getInfoPanel().getLabel5().setText("Status: Finished");
                    NavisMain.this.getInfoPanel().getLabel6().setText("Priority: " + priority);
                    NavisMain.this.getInfoPanel().getLabel7().setText("Template: " + template);
                    NavisMain.this.getHeaderPanel().getButton1().setEnabled(true);
                }
            }
        }.start();
    }

    public void displayInfo() {
        this.mainFrame.remove(1);

        if (this.infoPanel == null) {
            this.infoPanel = new InfoPanel(mainFrame);

        } else {
            this.mainFrame.add(infoPanel, java.awt.BorderLayout.CENTER, 1);
        }
        infoPanel.reset();
    }

    public void viewProfile() {
        this.headerPanel.getLabel3().setText("My profile");

        this.mainFrame.remove(1);

        if (this.profilePanel == null) {
            this.profilePanel = new ProfilePanel(mainFrame);

        } else {
            this.mainFrame.add(profilePanel, java.awt.BorderLayout.CENTER, 1);
        }

        profilePanel.getLabel1().setText("User Name: " + ((this.getConfigurations().get(USERNAME) == null) ? "" : this.getConfigurations().get(USERNAME)));
        profilePanel.getLabel2().setText("Full Name: " + ((this.getConfigurations().get(USERFULLNAME) == null) ? "" : this.getConfigurations().get(USERFULLNAME)));
        profilePanel.getLabel3().setText("Client Code: " + ((this.getConfigurations().get(CLIENT_CODE) == null) ? "" : this.getConfigurations().get(CLIENT_CODE)));
        profilePanel.getLabel6().setText("Client Name: " + ((this.getConfigurations().get(CLIENT_NAME) == null) ? "" : this.getConfigurations().get(CLIENT_NAME)));

        profilePanel.getList1().removeAll();
        profilePanel.getList2().removeAll();

        for (int i = 0; i < Integer.parseInt(this.getConfigurations().getProperty(PRIORITY_COUNT)); i++) {
            profilePanel.getList1().add(this.getConfigurations().getProperty(PRIORITY_CODE + "_" + i) + " (" + this.getConfigurations().getProperty(PRIORITY_NAME + "_" + i) + ")");
        }
        for (int i = 0; i < Integer.parseInt(this.getConfigurations().getProperty(TEMPLATE_COUNT)); i++) {
            profilePanel.getList2().add(this.getConfigurations().getProperty(TEMPLATE_CODE + "_" + i) + " (" + this.getConfigurations().getProperty(TEMPLATE_NAME + "_" + i) + ")");
        }

        mainFrame.pack();
    }

    public void exit() {
        this.mainFrame.dispose();
        System.exit(0);
    }
}
