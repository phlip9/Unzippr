package org.myftp.phlipserv.unzippr.utils;

import org.myftp.phlipserv.unzippr.decompression.RarArchive;
import org.myftp.phlipserv.unzippr.decompression.SevenZipArchive;
import org.myftp.phlipserv.unzippr.decompression.ZipArchive;

public class FileUtils {
	public static String getExtension(String uri) {
		if (uri == null) {
			return null;
		}

		int dot = uri.lastIndexOf(".");
		if (dot >= 0) {
			return uri.substring(dot);
		} else {
			return "";
		}
	}
	
	public static boolean isArchive(String extension) {
		return (extension.equalsIgnoreCase(RarArchive.extension) || 
				extension.equalsIgnoreCase(SevenZipArchive.extension) || 
				extension.equalsIgnoreCase(ZipArchive.extension));
	}
}
