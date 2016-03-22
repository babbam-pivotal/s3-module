package com.acme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class UnzipHelper {
	private static final String INPUT_GZIP_FILE = "/Users/mbabbar/Desktop/gz/20151216-18.json.gz";
	private static final String OUTPUT_DIR = "/Users/mbabbar/Desktop/gz2";
	private static final String TEMP_UNZIP_FILE_EXT = ".ziptmp";
	private static String tempFileName = "";
	private static String ActualFileName = "";

	/**
	 * GunZip it
	 * 
	 * @param outputDir
	 * @param inputGzipFile
	 * @return
	 */
	public static void gunzipIt(String inputGzipFile, String outputDir) {

		byte[] buffer = new byte[1024];

		try {

			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputGzipFile));

			File folder = new File(outputDir);
			if (!folder.exists()) {
				if (folder.mkdir()) {
					System.out.println("Local directory is created - " + outputDir);
				} else {
					System.out.println("Failed to create local directory - " + outputDir);
				}
			}
			String fileNameWithoutExt = getFileName(inputGzipFile).substring(0,
					getFileName(inputGzipFile).lastIndexOf('.'));
			tempFileName = outputDir + "/" + fileNameWithoutExt + TEMP_UNZIP_FILE_EXT;
			ActualFileName = outputDir + "/" + fileNameWithoutExt;
			FileOutputStream out = new FileOutputStream(tempFileName);
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

			gzis.close();
			out.close();
			// Move temp file to actual name
			if (new File(ActualFileName).exists())
				throw new java.io.IOException("ERROR: Unzipped file already exists");
			// Rename file (or directory)
			boolean success = new File(tempFileName).renameTo(new File(ActualFileName));
			if (!success) {
				System.out.println(ActualFileName + " created.");
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static String getFileName(String kn) {
		return kn.substring(kn.lastIndexOf('/') + 1);
	}
}
