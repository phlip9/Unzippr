package org.myftp.phlipserv.unzippr.decompression;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import SevenZip.ArchiveExtractCallback;
import SevenZip.HRESULT;
import SevenZip.MyRandomAccessFile;
import SevenZip.Archive.IArchiveExtractCallback;
import SevenZip.Archive.IInArchive;
import SevenZip.Archive.SevenZipEntry;
import SevenZip.Archive.SevenZip.Handler;


public class SevenZipArchive extends GenericArchive {

	public static String extension = ".7z";

	@Override
	public void compress(List<File> files) {
		log("[!] Unable to compress to .7z file, not supported!");
	}

	@Override
	public void decompress(final File aFile) throws IOException {
		MyRandomAccessFile input = null;
		IInArchive archive = null;

		try {
			log("Decompressing file: " + aFile.getName());
			input =  new MyRandomAccessFile(aFile, "r");

			archive = new Handler();

			if(archive.Open(input) != 0) {
				throw new Exception();
			}

			ArchiveExtractCallback extractCallbackSpec = new ArchiveExtractCallback() {
				
				@Override
				public int GetStream(int index, java.io.OutputStream [] outStream, int askExtractMode) throws IOException {
					outStream[0] = null;

					SevenZipEntry item = _archiveHandler.getEntry(index);

					File file = new File(aFile.getParentFile(), item.getName());
					_filePath = file.getPath();

					if (askExtractMode == IInArchive.NExtract_NAskMode_kExtract) {

						try {
							isDirectory = item.isDirectory();

							if (isDirectory) {
								if (file.isDirectory()) {
									return HRESULT.S_OK;
								}
								
								if (file.mkdirs()) {
									return HRESULT.S_OK;
								} else {
									return HRESULT.S_FALSE;
								}
							}


							File dirs = file.getParentFile();
							if (dirs != null) {
								if (!dirs.isDirectory()) {
									if (!dirs.mkdirs()) {
										return HRESULT.S_FALSE;
									}
								}
							}

							long pos = item.getPosition();
							if (pos == -1) {
								file.delete();
							}

							RandomAccessFile outStr = new RandomAccessFile(_filePath, "rw");

							if (pos != -1) {
								outStr.seek(pos);
							}

							outStream[0] = new OutputStream(outStr);
						} catch (IOException e) {
							return HRESULT.S_FALSE;
						}

						return HRESULT.S_OK;

					}
					
					return HRESULT.S_OK;
				}
			};
			
			IArchiveExtractCallback extractCallback = extractCallbackSpec;
			extractCallbackSpec.Init(archive);
			extractCallbackSpec.PasswordIsDefined = false;

			for(int i = 0; i < archive.size(); i++) {
				log("[-] Decompressing file: " + archive.getEntry(i).getName());
			}

			if(archive.Extract(null, -1, IInArchive.NExtract_NAskMode_kExtract, extractCallback) == HRESULT.S_OK) {
				if(extractCallbackSpec.NumErrors == 0) {
					log("Finished decompressing files");
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			log("[!] Decompression failed - Skipping");
			e.printStackTrace();
		} finally {
			if(archive != null) {
				archive.close();
			}
		}
	}
}
