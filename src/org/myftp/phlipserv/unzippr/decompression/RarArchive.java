package org.myftp.phlipserv.unzippr.decompression;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

public class RarArchive extends GenericArchive {

	public static String extension = ".rar";
	
	@Override
	public void compress(List<File> files) {
		log("[!] Unable to compress to .rar file, proprietary format!");
	}

	@Override
	public void decompress(File file) throws IOException {
		BufferedOutputStream output = null;
		Archive archive = null;
		FileHeader header = null;
		
		try {
			log("Decompressing file: " + file.getName());
			
			archive = new Archive(file);
			header = archive.nextFileHeader();
			
			while (header != null) {
				File entry = new File(file.getParent(), header.getFileNameString().trim());
				
				log("[-] Decompressing file: " + entry.getName());
				
				output = new BufferedOutputStream( new FileOutputStream(entry));
				archive.extractFile(header, output);
				
				output.flush();
				output.close();
				
				header = archive.nextFileHeader();
			}
		} catch(Exception e) {
			log("[!] Error decompressing .rar file!");
			e.printStackTrace();
		} finally {
			if(output != null) {
				output.flush();
				output.close();
				output = null;
			}
			
			if(archive != null) {
				archive.close();
				archive = null;
			}
			
			if(header != null) {
				header = null;
			}
		}
		
		log("Finished decompressing");
	}

}
