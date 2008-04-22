/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naviswirelesscdc;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * HP PocketPC Hardware SoundCard supports following audio formats,
 * PCM_SIGNED, 16bit(only), 8000Hz+, 1channel+, littleEndian(only)
 * PCM_UNSIGNED, 8bit(only), 8000Hz+, 1channel+, littleEndian(only).
 * Don't Support ALAW 
 * ULAW,
 * 
 * @author dr. xinyu liu
 */
public class AudioProcessor {

    final int bufSize = 16384;

    NavisMain main;
    
    Capture capture = new Capture();
    Playback playback = new Playback();
    Rewind rewind = new Rewind();
    Fastforward fastforward = new Fastforward();
    
    AudioInputStream audioInputStream;

    File file;
    String fileName;
    double duration;   // in second
    long cursor;
    String errStr;
    boolean stable;
        
    public AudioProcessor(NavisMain main) {
        this.main = main;
        
        audioInputStream = null;
        file = null;
        fileName = "untitled";
        duration = 0;    
        cursor = 0;
        stable = true;
    }

    public void reset()
    {
        try{
            if(audioInputStream != null)
                audioInputStream.close();   // close IO
        }catch(Exception ex)
        {
            //
        }        
        audioInputStream = null;
        file = null;
        fileName = "untitled";
        duration = 0;    
        cursor = 0;
        stable = true;
    }
    
    public AudioFormat getPreferredFormat() {

        AudioFormat.Encoding encoding;

        String _encoding = (String) main.getConfigurations().getProperty(main.AUDIO_FORMAT_CODEC);
        if (_encoding.equalsIgnoreCase("ALAW")) {
            encoding = AudioFormat.Encoding.ALAW;

        } else if (_encoding.equalsIgnoreCase("PCM_SIGNED")) {
            encoding = AudioFormat.Encoding.PCM_SIGNED;

        } else if (_encoding.equalsIgnoreCase("PCM_UNSIGNED")) {
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;

        } else {
            encoding = AudioFormat.Encoding.ULAW;
        }

        float rate = Float.valueOf(main.getConfigurations().getProperty(main.AUDIO_FORMAT_FREQUENCY)).floatValue();
        int sampleSize = Integer.valueOf(main.getConfigurations().getProperty(main.AUDIO_FORMAT_BIT)).intValue();
        int channels = ((String) main.getConfigurations().getProperty(main.AUDIO_FORMAT_STEREO)).equalsIgnoreCase("mono") ? 1 : 2;
        boolean bigEndian = ((String) main.getConfigurations().getProperty(main.AUDIO_FORMAT_ENDIAN)).equalsIgnoreCase("bigEndian") ? true : false;

        return new AudioFormat(encoding, rate, sampleSize,
                channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    public void createAudioInputStream(File file) {
                
        if (file != null && file.isFile()) {
            
            BufferedInputStream bis = null;
    
            try {
                this.file = file;
                errStr = null;
                
                //audioInputStream = AudioSystem.getAudioInputStream(file); //Doesn't support reset/mark
                bis = new BufferedInputStream(new FileInputStream(file));
                audioInputStream = AudioSystem.getAudioInputStream(bis);
                AudioFormat format = audioInputStream.getFormat();
                byte[] audioBytes = new byte[(int)(audioInputStream.getFrameLength()*audioInputStream.getFormat().getFrameSize())];
                audioInputStream.read(audioBytes);  
                audioInputStream.close();   // close IO, work around for java.io.IOException: Resetting to invalid mark
		ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        	audioInputStream = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize());                
                
                fileName = file.getName();
                long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;

                // reset to the beginnning of the stream
                try {
                    //audioInputStream.mark(0);
                    //audioInputStream.reset();
                    cursor = 0;
                } catch (Exception e) {
                }                
                
            } catch (Exception ex) {
                try{
                    bis.close();    // close non-audio file IO
                }catch(Exception e)
                {
                    // ignore
                }
                reportStatus(ex.toString());
            }
        } else {
            reportStatus("Audio file required.");
        }
    }

    public void saveToFile(String name, AudioFileFormat.Type fileType) {

        if (audioInputStream == null) {
            reportStatus("No loaded audio to save");
            return;
        }

        // reset to the beginnning of the captured data
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            reportStatus("Unable to reset stream " + e);
            return;
        }

        try {   // don't touch existing file
            if (AudioSystem.write(audioInputStream, fileType, new File(name)) == -1) {
                throw new IOException("Problems writing to file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            reportStatus(ex.toString());
        }
    }
    
    private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            main.getHeaderPanel().getLabel3().setText(errStr);
        }
    }

    public void start() {
        stable = false;
        if (audioInputStream != null) {
            // reset to the beginnning of the stream
            try {
                audioInputStream.reset();
                cursor = 0;
            } catch (Exception e) {
            }
            main.getRecordPanel().getProgessBar().resetProgress(0, (int) duration);
        } else {
            main.getRecordPanel().getProgessBar().resetProgress(0, 0);
        }
        
        main.getRecordPanel().getLabel2().setText(
                main.getRecordPanel().getProgessBar().getCount() / 60 + "'" + main.getRecordPanel().getProgessBar().getCount() % 60 + "\"");
        
        stable = true;
    }

    public void end() {
        stable = false;
        if (audioInputStream != null) {
            try {
                audioInputStream.reset();
                cursor = audioInputStream.getFrameLength() * audioInputStream.getFormat().getFrameSize();
                audioInputStream.skip(cursor);
            } catch (Exception e) {
            }
            main.getRecordPanel().getProgessBar().resetProgress((int) duration, (int) duration);
        }
        
        main.getRecordPanel().getLabel2().setText(
                main.getRecordPanel().getProgessBar().getCount() / 60 + "'" + main.getRecordPanel().getProgessBar().getCount() % 60 + "\"");
        
        stable = true;
    }

    public class Rewind implements Runnable {

        Thread thread;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Rewind");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null) {
                reportStatus(errStr);
            }
            if (thread != null) {
                thread = null;
            }
        }

        public void run() {
            stable = false;
            
            // make sure we have something to rewind
            if (audioInputStream == null) {
                shutDown("No loaded audio to rewind");
                return;
            }

            AudioFormat format = audioInputStream.getFormat();
            
            while ((thread!=null) && (cursor>0)) {
                try{                   
                    int step = (int)(format.getFrameRate() * format.getFrameSize());
                    
                    if((cursor-=step)<0)
                        cursor=0;
                    
                    audioInputStream.reset();
                    audioInputStream.skip(cursor);

                    // Update UI Graphics
                    if(main.getRecordPanel().getProgessBar().getCount()>0)
                    {
                        main.getRecordPanel().getProgessBar().updateProgress(-1);
                        main.getRecordPanel().getLabel2().setText(
                                main.getRecordPanel().getProgessBar().getCount() / 60 + "'" + main.getRecordPanel().getProgessBar().getCount() % 60 + "\"");
                    }
                    
                    Thread.sleep(200);
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }   
            }
            main.getHeaderPanel().getLabel3().setText("Stop");
            stable = true;            
        }
    }

    public class Fastforward implements Runnable {

        Thread thread;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Fastforward");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null) {
                reportStatus(errStr);
            }
            if (thread != null) {
                thread = null;
            }
        }

        public void run() {
            stable = false;
            
            // make sure we have something to rewind
            if (audioInputStream == null) {
                shutDown("No loaded audio to fastforward");
                return;
            }

            AudioFormat format = audioInputStream.getFormat();
            
            long audioInputStreamLength = audioInputStream.getFrameLength()*audioInputStream.getFormat().getFrameSize();
            int step = (int)(format.getFrameRate() * format.getFrameSize());
            
            while ((thread!=null) && (cursor<audioInputStreamLength)) {
                try{                   
                    if((cursor+=step)>audioInputStreamLength)
                        cursor=audioInputStreamLength;
                    
                    audioInputStream.reset();
                    audioInputStream.skip(cursor);

                    // Update UI Graphics
                    if(cursor<audioInputStreamLength)
                    {
                        main.getRecordPanel().getProgessBar().updateProgress(1);
                        main.getRecordPanel().getLabel2().setText(
                                main.getRecordPanel().getProgessBar().getCount() / 60 + "'" + main.getRecordPanel().getProgessBar().getCount() % 60 + "\"");
                    }
                    
                    Thread.sleep(200);
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }   
            }
            main.getHeaderPanel().getLabel3().setText("Stop");
            stable = true;            
        }
    }
    
    /**
     * Write data to the OutputChannel.
     */
    public class Playback implements Runnable {

        SourceDataLine line;
        Thread thread;
        //boolean running;  // pause for mysaifu

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Playback");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null) {
                reportStatus(errStr);
            }
            if (thread != null) {
                thread = null;
            }
        }

        public void run() {
            stable = false;
            
            //running = true; // pause for mysaifu
            
            // make sure we have something to play
            if (audioInputStream == null) {
                shutDown("No loaded audio to play back");
                return;
            }

            AudioFormat format = audioInputStream.getFormat();
            
            // get an AudioInputStream of the desired format for playback (doen't work in mysaifu)
            //AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
            
            AudioInputStream playbackInputStream = audioInputStream;
            
            if (playbackInputStream == null) {
                shutDown("Unable to generate audio input stream.");
                return;
            }

            // define the required attributes for our line, 
            // and make sure a compatible line is supported.

            DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                    format);
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the source data line for playback.

            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, bufSize);
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            }

            // play back the captured audio data

            int frameSizeInBytes = audioInputStream.getFormat().getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;
            int totalNumBytesRead = 0;

            // start the source data line
            line.start();

            long deltaTime = 0;

            while (thread != null) {
                                
                try {
                    if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                        break;
                    }
                    totalNumBytesRead += numBytesRead;
                    int numBytesRemaining = numBytesRead;

                    // Update UI Graphics
                    deltaTime += (long) ((numBytesRemaining * 1000) / (format.getFrameRate() * frameSizeInBytes));
                    if (deltaTime >= 1000) {
                        deltaTime -= 1000;
                        main.getRecordPanel().getProgessBar().updateProgress(1);
                        main.getRecordPanel().getLabel2().setText(
                                main.getRecordPanel().getProgessBar().getCount() / 60 + "'" + main.getRecordPanel().getProgessBar().getCount() % 60 + "\"");
                    }
                    
                    while (numBytesRemaining > 0) {
                        
                        // pause for mysaifu - not needed
                        //try{
                        //    if(!running)    //stop(pause)
                        //    {
                        //        Thread.sleep(500);
                        //        continue;
                        //    }
                        //}catch(Exception ex)
                        //{
                        //    ex.printStackTrace();
                        //}                            
                        
                        numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                        
                        // pause - doesn't work in mysaifu
                        //try{
                        //    if(!line.isRunning())  //if(numBytesRemaining>0) //
                        //        Thread.sleep(500);
                        //}catch(Exception ex)
                        //{
                        //    ex.printStackTrace();
                        //}                        
                    }
                    
                } catch (Exception e) {
                    shutDown("Error during playback: " + e);
                    break;
                }

            }
            // we reached the end of the stream.  let the data play out, then
            // stop and close the line.
            if (thread != null) {
                line.drain();
            }
            line.stop();
            line.close();
            line = null;
            shutDown(null);

            cursor += totalNumBytesRead;
            
            main.getHeaderPanel().getLabel3().setText("Stop");
            stable = true;
        }
    } // End class Playback

    /** 
     * Reads data from the input channel and writes to the output stream
     */
    class Capture implements Runnable {

        TargetDataLine line;
        Thread thread;
        //boolean running;  // pause for mysaifu

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Capture");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null && thread != null) {
                thread = null;
                reportStatus(errStr);
            }
        }

        public void run() {
            stable = false;

            //running = true;   // pause for mysaifu
            
            // define the required attributes for our line, 
            // and make sure a compatible line is supported.
            AudioFormat format;
            if (audioInputStream != null) {
                format = audioInputStream.getFormat();
            } else {
                format = getPreferredFormat();
            }

            DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                    format);

            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the target data line for capture.

            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            } catch (SecurityException ex) {
                shutDown(ex.toString());
                //JavaSound.showInfoDialog();
                return;
            } catch (Exception ex) {
                shutDown(ex.toString());
                return;
            }

            // play back the captured audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = -1;

            line.start();
            
            long deltaTime = 0;

            while (thread != null) 
            {        
                // pause for mysaifu - not needed
                //try{
                //    if(!running)    
                //    {
                //        Thread.sleep(500);
                //        continue;
                //    }
                //}catch(Exception ex)
                //{
                //    ex.printStackTrace();
                //}
                
                if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) 
                {               
                    break;
                }
                out.write(data, 0, numBytesRead);

                // Update UI Graphics
                deltaTime += (long) ((numBytesRead * 1000) / (format.getFrameRate() * frameSizeInBytes));
                if (deltaTime >= 1000) {
                    deltaTime -= 1000;
                    main.getRecordPanel().getProgessBar().updateProgress(1);
                    main.getRecordPanel().getLabel2().setText(
                            main.getRecordPanel().getProgessBar().getCount() / 60 + "'" + main.getRecordPanel().getProgessBar().getCount() % 60 + "\"");

                    if(main.getRecordPanel().getProgessBar().getThrust()>0) 
                    {
                        main.getRecordPanel().getButton9().setEnabled(true);
                    }                    
                    if(main.getRecordPanel().getProgessBar().getThrust()>=main.getRecordPanel().getProgessBar().getMark())
                    {
                        main.getRecordPanel().getButton8().setEnabled(true);            
                    }
                    
                    if(main.getRecordPanel().getProgessBar().getCount()>=main.getRecordPanel().getProgessBar().getMax())
                        break;
                }
                
                // pause - doesn't work in mysaifu
                //try{
                //    if(!line.isRunning())   //if(numBytesRead<=0) //
                //    {
                //        System.out.println("Thread.sleep(500)");
                //        Thread.currentThread().sleep(500);
                //    }
                //}catch(Exception ex)
                //{
                //    ex.printStackTrace();
                //}
            }
            
            // we reached the end of the stream.  stop and close the line.
            line.stop();
            line.close();
            line = null;

            // stop and close the output stream
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // load bytes into the audio input stream for playback

            byte audioBytes[] = out.toByteArray();

            if (audioInputStream == null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);   // no metaheader
                audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);
                
                try {
                    audioInputStream.reset();
                    cursor = audioInputStream.getFrameLength() * audioInputStream.getFormat().getFrameSize(); 
                    audioInputStream.skip(cursor);
                } catch (Exception e) {
                }                
                
            } else {
                try {
                    audioInputStream.reset();
                    
                    byte[] orgAudioBytes = new byte[(int)(audioInputStream.getFrameLength()*audioInputStream.getFormat().getFrameSize())];
                    audioInputStream.read(orgAudioBytes);
                         
                    // split by cursor
                    int preAudioBytes = (int)cursor;
                    int postAudioBytes = (int)(audioInputStream.getFrameLength()*audioInputStream.getFormat().getFrameSize()-cursor);
                                        
                    if(audioBytes.length<postAudioBytes)
                    {
                        postAudioBytes = postAudioBytes-audioBytes.length;
                        
                    }else
                    {
                        postAudioBytes = 0;
                    }
                                        
                    byte[] mergeAudioBytes = new byte[preAudioBytes + audioBytes.length + postAudioBytes];
                    
                    System.arraycopy(orgAudioBytes, 0, mergeAudioBytes, 0, preAudioBytes);
                    System.arraycopy(audioBytes, 0, mergeAudioBytes, preAudioBytes, audioBytes.length);
                    if(postAudioBytes>0)
                        System.arraycopy(orgAudioBytes, preAudioBytes+audioBytes.length, mergeAudioBytes, preAudioBytes+audioBytes.length, postAudioBytes);

                    ByteArrayInputStream bais = new ByteArrayInputStream(mergeAudioBytes);  // no metaheader
                    
                    audioInputStream.close(); // close IO for old file
                    
                    audioInputStream = new AudioInputStream(bais, format, mergeAudioBytes.length / frameSizeInBytes);

                    try {
                        audioInputStream.reset();
                        cursor = preAudioBytes + audioBytes.length; 
                        audioInputStream.skip(cursor);
                    } catch (Exception e) {
                    }                        

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // full length
            long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
            duration = milliseconds / 1000.0;
            
            shutDown(null);
            main.getHeaderPanel().getLabel3().setText("Stop");
            stable = true;
        }
    } // End class Capture
}
