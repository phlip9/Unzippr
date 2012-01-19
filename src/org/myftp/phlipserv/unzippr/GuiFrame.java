package org.myftp.phlipserv.unzippr;

import java.awt.Frame;

public class GuiFrame extends Frame {
	private static final long serialVersionUID = -6764983879788778137L;
	
	private static GuiFrame instance;
	
	private GuiFrame() {
		super(Unzippr.APP_NAME);
	}
	
	public static synchronized GuiFrame getInstance(){
		if(instance == null){
			instance = new GuiFrame();
		}
		return instance;
	}
}
