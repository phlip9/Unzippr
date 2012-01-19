package org.myftp.phlipserv.unzippr.decompression;

public class ArchiveFactory {
	
	public static GenericArchive getArchive(String extension) {
		if(extension.equalsIgnoreCase(RarArchive.extension)) {
			return new RarArchive();
		} else if(extension.equalsIgnoreCase(SevenZipArchive.extension)) {
			return new SevenZipArchive();
		} else {
			return new ZipArchive();
		}
	}
}
