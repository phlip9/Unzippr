package org.myftp.phlipserv.unzippr.decompression;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.myftp.phlipserv.unzippr.utils.Logger;

public abstract class GenericArchive {
	public static final int BUFFER = 4096;
	
	protected static String extension = "";
	
	public abstract void compress(List<File> files) throws IOException;
	public abstract void decompress(File file) throws IOException;
	
	public void log(String toLog) {
		Logger.getInstance().log(toLog);
	}
}
