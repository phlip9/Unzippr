package org.myftp.phlipserv.unzippr;

import java.awt.Panel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DropArea extends Panel implements DropTargetListener {
	
	private static final long serialVersionUID = 4214605569822338072L;

	@SuppressWarnings("unused")
	private DropTarget dropTarget;
	
	private DropCallback handler;
	
	public DropArea(DropCallback handler){
		this.handler = handler;
		dropTarget = new DropTarget(this, this);
	}

	
	@Override
	public void drop(DropTargetDropEvent dtde) {
		boolean gotData = false;
		
		try {
			dtde.acceptDrop(DnDConstants.ACTION_LINK);
			Transferable trans = dtde.getTransferable();
			
			if(trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
				@SuppressWarnings("unchecked")
				List<File> files = (List<File>) trans.getTransferData(DataFlavor.javaFileListFlavor);
				
				handler.handleList(files);
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} finally {
			dtde.dropComplete(gotData);
		}
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {}

	@Override
	public void dragExit(DropTargetEvent arg0) {}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {}

	public interface DropCallback {
		public void handleList(List<File> files);
	}
}
