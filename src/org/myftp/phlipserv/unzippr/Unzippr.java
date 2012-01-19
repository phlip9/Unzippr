package org.myftp.phlipserv.unzippr;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.myftp.phlipserv.unzippr.decompression.ArchiveFactory;
import org.myftp.phlipserv.unzippr.decompression.GenericArchive;
import org.myftp.phlipserv.unzippr.decompression.ZipArchive;
import org.myftp.phlipserv.unzippr.utils.FileUtils;
import org.myftp.phlipserv.unzippr.utils.Logger;
import org.myftp.phlipserv.unzippr.utils.Logger.LogInterface;

public class Unzippr implements DropArea.DropCallback{

	public static final String APP_NAME = "Unzippr";
	public static final int PREFERED_WIDTH = 512;
	public static final int PREFERED_HEIGHT = 512;

	private MultiLineLabel logOut;

	public Unzippr() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		GuiFrame window = GuiFrame.getInstance();
		window.setSize(PREFERED_WIDTH, PREFERED_HEIGHT);

		Label lDropFile = new Label("          Drop File          ", Label.CENTER);
		
		lDropFile.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
		lDropFile.setVisible(true);

		logOut = new MultiLineLabel("", MultiLineLabel.LEFT);
		logOut.setVisible(true);

		DropArea dropArea = new DropArea(this);
		dropArea.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(logOut, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		dropArea.add(lDropFile, BorderLayout.NORTH);
		dropArea.add(scrollPane, BorderLayout.CENTER);

		window.add(dropArea);
		window.addWindowListener(new WindowListener(){
			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowOpened(WindowEvent e) {}
		});
		window.setVisible(true);
		
		Logger.getInstance().init(new LogInterface() {

			@Override
			public void log(String toLog) {
				logOut.append(toLog + "<br />");
			}
		});
	}

	public Unzippr(String[] files_S) {
		this();

		File[] files_F = new File[files_S.length];
		for(int i = 0; i < files_S.length; i++) {
			files_F[i] = new File(files_S[i]);
		}
		
		handleList(Arrays.asList(files_F));
	}

	@Override
	public void handleList(List<File> files) {
		if (files.size() > 0) {
			List<File> toCompress = new ArrayList<File>();
			
			for (File file : files) {
				String extension = FileUtils.getExtension(file.getName());
				
				if(file.isDirectory() || !FileUtils.isArchive(extension)) {
					toCompress.add(file);
				} else {
					GenericArchive archive = ArchiveFactory.getArchive(extension);
					try {
						archive.decompress(file);
					} catch (IOException e) {
						log("[!] Error while decompressing!");
						e.printStackTrace();
					}
				}
			}
			
			try {
				if (toCompress.size() > 0) {
					GenericArchive archive = ArchiveFactory.getArchive(ZipArchive.extension);
					archive.compress(toCompress);
				}
			} catch (Exception e) {
				log("[!] Error while compressing!");
				e.printStackTrace();
			}
		}
	}
	
	public void log(String toLog) {
		Logger.getInstance().log(toLog);
	}

	private void exit(){
		System.exit(0);
	}

	public static void main(String[] args) {
		if(args.length > 0){
			new Unzippr(args);
		} else {
			new Unzippr();
		}
	}
}
