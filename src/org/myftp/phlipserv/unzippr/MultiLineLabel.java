package org.myftp.phlipserv.unzippr;

import javax.swing.JLabel;

public class MultiLineLabel extends JLabel {

	private static final long serialVersionUID = 5297073989633135226L;
	
	public MultiLineLabel() {
		super("");
	}

	public MultiLineLabel(String text) {
		super(wrapText(text));
		
	}

	public MultiLineLabel(String text, int alignment) {
		super(wrapText(text), alignment);
		
	}

	public void setText(String text) {
		super.setText(wrapText(text));
	}
	
	public void append(String text) {
		this.setText(unwrapText(getText()) + text);
	}
	
	private static String wrapText(String text) {
		if (!text.equals("")) {
			StringBuffer result = new StringBuffer();
			if (!text.substring(0, 6).matches("<[hH][tT][mM][lL]>")) {
				result.append("<html>");
			}
			result.append(text);
			if (!text.substring(text.length() - 7, text.length()).matches(
					"</[hH][tT][mM][lL]>")) {
				result.append("</html>");
			}
			return result.toString();
		} else {
			return "<html></html>";
		}
	}
	
	private static String unwrapText(String text) {
		if (!text.equals("")) {
			if (text.substring(0, 6).matches("<[hH][tT][mM][lL]>")) {
				text = text.replaceAll("<[hH][tT][mM][lL]>", "");
			}
			if (text.substring(text.length() - 7, text.length()).matches(
					"</[hH][tT][mM][lL]>")) {
				text = text.replaceAll("</[hH][tT][mM][lL]>", "");
			}
			return text;
		} else {
			return "";
		}
	}
	
}
