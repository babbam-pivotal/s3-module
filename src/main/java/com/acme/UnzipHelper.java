package com.acme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class UnzipHelper
{
    private static final String INPUT_GZIP_FILE = "/Users/mbabbar/Desktop/gz/20151216-18.json.gz";
    private static final String OUTPUT_DIR = "/Users/mbabbar/Desktop/gz2";
 
//    public static void main( String[] args )
//    {
//    	UnzipHelper gZip = new UnzipHelper();
//    	gZip.gunzipIt(INPUT_GZIP_FILE, OUTPUT_DIR);
//    }

    /**
     * GunZip it
     * @param outputDir 
     * @param inputGzipFile 
     * @return 
     */
    public static void gunzipIt(String inputGzipFile, String outputDir){
 
     byte[] buffer = new byte[1024];
 
     try{
 
    	 GZIPInputStream gzis = 
    		new GZIPInputStream(new FileInputStream(inputGzipFile));
    	 
    	 File folder = new File(outputDir);
    	 if(!folder.exists())
    	 {
			if (folder.mkdir()) {
				System.out.println("Local directory is created - " + outputDir);
			} else {
				System.out.println("Failed to create local directory - " + outputDir);
			}
    	 }
    	 FileOutputStream out = 
            new FileOutputStream(outputDir + "/" + getFileName(inputGzipFile).substring(0, getFileName(inputGzipFile).lastIndexOf('.')));
 
        int len;
        while ((len = gzis.read(buffer)) > 0) {
        	out.write(buffer, 0, len);
        }
 
        gzis.close();
    	out.close();
 
    	
    }catch(IOException ex){
       ex.printStackTrace();   
    }
   } 
    
    private static String getFileName(String kn) {
		return kn.substring(kn.lastIndexOf('/') + 1);
	}
}
