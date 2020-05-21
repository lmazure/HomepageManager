package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;

public class FormatFromURLChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
			NodeChecker.X
			} );
	
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[1];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkFormat(e);}
		                    @Override
							public String getDescription() { return "given the URL, the format is incorrect"; } };
		return a;
	}

	private CheckStatus checkFormat(final Element e) {

		String url="";
		String format="";
		
		final NodeList children = e.getChildNodes();
		for (int j=0; j<children.getLength(); j++) {
			final Node child = children.item(j);
			if ( child.getNodeType() == Node.ELEMENT_NODE ) {
				if (child.getNodeName()=="A") url=child.getTextContent();
				if (child.getNodeName()=="F") format=child.getTextContent();
			}		
		}
		
		if (url.toUpperCase().endsWith("PDF") && !format.equals("PDF"))
		   return new CheckStatus("\""+url+"\" is not indicated as being a PDF format");

		if (url.toUpperCase().endsWith("WMV") && !format.equals("Windows Media Player"))
			   return new CheckStatus("\""+url+"\" is not indicated as being a Windows Media Player format");

		if (url.contains("youtube.com/watch") && !format.equals("MP4"))
			   return new CheckStatus("\""+url+"\" is not indicated as being a MP4 format");

		if (url.contains("video.google.com/videoplay") && !format.equals("Flash Video"))
			   return new CheckStatus("\""+url+"\" is not indicated as being a Flash Video format");

		return null;
	}
}
