package org.myftp.phlipserv.unzippr.decompression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.myftp.phlipserv.unzippr.utils.FileUtils;

public class ZipArchive extends GenericArchive {

	public static String extension = ".zip";
	
	private void addFile(File file, String relativePath, ZipOutputStream output) throws IOException {
		String path;
		if(relativePath.equals("")) {
			path = "";
		} else {
			path = relativePath + File.separator;
		}
		
		if(file.isDirectory()) {
			File[] list = file.listFiles();
			String newPath = path + file.getName();
			
			log("[+] Compressing directory: " + newPath);
			
			for(File f : list) {
				addFile(f, newPath, output);
			}
		} else {
			FileInputStream input = null;
			try {
				input = new FileInputStream(file);
				
				ZipEntry entry = new ZipEntry(path + file.getName());
				
				output.putNextEntry(entry);
				
				byte[] dataBuffer = new byte[BUFFER];
				
				int count;
				while ((count = input.read(dataBuffer, 0, BUFFER)) != -1) {
					output.write(dataBuffer, 0, count);
				}
				
				log("[+] Compressed file: " + path + file.getName());
			} catch(Exception e) {
				log("[!] Error compressing file: " + path + file.getName() + " - Skipping");
				e.printStackTrace();
			} finally {
				if(input != null) {
					input.close();
					input = null;
				}
				
				if(output != null) {
					output.flush();
				}
			}
		}
	}
	
	@Override
	public void compress(List<File> files) throws IOException {
		BufferedInputStream input = null;
		ZipOutputStream output = null;

		try {
			String firstFile = files.get(0).getPath();
			String extension = FileUtils.getExtension(firstFile);
			
			File zipFile;
			if(extension.equals("")) {
				zipFile = new File(firstFile + ".zip");
			} else {
				zipFile = new File(firstFile.replace(extension, ".zip"));
			}
			
			output = new ZipOutputStream( new BufferedOutputStream( new FileOutputStream(zipFile)));
			output.setMethod(ZipOutputStream.DEFLATED);
			output.setLevel(Deflater.BEST_COMPRESSION);
			
			log("Compressing to file: " + zipFile.getName());
			
			for(File file : files) {
				addFile(file, "", output);
			}
		} finally {
			if(input != null) {
				input.close();
				input = null;
			}
			
			if(output != null) {
				output.flush();
				output.close();
				output = null;
			}
		}
		
		log("Finished compressing files");
	}
	
	@Override
	public void decompress(File file) throws IOException {
		BufferedOutputStream output = null;
		ZipInputStream input = null;
		ZipEntry entry = null;

		try {
			
			log("Decompressing file: " + file.getName());
			
			input = new ZipInputStream( new BufferedInputStream( new FileInputStream(file)));

			while ((entry = input.getNextEntry()) != null) {
				File newFile = new File(file.getParent(), entry.getName());
				newFile.getParentFile().mkdirs();
				
				if (!entry.isDirectory()) {
					output = new BufferedOutputStream(new FileOutputStream(
							newFile), BUFFER);
					byte[] dataBuffer = new byte[BUFFER];
					int count;
					while ((count = input.read(dataBuffer, 0, BUFFER)) != -1) {
						output.write(dataBuffer, 0, count);
					}
					output.flush();
					output.close();
					output = null;
					log("[-] Decompressed file: " + entry.getName());
					entry = null;
				} else {
					newFile.mkdirs();
				}
			}
		} catch(Exception e) {
			log("[!] Error decompressing file - Skipping");
			e.printStackTrace();
		} finally {
			if(input != null) {
				input.close();
				input = null;
			}
			
			if (output != null) {
				output.flush();
				output.close();
				output = null;
			}

			if(entry != null) {
				entry = null;
			}
		}
		
		log("Finished decompressing file");
	}
}
